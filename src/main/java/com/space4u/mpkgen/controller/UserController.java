package com.space4u.mpkgen.controller;

import com.space4u.mpkgen.api.CrmUser;
import com.space4u.mpkgen.entity.User;
import com.space4u.mpkgen.service.UserService;
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
        return "/users/list-users";
    }

    @GetMapping("/search")
    public String search(@RequestParam("userName") String name,
                         Model theModel) {

        List<User> users = userService.searchBy(name);
        theModel.addAttribute("users", users);
        return "/users/list-users";
    }

    @GetMapping("/showNewUserForm")
    public String showNewUserForm(Model theModel) {
        theModel.addAttribute("crmUser", new CrmUser());
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

        userService.save(crmUser);
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
