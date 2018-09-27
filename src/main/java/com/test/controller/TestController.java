package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Author:helloboy
 * Date:2018-07-18 下午2:12
 * Description:<描述>
 */
@Controller
public class TestController {

    @RequestMapping("/test1")
    public ModelAndView test1(String name){

        ModelAndView amv = new ModelAndView();
        amv.addObject("msg","hello"+name);
        amv.setViewName("hello");
        return amv;
    }
    @RequestMapping
    public  ModelAndView test2(Integer num){
        ModelAndView ac = new ModelAndView();
        ac.addObject("msg","sum="+num);
        ac.setViewName("hello");
        return ac;

    }
}
