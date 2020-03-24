package com.example.logsys.controller;

import com.example.logsys.entity.User;
import com.example.logsys.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping(value = "/admin")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        return "pages/index";
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        return "welcome";
    }

    @PostMapping("/login")
    @ResponseBody
    public String loginAt(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // 用户是否存在
        User existUser = userService.findByName(username);
        String message;
        if (existUser == null) {
            message = "该用户不存在，请您联系管理员";
            log.debug("用户登录，结果=responseResult:" + message);
            return this.outPutErr(message);
        } else {
        User user = userService.login(username, password);
        if (user == null) {
            return this.outPutErr("密码不正确");
        }
        request.getSession().setAttribute("user", user);
        }
        return this.outPutData("登录成功");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        return "redirect:"+path+"/admin/login";
    }


}
