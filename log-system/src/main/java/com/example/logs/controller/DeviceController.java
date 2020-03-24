package com.example.logs.controller;


import com.example.logs.entity.DeviceSearch;
import com.example.logs.service.DeviceService;
import com.example.logs.util.PageDataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/device")
public class DeviceController extends BaseController{


    @Autowired
    private DeviceService deviceService;


    @RequestMapping("")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("-------------index------------");
        return "pages/device/list";
    }


    /**
     * 分页查询设备列表
     * @return ok/fail
     */
    @RequestMapping(value = "/getDevices", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getDevices(@RequestParam("page") Integer page,
                                     @RequestParam("limit") Integer limit, DeviceSearch deviceSearch) {

        log.debug("分页查询产品列表！搜索条件：deviceSearch：" + deviceSearch + ",page:" + page
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
            pdr = deviceService.getDevices(deviceSearch, page, limit);
            log.info("设备列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("设备列表查询异常！", e);
        }
        return pdr;
    }
}
