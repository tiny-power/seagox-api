package com.seagox.lowcode.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;

public class Documents4jUtils {

	@SuppressWarnings("deprecation")
	public static void convert(String suffix, InputStream inputStream, OutputStream outputStream) {
		IConverter converter = LocalConverter.builder().build();
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
            } else if(suffix.equals("doc")) {
				converter.convert(inputStream).as(DocumentType.DOC).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("docx")) {
				converter.convert(inputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("xls")) {
				converter.convert(inputStream).as(DocumentType.XLS).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("xlsx")) {
				converter.convert(inputStream).as(DocumentType.XLSX).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("rtf")) {
				converter.convert(inputStream).as(DocumentType.RTF).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("txt")) {
				converter.convert(inputStream).as(DocumentType.TEXT).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("csv")) {
				converter.convert(inputStream).as(DocumentType.CSV).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("pptx")) {
				converter.convert(inputStream).as(DocumentType.PPTX).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("ppt")) {
				converter.convert(inputStream).as(DocumentType.PPT).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("xml")) {
				converter.convert(inputStream).as(DocumentType.XML).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("xltx")) {
				converter.convert(inputStream).as(DocumentType.XLTX).to(outputStream).as(DocumentType.PDF).execute();
			}else if (suffix.equals("ods")) {
				converter.convert(inputStream).as(DocumentType.ODS).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("ots")) {
				converter.convert(inputStream).as(DocumentType.OTS).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("mhtml")) {
				converter.convert(inputStream).as(DocumentType.MHTML).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("html")) {
				converter.convert(inputStream).as(DocumentType.HTML).to(outputStream).as(DocumentType.PDF).execute();
			} else if (suffix.equals("pdfa")) {
				converter.convert(inputStream).as(DocumentType.PDFA).to(outputStream).as(DocumentType.PDF).execute();
			} else if ("pdf".equals(suffix)) {
	            IOUtils.copy(inputStream, outputStream);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			converter.shutDown();
		}
	}

}
