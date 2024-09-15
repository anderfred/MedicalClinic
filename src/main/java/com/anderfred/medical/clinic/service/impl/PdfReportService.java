package com.anderfred.medical.clinic.service.impl;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class PdfReportService {

  public byte[] generatePdfReport(
      String templatePath, Map<String, Object> parameters, JRDataSource dataSource)
      throws JRException, IOException {
    ClassPathResource reportResource = new ClassPathResource(templatePath);
    InputStream reportStream = reportResource.getInputStream();
    JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

    JasperPrint jasperPrint =
        JasperFillManager.fillReport(
            jasperReport, parameters, isNull(dataSource) ? new JREmptyDataSource() : dataSource);

    return JasperExportManager.exportReportToPdf(jasperPrint);
  }
}
