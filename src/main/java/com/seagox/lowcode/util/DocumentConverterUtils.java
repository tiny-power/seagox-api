package com.seagox.lowcode.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class DocumentConverterUtils {

    @Value("${jodconverter.working-dir}")
    private String workingDir;

    @SuppressWarnings("deprecation")
    public void convert(String suffix, InputStream inputStream, OutputStream outputStream) {
        try {
            if ("tiff".equals(suffix) || "tif".equals(suffix)) {
                RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(inputStream);
                // 读取tiff文件页数
                int numberOfPages = TiffImage.getNumberOfPages(randomAccessFileOrArray);
                Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();
                // 读取tiff文件的图像信息，添加到pdf文件
                for (int i = 1; i <= numberOfPages; i++) {
                    Image tempImage = TiffImage.getTiffImage(randomAccessFileOrArray, i);
                    Rectangle pageSize = new Rectangle(tempImage.getWidth(), tempImage.getHeight());
                    document.setPageSize(pageSize);
                    document.newPage();
                    document.add(tempImage);
                }
                document.close();
            } else if ("pdf".equals(suffix)) {
                IOUtils.copy(inputStream, outputStream);
            } else {
            	String temporary = String.valueOf(System.currentTimeMillis());
                File sourceFile = new File(workingDir);
                if (!sourceFile.exists()) {
                    sourceFile.mkdirs();
                }
                IOUtils.copy(inputStream, new FileOutputStream(new File(workingDir + File.separator + temporary + "." + suffix)));
                String command = "docker exec libreoffice /opt/libreoffice/program/soffice --convert-to pdf:writer_pdf_Export /opt/workingdir/" + temporary + "." + suffix + " --outdir /opt/workingdir";
                boolean result = executeCommand(command);
                if (result) {
                    IOUtils.copy(new FileInputStream(new File(workingDir + File.separator + temporary + "." + "pdf")), outputStream);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean executeCommand(String command) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        process.destroy();
        return true;
    }
}
