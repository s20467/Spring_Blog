package com.springproject.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("")
    public String redirectToArticleHomePage(){
        return "redirect:/article";
    }
}
