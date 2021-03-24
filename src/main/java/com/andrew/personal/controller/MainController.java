package com.andrew.personal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping(value ="/")
    public String index() { return "personal/page/home"; }

    @RequestMapping(value="/detail")
    public String detail() {
        return "personal/page/detail";
    }

    @RequestMapping(value="/about")
    public String aboutMe() {
        return "personal/page/about";
    }

    @RequestMapping(value="/test")
    public String test() {
        return "/test";
    }

    @RequestMapping(value="/text")
    public String text() {
        return "/text";
    }

}
