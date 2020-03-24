package com.reacheng.rc.controller.admin;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.reacheng.rc.controller.BaseController;
import com.reacheng.rc.entity.Device;
import com.reacheng.rc.entity.Project;
import com.reacheng.rc.entity.User;
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
@RequestMapping(value = "admin/pro")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("")
    public String index(Model model, HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        String keywords = request.getParameter("keywords");
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Page<Project> pros = projectService.findAllByLikeName("projectName", keywords, page, 10, sort);
        System.out.println(JSON.toJSONString(pros));
        model.addAttribute("list", pros);
        model.addAttribute("page", page);
        model.addAttribute("keywords", keywords);

        User user = (User) request.getSession().getAttribute("admin");
        String roleName = user.getGadmin().getGname();
        model.addAttribute("roleName", roleName);
        return "admin/pro-list";
    }

    @GetMapping("/add")
    public String addPro(Model model){
        List<Project> devs = projectService.findAll();
        model.addAttribute("devs", devs);
        return "admin/pro-add";
    }

    @GetMapping("/edit/{project}")
    public String editPro(Project project, Model model){
        model.addAttribute("project",project);
        return "admin/pro-edit";
    }

    @DeleteMapping("/destroy/{project}")
    @ResponseBody
    public String destroy(Project project){
        this.adminLog("删除-"+project.getProjectName());
        try{
           projectService.delete(project);
        }catch (Exception e){
            System.out.println(e);
            return this.outPutErr("请先删除关联的数据");
        }

        return this.outPutData("删除成功");
    }


    @PostMapping("/save")
    @ResponseBody
    public String savePro(@Valid Project project, BindingResult result){
        if (result.hasErrors()) {//验证
            List <ObjectError> error = result.getAllErrors();
            for (ObjectError e : error) {
                return this.outPutErr(e.getDefaultMessage());
            }
            return null;
        }else{//验证通过

            if(project.getId()!=null){
                Project project_new = projectService.update(project);
                if(project_new!=null){
                    this.adminLog("修改项目信息-"+project.getProjectName());
                    return this.outPutData("保存成功");
                }
            }else{
                //新增
                project.setProjectName(project.getProjectName());
                project.setCreateTime(new Date());
                Project project1 = projectService.findByName(project.getProjectName());
                if (project1 == null){
                    Project device_new = projectService.save(project);
                    if(device_new!=null){
                        this.adminLog("新增项目信息-"+project.getProjectName());
                        return this.outPutData("保存成功");
                    }
                }else {
                    return this.outPutErr("保存失败,不能重复添加！");
                }

            }
        }
        return this.outPutErr("保存失败,请重试");
    }

}
