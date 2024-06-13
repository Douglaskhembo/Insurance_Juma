package com.brokersystems.brokerapp.reports.service;

import com.brokersystems.brokerapp.server.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.File;

public interface ReportGenerator {


     File generateReport(final String templateFile, final String styleFile,final String reportName, final String dataFile) throws BadRequestException;

}
