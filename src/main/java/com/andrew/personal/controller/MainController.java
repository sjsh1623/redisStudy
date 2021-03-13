package com.andrew.personal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping(value ="/")
    public String index() {
        return "index";
    }

    @RequestMapping(value="/detail")
    public String detail() {
        return "detail";
    }

    @RequestMapping(value="/aboutMe")
    public String aboutMe() {
        return "text";
    }

}
