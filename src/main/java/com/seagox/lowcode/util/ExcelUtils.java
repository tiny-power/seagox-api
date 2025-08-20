package com.seagox.lowcode.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.exception.FormulaException;
import com.seagox.lowcode.strategy.annotation.AnnotationHandler;
import com.seagox.lowcode.strategy.annotation.AnnotationHandlerFactory;
import com.seagox.lowcode.strategy.rule.RuleHandler;
import com.seagox.lowcode.strategy.rule.RuleHandlerFactory;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtils {

	public static final List<String> letterList = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE",
			"AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW",
			"AX", "AY", "AZ", "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO",
			"BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ", "CA", "CB", "CC", "CD", "CE", "CF", "CG",
			"CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY",
			"CZ", "DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", "DO", "DP", "DQ",
			"DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ");

	public static JSONObject readDicData(InputStream inp) {
		JSONObject result = new JSONObject();
		InputStream is = null;
		try {
			is = FileMagic.prepareToCheckMagic(inp);
			FileMagic fm = FileMagic.valueOf(is);
			if (fm.name().equals("OLE2")) {
				// 2003
				HSSFWorkbook workbook = new HSSFWorkbook(is);
				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					HSSFSheet sheet = workbook.getSheetAt(i);
					JSONArray rowArray = new JSONArray();
					for (int j = 1; j <= sheet.getLastRowNum(); j++) {
						HSSFRow row = sheet.getRow(j);
						JSONObject rowObject = new JSONObject();
						for (int k = 0; k < row.getLastCellNum(); k++) {
							HSSFCell cell = row.getCell(k);
							if (cell != null) {
								String value = getCellValue(cell);
								if (k == 0) {
									if (!StringUtils.isEmpty(value)) {
										rowObject.put("code", value);
									}
								} else {
									if (!StringUtils.isEmpty(value)) {
										rowObject.put("value", value);
										rowObject.put("level", k);
										break;
									}
								}
							}
						}
						if (!rowObject.isEmpty()) {
							rowArray.add(rowObject);
						}
					}
					if (!rowArray.isEmpty()) {
						result.put(sheet.getSheetName(), rowArray);
					}
				}
				workbook.close();
			} else if (fm.name().equals("OOXML")) {
				// 2007及2007以上
				XSSFWorkbook workbook = new XSSFWorkbook(is);
				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					XSSFSheet sheet = workbook.getSheetAt(i);
					JSONArray rowArray = new JSONArray();
					for (int j = 1; j <= sheet.getLastRowNum(); j++) {
						XSSFRow row = sheet.getRow(j);
						JSONObject rowObject = new JSONObject();
						for (int k = 0; k < row.getLastCellNum(); k++) {
							XSSFCell cell = row.getCell(k);
							if (cell != null) {
								String value = getCellValue(cell);
								if (k == 0) {
									if (!StringUtils.isEmpty(value)) {
										rowObject.put("code", value);
									}
								} else {
									if (!StringUtils.isEmpty(value)) {
										rowObject.put("value", value);
										rowObject.put("level", k);
										break;
									}
								}
							}
						}
						if (!rowObject.isEmpty()) {
							rowArray.add(rowObject);
						}
					}
					if (!rowArray.isEmpty()) {
						result.put(sheet.getSheetName(), rowArray);
					}
				}
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
				if (inp != null) {
					inp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static JSONObject analysisFirstSheet(String filePath) {
		JSONObject result = new JSONObject();
		FileInputStream inp = null;
		InputStream is = null;
		try {
			inp = new FileInputStream(filePath);
			is = FileMagic.prepareToCheckMagic(inp);
			FileMagic fm = FileMagic.valueOf(is);
			System.out.println(fm.name());
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
				if (inp != null) {
					inp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static JSONObject analysis(String filePath) {
		JSONObject result = new JSONObject();
		FileInputStream inp = null;
		InputStream is = null;
		try {
			inp = new FileInputStream(filePath);
			is = FileMagic.prepareToCheckMagic(inp);
			FileMagic fm = FileMagic.valueOf(is);
			if (fm.name().equals("OLE2")) {
				// 2003
				HSSFWorkbook workbook = new HSSFWorkbook(is);
				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					HSSFSheet sheet = workbook.getSheetAt(i);
					result.put(sheet.getSheetName(), readHSSF(workbook, sheet));
				}
				workbook.close();
			} else if (fm.name().equals("OOXML")) {
				// 2007及2007以上
				XSSFWorkbook workbook = new XSSFWorkbook(is);
				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					XSSFSheet sheet = workbook.getSheetAt(i);
					result.put(sheet.getSheetName(), readXSSF(sheet));
				}
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
				if (inp != null) {
					inp.close();
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
		// 获取宽带
		JSONArray widthJson = new JSONArray();
		// 获取每行JSON对象的值
		JSONObject rowJson = new JSONObject();
		for (int i = 0; i <= sheet.getLastRowNum() + 5; i++) {
			HSSFRow eachRow = sheet.getRow(i);
			JSONObject cellJson = new JSONObject();
			if (eachRow != null) {
				float height = (eachRow.getHeightInPoints() / 72) * 96;
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
			} else {
				for (int j = 0; j < 26; j++) {
					JSONObject map = new JSONObject();
					JSONObject cellStyle = new JSONObject();
					cellStyle.put("width", sheet.getColumnWidthInPixels(j) + "px");
					cellStyle.put("height", "36px");
					map.put("style", cellStyle);
					cellJson.put(letterList.get(j), map);
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
		// 结果json
		JSONObject resultJson = new JSONObject();
		// 获取宽带
		JSONArray widthJson = new JSONArray();
		// 获取每行JSON对象的值
		JSONObject rowJson = new JSONObject();
		for (int i = 0; i <= sheet.getLastRowNum() + 5; i++) {
			XSSFRow eachRow = sheet.getRow(i);
			JSONObject cellJson = new JSONObject();
			if (eachRow != null) {
				float height = (eachRow.getHeightInPoints() / 72) * 96;
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
						XSSFCell cell = eachRow.getCell(j);
						if (cell != null) {
							JSONObject cellStyle = getXSSFCellStyle(cell.getCellStyle());
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
			} else {
				for (int j = 0; j < 26; j++) {
					JSONObject map = new JSONObject();
					JSONObject cellStyle = new JSONObject();
					cellStyle.put("width", sheet.getColumnWidthInPixels(j) + "px");
					cellStyle.put("height", "36px");
					map.put("style", cellStyle);
					cellJson.put(letterList.get(j), map);
				}
			}
			rowJson.put(String.valueOf(i), cellJson);
		}
		resultJson.put("widthJson", widthJson);
		resultJson.put("rowJson", rowJson);
		return resultJson;
	}

	/**
	 * 获得单元格的样式
	 */
	public static JSONObject getXSSFCellStyle(XSSFCellStyle cellStyle) {
		JSONObject result = new JSONObject();
		if (cellStyle != null) {
			result.put("text-align", cellStyle.getAlignment().toString().toLowerCase());
			result.put("vertical-align", cellStyle.getVerticalAlignment().toString().toLowerCase());

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
			result.put("text-align", cellStyle.getAlignment().toString().toLowerCase());
			result.put("vertical-align", cellStyle.getVerticalAlignment().toString().toLowerCase());

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
			if (DateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
				cellValue = formater.format(date);
			} else {
				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setGroupingUsed(false);
				numberFormat.setMaximumFractionDigits(4);
				cellValue = numberFormat.format(cell.getNumericCellValue());
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
				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setGroupingUsed(false);
				numberFormat.setMaximumFractionDigits(4);
				cellValue = numberFormat.format(cell.getNumericCellValue());
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

	public static Workbook readWorkbook(InputStream input) {
		InputStream is = null;
		try {
			is = FileMagic.prepareToCheckMagic(input);
			FileMagic fm = FileMagic.valueOf(is);
			Workbook workbook = null;
			if (fm.name().equals("OLE2")) {
				// 2003
				workbook = new HSSFWorkbook(is);
			} else if (fm.name().equals("OOXML")) {
				// 2007及2007以上
				workbook = new XSSFWorkbook(is);
			} else {
				throw new FormulaException("文件格式不对");
			}
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FormulaException(e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 解析数据
	 *
	 * @param sheet 表格sheet对象
	 */
	public static ImportResult readCell(HttpServletRequest request, Sheet sheet, int startRow, JSONObject validRule,
			String mark, JSONObject options) {
		ImportResult importResult = new ImportResult();
		List<Map<String, Object>> result = new ArrayList<>();
		List<String> failList = new ArrayList<>();
		short maxCellNum = 0;
		Row firstRow = sheet.getRow(0);
		if (firstRow != null) {
			maxCellNum = firstRow.getLastCellNum();
		}
		for (int i = startRow - 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				short lastCellNum = row.getLastCellNum();
				if (lastCellNum > maxCellNum) {
					maxCellNum = lastCellNum;
				}
				JSONObject rowJson = new JSONObject();
				for (short colIx = 0; colIx < maxCellNum; colIx++) {
					Cell cell = row.getCell(colIx);
					String cellValue = getCellValue(cell);
					// 验证规则
					JSONObject fieldRule = validRule.getJSONObject(String.valueOf(colIx));
					if (fieldRule == null || fieldRule.isEmpty()) {
						// 跳过不验证数据
						continue;
					}
					JSONArray ruleList = fieldRule.getJSONArray("rule");
					if (ruleList != null && ruleList.size() > 0) {
						for (int j = 0; j < ruleList.size(); j++) {
							String annotation = ruleList.getString(j);
							String type = "";
							if (annotation.startsWith("@")) {
								type = annotation.substring(1, annotation.indexOf("("));
								if (type.equals("Company")) {
									if (StringUtils.isEmpty(cellValue)) {
										rowJson.put(fieldRule.getString("field"), null);
									} else {
										String value = options.getJSONObject("company").getString(cellValue);
										if (StringUtils.isEmpty(value)) {
											failList.add("第" + (i + 1) + "行" + letterList.get(colIx) + "列："
													+ fieldRule.getString("label") + "(" + cellValue + ")" + "不存在");
										} else {
											rowJson.put(fieldRule.getString("field"), value);
										}
									}
								} else if (type.equals("Department")) {
									if (StringUtils.isEmpty(cellValue)) {
										rowJson.put(fieldRule.getString("field"), null);
									} else {
										String value = options.getJSONObject("department").getString(cellValue);
										if (StringUtils.isEmpty(value)) {
											failList.add("第" + (i + 1) + "行" + letterList.get(colIx) + "列："
													+ fieldRule.getString("label") + "(" + cellValue + ")" + "不存在");
										} else {
											rowJson.put(fieldRule.getString("field"), value);
										}
									}
								} else if (type.equals("User")) {
									if (StringUtils.isEmpty(cellValue)) {
										rowJson.put(fieldRule.getString("field"), null);
									} else {
										String value = options.getJSONObject("user").getString(cellValue);
										if (StringUtils.isEmpty(value)) {
											failList.add("第" + (i + 1) + "行" + letterList.get(colIx) + "列："
													+ fieldRule.getString("label") + "(" + cellValue + ")" + "不存在");
										} else {
											rowJson.put(fieldRule.getString("field"), value);
										}
									}
								} else if (type.equals("Replace")) {
									if (StringUtils.isEmpty(cellValue)) {
										rowJson.put(fieldRule.getString("field"), null);
									} else {
										String value = options.getJSONObject(fieldRule.getString("field"))
												.getString(cellValue);
										if (StringUtils.isEmpty(value)) {
											failList.add("第" + (i + 1) + "行" + letterList.get(colIx) + "列："
													+ fieldRule.getString("label") + "(" + cellValue + ")" + "不存在");
										} else {
											rowJson.put(fieldRule.getString("field"), value);
										}
									}
								} else if (!type.equals("Ignore")) {
									AnnotationHandler annotationEventService = AnnotationHandlerFactory
											.getHandler(type);
									if (annotationEventService != null) {
										failList = annotationEventService.valid(type, i, colIx, cellValue, annotation,
												failList);
										rowJson.put(fieldRule.getString("field"), cellValue);
									} else {
										failList.add(letterList.get(colIx) + "列" + annotation + "注解不存在");
									}
								}
							} else {
								failList.add(letterList.get(colIx) + "列" + annotation + "注解无效");
							}
						}
					} else {
						if (StringUtils.isEmpty(cellValue)) {
							rowJson.put(fieldRule.getString("field"), null);
						} else {
							rowJson.put(fieldRule.getString("field"), cellValue);
						}
					}
				}
				try {
					RuleHandler eventService = RuleHandlerFactory.getHandler(mark);
					if (eventService != null) {
						VerifyHandlerResult verifyHandlerResult = eventService.importVerify(request, rowJson);
						if (!verifyHandlerResult.isSuccess()) {
							List<String> msgList = verifyHandlerResult.getMsg();
							// TODO
							for (int k = 0; k <= sheet.getLastRowNum(); k++) {
								failList.add("第" + (i + 1) + "行：" + msgList.get(k));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				rowJson.put("user_id", request.getParameter("userId"));
				if(!rowJson.containsKey("company_id")) {
					rowJson.put("company_id", request.getParameter("companyId"));
				}
				result.add(rowJson);
			}
		}
		importResult.setList(result);
		importResult.setFailList(failList);
		importResult.setVerifyFail(failList.size() != 0);
		return importResult;
	}

	/**
	 *
	 * @param headers          模板头数据
	 * @param fileNameNoSuffix 未带后缀名称
	 * @param suffix           后缀名称
	 * @return
	 */
	public static InputStream createTemplateByHeader(JSONArray headers, OutputStream os, Integer startLine,
			JSONArray data) {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();

			addValidationData(workbook, sheet, startLine, data);
			// 表头
			XSSFRow headerRom = sheet.createRow(0);
			headerRom.setHeight((short) (27 * 20));
			XSSFFont headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontName("宋体");
			for (int i = 0; i < headers.size(); i++) {
				// 设置样式
				XSSFCellStyle headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平居中
				headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
				headerCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(191, 191, 191), null)); // 背景颜色
				headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				headerCellStyle.setBorderTop(BorderStyle.THIN);
				headerCellStyle.setBorderBottom(BorderStyle.THIN);
				headerCellStyle.setBorderLeft(BorderStyle.THIN);
				headerCellStyle.setBorderRight(BorderStyle.THIN);

				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setFontName("宋体");

				JSONObject item = headers.getJSONObject(i);
				XSSFCell headerCell = headerRom.createCell(i);
				sheet.setColumnWidth(i, 24 * 256);
				if (item.getBooleanValue("required")) {
					font.setColor(IndexedColors.RED.getIndex());
				} else {
					font.setColor(IndexedColors.BLACK.getIndex());
				}
				headerCellStyle.setFont(font);
				headerCell.setCellStyle(headerCellStyle);
				headerCell.setCellValue(new XSSFRichTextString(item.getString("label")));
			}
			workbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	// 添加下拉框
	public static void addValidationData(XSSFWorkbook workbook, XSSFSheet sheet, int startLine, JSONArray data) {
		if (data != null && !data.isEmpty() && data.size() > 0) {
			XSSFSheet hidden = workbook.createSheet("hidden");
			for (int i = 0; i < data.size(); i++) {
				JSONObject item = data.getJSONObject(i);
				String field = item.getString("field");
				int col = item.getIntValue("col");
				JSONArray options = item.getJSONArray("options");
				for (int j = 0; j < options.size(); j++) {
					XSSFRow row = hidden.getRow(j);
					if (row == null) {
						row = hidden.createRow(j);
					}
					XSSFCell cell = row.createCell(i);
					cell.setCellValue(options.getString(j));
				}
				Name namedCell = workbook.createName();
				namedCell.setNameName(field);
				namedCell.setRefersToFormula(
						"hidden!$" + letterList.get(i) + "$1:$" + letterList.get(i) + "$" + options.size());
				XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
				XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
						.createFormulaListConstraint(field);
				CellRangeAddressList addressList = new CellRangeAddressList(startLine - 1, 65535, col, col);
				XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);
				validation.setShowErrorBox(true);
				sheet.addValidationData((XSSFDataValidation) validation);
			}
			workbook.setSheetHidden(1, true);
		}
	}

}
