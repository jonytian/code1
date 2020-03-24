package com.tyj.venus.controller.home;

import com.tyj.venus.controller.BaseController;
import com.tyj.venus.entity.Systems;
import com.tyj.venus.entity.User;
import com.tyj.venus.entity.UserInfo;
import com.tyj.venus.service.MenuService;
import com.tyj.venus.service.SystemService;
import com.tyj.venus.service.UserInfoService;
import com.tyj.venus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "")
public class AuthController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MenuService menuService;
    @Autowired
    private SystemService systemService;


    @GetMapping("/login")
    public String login(Model model) {
        Systems system = systemService.get(1);//系统设置
        model.addAttribute("sys", system);
        return "home/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String loginAt(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = "-1";
        UserInfo user = userInfoService.login(username, password);

        if (user == null) {
            return this.outPutErr("用户名或密码不正确");
        }
        // 记住密码session永不失效
        if (remember == "-1") {
            request.getSession().setMaxInactiveInterval(Integer.parseInt(remember));
        }
        request.getSession().setAttribute("user", user);
        return this.outPutData("登录成功");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        return "redirect:"+path+"/login";
    }


}
