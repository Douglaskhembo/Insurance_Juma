package com.brokersystems.brokerapp.mail.service.impl;

import com.brokersystems.brokerapp.claims.model.ClaimBookings;
import com.brokersystems.brokerapp.claims.repository.ClaimsBookingRepo;
import com.brokersystems.brokerapp.events.SendEmailEvent;
import com.brokersystems.brokerapp.mail.model.*;
import com.brokersystems.brokerapp.mail.service.Mailer;
import com.brokersystems.brokerapp.medical.model.MedicalParTrans;
import com.brokersystems.brokerapp.medical.repository.MedicalParRepo;
import com.brokersystems.brokerapp.quotes.model.QuoteTrans;
import com.brokersystems.brokerapp.quotes.repository.QuotTransRepo;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.TemplateMerger;
import com.brokersystems.brokerapp.setup.dto.OrganizationDTO;
import com.brokersystems.brokerapp.setup.model.Organization;
import com.brokersystems.brokerapp.setup.service.OrganizationService;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import com.brokersystems.brokerapp.uw.repository.PolicyTransRepo;
import com.brokersystems.brokerapp.uw.service.PolicyTransService;
import de.siegmar.fastcsv.writer.CsvWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by HP on 8/9/2017.
 */
@Service
public class MailerImpl implements Mailer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateMerger templateMerger;

    @Autowired
    private Environment env;

    @Autowired
    private PolicyTransRepo policyTransRepo;

    @Autowired
    private QuotTransRepo quotTransRepo;

    @Autowired
    private DataSource datasource;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private MedicalParRepo medicalParRepo;

    @Autowired
    private ClaimsBookingRepo claimsBookingRepo;


    @Override
    public void sendSimpleEmail(MailMessageBean message, Long transCode, String transType) throws BadRequestException {

        if (transType == null)
            throw new BadRequestException("Unable to determine Transaction Type To Merge the Message");

        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String emailMessage = "";
                String emailReceivers = "";
                if ("P".equalsIgnoreCase(transType)) {
                    PolicyTrans policyTrans = policyTransRepo.findOne(transCode);
                    emailMessage = templateMerger.mergePolicyDetails(policyTrans, message.getMessage());
                    emailReceivers = getEmailReceivers(transCode, "P", message.getReceiverType());
                }
                emailReceivers+=","+message.getSendTo();
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setTo(StringUtils.split(emailReceivers, ","));
                if (!message.getSendBcc().isEmpty())
                    messageHelper.setBcc(StringUtils.split(message.getSendBcc(),","));
                if (!message.getSendCC().isEmpty())
                    messageHelper.setCc(StringUtils.split(message.getSendCC(),","));
                messageHelper.setFrom(env.getProperty("sender.username"),organizationService.getOrganizationDetails().getOrgName());
                messageHelper.setSubject(message.getSubject());
                messageHelper.setText(emailMessage, true);

            }
        };
        mailSender.send(mimeMessagePreparator);
    }

    @Override
    public void sendEmail(MailMessageBean messageBean) throws BadRequestException {
        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String emailMessage = messageBean.getMessage();
                String emailReceivers = messageBean.getSendTo();
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setTo(StringUtils.split(emailReceivers, ","));
                messageHelper.setFrom(env.getProperty("sender.username"),organizationService.getOrganizationDetails().getOrgName());
                messageHelper.setSubject(messageBean.getSubject());
                messageHelper.setText(emailMessage, true);
            }
        };
        mailSender.send(mimeMessagePreparator);
    }


    @Override
    public void sendSmsAttachments(MailMessageBean message, Long transCode, String transType, HttpServletRequest request) throws BadRequestException {
        if (transType == null)
            throw new BadRequestException("Unable to determine Transaction Type To Merge the Message");


                String emailMessage = null;
                if ("P".equalsIgnoreCase(transType)) {
                    PolicyTrans policyTrans = policyTransRepo.findOne(transCode);
                    emailMessage = templateMerger.mergePolicyDetails(policyTrans, message.getMessage());
                }
                else  if ("Q".equalsIgnoreCase(transType)) {
                    QuoteTrans quoteTrans = quotTransRepo.findOne(transCode);
                    emailMessage = templateMerger.mergeQuoteDetails(quoteTrans, message.getMessage());
                }
                else  if ("M".equalsIgnoreCase(transType)) {
                    MedicalParTrans medicalParTrans = medicalParRepo.findOne(transCode);
                    emailMessage = templateMerger.mergeMedClaims(medicalParTrans, message.getMessage());
                }
        try {
            File dir = new File("sms");
            if(!dir.exists()) dir.mkdir();
            File file = new File("sms/FIA"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".csv");
            if(!file.exists()) file.createNewFile();
            CsvWriter csvWriter = new CsvWriter();

            Collection<String[]> data = new ArrayList<>();
            data.add(new String[] { message.getSendTo(), emailMessage.replace("<div>","").replace("</div>","") });


            csvWriter.write(file, StandardCharsets.UTF_8, data);

            try (FileWriter writer = new FileWriter("sms/sms.bat")) {
                writer.write("move *.csv  Z:/");
            }


//            Commandline commandLine = new Commandline();
//
//            File executable = new File("sms/sms.bat");
//            commandLine.setExecutable(executable.getAbsolutePath());
//
//            WriterStreamConsumer systemOut = new WriterStreamConsumer(
//                    new OutputStreamWriter(System.out));
//
//            WriterStreamConsumer systemErr = new WriterStreamConsumer(
//                    new OutputStreamWriter(System.out));
//
//            int returnCode = CommandLineUtils.executeCommandLine(commandLine, systemOut, systemErr,0);
//            if (returnCode != 0) {
//                System.out.println("Something Bad Happened!");
//            } else {
//                System.out.println("Taaa!! ddaaaaa!!");
//            };

        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }


    }






    @Override
    public void sendEmailAttachments(MailMessageBean message, Long transCode, String transType,HttpServletRequest request) throws BadRequestException {
        if (transType == null)
            throw new BadRequestException("Unable to determine Transaction Type To Merge the Message");

        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String emailMessage = "";
                String emailReceivers = "";
                if ("P".equalsIgnoreCase(transType)) {
                    PolicyTrans policyTrans = policyTransRepo.findOne(transCode);
                    emailMessage = templateMerger.mergePolicyDetails(policyTrans, message.getMessage());
                    emailReceivers = getEmailReceivers(transCode, "P", message.getReceiverType());
                }
                else  if ("Q".equalsIgnoreCase(transType)) {
                    QuoteTrans quoteTrans = quotTransRepo.findOne(transCode);
                    emailMessage = templateMerger.mergeQuoteDetails(quoteTrans, message.getMessage());
                    emailReceivers = getEmailReceivers(transCode, "Q", message.getReceiverType());
                }
                else  if ("M".equalsIgnoreCase(transType)) {
                    MedicalParTrans medicalParTrans = medicalParRepo.findOne(transCode);
                    emailMessage = templateMerger.mergeMedClaims(medicalParTrans, message.getMessage());
                    emailReceivers = getEmailReceivers(transCode, "M", message.getReceiverType());
                } else  if ("C".equalsIgnoreCase(transType)) {
                    ClaimBookings booking = claimsBookingRepo.findOne(transCode);
                    emailMessage = templateMerger.mergeClaims(booking, message.getMessage());
                    emailReceivers = getEmailReceivers(transCode, "C", message.getReceiverType());
                }

                    emailReceivers+=","+message.getSendTo();
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
                messageHelper.setTo(StringUtils.split(emailReceivers, ","));
                if (!message.getSendBcc().isEmpty())
                    messageHelper.setBcc(StringUtils.split(message.getSendBcc(),","));
                if (!message.getSendCC().isEmpty())
                    messageHelper.setCc(StringUtils.split(message.getSendCC(),","));
                messageHelper.setFrom(env.getProperty("sender.username"),organizationService.getOrganizationDetails().getOrgName());
                messageHelper.setSubject(message.getSubject());
                messageHelper.setText(emailMessage, true);
                message.getReports().stream().forEach(a -> {
                    byte[] f = new byte[0];
                    if ("P".equalsIgnoreCase(transType)) {
                        f = generateUWReport(transCode, a, request);
                    }
                    else if ("Q".equalsIgnoreCase(transType)) {
                        f = generateQuoteReport(transCode, a, request);
                    }
                    else if ("M".equalsIgnoreCase(transType)) {
                        f = generateMedClaimsReport(transCode, a, request);
                    }
                    else if ("C".equalsIgnoreCase(transType)) {
                        f = generateClaimsReport(transCode, a, request);
                    }
                    if(f.length > 0) {
                        try {
                            messageHelper.addAttachment(a+".pdf", new ByteArrayResource(f));
                        } catch (MessagingException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                });

            }
        };
        mailSender.send(mimeMessagePreparator);
    }

    @Override
    public String getSMSReceivers(Long transCode, String transType, String receiver) throws BadRequestException {
        if (transType == null)
            throw new BadRequestException("Unable to determine Transaction Type To Merge the Message");
        if (receiver == null)
            throw new BadRequestException("Receiver Type Cannot be null...");
        if ("P".equalsIgnoreCase(transType)) {
            PolicyTrans policyTrans = policyTransRepo.findOne(transCode);
            if ("B".equalsIgnoreCase(receiver)) {
                if (policyTrans.getClient().getSmsPrefix() != null) {
                    return "0"+ policyTrans.getClient().getSmsPrefix().getPrefixName()+policyTrans.getClient().getSmsNumber();
                }
                if (policyTrans.getAgent().getPhoneNo() != null) {
                    return "0"+ policyTrans.getAgent().getPhoneNo();
                }
            } else if ("C".equalsIgnoreCase(receiver)) {
                if (policyTrans.getClient().getSmsPrefix() != null) {
                    return "0"+ policyTrans.getClient().getSmsPrefix().getPrefixName()+policyTrans.getClient().getSmsNumber();
                }
            } else if ("A".equalsIgnoreCase(receiver)) {
                if (policyTrans.getAgent().getPhoneNo() != null) {
                    return "0"+ policyTrans.getAgent().getPhoneNo();
                }
            }

        } else if ("Q".equalsIgnoreCase(transType)) {
            QuoteTrans policyTrans = quotTransRepo.findOne(transCode);
            System.out.println("Receiver "+receiver+ " prefix "+policyTrans.getClient());
            if ("C".equalsIgnoreCase(receiver)) {
                if("C".equalsIgnoreCase(policyTrans.getClientType())) {
                    if (policyTrans.getClient().getSmsPrefix() != null) {
                        return "0"+ policyTrans.getClient().getSmsPrefix().getPrefixName()+policyTrans.getClient().getSmsNumber();
                    }
                }
                else if("P".equalsIgnoreCase(policyTrans.getClientType())) {
                    if (policyTrans.getProspect().getSmsPrefix() != null) {
                        return "0"+ policyTrans.getProspect().getSmsPrefix().getPrefixName()+policyTrans.getProspect().getSmsNumber();
                    }
                }
            }

        }
        else if ("M".equalsIgnoreCase(transType)) {
            MedicalParTrans parTrans = medicalParRepo.findOne(transCode);
            if ("S".equalsIgnoreCase(receiver)) {
                if (parTrans.getProviderContracts().getServiceProviders().getEmail() != null) {
                    return "0"+ parTrans.getProviderContracts().getServiceProviders().getTelNumber();
                }
            }

        }
        return null;
    }

    @Override
    public String getEmailReceivers(Long transCode, String transType, String receiver) throws BadRequestException {
        if (transType == null)
            throw new BadRequestException("Unable to determine Transaction Type To Merge the Message");
        if (receiver == null)
            throw new BadRequestException("Receiver Type Cannot be null...");
        StringBuilder emailHolder = new StringBuilder();
        if ("P".equalsIgnoreCase(transType)) {
            PolicyTrans policyTrans = policyTransRepo.findOne(transCode);
            if ("B".equalsIgnoreCase(receiver)) {
                if (policyTrans.getClient().getEmailAddress() != null) {
                    emailHolder.append(policyTrans.getClient().getEmailAddress());
                    emailHolder.append(",");
                }
                if (policyTrans.getAgent().getEmail() != null) {
                    emailHolder.append(policyTrans.getAgent().getEmail());
                }
            } else if ("C".equalsIgnoreCase(receiver)) {
                if (policyTrans.getClient().getEmailAddress() != null) {
                    emailHolder.append(policyTrans.getClient().getEmailAddress());
                }
            } else if ("A".equalsIgnoreCase(receiver)) {
                if (policyTrans.getAgent().getEmail() != null) {
                    emailHolder.append(policyTrans.getAgent().getEmail());
                }
            }

        } else if ("Q".equalsIgnoreCase(transType)) {
            QuoteTrans policyTrans = quotTransRepo.findOne(transCode);
            if ("C".equalsIgnoreCase(receiver)) {
                if("C".equalsIgnoreCase(policyTrans.getClientType())) {
                    if (policyTrans.getClient().getEmailAddress() != null) {
                        emailHolder.append(policyTrans.getClient().getEmailAddress());
                    }
                }
                else if("P".equalsIgnoreCase(policyTrans.getClientType())) {
                    if (policyTrans.getProspect().getEmailAddress() != null) {
                        emailHolder.append(policyTrans.getProspect().getEmailAddress());
                    }
                }
            }

        }
        else if ("M".equalsIgnoreCase(transType)) {
            MedicalParTrans parTrans = medicalParRepo.findOne(transCode);
            if ("S".equalsIgnoreCase(receiver)) {
                if (parTrans.getProviderContracts().getServiceProviders().getEmail() != null) {
                    emailHolder.append(parTrans.getProviderContracts().getServiceProviders().getEmail());
                }
            }

        }else if ("C".equalsIgnoreCase(transType)) {
            ClaimBookings booking = claimsBookingRepo.findOne(transCode);
            PolicyTrans policyTrans = booking.getRisk().getPolicy();
            if ("B".equalsIgnoreCase(receiver)) {
                if (policyTrans.getClient().getEmailAddress() != null) {
                    emailHolder.append(policyTrans.getClient().getEmailAddress());
                    emailHolder.append(",");
                }
                if (policyTrans.getAgent().getEmail() != null) {
                    emailHolder.append(policyTrans.getAgent().getEmail());
                }
            } else if ("C".equalsIgnoreCase(receiver)) {
                if (policyTrans.getClient().getEmailAddress() != null) {
                    emailHolder.append(policyTrans.getClient().getEmailAddress());
                }
            } else if ("A".equalsIgnoreCase(receiver)) {
                if (policyTrans.getAgent().getEmail() != null) {
                    emailHolder.append(policyTrans.getAgent().getEmail());
                }
            }

        }

        return emailHolder.toString();
    }

    private byte[] generateMedClaimsReport(Long parCode, String type, HttpServletRequest request)  {
        String databaseType = env.getProperty("database_type");
        String resourcePath = "/reports/";
        String fileName = "";
        if(StringUtils.equalsIgnoreCase("oracle",databaseType)){
            resourcePath = "/oracle_reports/";
        }
        else if(StringUtils.equalsIgnoreCase("mssql",databaseType)){
            resourcePath = "/mssql/";
        }
        else if(StringUtils.equalsIgnoreCase("postgres",databaseType))
            resourcePath = "/reports/";
        switch (type){
            case "IL":
                resourcePath = resourcePath+"rpt_inp_undertaking_letter.jasper";
                fileName = "inp_undertaking_letter.pdf";
                break;
            case "OL":
                resourcePath = resourcePath+"rpt_outp_undertaking_letter.jasper";
                fileName = "outp_undertaking_letter.pdf";
                break;
            case "DL":
                resourcePath = resourcePath+"rpt_med_clm_rejection_letter.jasper";
                fileName = "clm_rejection_letter.pdf";
                break;
            default:
        }
        InputStream jasperStream = this.getClass().getResourceAsStream(resourcePath);
        Map<String, Object> params = new HashMap<>();
        OrganizationDTO organization = organizationService.getOrganizationLogoDetails();

        File file = null;
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
            BufferedImage image = ImageIO.read(in);
            params.put("logo", image );
            params.put("parId", parCode);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException | IOException | SQLException e) {
            System.err.println("Error Generating report..."+e.getMessage());
        }
        finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    //Error closing conn....
                }
            }
        }
        return new byte[0];
    }

    private byte[] generateClaimsReport(Long clmId, String type, HttpServletRequest request)  {
        String databaseType = env.getProperty("database_type");
        String resourcePath = "/reports/";
        String fileName = "";
        if(StringUtils.equalsIgnoreCase("oracle",databaseType)){
            resourcePath = "/oracle_reports/";
        }
        else if(StringUtils.equalsIgnoreCase("mssql",databaseType)){
            resourcePath = "/mssql/";
        }
        else if(StringUtils.equalsIgnoreCase("postgres",databaseType))
            resourcePath = "/reports/";
        switch (type){
            case "SY":
                resourcePath = resourcePath+"rpt_claim_synopsis.jasper";
                fileName = "rpt_claim_synopsis.pdf";
                break;
            default:
        }
        InputStream jasperStream = this.getClass().getResourceAsStream(resourcePath);
        Map<String, Object> params = new HashMap<>();
        OrganizationDTO organization = organizationService.getOrganizationLogoDetails();

        File file = null;
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
            BufferedImage image = ImageIO.read(in);
            params.put("logo", image );
            params.put("clmid", clmId);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException | IOException | SQLException e) {
            System.err.println("Error Generating report..."+e.getMessage());
        }
        finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    //Error closing conn....
                }
            }
        }
        return new byte[0];
    }


    private byte[] generateQuoteReport(Long quoteCode, String type, HttpServletRequest request)  {
        String databaseType = env.getProperty("database_type");
        String resourcePath = "/reports/";
        String fileName = "";
        if(StringUtils.equalsIgnoreCase("oracle",databaseType)){
            resourcePath = "/oracle_reports/";
        }
        else if(StringUtils.equalsIgnoreCase("mssql",databaseType)){
            resourcePath = "/mssql/";
        }
        else if(StringUtils.equalsIgnoreCase("postgres",databaseType))
                resourcePath = "/reports/";
        System.out.println("resourcePath  =="+resourcePath+" ; db =="+ databaseType);
        switch (type){
            case "CQ":
                resourcePath = resourcePath+"rpt_client_quote.jasper";
                fileName = "client_quote.pdf";
                break;
            case "QI":
                resourcePath = resourcePath+"rpt_quote_info.jasper";
                fileName = "quote_info.pdf";
                break;
            default:
        }
        InputStream jasperStream = this.getClass().getResourceAsStream(resourcePath);
        Map<String, Object> params = new HashMap<>();
        OrganizationDTO organization = organizationService.getOrganizationLogoDetails();

        File file = null;
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
            BufferedImage image = ImageIO.read(in);
            params.put("logo", image );
            params.put("quotId", quoteCode);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException | IOException | SQLException e) {
            System.err.println("Error Generating report..."+e.getMessage());
        }
        finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    //Error closing conn....
                }
            }
        }
        return new byte[0];
    }

    private byte[] generateUWReport(Long polCode, String type, HttpServletRequest request)  {
        String databaseType = env.getProperty("database_type");
        String resourcePath = "/reports/";
        String fileName = "";
        PolicyTrans policyTrans = policyTransRepo.findOne(polCode);
        String renewalTemplate = "";
        if(policyTrans.getProduct().isMotorProduct()){
            renewalTemplate = "rpt_renewal_notice_non_motor.jasper";
        }
        else{
            renewalTemplate = "rpt_renewal_notice_motor.jasper";
        }
        if(StringUtils.equalsIgnoreCase("oracle",databaseType)){
              resourcePath = "/oracle_reports/";
        }
       else if(StringUtils.equalsIgnoreCase("mssql",databaseType)){
            resourcePath = "/mssql/";
        }
        else if(StringUtils.equalsIgnoreCase("postgres",databaseType))
            resourcePath = "/reports/";
        switch (type){
            case "RN":
                resourcePath = resourcePath+"rpt_risk_note.jasper";
                fileName = "risk_note.pdf";
                break;
            case "DN":
                resourcePath = resourcePath+"rpt_debit_note_client.jasper";
                fileName = "debit_note.pdf";
                break;
            case "PW":
                resourcePath = resourcePath+"rpt_prem_working.jasper";
                fileName = "prem_working.pdf";
                break;
            case "RT":
                resourcePath = resourcePath+renewalTemplate;
                fileName = "ren_notice.pdf";
                break;
            case "VR":
                resourcePath = resourcePath+"rpt_valuation_rpt.jasper";
                fileName = "valuation_rpt.pdf";
                break;
            case "ER":
                resourcePath = resourcePath+"rpt_endorse.jasper";
                fileName = "rpt_endorse.pdf";
                break;
            default:
        }
        InputStream jasperStream = this.getClass().getResourceAsStream(resourcePath);
        Map<String, Object> params = new HashMap<>();
        OrganizationDTO organization = organizationService.getOrganizationLogoDetails();

        Connection conn = null;
        try {
            conn = datasource.getConnection();
            InputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get(organization.getOrgLogo())));
            BufferedImage image = ImageIO.read(in);
            params.put("logo", image );
            params.put("polId", polCode);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            return bytes;
        } catch (JRException | IOException | SQLException e) {
             System.err.println("Error Generating report..."+e.getMessage());
        }
        finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    //Error closing conn....
                }
            }
        }
        return new byte[0];
    }
}
