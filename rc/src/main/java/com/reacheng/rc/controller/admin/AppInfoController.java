package com.reacheng.rc.controller.admin;

import com.alibaba.fastjson.JSON;
import com.reacheng.rc.controller.BaseController;
import com.reacheng.rc.entity.AppInfo;
import com.reacheng.rc.entity.Device;
import com.reacheng.rc.entity.Project;
import com.reacheng.rc.entity.User;
import com.reacheng.rc.service.AppInfoService;
import com.reacheng.rc.service.DeviceService;
import com.reacheng.rc.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "admin/app-info")
public class AppInfoController extends BaseController {

    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private ProjectService projectService;


    @GetMapping("")
    public String index(Model model, HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        String keywords = request.getParameter("keywords");
        Sort sort = new Sort(Sort.Direction.ASC, "id");

        Page<AppInfo> devs = appInfoService.findAllByLikeName("appName", keywords, page, 10, sort);
        System.out.println(JSON.toJSONString(devs));
        model.addAttribute("list", devs);
        model.addAttribute("page", page);
        model.addAttribute("keywords", keywords);
        User user = (User) request.getSession().getAttribute("admin");
        String roleName = user.getGadmin().getGname();
        model.addAttribute("roleName", roleName);
        return "admin/app-info-list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        List<AppInfo> pros = appInfoService.findAll();
        model.addAttribute("pros", pros);
        return "admin/app-info-add";
    }

    @GetMapping("/edit/{appInfo}")
    public String edit(AppInfo appInfo, Model model) {
        model.addAttribute("app", appInfo);
        return "admin/app-info-edit";
    }

    @DeleteMapping("/destroy/{appInfo}")
    @ResponseBody
    public String destroy(AppInfo appInfo) {
        this.adminLog("删除-" + appInfo.getAppName());
        appInfoService.delete(appInfo);
        return this.outPutData("删除成功");
    }


    @PostMapping("/save")
    @ResponseBody
    public String save(@Valid AppInfo appInfo, BindingResult result) {
        if (result.hasErrors()) {//验证
            List<ObjectError> error = result.getAllErrors();
            for (ObjectError e : error) {
                return this.outPutErr(e.getDefaultMessage());
            }
            return null;
        } else {//验证通过

            if (appInfo.getId() != null) {
                AppInfo appInfo_new = appInfoService.update(appInfo);
                if (appInfo_new != null) {
                    this.adminLog("修改应用信息-" + appInfo.getAppName());
                    return this.outPutData("保存成功");
                }
            } else {
                //新增
                appInfo.setCreateTime(new Date());
                AppInfo appInfo1 = appInfoService.findByPackageName(appInfo.getPackageName());
                AppInfo appInfo_new = appInfoService.save(appInfo);
                if (appInfo1 == null) {
                    if (appInfo_new != null) {
                        this.adminLog("新增应用信息-" + appInfo.getAppName());
                        return this.outPutData("保存成功");
                    }
                } else {
                    return this.outPutErr("保存失败,不能重复添加");
                }


            }
        }
        return this.outPutErr("保存失败,请重试");
    }

}
