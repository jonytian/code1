package com.reacheng.rc.controller.admin;

import com.alibaba.fastjson.JSON;
import com.reacheng.rc.controller.BaseController;
import com.reacheng.rc.entity.AppLog;
import com.reacheng.rc.entity.AppLogVO;
import com.reacheng.rc.entity.AppVO;
import com.reacheng.rc.service.AppLogService;
import com.reacheng.rc.tools.ExcelUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "admin/app-log")
public class AppLogController extends BaseController {

    @Autowired
    private AppLogService appLogService;

    List<AppLogVO> dataList = null;

    @GetMapping("")
    public String index(Model model, HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        String keywords = request.getParameter("keywords");
        String projectName = request.getParameter("projectName");
        String status = request.getParameter("status");
        if ("0".equals(status)){
            status = "";
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
//        Page<AppLog> logs = appLogService.findAllByLikeName("imei", keywords, page, 10, sort);
        page = page==0?1:page;
        Page<AppLog> logs = appLogService.findAllLog(projectName,keywords,status,page-1,10,sort);

        System.out.println(JSON.toJSONString(logs));
        model.addAttribute("list", logs);
        model.addAttribute("page", page);
        model.addAttribute("keywords", keywords);
        model.addAttribute("projectName", projectName);
        model.addAttribute("status", status);


        List<AppLog> appLogs = logs.getContent();
        dataList = new ArrayList<>();
        appLogs.stream().forEach(o->{
            AppLogVO appLogVO = new AppLogVO();
            appLogVO.setImei(o.getImei());
            appLogVO.setPackageName(o.getPackageName());
            appLogVO.setProjectName(o.getProjectName());
            appLogVO.setStatus(o.getStatus());
            appLogVO.setVersionCode(o.getVersionCode());


            dataList.add(appLogVO);
        });
        return "admin/app-log";
    }


    /**
     * 下载数据
     * @return 返回excel数据
     */
    @RequestMapping(value = "/exportData", method = RequestMethod.GET, produces ="application/json;charset=UTF-8")
    @ResponseBody
    @ApiOperation(value = "下载数据", httpMethod = "GET", produces = "application/json;charset=UTF-8")
    public void exportData(HttpServletResponse response){
        long t1 = System.currentTimeMillis();
        ExcelUtils.writeExcel(response,dataList,AppLogVO.class);
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("write over! cost:%sms", (t2 - t1)));
    }


}
