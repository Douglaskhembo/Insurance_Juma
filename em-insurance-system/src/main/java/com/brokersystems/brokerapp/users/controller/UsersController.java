package com.brokersystems.brokerapp.users.controller;

import com.brokersystems.brokerapp.server.datatables.DataTable;
import com.brokersystems.brokerapp.server.datatables.DataTablesRequest;
import com.brokersystems.brokerapp.server.datatables.DataTablesResult;
import com.brokersystems.brokerapp.server.exception.BadRequestException;
import com.brokersystems.brokerapp.server.utils.AuditTrailLogger;
import com.brokersystems.brokerapp.setup.dto.*;
import com.brokersystems.brokerapp.setup.model.SubAccountTypes;
import com.brokersystems.brokerapp.setup.model.User;
import com.brokersystems.brokerapp.setup.repository.UserRepository;
import com.brokersystems.brokerapp.setup.service.UserService;
import com.brokersystems.brokerapp.users.dto.PermissionDTO;
import com.brokersystems.brokerapp.users.model.*;
import com.brokersystems.brokerapp.users.repository.RolesRepo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * Created by peter on 4/13/2017.
 */
@Controller
@RequestMapping({"/protected/users"})
public class UsersController {

    private static final String TEMP_FOLDER_PATH = System.getProperty("java.io.tmpdir");
    private static final String IMAGES_SYSTEM_PATH = TEMP_FOLDER_PATH + File.separator + "images";
    private static final File IMAGES_SYSTEM_DIR = new File(IMAGES_SYSTEM_PATH);
    private static final String IMAGES_SYSTEM_DIR_ABSOLUTE_PATH = IMAGES_SYSTEM_DIR.getAbsolutePath() + File.separator;
    @Autowired
    private UserService userService;

    @Autowired
    private AuditTrailLogger auditTrailLogger;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepo rolesRepo;

