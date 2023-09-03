package com.andrew.personal.controller;

import com.andrew.personal.service.testService;
import com.andrew.personal.vo.testVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    testService testService;

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
    public ModelAndView test() throws Exception{

        ModelAndView mav = new ModelAndView("test");

        List<testVo> testList = testService.selectTest();
        mav.addObject("list", testList);

        return mav;
    }

    @RequestMapping(value="/text")
    public String text() {
        return "/text";
    }

}
