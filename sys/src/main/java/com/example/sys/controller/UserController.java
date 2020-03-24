package com.example.sys.controller;

import com.example.sys.entity.UserSearch;
import com.example.sys.service.UserService;
import com.example.sys.util.PageDataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by tyj on 2019/07/04.
 */
@Slf4j
@Controller
@RequestMapping("/user")

public class UserController extends BaseController{

    @Autowired
    private UserService userService;
    @RequestMapping("/userList")
    public String toUserList() {
        return "/auth/userList";
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
}
