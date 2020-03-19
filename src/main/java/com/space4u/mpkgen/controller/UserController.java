package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.api.CrmUser;
import com.space4u.mpkgen.entity.User;
import com.space4u.mpkgen.service.UserService;
import com.space4u.mpkgen.service.implementation.UserServiceImpl;
import com.space4u.mpkgen.util.RoleMappings;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/list")
    public String listEmployees(Model theModel) {
        List<User> users = userService.findAll();
        theModel.addAttribute("users", users);
        theModel.addAttribute("sessionUserId", UserServiceImpl.sessionUserId);
        System.out.println("Session User ID==" + UserServiceImpl.sessionUserId);
        return "/users/list-users";
    }

    @GetMapping("/search")
    public String search(@RequestParam("userName") String name,
                         Model theModel) {

        List<User> users = userService.searchBy(name);
        theModel.addAttribute("users", users);
        theModel.addAttribute("sessionUserId", UserServiceImpl.sessionUserId);
        return "/users/list-users";
    }

    @GetMapping("/showNewUserForm")
    public String showNewUserForm(Model model) {
        model.addAttribute("crmUser", new CrmUser());
        return "/users/new-user-form";
    }

    @PostMapping("/processNewUserRegistration")
    public String processNewUserRegistration(
            @Valid @ModelAttribute("crmUser") CrmUser crmUser,
            BindingResult bindingResult,
            Model model) {

        String userName = crmUser.getUserName();
        logger.info("Processing new-user-form for: " + userName);

        if (bindingResult.hasErrors()) {
            return "/users/new-user-form";
        }

        User existing = userService.findByUserName(userName);
        if (existing != null) {
            model.addAttribute("crmUser", new CrmUser());
            model.addAttribute("registrationError", "Użytkownik o podanym loginie już istnieje.");

            logger.warning("User name already exists.");
            return "/users/new-user-form";
        }

        userService.save(crmUser, true);
        logger.info("Successfully created user: " + userName);
        return "/users/new-user-confirmation";
    }

    @GetMapping("/showEditUserForm")
    public String showUpdateUserForm(@RequestParam("userId") int id, Model model) {

        User user = userService.findById(id);
        String role = RoleMappings.findRoleName(user.getRoles().size());

        CrmUser crmUser = new CrmUser();
        crmUser.setUserId(user.getId());
        crmUser.setUserName(user.getUserName());
        crmUser.setFirstName(user.getFirstName());
        crmUser.setLastName(user.getLastName());
        crmUser.setRoles(role);
        model.addAttribute("crmUser", crmUser);

        return "/users/update-user-form";
    }

    @PostMapping("/processUserUpdate")
    public String processUserUpdate(
            @Valid @ModelAttribute("crmUser") CrmUser crmUser,
            BindingResult bindingResult,
            Model model) {

        String userName = crmUser.getUserName();
        logger.info("Processing update-user-form for: " + userName);

        if (bindingResult.hasErrors()) {
            logger.info("has errors >>>>>>>>>>>>");
            return "/users/update-user-form";
        }

        User existing = userService.findById(crmUser.getUserId());
        if (!existing.getUserName().equals(userName)) {
            existing = userService.findByUserName(userName);
            if (existing != null) {
                model.addAttribute("crmUser", crmUser);
                model.addAttribute("registrationError", "Inny użytkownik o podanym loginie już istnieje.");

                logger.warning("User name already assign to another user.");
                return "/users/update-user-form";
            }
        }

        userService.save(crmUser, false);
        logger.info("Successfully created user: " + userName);
        return "/users/new-user-confirmation";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("userId") int id) {
        userService.findById(id).getRoles().clear();
        userService.deleteById(id);
        return "redirect:/users/list";
    }
}