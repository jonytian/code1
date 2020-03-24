package com.example.logs.controller;


import com.example.logs.entity.LogVoSearch;
import com.example.logs.entity.MessageSearch;
import com.example.logs.service.LogService;
import com.example.logs.service.MessageService;
import com.example.logs.util.PageDataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Controller
@RequestMapping("/log")
public class LogController extends BaseController{


    @Autowired
    private LogService logService;
    @Autowired
    private MessageService messageService;

    @RequestMapping("")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("-------------index------------");

        return "pages/logs/list";
    }



    @RequestMapping("/msg")
    public String msg(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("-------------index------------");

        return "pages/logs/msg-list";
    }

    /**
     * 添加配置
     * */
    @RequestMapping("/add")
    public String addConfig(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("-------------index------------");

        return "pages/logs/log-add";
    }

    /**
     * 分页查询列表
     * @return ok/fail
     */
    @RequestMapping(value = "/getLogs", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getLogs(@RequestParam("page") Integer page,
                                  @RequestParam("limit") Integer limit, LogVoSearch logVoSearch) {


        log.debug("分页查询异常列表！搜索条件：logVoSearch：" + logVoSearch + ",page:" + page
                + ",每页记录数量limit:" + limit);
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            // 获取产品列表
            pdr = logService.getLogs(logVoSearch, page, limit);
            log.info("异常列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常列表查询异常！", e);
        }
        return pdr;
    }

    /**
     * 分页查询列表
     * @return ok/fail
     */
    @RequestMapping(value = "/getMsgs", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getMsgs(@RequestParam("page") Integer page,
                                  @RequestParam("limit") Integer limit, MessageSearch messageSearch) {


        log.debug("分页查询异常列表！搜索条件：messageSearch：" + messageSearch + ",page:" + page
                + ",每页记录数量limit:" + limit);
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            // 获取产品列表
            pdr = messageService.getMessages(messageSearch, page, limit);
            log.info("消息列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息列表查询异常！", e);
        }
        return pdr;
    }

    @GetMapping("/download")
    @ResponseBody
    public String download(HttpServletRequest request) {
        return this.outPutData("没有可供下载的文件");
    }


}
