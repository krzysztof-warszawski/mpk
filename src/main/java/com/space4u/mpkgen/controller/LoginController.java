package com.space4u.mpkgen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/loginForm")
    public String showLoginPage() {
        return "loginForm";
    }
}
