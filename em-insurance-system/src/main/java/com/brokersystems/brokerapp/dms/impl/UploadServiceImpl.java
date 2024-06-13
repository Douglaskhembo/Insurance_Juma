package com.brokersystems.brokerapp.dms.impl;

import com.brokersystems.brokerapp.claims.model.ClaimBookings;
import com.brokersystems.brokerapp.claims.model.ClaimRequiredDocs;
import com.brokersystems.brokerapp.claims.model.ClaimUploads;
import com.brokersystems.brokerapp.claims.model.QClaimRequiredDocs;
import com.brokersystems.brokerapp.claims.repository.ClaimRequiredDocsRepo;
import com.brokersystems.brokerapp.claims.repository.ClaimUploadRepo;
import com.brokersystems.brokerapp.claims.repository.ClaimsBookingRepo;
import com.brokersystems.brokerapp.dms.UploadService;
import com.brokersystems.brokerapp.dms.model.UploadBean;
import com.brokersystems.brokerapp.medical.model.MedParReqDocs;
import com.brokersystems.brokerapp.medical.model.MedicalParTrans;
import com.brokersystems.brokerapp.medical.repository.CategoryMembersRepo;
import com.brokersystems.brokerapp.medical.repository.MedParDocsRepo;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.setup.dto.SubClassReqdDocsDTO;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.repository.*;
import com.brokersystems.brokerapp.setup.service.ParamService;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import com.brokersystems.brokerapp.uw.model.QRiskDocs;
import com.brokersystems.brokerapp.uw.model.RiskDocs;
import com.brokersystems.brokerapp.uw.model.RiskTrans;
import com.brokersystems.brokerapp.uw.repository.RiskDocsRepo;
import com.brokersystems.brokerapp.uw.repository.RiskTransRepo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.security.claims.authorization.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by HP on 8/14/2017.
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private ParamService paramService;

    @Autowired
    private RiskDocsRepo riskDocsRepo;

    @Autowired
    private ClaimUploadRepo uploadRepo;

    @Autowired
    private MedParDocsRepo parDocsRepo;

    @Autowired
    private ClientDocsRepo clientDocsRepo;

    @Autowired
    private AccountDocsRepo accountDocsRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private BinderReqrdDocsRepo reqrdDocsRepo;

    @Autowired
    private CategoryMembersRepo categoryMembersRepo;

    @Autowired
    private RiskTransRepo riskTransRepo;


    @Autowired
    private ClaimRequiredDocsRepo claimRequiredDocsRepo;


    @Autowired
    private ClaimsBookingRepo claimsBookingRepo;

    @Autowired
    private SubclassReqDocRepo requiredDocsRepo;

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void uploadRiskDocument(UploadBean uploadBean) throws BadRequestException {
        if(uploadBean.getFile().isEmpty())
            throw new BadRequestException("Upload File is Empty...");
        if(uploadBean.getDocId()==null)
            throw new BadRequestException("Risk Document ID cannot be null");
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        System.out.println("doc id ="+uploadBean.getDocId());

        RiskDocs riskDoc = riskDocsRepo.getRiskDocsVal(uploadBean.getDocId());

        String batchNo = "";
        if(riskDoc.getRiskId()!=null)
         batchNo = "BATCH_"+String.valueOf(riskDoc.getPolId());
        else if(riskDoc.getMember()!=null)
            batchNo = "BATCH_"+String.valueOf(riskDoc.getPolId());
        String riskId ="";
        if(riskDoc.getRiskId()!=null)
         riskId = "RSK_"+String.valueOf(riskDoc.getRiskId());
        else if(riskDoc.getMember()!=null)
            riskId = "MEM_"+String.valueOf(riskDoc.getMember().getMemberShipNo());
        riskDoc.setUploadedFileName(uploadBean.getFile().getOriginalFilename());
        try {
            byte[] bytes = uploadBean.getFile().getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(bytes);
            String hashString = new BigInteger(1, digest).toString(16);
            riskDoc.setCheckSum(hashString);
            riskDoc.setContentType(uploadBean.getFile().getContentType());
            if(riskDoc.getMember()!=null){
                riskDoc.setMember(riskDoc.getMember());
            }
            if(riskDoc.getRiskId()!=null){
                riskDoc.setRisk(riskTransRepo.QueryRiskTrans(riskDoc.getRiskId()));
            }
            riskDocsRepo.save(riskDoc);
            String folderName = uploadFolder+"/"+batchNo+"/"+riskId;
            File file = new File(folderName);
            if(!file.exists())
            FileUtils.forceMkdir(file);
            Path path = Paths.get(uploadFolder+"/"+batchNo+"/"+riskId+"/"+uploadBean.getFile().getOriginalFilename());
            Files.write(path,bytes);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void uploadClientDocument(UploadBean uploadBean) throws BadRequestException {
        if(uploadBean.getFile().isEmpty())
            throw new BadRequestException("Upload File is Empty...");
        if(uploadBean.getDocId()==null)
            throw new BadRequestException("Client Document ID cannot be null");
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        ClientDocs clientDocs =clientDocsRepo.findOne(uploadBean.getDocId());
        ClientDef clientDef = clientDocs.getClientDef();
        String folderName = clientDef.getTenantNumber()+"_"+clientDef.getTenId();
        clientDocs.setUploadedFileName(uploadBean.getFile().getOriginalFilename());
        try {
            byte[] bytes = uploadBean.getFile().getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(bytes);
            String hashString = new BigInteger(1, digest).toString(16);
            clientDocs.setCheckSum(hashString);
            clientDocs.setContentType(uploadBean.getFile().getContentType());
            clientDocs.setFileId(uploadBean.getFileId());
            clientDocs.setClientDef(clientDef);
            clientDocsRepo.save(clientDocs);
            String folderPath = uploadFolder+"/"+folderName;
            File file = new File(folderPath);
            if(!file.exists())
                FileUtils.forceMkdir(file);
            Path path = Paths.get(folderPath+"/"+uploadBean.getFile().getOriginalFilename());
            Files.write(path,bytes);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }


    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void uploadProspectDocument(UploadBean uploadBean) throws BadRequestException {
        if(uploadBean.getFile().isEmpty())
            throw new BadRequestException("Upload File is Empty...");
        if(uploadBean.getDocId()==null)
            throw new BadRequestException("Client Document ID cannot be null");
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        ClientDocs clientDocs =clientDocsRepo.findOne(uploadBean.getDocId());
        ProspectDef clientDef = clientDocs.getProspectDef();
        String folderName = clientDef.getProspShtDesc()+"_"+clientDef.getTenId();
        clientDocs.setUploadedFileName(uploadBean.getFile().getOriginalFilename());
        try {
            byte[] bytes = uploadBean.getFile().getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(bytes);
            String hashString = new BigInteger(1, digest).toString(16);
            clientDocs.setCheckSum(hashString);
            clientDocs.setContentType(uploadBean.getFile().getContentType());
            clientDocs.setProspectDef(clientDef);
            clientDocsRepo.save(clientDocs);
            String folderPath = uploadFolder+"/"+folderName;
            File file = new File(folderPath);
            if(!file.exists())
                FileUtils.forceMkdir(file);
            Path path = Paths.get(folderPath+"/"+uploadBean.getFile().getOriginalFilename());
            Files.write(path,bytes);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void uploadAccountDocument(UploadBean uploadBean) throws BadRequestException {
        if(uploadBean.getFile().isEmpty())
            throw new BadRequestException("Upload File is Empty...");
        if(uploadBean.getDocId()==null)
            throw new BadRequestException("Account Document ID cannot be null");
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        AccountsDocs accountsDocs = accountDocsRepo.findOne(uploadBean.getDocId());
        AccountDef accountDef = accountsDocs.getAccountDef();
        String folderName = accountDef.getShtDesc()+"_"+accountDef.getAcctId();
        accountsDocs.setUploadedFileName(uploadBean.getFile().getOriginalFilename());
        try {
            byte[] bytes = uploadBean.getFile().getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(bytes);
            String hashString = new BigInteger(1, digest).toString(16);
            accountsDocs.setCheckSum(hashString);
            accountsDocs.setContentType(uploadBean.getFile().getContentType());
            accountsDocs.setAccountDef(accountDef);
            accountDocsRepo.save(accountsDocs);
            String folderPath = uploadFolder+"/"+folderName;
            File file = new File(folderPath);
            if(!file.exists())
                FileUtils.forceMkdir(file);
            Path path = Paths.get(folderPath+"/"+uploadBean.getFile().getOriginalFilename());
            Files.write(path,bytes);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    @Modifying
    @Transactional(readOnly = true)
    public byte[] getRiskDocFileDetails(Long rdocId) throws BadRequestException {
        byte[] arr = new byte[0];
        if(rdocId==null)
            throw new BadRequestException("Document does not exist...");
        RiskDocs riskDoc = riskDocsRepo.getRiskDocsVal(rdocId);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String batchNo = "";
        if(riskDoc.getRiskId()!=null)
            batchNo = "BATCH_"+String.valueOf(riskDoc.getPolId());
        else if(riskDoc.getMember()!=null)
            batchNo = "BATCH_"+String.valueOf(riskDoc.getPolId());
        String riskId ="";
        if(riskDoc.getRiskId()!=null)
            riskId = "RSK_"+String.valueOf(riskDoc.getRiskId());
        else if(riskDoc.getMember()!=null)
            riskId = "MEM_"+String.valueOf(riskDoc.getMember().getMemberShipNo());
        String uploadedName = riskDoc.getUploadedFileName();
        Path path = Paths.get(uploadFolder+"/"+batchNo+"/"+riskId+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                arr = Files.readAllBytes(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return arr;
    }

    @Override
    public byte[] getAccountDocument(Long adId) throws BadRequestException {
        byte[] arr = new byte[0];
        if(adId==null)
            throw new BadRequestException("Document does not exist...");
        AccountsDocs accountsDoc = accountDocsRepo.findOne(adId);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String uploadedName = accountsDoc.getUploadedFileName();
        AccountDef accountDef = accountsDoc.getAccountDef();
        String folderName = accountDef.getShtDesc()+"_"+accountDef.getAcctId();
        Path path = Paths.get(uploadFolder+"/"+folderName+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                arr = Files.readAllBytes(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return arr;
    }

    @Override
    public String getAcctDocumentType(Long adId) throws BadRequestException {
        if(adId==null)
            throw new BadRequestException("Document does not exist...");
        AccountsDocs accountsDoc = accountDocsRepo.findOne(adId);
        return accountsDoc.getContentType();
    }

    @Override
    public byte[] getClientDocument(Long adId) throws BadRequestException {
        byte[] arr = new byte[0];
        if(adId==null)
            throw new BadRequestException("Document does not exist...");
        ClientDocs clientDocs = clientDocsRepo.findOne(adId);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String uploadedName = clientDocs.getUploadedFileName();
        String folderName="";
        if (clientDocs.getProspectDef()!=null){
            ProspectDef clientDef = clientDocs.getProspectDef();

            folderName = clientDef.getProspShtDesc()+"_"+clientDef.getTenId();
        } else{
        ClientDef clientDef = clientDocs.getClientDef();

         folderName = clientDef.getTenantNumber()+"_"+clientDef.getTenId();
        }
        Path path = Paths.get(uploadFolder+"/"+folderName+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                arr = Files.readAllBytes(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return arr;
    }

    @Override
    public byte[] getProspectDocument(Long adId) throws BadRequestException {
        byte[] arr = new byte[0];
        if(adId==null)
            throw new BadRequestException("Document does not exist...");
        ClientDocs clientDocs = clientDocsRepo.findOne(adId);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String uploadedName = clientDocs.getUploadedFileName();
        ProspectDef clientDef = clientDocs.getProspectDef();
        String folderName = clientDef.getProspShtDesc()+"_"+clientDef.getTenId();
        Path path = Paths.get(uploadFolder+"/"+folderName+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                arr = Files.readAllBytes(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return arr;
    }

    @Override
    public String getClientDocumentType(Long adId) throws BadRequestException {
        if(adId==null)
            throw new BadRequestException("Document does not exist...");
        ClientDocs clientDocs = clientDocsRepo.findOne(adId);
        return clientDocs.getContentType();
    }

    @Override
    public byte[] getMedClmDocFileDetails(Long rdocId) throws BadRequestException {
        byte[] arr = new byte[0];
        if(rdocId==null)
            throw new BadRequestException("Document does not exist...");
        MedParReqDocs parReqDocs = parDocsRepo.findOne(rdocId);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String folderName =  uploadFolder+"/MED_"+parReqDocs.getParTrans().getParId();
        Path path = Paths.get(folderName+"/"+parReqDocs.getUploadedFileName());
        if(path.toFile().exists()){
            try {
                arr = Files.readAllBytes(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return arr;
    }

    @Override
    public String getClmDocContentTyoe(Long docId) throws BadRequestException {
        if(docId==null)
            throw new BadRequestException("Document does not exist...");
        MedParReqDocs parReqDocs = parDocsRepo.findOne(docId);
        return parReqDocs.getContentType();

    }

    @Override
    public byte[] getGeneralClaimDoc(Long uploadId) throws BadRequestException {
        byte[] arr = new byte[0];
        if(uploadId==null)
            throw new BadRequestException("Document does not exist...");
        ClaimUploads upload = uploadRepo.findOne(uploadId);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String folderName = uploadFolder+"/"+upload.getClaimBookings().getClaimNo();
        Path path = Paths.get(folderName+"/"+upload.getFileName());
        if(path.toFile().exists()){
            try {
                arr = Files.readAllBytes(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return arr;
    }

    @Override
    public byte[] getRequiredClaimDoc(Long clmRequiredId) throws BadRequestException {
        byte[] arr = new byte[0];
        if(clmRequiredId==null)
            throw new BadRequestException("Document does not exist...");
        ClaimRequiredDocs upload = claimRequiredDocsRepo.findOne(clmRequiredId);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String folderName = uploadFolder+"/"+upload.getClaimBookings().getClaimNo();
        Path path = Paths.get(folderName+"/"+upload.getFileName());
        if(path.toFile().exists()){
            try {
                arr = Files.readAllBytes(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return arr;
    }

    @Override
    @Transactional(readOnly = true)
    public String getDocContentTyoe(Long docId) throws BadRequestException {
        if(docId==null)
            throw new BadRequestException("Document does not exist...");
        RiskDocs riskDoc = riskDocsRepo.getRiskDocsVal(docId);
        return riskDoc.getContentType();
    }

    @Override
    public String getGeneralClmContentType(Long docId) throws BadRequestException {
        if(docId==null)
            throw new BadRequestException("Document does not exist...");
        ClaimUploads upload = uploadRepo.findOne(docId);
        return upload.getContentType();
    }


    @Override
    public String getReqClmContentType(Long docId) throws BadRequestException {
        if(docId==null)
            throw new BadRequestException("Document does not exist...");
        ClaimRequiredDocs upload = claimRequiredDocsRepo.findOne(docId);
        return upload.getContentType();
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void deleteRiskDoc(Long docId) throws BadRequestException {
        if(docId==null)
            throw new BadRequestException("Document does not exist...");
        RiskDocs riskDoc = riskDocsRepo.findOne(docId);

        long count  = reqrdDocsRepo.count(QBinderReqrdDocs.binderReqrdDocs.requiredDocs.sclReqrdId.eq(riskDoc.getReqdDocs().getSclReqrdId())
                                         .and(QBinderReqrdDocs.binderReqrdDocs.binderDetail.detId.eq(riskDoc.getBinderDetId()))
                                          .and(QBinderReqrdDocs.binderReqrdDocs.mandatory.eq(true)));
        if(count > 0)
            throw new BadRequestException("Cannot delete Binder mandatory document...");
        if(riskDoc.getReqdDocs().isMandatory())
            throw new BadRequestException("Cannot delete Subclass mandatory document...");
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String batchNo = "";
        if(riskDoc.getRiskId()!=null)
            batchNo = "BATCH_"+String.valueOf(riskDoc.getPolId());
        else if(riskDoc.getMember()!=null)
            batchNo = "BATCH_"+String.valueOf(riskDoc.getPolId());
        String riskId ="";
        if(riskDoc.getRisk()!=null)
            riskId = "RSK_"+String.valueOf(riskDoc.getRiskId());
        else if(riskDoc.getMember()!=null)
            riskId = "MEM_"+String.valueOf(riskDoc.getMember().getMemberShipNo());
        String uploadedName = riskDoc.getUploadedFileName();
        Path path = Paths.get(uploadFolder+"/"+batchNo+"/"+riskId+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        riskDocsRepo.deleteRiskDocs(docId);
    }

    @Override
    public void deleteAcctDocument(Long adId) throws BadRequestException {
        if(adId==null)
            throw new BadRequestException("Document does not exist...");
        AccountsDocs accountsDoc = accountDocsRepo.findOne(adId);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String uploadedName = accountsDoc.getUploadedFileName();
        AccountDef accountDef = accountsDoc.getAccountDef();
        String folderName = accountDef.getShtDesc()+"_"+accountDef.getAcctId();
        Path path = Paths.get(uploadFolder+"/"+folderName+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        accountDocsRepo.delete(adId);
    }

    @Override
    public void deleteClntDocument(Long adId) throws BadRequestException {
        if(adId==null)
            throw new BadRequestException("Document does not exist...");
        ClientDocs clientDocs = clientDocsRepo.findOne(adId);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String uploadedName = clientDocs.getUploadedFileName();
        ClientDef clientDef = clientDocs.getClientDef();
        String folderName = clientDef.getTenantNumber()+"_"+clientDef.getTenId();
        Path path = Paths.get(uploadFolder+"/"+folderName+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        clientDocsRepo.delete(adId);
    }


    @Override
    public void deletePrspctDocument(Long adId) throws BadRequestException {
        if(adId==null)
            throw new BadRequestException("Document does not exist...");
        ClientDocs clientDocs = clientDocsRepo.findOne(adId);
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String uploadedName = clientDocs.getUploadedFileName();
        ProspectDef clientDef = clientDocs.getProspectDef();
        String folderName = clientDef.getProspShtDesc()+"_"+clientDef.getTenId();
        Path path = Paths.get(uploadFolder+"/"+folderName+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        clientDocsRepo.delete(adId);
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void deleteClmDoc(Long docId) throws BadRequestException {
        if(docId==null)
            throw new BadRequestException("Document does not exist...");
        MedParReqDocs parReqDocs = parDocsRepo.findOne(docId);
        if(parReqDocs.getReqdDocs().isMandatory())
            throw new BadRequestException("Cannot delete mandatory document...");
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String folderName = uploadFolder+"/MED_"+parReqDocs.getParTrans().getParId();
        String uploadedName = parReqDocs.getUploadedFileName();
        Path path = Paths.get(folderName+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        parDocsRepo.delete(docId);
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void deleteClmReqDoc(Long docId) throws BadRequestException {
        if(docId==null)
            throw new BadRequestException("Document does not exist...");
        ClaimRequiredDocs reqDocs = claimRequiredDocsRepo.findOne(docId);
        if(requiredDocsRepo.count(QSubClassReqdDocs.subClassReqdDocs.requiredDoc.reqId.eq(reqDocs.getRequiredDoc().getReqId())
                .and(QSubClassReqdDocs.subClassReqdDocs.requiredDoc.appliesLossOpening.eq(true))
                .and(QSubClassReqdDocs.subClassReqdDocs.mandatory.eq(true)))!=0)
            throw new BadRequestException("Cannot delete mandatory document...");

        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String folderName = uploadFolder+"/"+reqDocs.getClaimBookings().getClaimNo();
        String uploadedName = reqDocs.getFileName();
        Path path = Paths.get(folderName+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        claimRequiredDocsRepo.delete(docId);
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void deleteClmUploadDoc(Long docId) throws BadRequestException {
        if(docId==null)
            throw new BadRequestException("Document does not exist...");
        ClaimUploads uploadDocs = uploadRepo.findOne(docId);

        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        String folderName = uploadFolder+"/"+uploadDocs.getClaimBookings().getClaimNo();
        String uploadedName = uploadDocs.getFileName();
        Path path = Paths.get(folderName+"/"+uploadedName);
        if(path.toFile().exists()){
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        uploadRepo.delete(docId);
    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void uploadGeneralClaimDoc(ClaimUploads upload) throws BadRequestException {
        if(upload.getFile().isEmpty())
            throw new BadRequestException("Upload File is Empty...");
        if(upload.getClaimBookings()==null)
            throw new BadRequestException("Claim File cannot be null");
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        ClaimBookings bookings = upload.getClaimBookings();
        try {
            byte[] bytes = upload.getFile().getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(bytes);
            String hashString = new BigInteger(1, digest).toString(16);
            upload.setCheckSum(hashString);
            upload.setFileName(upload.getFile().getOriginalFilename());
            upload.setDateUploaded(new Date());
            upload.setUploadedBy(userUtils.getCurrentUser());
            upload.setContentType(upload.getFile().getContentType());
            uploadRepo.save(upload);
            String folderName = uploadFolder+"/"+bookings.getClaimNo();
            File file = new File(folderName);
            if(!file.exists())
                FileUtils.forceMkdir(file);
            Path path = Paths.get(uploadFolder+"/"+bookings.getClaimNo()+"/"+upload.getFile().getOriginalFilename());
            Files.write(path,bytes);
        } catch (IOException | NoSuchAlgorithmException  e) {
            throw new BadRequestException(e.getMessage());
        }

    }


    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void uploadClaimReqDoc(ClaimRequiredDocs newUpload) throws BadRequestException {
        if (("N").equalsIgnoreCase(newUpload.getTransType())) // N- upload OPTION , E - EDIT OPTION
        {
            if(newUpload.getFile().isEmpty())
                throw new BadRequestException("Upload File is Empty...");
        }
        if (newUpload.getClmRequiredId()==null){
            throw new BadRequestException("No document record to update...");
        }
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        ClaimRequiredDocs upload = claimRequiredDocsRepo.findOne(QClaimRequiredDocs.claimRequiredDocs.clmRequiredId.eq(newUpload.getClmRequiredId()));
        ClaimBookings bookings = upload.getClaimBookings();
        try {
            if (newUpload.getTransType().equalsIgnoreCase("N")) // N- upload OPTION , E - EDIT OPTION
            {
                if (!(newUpload.getFile().isEmpty())) {
                    byte[] bytes = newUpload.getFile().getBytes();
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    byte[] digest = md5.digest(bytes);
                    String hashString = new BigInteger(1, digest).toString(16);
                    upload.setCheckSum(hashString);
                    upload.setFileName(newUpload.getFile().getOriginalFilename());
                    upload.setDateReceived(new Date());
                    upload.setDateSubmitted(new Date());
                    upload.setUserReceived(userUtils.getCurrentUser());
                    upload.setContentType(newUpload.getFile().getContentType());
                    upload.setRemarks(newUpload.getRemarks());
                    upload.setDocRefNo(newUpload.getDocRefNo());

                    claimRequiredDocsRepo.save(upload);

                    String folderName = uploadFolder + "/" + bookings.getClaimNo();
                    File file = new File(folderName);
                    if (!file.exists())
                        FileUtils.forceMkdir(file);
                    Path path = Paths.get(uploadFolder + "/" + bookings.getClaimNo() + "/" + newUpload.getFile().getOriginalFilename());
                    Files.write(path, bytes);
                }
            }else {
                upload.setRemarks(newUpload.getRemarks());
                upload.setDocRefNo(newUpload.getDocRefNo());
                claimRequiredDocsRepo.save(upload);
            }
        } catch (IOException | NoSuchAlgorithmException  e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    @Override
    @Modifying
    @Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
    public void uploadClaimDocument(MedParReqDocs parReqDocs) throws BadRequestException {
        if(parReqDocs.getFile().isEmpty()){
            throw new BadRequestException("Upload File is Empty..");
        }
        if(parReqDocs.getReqdDocs()==null)
            throw new BadRequestException("Select Document Type to be uploaded");
        String uploadFolder = paramService.getParameterString("APP_UPLOAD_FOLDER");
        MedicalParTrans parTrans = parReqDocs.getParTrans();
        try {
            byte[] bytes = parReqDocs.getFile().getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(bytes);
            String hashString = new BigInteger(1, digest).toString(16);
            parReqDocs.setContentType(parReqDocs.getFile().getContentType());
            parReqDocs.setCheckSum(hashString);
            parReqDocs.setUploadedFileName(parReqDocs.getFile().getOriginalFilename());
            parReqDocs.setDateUploaded(new Date());
            parDocsRepo.save(parReqDocs);
            String folderName = uploadFolder+"/MED_"+parTrans.getParId();
            File file = new File(folderName);
            if(!file.exists())
                FileUtils.forceMkdir(file);
            Path path = Paths.get(folderName+"/"+parReqDocs.getFile().getOriginalFilename());
            Files.write(path,bytes);

        } catch (IOException | NoSuchAlgorithmException  e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<SubClassReqdDocsDTO> findUnassignedRiskDocs(Long clmId, String docName) {
        List<Object[]> clmReqdDocs = requiredDocsRepo.getUnassignedClaimsReqDocs(clmId, (docName!=null)?"%"+docName+"%":"%%");
        final List<SubClassReqdDocsDTO> reqdDocsDTOList = new ArrayList<>();
        for(Object[] tran:clmReqdDocs){
            SubClassReqdDocsDTO reqdDocsDTO = new SubClassReqdDocsDTO();
            reqdDocsDTO.setSclReqrdId(((BigDecimal)tran[0]).longValue());
            reqdDocsDTO.setReqShtDesc((String) tran[1]);
            reqdDocsDTO.setReqDesc((String) tran[2]);
            reqdDocsDTOList.add(reqdDocsDTO);
        }
        return reqdDocsDTOList;
    }

    @Override
    public File getMedMembersTemplate() throws BadRequestException{
        File file;
        try {
            file = ResourceUtils.getFile("classpath:templates/medical_upload_template.xls");
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
        return file;
    }
}
