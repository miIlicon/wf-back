package com.festival.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
public class MemberPageController {
    @GetMapping("/home")
    public String login(){
        return "login";
    }

}
