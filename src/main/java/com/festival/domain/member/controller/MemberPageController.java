package com.festival.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberPageController {
    @GetMapping
    public String login(){
        return "login";
    }

}
