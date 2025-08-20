package com.seagox.lowcode.assets.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelPrint {

    private static final List<String> letterList = Arrays.asList(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM",
            "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ",
            "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM",
            "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ",
            "CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM",
            "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY", "CZ",
            "DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM",
            "DN", "DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ"
    );
    
    public static JSONObject readSheet(InputStream inputStream) {
        JSONObject result = new JSONObject();
        InputStream is = null;
        try {
            is = FileMagic.prepareToCheckMagic(inputStream);
            FileMagic fm = FileMagic.valueOf(is);
            if (fm.name().equals("OLE2")) {
                // 2003
                HSSFWorkbook workbook = new HSSFWorkbook(is);
                result = readHSSF(workbook, workbook.getSheetAt(0));
                workbook.close();
            } else if (fm.name().equals("OOXML")) {
                // 2007及2007以上
                XSSFWorkbook workbook = new XSSFWorkbook(is);
                result = readXSSF(workbook.getSheetAt(0));
                workbook.close();
            } else {
                throw new IOException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (inputStream != null) {
                	inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


	/**
	 * 解析数据
	 *
	 * @param sheet 表格sheet对象
	 * @return
	 * @throws IOException
	 */
	public static JSONObject readHSSF(HSSFWorkbook workbook, HSSFSheet sheet) throws IOException {
		JSONArray mergedRegions = getHSSFMergedRegions(sheet);
		// 结果json
		JSONObject resultJson = new JSONObject();
		// 获取宽度
		JSONArray widthJson = new JSONArray();
		// 获取高度
		JSONArray heightJson = new JSONArray();
		// 获取每行JSON对象的值
		JSONObject rowJson = new JSONObject();
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			HSSFRow eachRow = sheet.getRow(i);
			JSONObject cellJson = new JSONObject();
			if (eachRow != null) {
				float height = (eachRow.getHeightInPoints() / 72) * 96;
				heightJson.add(height);
				for (int j = 0; j < 26; j++) {
					JSONObject map = new JSONObject();
					boolean isMergeFlag = false;
					for (int k = 0; k < mergedRegions.size(); k++) {
						JSONObject mergeItem = mergedRegions.getJSONObject(k);
						int firstRow = mergeItem.getIntValue("firstRow");
						int lastRow = mergeItem.getIntValue("lastRow");
						int firstColumn = mergeItem.getIntValue("firstColumn");
						int lastColumn = mergeItem.getIntValue("lastColumn");
						if (i == firstRow && j == firstColumn) {
							// 合并单元格第一个
							map.put("rowspan", lastRow - firstRow + 1);
							map.put("colspan", lastColumn - firstColumn + 1);
							break;
						} else if (i >= firstRow && i <= lastRow && j >= firstColumn && j <= lastColumn) {
							isMergeFlag = true;
							break;
						}
					}
					if (i == 0) {
						widthJson.add(sheet.getColumnWidthInPixels(j));
					}
					if (!isMergeFlag) {
						HSSFCell cell = eachRow.getCell(j);
						if (cell != null) {
							JSONObject cellStyle = getHSSFCellStyle(workbook, cell.getCellStyle());
							cellStyle.put("width", sheet.getColumnWidthInPixels(j) + "px");
							cellStyle.put("height", height + "px");
							map.put("style", cellStyle);
							String value = getCellValue(cell);// 获取单元格数据
							map.put("value", value);
						} else {
							JSONObject cellStyle = new JSONObject();
							cellStyle.put("width", sheet.getColumnWidthInPixels(j) + "px");
							cellStyle.put("height", height + "px");
							map.put("style", cellStyle);
						}
						cellJson.put(letterList.get(j), map);
					}
				}
			}
			rowJson.put(String.valueOf(i), cellJson);
		}
		resultJson.put("widthJson", widthJson);
		resultJson.put("rowJson", rowJson);
		return resultJson;
	}

    /**
     * 解析数据
     *
     * @param sheet 表格sheet对象
     * @return
     * @throws IOException
     */
    public static JSONObject readXSSF(XSSFSheet sheet) throws IOException {
        JSONArray mergedRegions = getXSSFMergedRegions(sheet);
        //结果json
        JSONObject resultJson = new JSONObject();
        //获取宽度
        JSONArray widthJson = new JSONArray();
        //获取高度
        JSONArray heightJson = new JSONArray();
        // 获取每行JSON对象的值
        JSONObject rowJson = new JSONObject();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow eachRow = sheet.getRow(i);
            JSONObject cellJson = new JSONObject();
            if (eachRow != null) {
                float height = (eachRow.getHeightInPoints() / 72) * 96;
                heightJson.add(height);
                for (int j = 0; j < eachRow.getLastCellNum(); j++) {
                    JSONObject map = new JSONObject();
                    boolean isMergeFlag = false;
                    for (int k = 0; k < mergedRegions.size(); k++) {
                        JSONObject mergeItem = mergedRegions.getJSONObject(k);
                        int firstRow = mergeItem.getIntValue("firstRow");
                        int lastRow = mergeItem.getIntValue("lastRow");
                        int firstColumn = mergeItem.getIntValue("firstColumn");
                        int lastColumn = mergeItem.getIntValue("lastColumn");
                        if (i == firstRow && j == firstColumn) {
                            //合并单元格第一个
                            map.put("rowspan", lastRow - firstRow + 1);
                            map.put("colspan", lastColumn - firstColumn + 1);
                            break;
                        } else if (i >= firstRow && i <= lastRow && j >= firstColumn && j <= lastColumn) {
                            isMergeFlag = true;
                            break;
                        }
                    }
                    if (i == 0) {
                        widthJson.add(sheet.getColumnWidthInPixels(j));
                    }
                    if (!isMergeFlag) {
                        XSSFCell cell = eachRow.getCell(j);
                        if (cell != null) {
                            JSONObject cellStyle = getXSSFCellStyle(cell.getCellStyle());
                            cellStyle.put("width", sheet.getColumnWidthInPixels(j) + "px");
                            cellStyle.put("height", height + "px");
                            map.put("style", cellStyle);
                            String value = getCellValue(cell);//获取单元格数据
                            map.put("value", value);
                        } else {
                            JSONObject cellStyle = new JSONObject();
                            cellStyle.put("width", sheet.getColumnWidthInPixels(j) + "px");
                            cellStyle.put("height", height + "px");
                            map.put("style", cellStyle);
                        }
                        cellJson.put(letterList.get(j), map);
                    }
                }
            }
            rowJson.put(String.valueOf(i), cellJson);
        }
        resultJson.put("widthJson", widthJson);
        resultJson.put("heightJson", heightJson);
        resultJson.put("rowJson", rowJson);
        return resultJson;
    }

    /**
     * 获得单元格的样式
     */
    public static JSONObject getXSSFCellStyle(XSSFCellStyle cellStyle) {
        JSONObject result = new JSONObject();
        if (cellStyle != null) {
        	String textAlign = cellStyle.getAlignment().toString().toLowerCase();
        	if(textAlign.equals("general")) {
        		textAlign = "left";
            }
        	result.put("text-align", textAlign);
            String verticalAlign = cellStyle.getVerticalAlignment().toString().toLowerCase();
            if(verticalAlign.equals("center")) {
            	verticalAlign = "middle";
            }
            result.put("vertical-align", verticalAlign);

            XSSFColor backgroundColor = cellStyle.getFillForegroundXSSFColor();
            if (backgroundColor != null) {
                String rgbColor = backgroundColor.getARGBHex();
                if (!StringUtils.isEmpty(rgbColor)) {
                    result.put("background-color", getColor(rgbColor));
                }
            }

            String borderTopStyle = cellStyle.getBorderTop().toString().toLowerCase();
            if (borderTopStyle.equals("none")) {
                result.put("border-top-style", "solid");
            } else {
                result.put("border-top-style", borderTopStyle);
                result.put("border-top-color", "#000");
            }

            String borderBottomStyle = cellStyle.getBorderBottom().toString().toLowerCase();
            if (borderBottomStyle.equals("none")) {
                result.put("border-bottom-style", "solid");
            } else {
                result.put("border-bottom-style", borderBottomStyle);
                result.put("border-bottom-color", "#000");
            }

            String borderLeftStyle = cellStyle.getBorderLeft().toString().toLowerCase();
            if (borderLeftStyle.equals("none")) {
                result.put("border-left-style", "solid");
            } else {
                result.put("border-left-style", borderLeftStyle);
                result.put("border-left-color", "#000");
            }

            String borderRightStyle = cellStyle.getBorderRight().toString().toLowerCase();
            if (borderRightStyle.equals("none")) {
                result.put("border-right-style", "solid");
            } else {
                result.put("border-right-style", borderRightStyle);
                result.put("border-right-color", "#000");
            }

            XSSFColor topBorderXSSFColor = cellStyle.getTopBorderXSSFColor();
            if (topBorderXSSFColor != null) {
                String rgbColor = topBorderXSSFColor.getARGBHex();
                if (!StringUtils.isEmpty(rgbColor)) {
                    result.put("border-top-color", getColor(rgbColor));
                }
            }
            XSSFColor bottomBorderXSSFColor = cellStyle.getBottomBorderXSSFColor();
            if (bottomBorderXSSFColor != null) {
                String rgbColor = bottomBorderXSSFColor.getARGBHex();
                if (!StringUtils.isEmpty(rgbColor)) {
                    result.put("border-bottom-color", getColor(rgbColor));
                }
            }
            XSSFColor leftBorderXSSFColor = cellStyle.getLeftBorderXSSFColor();
            if (leftBorderXSSFColor != null) {
                String rgbColor = leftBorderXSSFColor.getARGBHex();
                if (!StringUtils.isEmpty(rgbColor)) {
                    result.put("border-left-color", getColor(rgbColor));
                }
            }
            XSSFColor rightBorderXSSFColor = cellStyle.getRightBorderXSSFColor();
            if (rightBorderXSSFColor != null) {
                String rgbColor = rightBorderXSSFColor.getARGBHex();
                if (!StringUtils.isEmpty(rgbColor)) {
                    result.put("border-right-color", getColor(rgbColor));
                }
            }

            XSSFFont font = cellStyle.getFont();
            if (font.getBold()) {
                result.put("font-weight", "bold");
            } else {
                result.put("font-weight", "normal");
            }
            XSSFColor fontColor = font.getXSSFColor();
            if (fontColor != null) {
                String rgbColor = fontColor.getARGBHex();
                if (!StringUtils.isEmpty(rgbColor)) {
                    result.put("color", getColor(rgbColor));
                }
            }
            result.put("font-family", font.getFontName());
            if (font.getItalic()) {
                result.put("font-style", "italic");
            } else {
                result.put("font-style", "normal");
            }
            result.put("font-size", font.getFontHeightInPoints() + "px");
            if (font.getStrikeout()) {
                result.put("text-decoration", "line-through");
            }
            short underline = font.getUnderline();
            if (underline == Font.U_NONE) {
                result.put("text-decoration", "none");
            } else if (underline == Font.U_SINGLE) {
                result.put("text-decoration", "underline");
            }
        }
        return result;
    }

    /**
     * 获得单元格的样式
     */
    public static JSONObject getHSSFCellStyle(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
        JSONObject result = new JSONObject();
        if (cellStyle != null) {
        	String textAlign = cellStyle.getAlignment().toString().toLowerCase();
        	if(textAlign.equals("general")) {
        		textAlign = "left";
            }
            result.put("text-align", textAlign);
            String verticalAlign = cellStyle.getVerticalAlignment().toString().toLowerCase();
            if(verticalAlign.equals("center")) {
            	verticalAlign = "middle";
            }
            result.put("vertical-align", verticalAlign);

            HSSFColor backgroundColor = cellStyle.getFillForegroundColorColor();
            if (backgroundColor != null && cellStyle.getFillPattern().equals(FillPatternType.SOLID_FOREGROUND)) {
                short[] rgb = backgroundColor.getTriplet();
                result.put("background-color", "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
            }

            String borderTopStyle = cellStyle.getBorderTop().toString().toLowerCase();
            if (borderTopStyle.equals("none")) {
                result.put("border-top-style", "solid");
            } else {
                result.put("border-top-style", borderTopStyle);
                result.put("border-top-color", "#000");
            }

            String borderBottomStyle = cellStyle.getBorderBottom().toString().toLowerCase();
            if (borderBottomStyle.equals("none")) {
                result.put("border-bottom-style", "solid");
            } else {
                result.put("border-bottom-style", borderBottomStyle);
                result.put("border-bottom-color", "#000");
            }

            String borderLeftStyle = cellStyle.getBorderLeft().toString().toLowerCase();
            if (borderLeftStyle.equals("none")) {
                result.put("border-left-style", "solid");
            } else {
                result.put("border-left-style", borderLeftStyle);
                result.put("border-left-color", "#000");
            }

            String borderRightStyle = cellStyle.getBorderRight().toString().toLowerCase();
            if (borderRightStyle.equals("none")) {
                result.put("border-right-style", "solid");
            } else {
                result.put("border-right-style", borderRightStyle);
                result.put("border-right-color", "#000");
            }

            HSSFPalette palette = workbook.getCustomPalette();
            HSSFColor borderTopColor = palette.getColor(cellStyle.getTopBorderColor());
            if (borderTopColor != null) {
                short[] rgb = borderTopColor.getTriplet();
                result.put("border-top-color", "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
            }

            HSSFColor borderBottomColor = palette.getColor(cellStyle.getBottomBorderColor());
            if (borderBottomColor != null) {
                short[] rgb = borderBottomColor.getTriplet();
                result.put("border-bottom-color", "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
            }

            HSSFColor borderLeftColor = palette.getColor(cellStyle.getLeftBorderColor());
            if (borderLeftColor != null) {
                short[] rgb = borderLeftColor.getTriplet();
                result.put("border-left-color", "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
            }

            HSSFColor borderRightColor = palette.getColor(cellStyle.getRightBorderColor());
            if (borderRightColor != null) {
                short[] rgb = borderRightColor.getTriplet();
                result.put("border-right-color", "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
            }

            HSSFFont font = cellStyle.getFont(workbook);
            if (font.getBold()) {
                result.put("font-weight", "bold");
            } else {
                result.put("font-weight", "normal");
            }
            HSSFColor fontColor = font.getHSSFColor(workbook);
            if (fontColor != null) {
                short[] rgb = fontColor.getTriplet();
                result.put("color", "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
            }
            result.put("font-family", font.getFontName());
            if (font.getItalic()) {
                result.put("font-style", "italic");
            } else {
                result.put("font-style", "normal");
            }
            result.put("font-size", font.getFontHeightInPoints() + "px");
            if (font.getStrikeout()) {
                result.put("text-decoration", "line-through");
            }
            short underline = font.getUnderline();
            if (underline == Font.U_NONE) {
                result.put("text-decoration", "none");
            } else if (underline == Font.U_SINGLE) {
                result.put("text-decoration", "underline");
            }
        }
        return result;
    }

    /**
     * 获得单元格的数据
     */
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        // 判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC: // 数字
                if(DateUtil.isCellDateFormatted(cell)){
                    Date date = cell.getDateCellValue();
                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = formater.format(date);
                } else {
                    //cellValue = String.valueOf(cell.getNumericCellValue());
                    NumberFormat nfNumeric = NumberFormat.getInstance();
                    cellValue = nfNumeric.format(cell.getNumericCellValue());
                    cellValue = cellValue.replaceAll(",", "");
                }
                break;
            case STRING: // 字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: // 公式
                try {
                    cellValue = cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    //cellValue = String.valueOf(cell.getNumericCellValue());
                    NumberFormat nfFormula = NumberFormat.getInstance();
                    cellValue = nfFormula.format(cell.getNumericCellValue());
                    cellValue = cellValue.replaceAll(",", "");
                }
                break;
            case BLANK: // 空值
                cellValue = "";
                break;
            case ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public static String getColor(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append("#");
        if (str.length() >= 8) {
            str = str.substring(2, 8);
        }
        sb.append(str);
        return sb.toString();
    }

    public static JSONArray getXSSFMergedRegions(XSSFSheet sheet) {
        JSONArray result = new JSONArray();
        int sheetmergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetmergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("firstRow", range.getFirstRow());
            jsonObject.put("lastRow", range.getLastRow());
            jsonObject.put("firstColumn", range.getFirstColumn());
            jsonObject.put("lastColumn", range.getLastColumn());
            result.add(jsonObject);
        }
        return result;
    }

    public static JSONArray getHSSFMergedRegions(HSSFSheet sheet) {
        JSONArray result = new JSONArray();
        int sheetmergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetmergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("firstRow", range.getFirstRow());
            jsonObject.put("lastRow", range.getLastRow());
            jsonObject.put("firstColumn", range.getFirstColumn());
            jsonObject.put("lastColumn", range.getLastColumn());
            result.add(jsonObject);
        }
        return result;
    }

}