    @PreAuthorize("hasAnyAuthority('ACCESS_USERS')")
    @RequestMapping(value = "usersHome", method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String classHome(Model model, HttpServletRequest request) {
        auditTrailLogger.log("Accessed Users and Roles Screen ", request);
        return "users";
    }

    @RequestMapping(value = "roles", method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String roles(Model model) {

        return "roles";
    }


    @RequestMapping(value = {"usersList/{userstatus}"}, method = {RequestMethod.GET})
    @ResponseBody
    public DataTablesResult<UserDTO> getClasses(@DataTable DataTablesRequest pageable, @PathVariable String userstatus) {
        return userService.findDatatables(pageable, userstatus);
    }

    @RequestMapping(value = {"rolesList"}, method = {RequestMethod.GET})
    @ResponseBody
    public DataTablesResult<RolesDef> rolesList(@DataTable DataTablesRequest pageable)
            throws IllegalAccessException {
        return userService.findRoles(pageable);
    }

    @RequestMapping(value = "/usersignature/{id}")
    public void getImage(HttpServletResponse response, @PathVariable Long id) throws IOException, ServletException {
        UserDTO userDTO = userService.findUserSignatureDetails(id);
        if(userDTO.getSignature()!=null) {
            response.setContentType(userDTO.getSignatureContentType());
            response.getOutputStream().write(Files.readAllBytes(Paths.get(userDTO.getSignature())));
            response.getOutputStream().close();
        }
    }

    @PreAuthorize("hasAnyAuthority('CREATE_USER')")
    @RequestMapping(value = {"createUsers"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public void createUsers(UserDTO user, HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
        String message = "";
        final boolean newUser = user.getId() == null;
        User currentUser = user.getId() != null ? userRepository.findOne(user.getId()) : null;
        if (newUser) {
            String isMarketer ="No";
            String typeOfMarketer ="None";
            message = "Created a new User with the following attributes, Name: " + user.getName() + " UserName: " + user.getUsername()
                    + " Marketer: " + isMarketer + " Marketer Type: " + typeOfMarketer + " Email: " + user.getEmail() +
                    " Active " + user.getStatus();
        } else {
            String nameAudit = currentUser.getName().equalsIgnoreCase(user.getName()) ? "" : "Changed User Name From " + currentUser.getName() + " to " + user.getName();
            String userName = currentUser.getUsername().equalsIgnoreCase(user.getUsername()) ? "" : "Changed User UserName From " + currentUser.getUsername() + " to " + user.getUsername();
            String marketer = "";

            String email = currentUser.getEmail().equalsIgnoreCase(user.getEmail()) ? "" : "Changed User Email From " + (currentUser.getEmail().isEmpty()?"None ": currentUser.getEmail()) + " to " + user.getEmail();
            String currActive = (currentUser.getEnabled()!=null && currentUser.getEnabled().equalsIgnoreCase("1"))?"on":"off";
            String active = currActive.equalsIgnoreCase(user.getStatus()) ? "" : "Changed User Active Flag From " + currActive + " to " + user.getStatus();
            message = "Edited " + currentUser.getUsername() + nameAudit + " " + userName + " " + marketer  + " " + email + " " + active;

        }
        if ((user.getFile() != null) &&
                (!user.getFile().isEmpty())) {
            String uploadFolder = IMAGES_SYSTEM_DIR_ABSOLUTE_PATH;
            byte[] bytes = user.getFile().getBytes();
            String folderName = uploadFolder + "/USERS"+ "/"+ user.getUsername();
            File file = new File(folderName);
            if (!file.exists())
                FileUtils.forceMkdir(file);
            Path path = Paths.get(folderName  +"/"+ user.getFile().getOriginalFilename());
            Files.write(path, bytes);
            user.setSignatureContentType(user.getFile().getContentType());
            user.setSignature(path.toFile().getAbsolutePath());
        }
        else{
            UserDTO dto = userService.findUserSignatureDetails(user.getId());
            if(dto!=null && dto.getSignature()!=null) {
                user.setSignatureContentType(dto.getSignatureContentType());
                user.setSignature(dto.getSignature());
            }
        }
        userService.saveOrUpdate(user, request);
        auditTrailLogger.log(message, request);
    }


    @RequestMapping(value = {"deleteUser/{userId}"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSequence(@PathVariable Long userId, HttpServletRequest request) throws BadRequestException {
        User user = userRepository.findOne(userId);
        String message = "De-activated User : " + user.getName() + " Of UserName: " + user.getUsername();
        userService.deleteUser(userId);
        auditTrailLogger.log(message, request);

    }

    @RequestMapping(value = {"modulesList"}, method = {RequestMethod.GET})
    @ResponseBody
    public DataTablesResult<ModulesDef> modulesList(@DataTable DataTablesRequest pageable)
            throws IllegalAccessException {
        return userService.getModules(pageable);
    }

    @RequestMapping(value = { "addPermission" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    @ResponseBody
    public void selproducts(PermissionsDef permissionsDef)
            throws IllegalAccessException, BadRequestException {
        userService.setPermissionSetups(permissionsDef);
    }
    @RequestMapping(value = {"permissions/{roleId}/{moduleId}"}, method = {RequestMethod.GET})
    @ResponseBody
    public DataTablesResult<PermissionDTO> getPermissions(@DataTable DataTablesRequest pageable,
                                                          @PathVariable("roleId") Long roleId,
                                                          @PathVariable("moduleId") Long moduleId) {
        return userService.findPermissions(pageable, moduleId,roleId);
    }

    @RequestMapping(value = {"grantPermission"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public void grantPermission(PermissionBean permissionBean) throws IllegalAccessException, IOException, BadRequestException {
        userService.grantPermission(permissionBean);
    }

    @RequestMapping(value = {"addModule"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public void addModule(ModulesDef modulesDef) throws IllegalAccessException, IOException, BadRequestException {
        userService.addModule(modulesDef);
    }

    @RequestMapping(value = {"revokePermission"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public void revokePermission(PermissionBean permissionBean) throws IllegalAccessException, IOException, BadRequestException {
        userService.revokePermission(permissionBean);
    }

    @RequestMapping(value = {"checkPermissionExists"}, method = {RequestMethod.GET})
    @ResponseBody
    public Long getPermissionsCount(@RequestParam(value = "roleId", required = false) Long roleId, @RequestParam(value = "permId", required = false) Long permId)
            throws IllegalAccessException {
        return userService.checkIfRoleExists(roleId, permId);
    }

    @RequestMapping(value = {"minLimitAmt"}, method = {RequestMethod.GET})
    @ResponseBody
    public BigDecimal getMinLimitAmount(@RequestParam(value = "roleId", required = false) Long roleId, @RequestParam(value = "permId", required = false) Long permId)
            throws IllegalAccessException {
        return userService.getMinLimitAmount(roleId, permId);
    }

    @RequestMapping(value = {"maxLimitAmt"}, method = {RequestMethod.GET})
    @ResponseBody
    public BigDecimal getMaxLimitAmount(@RequestParam(value = "roleId", required = false) Long roleId, @RequestParam(value = "permId", required = false) Long permId)
            throws IllegalAccessException {
        return userService.getMaxLimitAmount(roleId, permId);
    }

    @RequestMapping(value = {"createRoles"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public void createRoles(RolesDef rolesDef) throws IllegalAccessException, IOException, BadRequestException {
        userService.createRole(rolesDef);
    }

    @RequestMapping(value = {"subaccounts"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Page<AccountTypesDTO> selectSubaccountTypes(@RequestParam(value = "term", required = false) String term, Pageable pageable) {
        return userService.findSubAccountTypes(term, pageable);
    }

    @RequestMapping(value = {"accountTypes"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Page<AccountTypesDTO> selectAccountTypes(@RequestParam(value = "term", required = false) String term, Pageable pageable) {
        return userService.findSubAccountTypes(term, pageable);
    }



    @RequestMapping(value = {"deleteRole/{roleId}"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Long roleId)  {
        userService.deleteRole(roleId);
    }

    @RequestMapping(value = {"assignedRoles/{userId}"}, method = {RequestMethod.GET})
    @ResponseBody
    public DataTablesResult<RolesDef> getAssignedRoles(@DataTable DataTablesRequest pageable, @PathVariable Long userId)
            throws IllegalAccessException {
        return userService.findassignedUserRoles(pageable, userId);
    }

    @RequestMapping(value = {"unAssignedRoles/{userId}"}, method = {RequestMethod.GET})
    @ResponseBody
    public DataTablesResult<RolesDef> getUnAssignedRoles(@DataTable DataTablesRequest pageable, @PathVariable Long userId)
            throws IllegalAccessException {
        return userService.findUnassignedUserRoles(pageable, userId);
    }

    @RequestMapping(value = {"assignRoles"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResponseEntity<String> assignRoles(@RequestBody UserRolesBean rolesBean, HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
        User user = userRepository.findOne(rolesBean.getUserId());
        String assignedRoles="";
        for(Long roleId:rolesBean.getRoles()){
            RolesDef role = rolesRepo.findOne(roleId);
            assignedRoles = assignedRoles+role.getRoleName()+" ,";
        }
        String message = "Assigned User : " + user.getName()+ " Of UserName: " + user.getUsername() + " The following roles: " + assignedRoles;
        userService.assignUserRoles(rolesBean);
        auditTrailLogger.log(message, request);
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = {"unAssignRoles"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResponseEntity<String> unAssignRoles(@RequestBody UserRolesBean rolesBean, HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
        User user = userRepository.findOne(rolesBean.getUserId());
        String assignedRoles="";
        for(Long roleId:rolesBean.getRoles()){
            RolesDef role = rolesRepo.findOne(roleId);
            assignedRoles = assignedRoles+role.getRoleName()+" ,";
        }
        String message = "Un-Assigned User : " + user.getName()+ " Of UserName: " + user.getUsername() + " The following roles: " + assignedRoles;
        userService.unAssignUserRoles(rolesBean);
        auditTrailLogger.log(message, request);
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = {"updatePermLimits"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public void updatePermLimits(PermLimitsBean limitsBean) throws IllegalAccessException, IOException, BadRequestException {
        userService.savePermLimits(limitsBean);
    }

    @RequestMapping(value = {"assignUserBranches"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResponseEntity<String> assignUserBranches(@RequestBody CreateUserBranchesDTO userBranchesDTO, HttpServletRequest request) throws IllegalAccessException, IOException, BadRequestException {
        userService.createUserBranch(userBranchesDTO);
        auditTrailLogger.log(userBranchesDTO.toString(), request);
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = { "getUserAssignedBranches/{userId}" }, method = { RequestMethod.GET })
    @ResponseBody
    public DataTablesResult<UserBranchesDTO> getUserAssignedBranches(@DataTable DataTablesRequest pageable,@PathVariable Long userId) throws IllegalAccessException {
        return userService.findUserBranches(userId, pageable);
    }

    @RequestMapping(value = { "userbranches" }, method = { RequestMethod.GET })
    @ResponseBody
    public List<BranchDTO> userbranches(@RequestParam(value = "userId", required = false) Long userId, @RequestParam(value = "search", required = false) String search ) {
        return userService.findUnassignedBranches(userId,search);
    }

    @RequestMapping(value = {"deleteUserBranch/{userBranchId}"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserBranch(@PathVariable Long userBranchId)  {
        userService.deleteUserBranch(userBranchId);
    }
}
