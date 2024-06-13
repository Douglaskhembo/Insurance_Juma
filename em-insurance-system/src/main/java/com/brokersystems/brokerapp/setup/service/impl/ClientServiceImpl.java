package com.brokersystems.brokerapp.setup.service.impl;

import com.brokersystems.brokerapp.claims.dtos.ClaimEnquiryDTO;
import com.brokersystems.brokerapp.claims.dtos.ClaimantsDTO;
import com.brokersystems.brokerapp.kie.rules.ClientRulesExecutor;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.AuditTrailLogger;
import com.brokersystems.brokerapp.server.utils.DateUtilities;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.server.utils.ValidatorUtils;
import com.brokersystems.brokerapp.setup.dto.ClientDTO;
import com.brokersystems.brokerapp.setup.dto.ClientDocsDTO;
import com.brokersystems.brokerapp.setup.dto.ClientTransactionDTO;
import com.brokersystems.brokerapp.setup.dto.ProspectsDTO;
import com.brokersystems.brokerapp.setup.repository.*;
import com.brokersystems.brokerapp.setup.service.ParamService;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.setup.model.*;
import com.brokersystems.brokerapp.setup.service.ClientService;
import com.mysema.query.types.Predicate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private ClientRepository clientRepo;
	@Autowired
	private ClientTitleRepo clientTitleRepo;
	@Autowired
	private ProspectsRepo prospectsRepo;
	@Autowired
	private ValidatorUtils validator;
	@Autowired
	private ClientDocsRepo clientDocsRepo;
	@Autowired
	private RequiredDocsRepo requiredDocsRepo;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private ClientRulesExecutor clientRulesExecutor;
	@Autowired
	private AuditTrailLogger auditTrailLogger;
    @Autowired
    private ParamService paramService;
	@Autowired
	private AccountRepo accountRepo;
	@Autowired
	private ClientTypeRepo clientTypeRepo;
	@Autowired
	private OrgBranchRepository branchRepository;
	@Autowired
	private MobPrefixRepo mobPrefixRepo;


	@Override
	@Transactional(readOnly = true)
	public DataTablesResult<ClientDTO> findAllClients(DataTablesRequest request,  String search) {
		if(search==null) search="%"; else search="%"+search+"%";
		final List<ClientDTO> clientDTOList = new ArrayList<>();
		List<Object[]> clientsList = clientRepo.searchClientsList(search.toLowerCase(),
											request.getPageNumber(),request.getPageSize());
		long rowCount = 0l;
		if(!clientsList.isEmpty()) rowCount = ((BigInteger)clientsList.get(0)[13]).intValue();
		for(Object[] client:clientsList ){
			ClientDTO clientDTO = new ClientDTO();
			clientDTO.setTenantNumber((String) client[0]);
			clientDTO.setClientName(client[1] + " "+ client[2]);
			clientDTO.setIdNo((String) client[3]);
			clientDTO.setEmailAddress((String) client[4]);
			clientDTO.setPhoneNo((String) client[5]);
			clientDTO.setClientType((String) client[6]);
			clientDTO.setStatus((String) client[7]);
			clientDTO.setDateCreated((Date) client[8]);
			clientDTO.setTenId(((BigInteger) client[9]).longValue());
			clientDTO.setUsername((String) client[10]);
			clientDTO.setAuthStatus((String) client[11]);
			clientDTO.setHashCode((String) client[12]);
			clientDTOList.add(clientDTO);
		}
		Page<ClientDTO>  page = new PageImpl<>(clientDTOList,request, rowCount);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public DataTablesResult<ClientTransactionDTO> findClientTransactions(DataTablesRequest request, Long clientId) {
		final String search = (request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%";
		final List<ClientTransactionDTO> transactionList = new ArrayList<>();
		List<Object[]> transList = clientRepo.getClientTransactions(clientId, search,
				request.getPageNumber(),request.getPageSize());
		long rowCount = 0l;
		if(!transList.isEmpty()) rowCount = ((BigInteger)transList.get(0)[10]).intValue();
		for(Object[] trans:transList ){
			ClientTransactionDTO transaction = new ClientTransactionDTO();
			transaction.setGrossAmount((BigDecimal) trans[0]);
			transaction.setTransDate((Date) trans[1]);
			transaction.setNetAmount((BigDecimal) trans[2]);
			transaction.setTransactionBalance((BigDecimal) trans[3]);
			transaction.setTransactionRef((String) trans[4]);
			transaction.setTransactionType((String) trans[5]);
			transaction.setWefDate((Date) trans[6]);
			transaction.setWetDate((Date) trans[7]);
			transaction.setPolicyNo((String) trans[8]);
			transaction.setProduct((String) trans[9]);
			transactionList.add(transaction);
		}
		Page<ClientTransactionDTO>  page = new PageImpl<>(transactionList,request, rowCount);
		return new DataTablesResult<>(request, page);
	}

	@Override
	@Modifying
    @Transactional(readOnly=false)
	public ClientDef defineClient(ClientDef tenant) {
		return clientRepo.save(tenant);
	}

	@Override
	@Modifying
    @Transactional(readOnly=false)
	public void deleteClient(Long tenId) {
		clientRepo.delete(tenId);
		
	}

	@Override
	@Transactional(readOnly=true)
	public ClientDef getClientDetails(Long tenId) {
		return clientRepo.findByTenId(tenId).orElse(new ClientDef());
	}

	@Override
	@Transactional(readOnly=true)
	public DataTablesResult<ProspectsDTO> findAllProspects(DataTablesRequest request) throws IllegalAccessException {
		final String search = (request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue()+"%":"%%";
		List<Object[]> prospcts = prospectsRepo.findProspctsListing(search.toLowerCase(),request.getPageNumber(), request.getPageSize());
		final List<ProspectsDTO> prospectsDTOList = new ArrayList<>();
		long rowCount = 0l;
		if(!prospcts.isEmpty()) rowCount = ((BigInteger)prospcts.get(0)[17]).intValue();
		for(Object[] prospect:prospcts){
			ProspectsDTO prospectsDTO = new ProspectsDTO();
			prospectsDTO.setTenId(((BigInteger)prospect[0]).longValue());
			prospectsDTO.setFname((String) prospect[1]);
			prospectsDTO.setOtherNames((String) prospect[2]);
			prospectsDTO.setPhoneNo((String) prospect[3]);
			prospectsDTO.setDob((Date) prospect[4]);
			prospectsDTO.setClientType((String) prospect[5]);
			if(prospect[6]!=null)
			prospectsDTO.setClientTypeId(((BigInteger)prospect[6]).longValue());
			prospectsDTO.setStatus((String) prospect[7]);
			prospectsDTO.setCategory((String) prospect[8]);
			if(prospect[9]!=null)
			prospectsDTO.setBranchId(((BigInteger)prospect[9]).longValue());
			prospectsDTO.setBranchName((String) prospect[10]);
			if(prospect[11]!=null)
			prospectsDTO.setAcctId(((BigInteger)prospect[11]).longValue());
			prospectsDTO.setAcctName((String) prospect[12]);
			prospectsDTO.setComment((String) prospect[13]);
			prospectsDTO.setProspShtDesc((String) prospect[14]);
			prospectsDTO.setEmailAddress((String) prospect[15]);
			prospectsDTO.setGender((String) prospect[16]);
			prospectsDTOList.add(prospectsDTO);
		}
		Page<ProspectsDTO>  page = new PageImpl<>(prospectsDTOList,request, rowCount);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public ProspectsDTO findOneProspect(long id) throws BadRequestException {
		List<Object[]> prospcts = prospectsRepo.findProspctsId(id);
		if(prospcts.size() != 1)
			throw new BadRequestException("Invalid Prospect...");
		for(Object[] prospect:prospcts){
			ProspectsDTO prospectsDTO = new ProspectsDTO();
			prospectsDTO.setTenId(((BigDecimal)prospect[0]).longValue());
			prospectsDTO.setFname((String) prospect[1]);
			prospectsDTO.setOtherNames((String) prospect[2]);
			prospectsDTO.setPhoneNo((String) prospect[3]);
			prospectsDTO.setDob((Date) prospect[4]);
			prospectsDTO.setClientType((String) prospect[5]);
			if(prospect[6]!=null)
				prospectsDTO.setClientTypeId(((BigDecimal)prospect[6]).longValue());
			prospectsDTO.setStatus((String) prospect[7]);
			prospectsDTO.setCategory((String) prospect[8]);
			if(prospect[9]!=null)
				prospectsDTO.setBranchId(((BigDecimal)prospect[9]).longValue());
			prospectsDTO.setBranchName((String) prospect[10]);
			if(prospect[11]!=null)
				prospectsDTO.setAcctId(((BigDecimal)prospect[11]).longValue());
			prospectsDTO.setAcctName((String) prospect[12]);
			prospectsDTO.setComment((String) prospect[13]);
			prospectsDTO.setProspShtDesc((String) prospect[14]);
			prospectsDTO.setEmailAddress((String) prospect[15]);
			prospectsDTO.setGender((String) prospect[16]);
			return prospectsDTO;
		}
		return new ProspectsDTO();
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteProspect(Long prosId) {
		ProspectDef prospectDef = prospectsRepo.findOne(prosId);
		prospectDef.setStatus("T");
        prospectsRepo.save(prospectDef);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public ProspectDef defineProspect(ProspectsDTO prospect) throws BadRequestException {
		ProspectDef prospectDef = new ProspectDef();
		prospectDef.setCategory(prospect.getCategory());
		final AccountDef accountDef = (prospect.getAcctId()!=null)?accountRepo.findOne(prospect.getAcctId()):null;
		final ClientTypes clientTypes = (prospect.getClientTypeId()!=null)?clientTypeRepo.findOne(prospect.getClientTypeId()):null;
		if(clientTypes==null){
			throw new BadRequestException("Client Type cannot be null");
		}
//		if(prospect.getClientTitle()==null && prospect.getClientType().equalsIgnoreCase("I")){
//			throw new BadRequestException("Please Select a Valid Client Title");
//		}
//		if(prospect.getClientTitle()==null){
//			if(clientTitleRepo.findOne(prospect.getClientTitle())==null){
//				throw new BadRequestException("Please Select a Valid Client Title");
//			}
//		}
		prospectDef.setTenantType(clientTypes);
		prospectDef.setCategory(prospect.getCategory());
		if(prospect.getClientTitle()!=null) {
			prospectDef.setClientTitle(clientTitleRepo.findOne(prospect.getClientTitle()));
		}
		prospectDef.setComment(prospect.getComment());
		prospectDef.setDateregistered(new Date());
		prospectDef.setBranch((prospect.getBranchId()!=null)?branchRepository.findOne(prospect.getBranchId()):null);
		prospectDef.setPhoneNo(prospect.getPhoneNo());
		prospectDef.setEmailAddress(prospect.getEmailAddress());
		prospectDef.setPhonePrefix((prospect.getPrefixId()!=null)?mobPrefixRepo.findOne(prospect.getPrefixId()):null);
		prospectDef.setAcc(accountDef);
		prospectDef.setGender(prospect.getGender());
		prospectDef.setDob(prospect.getDob());
		prospectDef.setFname(prospect.getFname());
		prospectDef.setOtherNames(prospect.getOtherNames());
		if(prospect.getTenId()==null){
			prospectDef.setStatus("A");
			prospectDef.setProspShtDesc("PRS/"+ String.format("%05d", prospectsRepo.count()+1));
		}
		else{
			prospectDef.setStatus(prospect.getStatus());
			prospectDef.setProspShtDesc(prospect.getProspShtDesc());
			prospectDef.setTenId(prospect.getTenId());
		}
		clientRulesExecutor.handleProspectChecks(prospectDef);
		return prospectsRepo.save(prospectDef);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public ProspectDef defineQuotProspect(ProspectDef prospect) throws BadRequestException {
		if(prospect.getTenId()==null){
			prospect.setProspShtDesc("PRS/"+ String.format("%05d", prospectsRepo.count()+1));
		}
		clientRulesExecutor.handleProspectChecks(prospect);
		return prospectsRepo.save(prospect);
	}

	@Override
	public DataTablesResult<ClientDocsDTO> findClientDOcs(DataTablesRequest request, Long clientId) {
		final String search = ( request.getSearch()!=null && request.getSearch().getValue()!=null)?"%"+request.getSearch().getValue().toLowerCase()+"%":"%%";
		List<Object[]> docsList = clientDocsRepo.getAllClientDocuments(clientId,search.toLowerCase(),request.getPageNumber(), request.getPageSize());
		final List<ClientDocsDTO> clientDocsList = new ArrayList<>();
		long rowCount = 0l;
		if(!docsList.isEmpty()) rowCount = (Integer)docsList.get(0)[9];
		for(Object[] doc:docsList){
			final ClientDocsDTO docsDTO = new ClientDocsDTO();
			docsDTO.setCdId(((BigDecimal)doc[0]).longValue());
			docsDTO.setFileId((String) doc[1]);
			docsDTO.setUploadedFileName((String) doc[2]);
			docsDTO.setCheckSum((String) doc[3]);
			docsDTO.setContentType((String) doc[4]);
			docsDTO.setReqId(((BigDecimal)doc[5]).longValue());
			docsDTO.setReqShtDesc((String) doc[7]);
			docsDTO.setReqDesc((String) doc[6]);
			docsDTO.setAuthStatus((String) doc[8]);
			clientDocsList.add(docsDTO);
		}
		Page<ClientDocsDTO>  page = new PageImpl<>(clientDocsList,request, rowCount);
		return new DataTablesResult<>(request, page);
	}



	@Override
	public DataTablesResult<ClientDocs> findProspectDOcs(DataTablesRequest request, Long clientId) throws IllegalAccessException {
		BooleanExpression pred = QClientDocs.clientDocs.prospectDef.tenId.eq(clientId);
		Page<ClientDocs> page = clientDocsRepo.findAll(pred.and(request.searchPredicate(QClientDocs.clientDocs)), request);
		return new DataTablesResult<>(request, page);
	}

	@Override
	public List<RequiredDocs> findUnassignedClientDocs(Long clientCode, String docName) throws IllegalAccessException {
		if(clientCode==null) throw new IllegalArgumentException("No Client Selected...");
		ClientDef clientDef = clientRepo.findOne(clientCode);
		if("I".equalsIgnoreCase(clientDef.getTenantType().getClientType()))
		return requiredDocsRepo.getUnassignedClientDocs(clientCode,docName);
		else if("C".equalsIgnoreCase(clientDef.getTenantType().getClientType()))
			return requiredDocsRepo.getUnassignedCorporateClientDocs(clientCode,docName);
		else return new ArrayList<>();
	}

	@Override
	public List<RequiredDocs> findUnassignedProspectDocs(Long clientCode, String docName) throws IllegalAccessException {
		if(clientCode==null) throw new IllegalArgumentException("No Client Selected...");
		ProspectDef clientDef = prospectsRepo.findOne(clientCode);
		if("I".equalsIgnoreCase(clientDef.getTenantType().getClientType()))
			return requiredDocsRepo.getUnassignedProspectDocs(clientCode,docName);
		else if("C".equalsIgnoreCase(clientDef.getTenantType().getClientType()))
			return requiredDocsRepo.getUnassignedCorporateProspectDocs(clientCode,docName);
		else return new ArrayList<>();
	}

	@Override
	public void createClientRequiredDocs(RequiredDocBean requiredDocBean) {
		List<ClientDocs> clientDocs = new ArrayList<>();
		for(Long reqId:requiredDocBean.getRequiredDocs()){
			ClientDocs clientDoc = new ClientDocs();
			clientDoc.setRequiredDoc(requiredDocsRepo.findOne(reqId));
			clientDoc.setClientDef(clientRepo.findOne(requiredDocBean.getSubCode()));
			clientDocs.add(clientDoc);
		}
		clientDocsRepo.save(clientDocs);
	}

	@Override
	public void createProspectRequiredDocs(RequiredDocBean requiredDocBean) {
		List<ClientDocs> clientDocs = new ArrayList<>();
		for(Long reqId:requiredDocBean.getRequiredDocs()){
			ClientDocs clientDoc = new ClientDocs();
			clientDoc.setRequiredDoc(requiredDocsRepo.findOne(reqId));
			clientDoc.setProspectDef(prospectsRepo.findOne(requiredDocBean.getSubCode()));
			clientDocs.add(clientDoc);
		}
		clientDocsRepo.save(clientDocs);
	}


	@Override
	@Transactional(readOnly = false, rollbackFor = { BadRequestException.class })
	public void authorizeClient(Long clientCode, HttpServletRequest request) throws BadRequestException {
		if(clientCode==null)
			throw new BadRequestException("Client Code cannot be null....");
		ClientDef clientDef = clientRepo.findOne(clientCode);

		if(clientDef.getAuthStatus()!=null && "Y".equalsIgnoreCase(clientDef.getAuthStatus())){
			clientDef.setAuthStatus("N");
			clientDef.setAuthBy(null);
			clientRepo.save(clientDef);
		}
		else{
			long count = clientDocsRepo.count(QClientDocs.clientDocs.clientDef.tenId.eq(clientCode));
//			if(count==0)
//				throw new BadRequestException("Cannot Authorize Client without populating Documents...");
			/* for(ClientDocs clientDocs:clientDocsRepo.findAll(QClientDocs.clientDocs.clientDef.tenId.eq(clientCode))){
				if(StringUtils.isBlank(clientDocs.getCheckSum()))
					throw new BadRequestException("Cannot Authorize Client..Upload Document for Doc. "+clientDocs.getRequiredDoc().getReqDesc());
			} */
			clientDef.setAuthStatus("Y");
			clientDef.setAuthBy(userUtils.getCurrentUser());
			clientRepo.save(clientDef);
			/*
			clientRulesExecutor.handleClientChecks(clientDef);
            String pushProspects = "N";
            int minAge = 18;
            try {
                pushProspects = paramService.getParameterString("PUSH_PROSPECTS");
                minAge = paramService.getParamInt("MIN_PUSH_AGE");
            } catch (BadRequestException e) {
                throw new BadRequestException("Parameters PUSH_PROSPECTS and MIN_PUSH_AGE have not been Defined! Define the parameter to proceed");
            }
			if(StringUtils.isBlank(clientDef.getClientRef())&&"Y".equalsIgnoreCase(pushProspects)&& DateUtilities.computeAge(clientDef.getDob())>=minAge){
				synchronizeTransaction(clientDef,request);
			}

			 */
		}

	}

}
