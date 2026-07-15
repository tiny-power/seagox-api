package com.seagox.lowcode.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.seagox.lowcode.common.OssConfig;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.Disk;
import com.seagox.lowcode.system.mapper.DiskMapper;
import com.seagox.lowcode.system.service.IDiskService;
import com.seagox.lowcode.system.service.IUploadService;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网盘
 */
@Service
public class DiskService implements IDiskService {

    @Autowired
    private DiskMapper diskMapper;

    @Autowired
    private IUploadService uploadService;

    @Autowired
    private OssConfig ossConfig;

    private static final long MAX_ARCHIVE_SIZE = 100L * 1024 * 1024;

    private static final long MAX_TOTAL_SIZE = 300L * 1024 * 1024;

    private static final long MAX_SINGLE_FILE_SIZE = 50L * 1024 * 1024;

    private static final int MAX_FILE_COUNT = 1000;

    private static final Map<String, Object> ARCHIVE_PREVIEW_CACHE = new ConcurrentHashMap<>();

    @Override
    public ResultData queryChildren(Long companyId, Long parentId, String keyword) {
        LambdaQueryWrapper<Disk> qw = new LambdaQueryWrapper<>();
        qw.eq(Disk::getCompanyId, companyId);
        if (!StringUtils.isEmpty(keyword)) {
            qw.like(Disk::getName, keyword);
        } else if (parentId == null) {
            qw.isNull(Disk::getParentId);
        } else {
            qw.eq(Disk::getParentId, parentId);
        }
        qw.orderByDesc(Disk::getUpdatedAt);
        List<Disk> list = diskMapper.selectList(qw);
        list.sort(Comparator
                .comparing((Disk disk) -> !"folder".equals(disk.getType()))
                .thenComparing(Disk::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
        return ResultData.success(list);
    }

    @Override
    public ResultData previewArchive(Long companyId, Long id) {
        if (id == null) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "文件不能为空");
        }
        Disk disk = diskMapper.selectById(id);
        if (disk == null || !companyId.equals(disk.getCompanyId())) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "文件不存在");
        }
        String archiveType = StringUtils.isEmpty(disk.getType()) ? getFileType(disk.getName()) : disk.getType();
        if (!isSupportedArchive(archiveType)) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "仅支持zip、rar、7z压缩文件预览");
        }
        if (StringUtils.isEmpty(disk.getUrl())) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "文件地址不能为空");
        }
        if (disk.getSize() != null && disk.getSize() > MAX_ARCHIVE_SIZE) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "压缩包超过100MB，暂不支持在线预览");
        }

        String cacheKey = buildArchiveCacheKey(disk);
        Object cached = ARCHIVE_PREVIEW_CACHE.get(cacheKey);
        if (cached != null) {
            return ResultData.success(cached);
        }

        File archiveFile = null;
        try {
            archiveFile = downloadArchive(disk);
            Object preview = extractArchive(disk, archiveFile);
            ARCHIVE_PREVIEW_CACHE.put(cacheKey, preview);
            return ResultData.success(preview);
        } catch (ArchivePreviewException e) {
            e.printStackTrace();
            return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.warn(ResultCode.OTHER_ERROR, "压缩包解析失败");
        } finally {
            if (archiveFile != null && archiveFile.exists()) {
                archiveFile.delete();
            }
        }
    }

    @Override
    public ResultData previewArchiveUrl(Long companyId, String url, String name) {
        if (StringUtils.isEmpty(url)) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "文件地址不能为空");
        }
        String archiveName = StringUtils.isEmpty(name) ? fileNameOf(url) : name;
        String archiveType = getFileType(archiveName);
        if (StringUtils.isEmpty(archiveType)) {
            archiveType = getFileType(url);
        }
        if (!isSupportedArchive(archiveType)) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "仅支持zip、rar、7z压缩文件预览");
        }

        Disk disk = new Disk();
        disk.setId(Math.abs((long) (url + archiveName).hashCode()));
        disk.setCompanyId(companyId);
        disk.setName(archiveName);
        disk.setUrl(url);
        disk.setType(archiveType);
        disk.setSize(0L);

        String cacheKey = buildArchiveUrlCacheKey(disk);
        Object cached = ARCHIVE_PREVIEW_CACHE.get(cacheKey);
        if (cached != null) {
            return ResultData.success(cached);
        }

        File archiveFile = null;
        try {
            archiveFile = downloadArchive(disk);
            Object preview = extractArchive(disk, archiveFile);
            ARCHIVE_PREVIEW_CACHE.put(cacheKey, preview);
            return ResultData.success(preview);
        } catch (ArchivePreviewException e) {
            e.printStackTrace();
            return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.warn(ResultCode.OTHER_ERROR, "压缩包解析失败");
        } finally {
            if (archiveFile != null && archiveFile.exists()) {
                archiveFile.delete();
            }
        }
    }

    @Transactional
    @Override
    public ResultData insertFolder(Long companyId, Long userId, Disk disk) {
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        disk.setCompanyId(companyId);
        disk.setCreatedBy(userId);
        disk.setUpdatedBy(userId);
        disk.setType("folder");
        disk.setUrl("");
        disk.setSize(0L);
        ResultData checkResult = checkName(companyId, disk.getParentId(), disk.getName(), null);
        if (checkResult != null) {
            return checkResult;
        }
        Disk parent = getParent(companyId, disk.getParentId());
        if (disk.getParentId() != null && parent == null) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "上级目录不存在");
        }
        disk.setLevel(parent == null ? 1 : parent.getLevel() + 1);
        disk.setPath("/");
        diskMapper.insert(disk);
        disk.setPath(buildPath(parent, disk.getId()));
        diskMapper.updateById(disk);
        return ResultData.success(disk);
    }

    @Transactional
    @Override
    public ResultData insertFile(Long companyId, Long userId, Disk disk) {
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        disk.setCompanyId(companyId);
        disk.setCreatedBy(userId);
        disk.setUpdatedBy(userId);
        ResultData checkResult = checkName(companyId, disk.getParentId(), disk.getName(), null);
        if (checkResult != null) {
            return checkResult;
        }
        Disk parent = getParent(companyId, disk.getParentId());
        if (disk.getParentId() != null && parent == null) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "上级目录不存在");
        }
        if (StringUtils.isEmpty(disk.getUrl())) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "文件地址不能为空");
        }
        disk.setType(getFileType(disk.getName()));
        if (disk.getSize() == null) {
            disk.setSize(0L);
        }
        disk.setLevel(parent == null ? 1 : parent.getLevel() + 1);
        disk.setPath("/");
        diskMapper.insert(disk);
        disk.setPath(buildPath(parent, disk.getId()));
        diskMapper.updateById(disk);
        return ResultData.success(disk);
    }

    @Transactional
    @Override
    public ResultData update(Long companyId, Long userId, Disk disk) {
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        Disk original = diskMapper.selectById(disk.getId());
        if (original == null || !companyId.equals(original.getCompanyId())) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "文件不存在");
        }
        ResultData checkResult = checkName(companyId, original.getParentId(), disk.getName(), disk.getId());
        if (checkResult != null) {
            return checkResult;
        }
        original.setName(disk.getName());
        original.setUpdatedBy(userId);
        diskMapper.updateById(original);
        return ResultData.success(original);
    }

    @Transactional
    @Override
    public ResultData delete(Long companyId, Long id) {
        Disk disk = diskMapper.selectById(id);
        if (disk == null || !companyId.equals(disk.getCompanyId())) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "文件不存在");
        }
        if ("folder".equals(disk.getType())) {
            LambdaQueryWrapper<Disk> qw = new LambdaQueryWrapper<>();
            qw.eq(Disk::getCompanyId, companyId).likeRight(Disk::getPath, disk.getPath());
            diskMapper.delete(qw);
            return ResultData.success(null);
        }
        diskMapper.deleteById(id);
        return ResultData.success(null);
    }

    private ResultData checkUserId(Long userId) {
        if (userId == null) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "用户不能为空");
        }
        return null;
    }

    private Disk getParent(Long companyId, Long parentId) {
        if (parentId == null) {
            return null;
        }
        Disk parent = diskMapper.selectById(parentId);
        if (parent == null || !companyId.equals(parent.getCompanyId()) || !"folder".equals(parent.getType())) {
            return null;
        }
        return parent;
    }

    private ResultData checkName(Long companyId, Long parentId, String name, Long id) {
        if (StringUtils.isEmpty(name)) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "名称不能为空");
        }
        LambdaQueryWrapper<Disk> qw = new LambdaQueryWrapper<>();
        qw.eq(Disk::getCompanyId, companyId).eq(Disk::getName, name);
        if (parentId == null) {
            qw.isNull(Disk::getParentId);
        } else {
            qw.eq(Disk::getParentId, parentId);
        }
        if (id != null) {
            qw.ne(Disk::getId, id);
        }
        Long count = diskMapper.selectCount(qw);
        if (count > 0) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "同级目录下名称已经存在");
        }
        return null;
    }

    private String buildPath(Disk parent, Long id) {
        if (parent == null) {
            return "/" + id + "/";
        }
        return parent.getPath() + id + "/";
    }

    private String getFileType(String filename) {
        if (StringUtils.isEmpty(filename) || filename.lastIndexOf(".") == -1) {
            return "";
        }
        String suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return suffix;
    }

    private boolean isSupportedArchive(String typeOrFilename) {
        String suffix = getSuffix(typeOrFilename);
        if (StringUtils.isEmpty(suffix)) {
            suffix = StringUtils.isEmpty(typeOrFilename) ? "" : typeOrFilename.toLowerCase();
        }
        return Arrays.asList("zip", "rar", "7z").contains(suffix);
    }

    private String getSuffix(String filename) {
        if (StringUtils.isEmpty(filename) || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private String buildArchiveCacheKey(Disk disk) {
        Date updatedAt = disk.getUpdatedAt();
        long updatedTime = updatedAt == null ? 0L : updatedAt.getTime();
        return disk.getId() + "_" + updatedTime + "_" + (disk.getSize() == null ? 0L : disk.getSize());
    }

    private String buildArchiveUrlCacheKey(Disk disk) {
        return "url_" + disk.getName() + "_" + disk.getUrl() + "_" + (disk.getSize() == null ? 0L : disk.getSize());
    }

    private File downloadArchive(Disk disk) throws Exception {
        String suffix = getSuffix(disk.getName());
        if (StringUtils.isEmpty(suffix)) {
            suffix = disk.getType();
        }
        File archiveFile = File.createTempFile("disk-archive-" + disk.getId() + "-", "." + suffix);
        URLConnection connection = new URL(buildDownloadUrl(disk.getUrl())).openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(30000);
        try (InputStream inputStream = connection.getInputStream()) {
            Files.copy(inputStream, archiveFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new ArchivePreviewException("压缩包下载失败，请检查文件地址或对象存储配置", e);
        }
        if (archiveFile.length() > MAX_ARCHIVE_SIZE) {
            throw new ArchivePreviewException("压缩包超过100MB，暂不支持在线预览");
        }
        return archiveFile;
    }

    private String buildDownloadUrl(String url) {
        if (!StringUtils.isEmpty(ossConfig.getUrl()) && !StringUtils.isEmpty(ossConfig.getEndpoint())) {
            return url.replace(ossConfig.getUrl(), ossConfig.getEndpoint());
        }
        return url;
    }

    private Object extractArchive(Disk disk, File archiveFile) throws Exception {
        Map<String, Map<String, Object>> nodeMap = new LinkedHashMap<>();
        List<Map<String, Object>> rootChildren = new ArrayList<>();
        long[] totalSize = new long[]{0L};
        int[] fileCount = new int[]{0};
        String suffix = getSuffix(disk.getName());
        if (StringUtils.isEmpty(suffix)) {
            suffix = disk.getType();
        }

        if ("zip".equals(suffix)) {
            extractZip(archiveFile, rootChildren, nodeMap, totalSize, fileCount);
        } else if ("7z".equals(suffix)) {
            extractSevenZ(archiveFile, rootChildren, nodeMap, totalSize, fileCount);
        } else if ("rar".equals(suffix)) {
            extractRar(archiveFile, rootChildren, nodeMap, totalSize, fileCount);
        } else {
            throw new ArchivePreviewException("仅支持zip、rar、7z压缩文件预览");
        }

        sortNodes(rootChildren);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", disk.getId());
        result.put("name", disk.getName());
        result.put("files", rootChildren);
        return result;
    }

    private void extractZip(File archiveFile, List<Map<String, Object>> rootChildren,
                            Map<String, Map<String, Object>> nodeMap, long[] totalSize, int[] fileCount) throws Exception {
        try (ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new FileInputStream(archiveFile), "UTF-8", true, true)) {
            ZipArchiveEntry entry;
            while ((entry = inputStream.getNextZipEntry()) != null) {
                processArchiveEntry(rootChildren, nodeMap, totalSize, fileCount, entry.getName(), entry.isDirectory(),
                        entry.getSize(), () -> inputStream);
            }
        }
    }

    private void extractSevenZ(File archiveFile, List<Map<String, Object>> rootChildren,
                               Map<String, Map<String, Object>> nodeMap, long[] totalSize, int[] fileCount) throws Exception {
        try (SevenZFile sevenZFile = new SevenZFile(archiveFile)) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                final SevenZArchiveEntry currentEntry = entry;
                processArchiveEntry(rootChildren, nodeMap, totalSize, fileCount, currentEntry.getName(), currentEntry.isDirectory(),
                        currentEntry.getSize(), () -> sevenZFile.getInputStream(currentEntry));
            }
        }
    }

    private void extractRar(File archiveFile, List<Map<String, Object>> rootChildren,
                            Map<String, Map<String, Object>> nodeMap, long[] totalSize, int[] fileCount) throws Exception {
        try (Archive archive = new Archive(archiveFile)) {
            if (archive.isEncrypted() || archive.isPasswordProtected()) {
                throw new ArchivePreviewException("加密压缩包暂不支持在线预览");
            }
            for (FileHeader header : archive.getFileHeaders()) {
                final FileHeader currentHeader = header;
                processArchiveEntry(rootChildren, nodeMap, totalSize, fileCount, currentHeader.getFileName(), currentHeader.isDirectory(),
                        currentHeader.getFullUnpackSize(), () -> archive.getInputStream(currentHeader));
            }
        }
    }

    private void processArchiveEntry(List<Map<String, Object>> rootChildren, Map<String, Map<String, Object>> nodeMap,
                                     long[] totalSize, int[] fileCount, String rawPath, boolean directory, long rawSize,
                                     ArchiveInputStreamSupplier inputStreamSupplier) throws Exception {
        String path = normalizeArchivePath(rawPath);
        if (StringUtils.isEmpty(path)) {
            return;
        }

        if (directory) {
            ensureFolder(rootChildren, nodeMap, path);
            return;
        }

        fileCount[0]++;
        if (fileCount[0] > MAX_FILE_COUNT) {
            throw new ArchivePreviewException("压缩包内文件超过1000个，暂不支持在线预览");
        }

        long size = rawSize < 0L ? 0L : rawSize;
        totalSize[0] += size;
        if (totalSize[0] > MAX_TOTAL_SIZE) {
            throw new ArchivePreviewException("压缩包解压后超过300MB，暂不支持在线预览");
        }

        String parentPath = parentPath(path);
        List<Map<String, Object>> siblings = StringUtils.isEmpty(parentPath)
                ? rootChildren
                : childrenOf(ensureFolder(rootChildren, nodeMap, parentPath));

        Map<String, Object> fileNode = buildFileNode(path, size);
        if (size <= MAX_SINGLE_FILE_SIZE) {
            try {
                InputStream inputStream = inputStreamSupplier.open();
                String url = uploadArchiveItem(inputStream, fileNameOf(path), size);
                if (StringUtils.isEmpty(url)) {
                    fileNode.put("disabled", true);
                    fileNode.put("disabledReason", "文件上传失败，请下载压缩包后查看");
                } else {
                    fileNode.put("url", url);
                }
            } catch (Exception e) {
                e.printStackTrace();
                fileNode.put("disabled", true);
                fileNode.put("disabledReason", "文件解压失败，请下载压缩包后查看");
            }
        } else {
            fileNode.put("disabled", true);
            fileNode.put("disabledReason", "文件超过50MB，请下载压缩包后查看");
        }
        siblings.add(fileNode);
    }


    private String normalizeArchivePath(String path) {
        String value = path == null ? "" : path.replace("\\", "/").trim();
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        if (StringUtils.isEmpty(value) || value.contains("../") || value.startsWith("../") || value.contains(":/")) {
            return "";
        }
        String[] parts = value.split("/");
        List<String> cleanParts = new ArrayList<>();
        for (String part : parts) {
            if (StringUtils.isEmpty(part) || ".".equals(part) || "..".equals(part)) {
                return "";
            }
            cleanParts.add(part);
        }
        return String.join("/", cleanParts);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> childrenOf(Map<String, Object> folder) {
        return (List<Map<String, Object>>) folder.get("children");
    }

    private Map<String, Object> ensureFolder(List<Map<String, Object>> rootChildren, Map<String, Map<String, Object>> nodeMap, String path) {
        Map<String, Object> exist = nodeMap.get(path);
        if (exist != null) {
            return exist;
        }

        String parentPath = parentPath(path);
        List<Map<String, Object>> siblings = StringUtils.isEmpty(parentPath)
                ? rootChildren
                : childrenOf(ensureFolder(rootChildren, nodeMap, parentPath));

        Map<String, Object> folder = new LinkedHashMap<>();
        folder.put("id", archiveNodeId(path));
        folder.put("name", fileNameOf(path));
        folder.put("path", path);
        folder.put("type", "folder");
        folder.put("size", 0L);
        folder.put("isFolder", true);
        folder.put("children", new ArrayList<Map<String, Object>>());
        siblings.add(folder);
        nodeMap.put(path, folder);
        return folder;
    }

    private Map<String, Object> buildFileNode(String path, long size) {
        Map<String, Object> node = new LinkedHashMap<>();
        String name = fileNameOf(path);
        node.put("id", archiveNodeId(path));
        node.put("name", name);
        node.put("path", path);
        node.put("type", getFileType(name));
        node.put("size", size);
        node.put("isFolder", false);
        return node;
    }

    private String uploadArchiveItem(InputStream inputStream, String fileName, long size) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(size > 0L && size < Integer.MAX_VALUE ? (int) size : 4096);
        byte[] buffer = new byte[8192];
        int length;
        long total = 0L;
        while ((length = inputStream.read(buffer)) != -1) {
            total += length;
            if (total > MAX_SINGLE_FILE_SIZE) {
                throw new ArchivePreviewException("文件超过50MB，请下载压缩包后查看");
            }
            outputStream.write(buffer, 0, length);
        }

        String contentType = URLConnection.guessContentTypeFromName(fileName);
        if (StringUtils.isEmpty(contentType)) {
            contentType = "application/octet-stream";
        }
        Thread.sleep(2L);
        return uploadService.uploadByInputStream("business", fileName, contentType, outputStream.size(),
                new ByteArrayInputStream(outputStream.toByteArray()));
    }

    private String parentPath(String path) {
        int index = path.lastIndexOf("/");
        return index == -1 ? "" : path.substring(0, index);
    }

    private String fileNameOf(String path) {
        int index = path.lastIndexOf("/");
        return index == -1 ? path : path.substring(index + 1);
    }

    private String archiveNodeId(String path) {
        return "archive-" + Integer.toHexString(path.hashCode());
    }

    @SuppressWarnings("unchecked")
    private void sortNodes(List<Map<String, Object>> nodes) {
        nodes.sort(Comparator
                .comparing((Map<String, Object> item) -> !Boolean.TRUE.equals(item.get("isFolder")))
                .thenComparing(item -> String.valueOf(item.get("name"))));
        for (Map<String, Object> node : nodes) {
            if (Boolean.TRUE.equals(node.get("isFolder"))) {
                sortNodes((List<Map<String, Object>>) node.get("children"));
            }
        }
    }

    private static class ArchivePreviewException extends Exception {

        private static final long serialVersionUID = 1L;

        ArchivePreviewException(String message) {
            super(message);
        }

        ArchivePreviewException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private interface ArchiveInputStreamSupplier {
        InputStream open() throws Exception;
    }

}
