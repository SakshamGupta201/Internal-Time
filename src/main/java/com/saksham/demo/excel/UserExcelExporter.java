package com.saksham.demo.excel;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.saksham.demo.excel.UserExcelExporter;
import com.saksham.demo.model.Task;

public class UserExcelExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	private List<Task> listStudent;

	public UserExcelExporter(List<Task> listStudent) {
		this.listStudent = listStudent;
		workbook = new XSSFWorkbook();

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Student");

		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(20);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		createCell(row, 0, "USer Info", style);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		font.setFontHeightInPoints((short) (10));

		row = sheet.createRow(1);
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);
		createCell(row, 0, "Id", style);
		createCell(row, 1, "name", style);
		createCell(row, 2, "description", style);
		createCell(row, 3, "Creator Name", style);
		createCell(row, 4, "Owner", style);

	}

	private void writeDataLines() {
		int rowCount = 2;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (Task task : listStudent) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, task.getId(), style);
			createCell(row, columnCount++, task.getName(), style);
			createCell(row, columnCount++, task.getCreatorName(), style);
			createCell(row, columnCount++, task.getDate(), style);
			createCell(row, columnCount++, task.getTimeSpent(), style);
			createCell(row, columnCount++, task.getOwner(), style);
		}
	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

}
