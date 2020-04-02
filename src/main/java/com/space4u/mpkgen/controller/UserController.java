package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.api.CrmUser;
import com.space4u.mpkgen.entity.User;
import com.space4u.mpkgen.service.UserService;
import com.space4u.mpkgen.service.implementation.UserServiceImpl;
import com.space4u.mpkgen.util.PasswordGenerator;
import com.space4u.mpkgen.util.mappings.MpkMappings;
import com.space4u.mpkgen.util.mappings.NavMappings;
import com.space4u.mpkgen.util.mappings.RoleMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping(NavMappings.USERS)
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping(NavMappings.USERS_LIST)
    public String listEmployees(Model theModel) {
        List<User> users = userService.findAll();
        theModel.addAttribute("users", users);
        theModel.addAttribute("sessionUserId", UserServiceImpl.sessionUserId);
        System.out.println("Session User ID==" + UserServiceImpl.sessionUserId);
        return MpkMappings.USERS_LIST;
    }

    @GetMapping(NavMappings.USERS_SEARCH)
    public String search(@RequestParam("userName") String name,
                         Model theModel) {

        List<User> users = userService.searchBy(name);
        theModel.addAttribute("users", users);
        theModel.addAttribute("sessionUserId", UserServiceImpl.sessionUserId);
        return MpkMappings.USERS_LIST;
    }

    @GetMapping(NavMappings.USERS_NEW_USER_FORM)
    public String showNewUserForm(Model model) {
        model.addAttribute("crmUser", new CrmUser());
        return MpkMappings.USERS_NEW_USER_FORM;
    }

    @PostMapping(NavMappings.USERS_PROCESS_NEW_USER_REGISTRATION)
    public String processNewUserRegistration(
            @Valid @ModelAttribute("crmUser") CrmUser crmUser,
            BindingResult bindingResult,
            Model model) {

        String userName = crmUser.getUserName();
        logger.info("Processing new-user-form for: " + userName);

        if (bindingResult.hasErrors()) {
            return MpkMappings.USERS_NEW_USER_FORM;
        }

        User existing = userService.findByUserName(userName);
        if (existing != null) {
            model.addAttribute("crmUser", new CrmUser());
            model.addAttribute("registrationError", "Użytkownik o podanym loginie już istnieje.");

            logger.warning("User name already exists.");
            return MpkMappings.USERS_NEW_USER_FORM;
        }

        userService.save(crmUser, true);
        logger.info("Successfully created user: " + userName);
        return MpkMappings.USERS_NEW_USER_CONFIRMATION;
    }

    @GetMapping(NavMappings.USERS_EDIT_USER_FORM)
    public String showUpdateUserForm(@RequestParam("userId") int id, Model model) {
        CrmUser crmUser = uploadUserProfile(id);
        model.addAttribute("crmUser", crmUser);
        return MpkMappings.USERS_UPDATE;
    }

    @PostMapping(NavMappings.USERS_PROCESS_USER_UPDATE)
    public String processUserUpdate(
            @Valid @ModelAttribute("crmUser") CrmUser crmUser,
            BindingResult bindingResult,
            Model model) {

        String userName = crmUser.getUserName();
        logger.info("Processing update-user-form for: " + userName);

        if (bindingResult.hasErrors()) {
            logger.info("has errors >>>>>>>>>>>>");
            return MpkMappings.USERS_UPDATE;
        }

        User existing = userService.findById(crmUser.getUserId());
        if (!existing.getUserName().equals(userName)) {
            existing = userService.findByUserName(userName);
            if (existing != null) {
                model.addAttribute("crmUser", crmUser);
                model.addAttribute("registrationError", "Inny użytkownik o podanym loginie już istnieje.");

                logger.warning("User name already assign to another user.");
                return MpkMappings.USERS_UPDATE;
            }
        }

        userService.save(crmUser, false);
        logger.info("Successfully created user: " + userName);
        return MpkMappings.USERS_NEW_USER_CONFIRMATION;
    }

    @GetMapping(NavMappings.USERS_PROFILE)
    public String showUserProfile(Model model) {
        CrmUser crmUser = uploadUserProfile(UserServiceImpl.sessionUserId);
        model.addAttribute("crmUser", crmUser);
        return MpkMappings.USERS_PROFILE;
    }

    @PostMapping("/password-change")
    public String passwordChange(
            @Valid @ModelAttribute("crmUser") CrmUser crmUser,
            BindingResult bindingResult) {

        String userName = crmUser.getUserName();
        logger.info("Processing update-user-form for: " + userName);

        if (bindingResult.hasErrors()) {
            logger.info("has errors >>>>>>>>>>>>");
            return MpkMappings.USERS_PROFILE;
        }

        userService.save(crmUser, false);
        logger.info("Successfully updated user: " + userName);
        return MpkMappings.USERS_NEW_USER_CONFIRMATION;
    }

    @GetMapping("/password-reset")
    public String passwordReset(@RequestParam("userId") int id, Model model) {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        CrmUser crmUser = uploadUserProfile(id);
        String password = passwordGenerator.randomPassword();
        crmUser.setPassword(password);
        model.addAttribute("crmUser", crmUser);
        userService.save(crmUser, false);
        return MpkMappings.USERS_PASSWORD_RESET;
    }

    @GetMapping(NavMappings.USERS_DELETE)
    public String delete(@RequestParam("userId") int id) {
        userService.findById(id).getRoles().clear();
        userService.deleteById(id);
        return "redirect:" + NavMappings.USERS + NavMappings.USERS_LIST;
    }

    private CrmUser uploadUserProfile(int id) {
        User user = userService.findById(id);
        String role = RoleMappings.findRoleName(user.getRoles().size());

        CrmUser crmUser = new CrmUser();
        crmUser.setUserId(user.getId());
        crmUser.setUserName(user.getUserName());
        crmUser.setFirstName(user.getFirstName());
        crmUser.setLastName(user.getLastName());
        crmUser.setRoles(role);
        return crmUser;
    }
}