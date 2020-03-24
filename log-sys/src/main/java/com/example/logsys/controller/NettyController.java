package com.example.logsys.controller;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/ws")
public class NettyController extends BaseController{

    @GetMapping("/index")
    public ModelAndView index(){
        ModelAndView mav=new ModelAndView("wbsocket");
        mav.addObject("uid", RandomUtil.randomNumbers(6));
        return mav;
    }



}
