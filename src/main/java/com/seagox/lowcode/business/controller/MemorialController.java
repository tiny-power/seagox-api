package com.seagox.lowcode.business.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;

/**
 * 建造纪念册
 */
@RestController
@RequestMapping("/memorial")
public class MemorialController {

    private static final BaseColor PRIMARY = new BaseColor(36, 75, 116);
    private static final BaseColor MUTED = new BaseColor(104, 117, 139);
    private static final BaseColor LIGHT_BG = new BaseColor(247, 248, 250);

    /**
     * 导出PDF纪念册
     *
     * @param payload 纪念册数据JSON
     * @param response 响应
     */
    @PostMapping("/exportPdf")
    public void exportPdf(String payload, HttpServletResponse response) {
        JSONObject data = parsePayload(payload);
        JSONObject project = data.getJSONObject("project");
        String projectName = text(project, "name", "项目纪念册");
        try {
            response.setContentType("application/pdf");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode(projectName + "-建造纪念册.pdf", "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);

            Document document = new Document(PageSize.A4, 42, 42, 48, 48);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            BaseFont baseFont = createBaseFont();
            Font titleFont = new Font(baseFont, 28, Font.BOLD, PRIMARY);
            Font subTitleFont = new Font(baseFont, 16, Font.BOLD, PRIMARY);
            Font textFont = new Font(baseFont, 11, Font.NORMAL, BaseColor.DARK_GRAY);
            Font mutedFont = new Font(baseFont, 10, Font.NORMAL, MUTED);

            writeCover(document, project, titleFont, subTitleFont, textFont, mutedFont);
            writeOverview(document, project, subTitleFont, textFont);
            writeRows(document, "项目阶段", data.getJSONArray("stages"), "name", "dates", "description", subTitleFont, textFont, mutedFont);
            writeRows(document, "施工故事", data.getJSONArray("stories"), "title", "date", "description", subTitleFont, textFont, mutedFont);
            writePeople(document, data.getJSONArray("people"), subTitleFont, textFont, mutedFont);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("PDF纪念册生成失败", e);
        }
    }

    private JSONObject parsePayload(String payload) {
        if (!StringUtils.hasText(payload)) {
            JSONObject data = new JSONObject();
            data.put("project", new JSONObject());
            return data;
        }
        return JSON.parseObject(payload);
    }

    private void writeCover(Document document, JSONObject project, Font titleFont, Font subTitleFont,
                            Font textFont, Font mutedFont) throws Exception {
        Paragraph eyebrow = new Paragraph("建造纪念册", subTitleFont);
        eyebrow.setAlignment(Element.ALIGN_CENTER);
        eyebrow.setSpacingBefore(80);
        document.add(eyebrow);

        Paragraph title = new Paragraph(text(project, "name", "项目纪念册"), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(16);
        title.setSpacingAfter(16);
        document.add(title);

        Paragraph slogan = new Paragraph("记录家的诞生  致敬每一份用心", textFont);
        slogan.setAlignment(Element.ALIGN_CENTER);
        slogan.setSpacingAfter(42);
        document.add(slogan);

        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(76);
        PdfPCell cell = new PdfPCell();
        cell.setPadding(22);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(LIGHT_BG);
        cell.addElement(new Paragraph("项目地址：" + text(project, "address", "待更新"), textFont));
        cell.addElement(new Paragraph("项目总历时：" + text(project, "duration", "0") + " 天", textFont));
        cell.addElement(new Paragraph("建设周期：" + text(project, "startDate", "待更新")
                + " 开工 - " + text(project, "deliveryDate", "待更新") + " 交付", textFont));
        card.addCell(cell);
        document.add(card);

        Paragraph footer = new Paragraph("由工程项目数字化管控平台生成", mutedFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(36);
        document.add(footer);
        document.newPage();
    }

    private void writeOverview(Document document, JSONObject project, Font subTitleFont, Font textFont) throws Exception {
        addSectionTitle(document, "项目概览", subTitleFont);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[] {1, 1});
        addMetric(table, "照片数量", text(project, "photoCount", "0"), textFont);
        addMetric(table, "里程碑数量", text(project, "milestoneCount", "0"), textFont);
        addMetric(table, "施工日志", text(project, "logCount", "0"), textFont);
        addMetric(table, "项目历时", text(project, "duration", "0") + " 天", textFont);
        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void writeRows(Document document, String title, JSONArray rows, String nameKey, String metaKey,
                           String descKey, Font subTitleFont, Font textFont, Font mutedFont) throws Exception {
        addSectionTitle(document, title, subTitleFont);
        if (rows == null || rows.isEmpty()) {
            document.add(new Paragraph("暂无记录", mutedFont));
            document.add(Chunk.NEWLINE);
            return;
        }
        for (int i = 0; i < rows.size(); i++) {
            JSONObject row = rows.getJSONObject(i);
            Paragraph name = new Paragraph(text(row, nameKey, "记录" + (i + 1)), textFont);
            name.setSpacingBefore(6);
            document.add(name);
            String meta = text(row, metaKey, "");
            if (StringUtils.hasText(meta)) {
                document.add(new Paragraph(meta, mutedFont));
            }
            String desc = text(row, descKey, "");
            if (StringUtils.hasText(desc)) {
                document.add(new Paragraph(desc, textFont));
            }
        }
        document.add(Chunk.NEWLINE);
    }

    private void writePeople(Document document, JSONArray people, Font subTitleFont, Font textFont, Font mutedFont) throws Exception {
        addSectionTitle(document, "项目团队", subTitleFont);
        if (people == null || people.isEmpty()) {
            document.add(new Paragraph("暂无团队成员", mutedFont));
            return;
        }
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[] {1, 2});
        for (int i = 0; i < people.size(); i++) {
            JSONObject item = people.getJSONObject(i);
            addCell(table, text(item, "role", "成员"), textFont);
            addCell(table, text(item, "name", "-") + "\n" + text(item, "description", ""), textFont);
        }
        document.add(table);
    }

    private void addSectionTitle(Document document, String title, Font font) throws Exception {
        Paragraph paragraph = new Paragraph(title, font);
        paragraph.setSpacingBefore(10);
        paragraph.setSpacingAfter(12);
        document.add(paragraph);
    }

    private void addMetric(PdfPTable table, String label, String value, Font font) {
        addCell(table, label + "\n" + value, font);
    }

    private void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(12);
        cell.setBorderColor(new BaseColor(232, 236, 242));
        table.addCell(cell);
    }

    private String text(JSONObject object, String key, String defaultValue) {
        if (object == null || object.get(key) == null) {
            return defaultValue;
        }
        String value = String.valueOf(object.get(key));
        return StringUtils.hasText(value) ? value : defaultValue;
    }

    private BaseFont createBaseFont() throws Exception {
        String[] candidates = {
                "/System/Library/Fonts/Supplemental/Arial Unicode.ttf",
                "/Library/Fonts/Arial Unicode.ttf",
                "/System/Library/Fonts/Supplemental/Songti.ttc,0",
                "/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc,0",
                "/usr/share/fonts/truetype/arphic/uming.ttc,0"
        };
        for (String candidate : candidates) {
            String path = candidate.contains(",") ? candidate.substring(0, candidate.indexOf(',')) : candidate;
            if (new File(path).exists()) {
                return BaseFont.createFont(candidate, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
        }
        return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
    }
}
