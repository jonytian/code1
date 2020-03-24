package com.example.logsys.controller;


import com.example.logsys.entity.User;
import com.example.logsys.entity.UserSearch;
import com.example.logsys.service.UserService;
import com.example.logsys.util.PageDataResult;
import com.example.logsys.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/user")

public class UserController extends BaseController{

    @Autowired
    private UserService userService;
    @RequestMapping("")
    public String toUserList(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("username",user.getUsername());
        return "pages/users/list";
    }

    /**
     * 分页查询用户列表
     * @return ok/fail
     */
    @RequestMapping(value = "/getUsers", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getUsers(@RequestParam("page") Integer page,
                                   @RequestParam("limit") Integer limit, UserSearch userSearch) {
        log.debug("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",page:" + page
                + ",每页记录数量limit:" + limit);
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            // 获取用户和角色列表
            pdr = userService.getUsers(userSearch, page, limit);
            log.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户列表查询异常！", e);
        }
        return pdr;
    }

    @GetMapping("/add")
    public String addUser(User user, Model model){
        model.addAttribute("user",user);
        return "pages/users/add";
    }

    @PostMapping("/save")
    @ResponseBody
    public String saveUser(@Valid User user, BindingResult result){
        if (result.hasErrors()) {//验证
            List<ObjectError> error = result.getAllErrors();
            for (ObjectError e : error) {
                return this.outPutErr(e.getDefaultMessage());
            }

            return null;
        }else{//验证通过

            if(user.getUserId()!=null){
                int b = userService.update(user);
                if(b == 1){
                    this.adminLog("修改用户-"+user.getUsername());
                    return this.outPutData("保存成功");
                }
            }else{
                //新增
                user.setPassword(Tools.Md5(user.getPassword()));
                user.setCreateTime(new Date());
                user.setIsAdmin(0);
//                user.setLoginNum(0);
                User user1 = userService.findByName(user.getUsername());
                if(user1!=null){
                    return this.outPutErr("保存失败,账户已存在");
                }
                int b = userService.save(user);
                if(b == 1){
                    this.adminLog("新增用户-"+user.getUsername());
                    return this.outPutData("保存成功");
                }
            }
        }
        return this.outPutErr("保存失败,请重试");
    }

    @GetMapping("/edit/{user}")
    public String editUser(User user, Model model){
        model.addAttribute("user",user);
        return "pages/users/user-edit";
    }

    @DeleteMapping("/del")
    @ResponseBody
    public String delUser(@RequestBody User user){
        this.adminLog("删除用户-"+user.getUsername());
        try{
            userService.delete(user);
        }catch (Exception e){
            return this.outPutErr("删除失败");
        }
        return this.outPutData("删除成功");
    }


    @GetMapping("/set-pwd/{user}")
    public String setPwd(User user, HttpServletResponse response, Model model) throws IOException {
        model.addAttribute("user",user);
        return "pages/users/set-pwd";
    }

    @PostMapping("/set-pwd")
    @ResponseBody
    public String setPwdAct(HttpServletRequest request, User user){
        String oldPassword = request.getParameter("oldPassword");//旧密码
        String password = request.getParameter("password");//新密码
        User admin = (User) request.getSession().getAttribute("user");
        if(admin.getPassword().equals(Tools.Md5(oldPassword))){
            admin.setPassword(Tools.Md5(password));
            userService.updatePwd(admin);
            log.info("修改用户密码-"+user.getUsername());
            return this.outPutData("修改完成");
        }else{
            return this.outPutErr("旧密码不相等");
        }
    }





}
