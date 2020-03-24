package com.tyj.venus.controller.home;


import com.alibaba.fastjson.JSON;
import com.sun.deploy.ui.AppInfo;
import com.tyj.venus.controller.BaseController;
import com.tyj.venus.entity.*;
import com.tyj.venus.service.NewsService;
import com.tyj.venus.service.SystemService;
import com.tyj.venus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private NewsService newsService;
    @Autowired
    private SystemService systemService;


    @GetMapping("/")
    public String index(Model model) {

        List<Banner> banners = new ArrayList<>();
        Banner banner = new Banner();
        banner.setImageUrl("home_img1.jpg");
        banner.setTitle("“几亿人都在买”拼多多，终被刘强东“质疑”：网友回复“打脸”");
        banner.setTag("娱乐");
        banners.add(banner);

        Banner banner1 = new Banner();
        banner1.setImageUrl("home_img2.jpg");
        banner1.setTitle("“几亿人都在买”拼多多，终被刘强东“质疑”：网友回复“打脸1”");
        banner1.setTag("科技");
        banners.add(banner1);

        model.addAttribute("banners",banners);
        return "home/index";
    }



    @GetMapping("/api/list")
    @ResponseBody
    public Page<News> getData( HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        String keywords = request.getParameter("keywords");
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Page<News> news = newsService.findAllByLikeName("title", keywords, page, 10, sort);
        System.out.println("data:"+JSON.toJSONString(news));
        return news;
    }


    @GetMapping("list")
    public String list(Model model) {
        Systems systems = systemService.get(1);
        model.addAttribute("sys",systems);
        return "home/list";
    }

    @GetMapping("iframe")
    public String iframe(Model model) {
        Systems systems = systemService.get(1);
        model.addAttribute("sys",systems);
        return "home/iframe";
    }


    @GetMapping("search")
    public String search(Model model) {
        Systems systems = systemService.get(1);
        model.addAttribute("sys",systems);
        return "home/search";
    }
    @GetMapping("user")
    public String user(Model model) {
        Systems systems = systemService.get(1);
        model.addAttribute("sys",systems);
        return "home/user";
    }
    @GetMapping("detail")
    public String detail(Model model) {
        Systems systems = systemService.get(1);
        model.addAttribute("sys",systems);
        return "home/detail";
    }




    @PostMapping("msg-commit")
    @ResponseBody
    public String msgCommit(@Valid UserMsg userMsg, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> error = result.getAllErrors();
            for (ObjectError e : error) {
                return this.outPutErr(e.getDefaultMessage());
            }
        }
        Systems  systems = systemService.get(1);
        userMsg.setSendEmail(systems.getEmail());//管理员后台设置的邮箱地址

        return this.outPutData("提交成功,我们将尽快和您联系");

    }

}
