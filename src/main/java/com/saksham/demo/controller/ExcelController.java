package com.saksham.demo.controller;

import java.util.List;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.saksham.demo.excel.UserExcelExporter;
import com.saksham.demo.model.Task;
import com.saksham.demo.repository.TaskRepository;

@Controller
public class ExcelController {
    @Autowired
	private TaskRepository taskRepository;

	@GetMapping("/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headervalue = "attachment; filename=Task_info.xlsx";

		response.setHeader(headerKey, headervalue);
		List<Task> listStudent = taskRepository.findAll();
        System.out.println(listStudent+"==========================================================");
		UserExcelExporter exp = new UserExcelExporter(listStudent);
		exp.export(response);
	}
}
