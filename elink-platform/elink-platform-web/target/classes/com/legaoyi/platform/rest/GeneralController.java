package com.legaoyi.platform.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.legaoyi.platform.util.HttpClientUtil;

/**
 * @author gaoshengbo
 */

@Controller
@RequestMapping(value = "/common", method = {org.springframework.web.bind.annotation.RequestMethod.POST}, produces = {"application/json", "application/xml"})
public class GeneralController {

    @Value("${platform.api.host}")
    private String apiHost;

    private void render(HttpServletRequest request, HttpServletResponse response, String method, RequestParams param) {
        String ContentType = request.getContentType();
        Map<?, ?> user = (Map<?, ?>) request.getSession().getAttribute("user");
        String userName = (String) user.get("userAccount");
        String password = (String) user.get("password");
        String urlStr = HttpClientUtil.formatUrl(apiHost, param.getUrl());
        try {
            String data = HttpClientUtil.send(urlStr, method, param.getReadTimeout(), param.getConnectTimeout(), param.getData(), userName, password);
            HttpClientUtil.render(response, ContentType, data);
        } catch (Exception e) {
            HttpClientUtil.render(response, request.getContentType(), e.getMessage());
        }
    }

    @RequestMapping("/post")
    public void post(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestParams param) {
        render(request, response, HttpClientUtil.POST, param);
    }

    @RequestMapping("/get")
    public void get(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestParams param) {
        render(request, response, HttpClientUtil.GET, param);
    }

    @RequestMapping("/put")
    public void put(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestParams param) {
        render(request, response, HttpClientUtil.PUT, param);
    }

    @RequestMapping("/patch")
    public void patch(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestParams param) {
        render(request, response, HttpClientUtil.PATCH, param);
    }

    @RequestMapping("/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestParams param) {
        render(request, response, HttpClientUtil.DELETE, param);
    }

}
