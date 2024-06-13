package com.brokersystems.brokerapp.reports.service.impl;

import com.brokersystems.brokerapp.reports.service.ReportGenerator;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import oracle.apps.xdo.XDOException;
import oracle.apps.xdo.dataengine.DataProcessor;
import oracle.apps.xdo.dataengine.Parameter;
import oracle.apps.xdo.template.FOProcessor;
import oracle.apps.xdo.template.RTFProcessor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class ReportGeneratorImpl implements ReportGenerator {

    @Autowired
    private DataSource datasource;


    @Override
    public File generateReport(String templateFile, String styleFile, String reportName, String dataFile) throws BadRequestException {
        try {
            FOProcessor processor = new FOProcessor();
            InputStream inputStream = this.getClass().getResourceAsStream("/reports/"+templateFile);
            File file = new File("/reports/"+templateFile);
            File datafile = new File("/reports/"+dataFile);
            System.out.println(file.getAbsolutePath());
            System.out.println(datafile.getAbsolutePath());
           // FileUtils.copyInputStreamToFile(inputStream, file);
           // InputStream dataStream = this.getClass().getResourceAsStream("/reports/"+dataFile);
            //FileUtils.copyInputStreamToFile(dataStream, datafile);
            System.out.println(file.getAbsolutePath());
            System.out.println(datafile.getAbsolutePath());
            String output = "reports/"+reportName + ".xml";
            DataProcessor dataProcessor = new DataProcessor();
            System.out.println(datafile.getAbsolutePath());
            dataProcessor.setDataTemplate(datafile.getAbsolutePath());
            com.sun.java.util.collections.ArrayList parameters2 = dataProcessor.getParameters();
            com.sun.java.util.collections.Iterator it = parameters2.iterator();

            while (it.hasNext()) {
                Parameter p = (Parameter)it.next();
                if(p.getName().equalsIgnoreCase("V_TRANSNO")) {
                    p.setValue(1L);
                }
            }

            dataProcessor.setParameters(parameters2);
            dataProcessor.setParameters(parameters2);
            dataProcessor.setConnection(datasource.getConnection());
            dataProcessor.setOutput(output);
            dataProcessor.processData();

            RTFProcessor rtfProcessor =
                    new RTFProcessor(file.getAbsolutePath()); //input template
            rtfProcessor.setOutput(styleFile); // output file
            rtfProcessor.process();

            String filename = null;
            filename = reportName;
            String currentTime = new SimpleDateFormat("ddMMyyyy").format(new Date());
            String data = reportName + ".xml";
            String template = styleFile;
            processor.setData(Files.newInputStream(Paths.get(data)));
            processor.setTemplate(template);
            processor.setOutput(filename+"_"+currentTime + ".pdf");
            output = filename+"_" +currentTime+ ".pdf";
            processor.setOutputFormat(FOProcessor.FORMAT_PDF);
            processor.generate();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BadRequestException("Report File not found ");
        } catch (XDOException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}
