package org.websparrow.report.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.websparrow.report.entity.Employee;
import org.websparrow.report.repository.ReportRepository;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class EmployeeReportService {

	@Autowired
	private ReportRepository reportRepository;
	
	@Value("classpath:employee-rpt-database.jrxml")
	Resource resourceFile;

	public File generateReport() throws Exception {
		try {

			List<Employee> employees = reportRepository.findAll();

			// Compile the Jasper report from .jrxml to .japser
			JasperReport jasperReport = JasperCompileManager
					.compileReport(resourceFile.getInputStream());

			// Get your data source
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(employees);

			// Add parameters
			Map<String, Object> parameters = new HashMap<>();

			parameters.put("createdBy", "Websparrow.org");

			// Fill the report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					jrBeanCollectionDataSource);

			// Export the report to a PDF file
			File file = new File("Emp-Rpt-Database.pdf");
			JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());

			System.out.println("Done");

			return file;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
