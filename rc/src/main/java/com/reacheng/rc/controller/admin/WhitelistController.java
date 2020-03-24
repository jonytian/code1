package com.reacheng.rc.controller.admin;

import com.alibaba.fastjson.JSON;
import com.reacheng.rc.controller.BaseController;
import com.reacheng.rc.dao.WhitelistRepository;
import com.reacheng.rc.entity.Device;
import com.reacheng.rc.entity.User;
import com.reacheng.rc.entity.Whitelist;
import com.reacheng.rc.service.WhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "admin/white")
public class WhitelistController extends BaseController {

    @Autowired
    private WhitelistService whitelistService;

    @Autowired
    private WhitelistRepository  whitelistRepository;

    @GetMapping("")
    public String index(Model model, HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        String keywords = request.getParameter("keywords");
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Page<Whitelist> whites = whitelistService.findAllByLikeName("imei", keywords, page, 10, sort);

        System.out.println(JSON.toJSONString(whites));
        model.addAttribute("list", whites);
        model.addAttribute("page", page);
        model.addAttribute("keywords", keywords);

        User user = (User) request.getSession().getAttribute("admin");
        String roleName = user.getGadmin().getGname();
        model.addAttribute("roleName", roleName);
        return "admin/white-list";
    }

    @DeleteMapping("/destroy/{whitelist}")
    @ResponseBody
    public String destroy(Whitelist whitelist){
        this.adminLog("删除-"+whitelist.getImei());
        whitelistService.delete(whitelist);
        return this.outPutData("删除成功");
    }

}
