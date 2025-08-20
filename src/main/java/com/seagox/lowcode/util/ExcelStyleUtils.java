package com.seagox.lowcode.util;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;

public class ExcelStyleUtils implements IExcelExportStyler {

	private static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");

	/**
	 * 每列标题样式
	 */
	private CellStyle titleStyle;
	
	/**
	 * 数据行样式
	 */
	private CellStyle styles;

	public ExcelStyleUtils(Workbook workbook) {
		this.init(workbook);
	}

	/**
	 * 初始化样式
	 *
	 * @param workbook
	 */
	private void init(Workbook workbook) {
		this.titleStyle = initTitleStyle(workbook);
		this.styles = initStyles(workbook);
	}

	@Override
	public CellStyle getHeaderStyle(short headerColor) {

		return null;
	}

	/**
	 * 每列标题样式
	 *
	 * @param color
	 * @return
	 */
	@Override
	public CellStyle getTitleStyle(short color) {
		return titleStyle;
	}

	/**
	 * 数据行样式
	 *
	 * @param parity 可以用来表示奇偶行
	 * @param entity 数据内容
	 * @return 样式
	 */
	@Override
	public CellStyle getStyles(boolean parity, ExcelExportEntity entity) {
		return styles;
	}

	/**
	 * 获取样式方法
	 *
	 * @param dataRow 数据行
	 * @param obj     对象
	 * @param data    数据
	 */
	@Override
	public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
		return getStyles(true, entity);
	}

	/**
	 * 模板使用的样式设置
	 */
	@Override
	public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
		return null;
	}

	/**
	 * 初始化--每列标题样式
	 *
	 * @param workbook
	 * @return
	 */
	private CellStyle initTitleStyle(Workbook workbook) {
		CellStyle style = getBaseCellStyle(workbook);
		Font font = workbook.createFont();
		// 字体样式
		font.setFontName("宋体");
		// 是否加粗
		font.setBold(true);
		// 字体大小
		font.setFontHeightInPoints((short) 12);
		// 背景色
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);
		return style;
	}

	/**
	 * 初始化--数据行样式
	 *
	 * @param workbook
	 * @return
	 */
	private CellStyle initStyles(Workbook workbook) {
		CellStyle style = getBaseCellStyle(workbook);
		Font font = workbook.createFont();
		// 字体样式
		font.setFontName("宋体");
		font.setFontHeightInPoints(STRING_FORMAT);
		// 字体大小
		font.setFontHeightInPoints((short) 11);
		style.setFont(font);
		style.setDataFormat(STRING_FORMAT);
		return style;
	}

	/**
	 * 基础样式
	 *
	 * @return
	 */
	private CellStyle getBaseCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		// 下边框
		style.setBorderBottom(BorderStyle.THIN);
		// 左边框
		style.setBorderLeft(BorderStyle.THIN);
		// 上边框
		style.setBorderTop(BorderStyle.THIN);
		// 右边框
		style.setBorderRight(BorderStyle.THIN);
		// 水平居中
		style.setAlignment(HorizontalAlignment.CENTER);
		// 上下居中
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// 设置自动换行
		style.setWrapText(true);
		return style;
	}

}
