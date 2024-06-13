package com.brokersystems.brokerapp.nav.controllers;


import com.brokersystems.brokerapp.quotes.dto.PendingQuotDTO;
import com.brokersystems.brokerapp.quotes.dto.QuoteEnquireDTO;
import com.brokersystems.brokerapp.server.datatables.DataTable;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.DateUtilities;
import com.brokersystems.brokerapp.server.utils.UserUtils;
import com.brokersystems.brokerapp.setup.model.ModelHelperForm;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.setup.service.UserService;
import com.brokersystems.brokerapp.trans.model.HomeAggregateBean;
import com.brokersystems.brokerapp.trans.model.HomePremiumBean;
import com.brokersystems.brokerapp.trans.service.HomeService;
import com.brokersystems.brokerapp.users.model.PasswordResetToken;
import com.brokersystems.brokerapp.users.model.QPasswordResetToken;
import com.brokersystems.brokerapp.users.repository.PasswordResetTokenRepo;
import com.brokersystems.brokerapp.uw.dtos.PolicyEnquiryDTO;
import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import com.brokersystems.brokerapp.uw.service.PolicyTransService;
import com.brokersystems.brokerapp.workflow.docs.SysWfDocs;
import com.brokersystems.brokerapp.workflow.dto.WorkFlowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping({"/protected/home","/protected"})
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private PolicyTransService policyService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private DateUtilities dateUtilities;

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String entry(HttpServletRequest request) {
        User user = userUtils.getCurrentUser();
        if("Y".equalsIgnoreCase(user.getResetPass())){
            PasswordResetToken resetToken = passwordResetTokenRepo.findOne(QPasswordResetToken.passwordResetToken.user.id.eq(user.getId()));
            return "redirect:/reset-password?token="+resetToken.getToken();
        }
        else {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(2018,05,31);
//             long days = dateUtilities.daysBetweenUsingJoda(new Date(),calendar.getTime());
//             System.out.println("days..."+days);
//             if(days > 0)
//            request.getSession().setAttribute("timeoutMessage","Number of Days Remaining for the Trial Period.... "+days);
//            else{
//                 request.getSession().removeAttribute("Username");
//                 return "redirect:/login";
//             }
            return "home";
        }
    }


    @RequestMapping(value = { "dashboardDetails" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    public ResponseEntity<HomeAggregateBean> getPolicyDetails() throws BadRequestException {
        HomeAggregateBean aggregateBean = homeService.getDashBoardDetails();
        return new ResponseEntity<HomeAggregateBean>(aggregateBean, HttpStatus.OK);
    }

    @RequestMapping(value = { "premiumProduction" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    public ResponseEntity<List<HomePremiumBean>> getPremiumProduction() throws BadRequestException {
        List<HomePremiumBean> aggregateBean = homeService.getPremiumProduction();
        return new ResponseEntity<List<HomePremiumBean>>(aggregateBean, HttpStatus.OK);
    }

    @RequestMapping(value = { "productPremium" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    public ResponseEntity<List<HomePremiumBean>> getProductPremium() throws BadRequestException {
        List<HomePremiumBean> aggregateBean = homeService.getProductPremium();
        return new ResponseEntity<List<HomePremiumBean>>(aggregateBean, HttpStatus.OK);
    }

    @RequestMapping(value = { "branchPremium" }, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET })
    public ResponseEntity<List<HomePremiumBean>> getBranchPremium() throws BadRequestException {
        List<HomePremiumBean> aggregateBean = homeService.getBranchPremium();
        return new ResponseEntity<List<HomePremiumBean>>(aggregateBean, HttpStatus.OK);
    }

    @RequestMapping(value = { "userTickets" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<WorkFlowDTO> getUserPolTrans(@DataTable DataTablesRequest pageable) {
        return policyService.findUserPolicies(pageable);
    }

    @RequestMapping(value = "/edituwtrans", method = RequestMethod.POST)
    public String editPolicyForm(@Valid @ModelAttribute ModelHelperForm helperForm, Model model, HttpServletRequest request) throws BadRequestException {
        request.getSession().setAttribute("policyCode", helperForm.getId( ));
        PolicyTrans policyTrans = policyService.getPolicyDetails(helperForm.getId());
        if("MD".equalsIgnoreCase(policyTrans.getProduct().getProGroup().getPrgType()))
            return  "redirect:/protected/medical/policies/edituwpolicy";
        else if("L".equalsIgnoreCase(policyTrans.getProduct().getProGroup().getPrgType())){
            return "redirect:/protected/uw/policies/editlifepolicy";
        }
        else{
           return "redirect:/protected/uw/policies/edituwpolicy";
        }

    }

    @RequestMapping(value = "/editquottrans", method = RequestMethod.POST)
    public String editQuoteForm(@Valid @ModelAttribute ModelHelperForm helperForm, Model model, HttpServletRequest request) {
       request.getSession().setAttribute("quotCode", helperForm.getId());
        return "redirect:/protected/quotes/editquote";
    }

    @RequestMapping(value = "updateUserIP", method = RequestMethod.GET)
    @ResponseBody
    public String entry(@RequestParam(value = "ipAddress")  String ip) throws BadRequestException  {
        User user = userUtils.getCurrentUser();
        userService.updateUserIP(user.getId(),ip);
        return ip;
    }

    @RequestMapping(value = { "pendingQuotes" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<PendingQuotDTO> getPendingQuotes(@DataTable DataTablesRequest pageable) {
        return homeService.getPendingQuotes(pageable);
    }

    @RequestMapping(value = { "expiredPolicies" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<PolicyEnquiryDTO> getExpiredPolicies(@DataTable DataTablesRequest pageable) {
        return homeService.getExpiredPolicies(pageable);
    }

    @RequestMapping(value = { "pendingEndorsements" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<PolicyEnquiryDTO> getPendingEndorsements(@DataTable DataTablesRequest pageable) {
        return homeService.getPendingEndorsements(pageable);
    }

    @RequestMapping(value = { "pendingRenewals" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<PolicyEnquiryDTO> getPendingRenewals(@DataTable DataTablesRequest pageable) {
        return homeService.getPendingRenewals(pageable);
    }

    @RequestMapping(value = "pendingQt",method={org.springframework.web.bind.annotation.RequestMethod.GET})
    public String convertQuotes(Model model)
    {
        return "pendingquotes";
    }

    @RequestMapping(value = "expiredpols",method={org.springframework.web.bind.annotation.RequestMethod.GET})
    public String expiredpolicies(Model model)
    {
        return "expiredpolicies";
    }

    @RequestMapping(value = "pendingendorsements",method={org.springframework.web.bind.annotation.RequestMethod.GET})
    public String pendingendorsements(Model model)
    {
        return "pendingendorsements";
    }

    @RequestMapping(value = "pendingrenewals",method={org.springframework.web.bind.annotation.RequestMethod.GET})
    public String pendingrenewals(Model model)
    {
        return "pendingrenewals";
    }
}
