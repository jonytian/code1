package com.example.sys.controller;

import com.alibaba.fastjson.JSON;
import com.example.sys.entity.User;
import com.example.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "admin")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

//    @GetMapping("/index")
//    public String index() {
//        return "auth/index";
//    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

//    @PostMapping("/login")
//    public String loginAt(HttpServletRequest request) {
//        String userName = request.getParameter("username");
//        String password = request.getParameter("password");
//                String remember = "-1";
//
//        User admin = userService.adminLogin(userName, password);
//
//        if (admin == null) {
//            return this.outPutErr("用户名或密码不正确");
//        }
//        System.out.println(JSON.toJSONString(admin));
//        //记住密码 session 永远不失效
//        if (remember == "-1") {
//            request.getSession().setMaxInactiveInterval(Integer.parseInt(remember));
//        }
//
//        request.getSession().setAttribute("admin", admin);
//        return "product/productList";
//    }

    @PostMapping("/login1")
    @ResponseBody
    public String loginAt(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = "-1";

        User admin = userService.adminLogin(username,password);
        if (admin == null) {
            return this.outPutErr("用户名或密码不正确");
        }

        System.out.println(JSON.toJSONString(admin));
        //记住密码 session 永远不失效
        if (remember == "-1") {
            request.getSession().setMaxInactiveInterval(Integer.parseInt(remember));
        }

        request.getSession().setAttribute("admin", admin);
        return this.outPutData("登录成功");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("admin");
        String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        return "redirect:"+path+"/admin";
    }


}
