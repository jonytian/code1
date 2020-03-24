package com.legaoyi.platform.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.legaoyi.platform.rest.RequestParams;
import com.legaoyi.platform.util.AesUtils;
import com.legaoyi.platform.util.HttpClientUtil;
import com.legaoyi.platform.util.JsonUtil;
import com.wf.captcha.utils.CaptchaUtil;

/**
 * @author gaoshengbo
 */
@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Value("${platform.api.host}")
    private String apiHost;

    @GetMapping("/")
    public String index() {
        return "redirect:/index.do";
    }

    @RequestMapping("/captcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        CaptchaUtil.out(httpServletRequest, httpServletResponse);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestParams param) throws Exception {
        Map<String, Object> form = param.getData();
        String userName = (String) form.get("userName");
        String password = (String) form.get("password");
        String vrifyCode = (String) form.get("checkCode");

        String key = userName;
        while (key.length() < 16) {
            key += userName;
        }
        key = key.substring(0, 16);
        password = AesUtils.decrypt(password, key);
        logger.info("user login,userName={}, ip={}", userName, getRemoteAddr(request));
        try {
            if (!CaptchaUtil.ver(vrifyCode, request)) {
                CaptchaUtil.clear(request);
                throw new IllegalAccessException("验证码错误");
            } else {
                try {
                    String data = HttpClientUtil.send(HttpClientUtil.formatUrl(apiHost, param.getUrl()), HttpClientUtil.GET, param.getReadTimeout(), param.getConnectTimeout(), null, userName, password);
                    Map<String, Object> map = JsonUtil.json2Map(data);
                    Map<String, Object> resp = (Map<String, Object>) map.get("data");
                    if ("0".equals(String.valueOf(map.get("code")))) {
                        if (resp.get("userAccount") != null) {
                            resp.put("password", password);
                            request.getSession(true).setAttribute("user", resp);
                        }
                    }
                    HttpClientUtil.render(response, request.getContentType(), JsonUtil.covertObj2Str(resp));
                } catch (Exception e) {
                    HttpClientUtil.render(response, request.getContentType(), e.getMessage());
                }
            }
        } catch (Exception e) {
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put("code", 1);
            ret.put("message", "验证码错误！");
            HttpClientUtil.render(response, request.getContentType(), JsonUtil.covertObj2Str(ret));
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().removeAttribute("user");
        return index();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestParams param) throws Exception {
        try {
            Map<String, Object> form = param.getData();
            String captcha = (String) form.get("checkCode");
            String vrifyCode = (String) request.getSession().getAttribute("vrifyCode");
            if (StringUtils.isEmpty(captcha) || StringUtils.isEmpty(vrifyCode) || !vrifyCode.toLowerCase().equals(captcha.toLowerCase())) {
                throw new IllegalAccessException("验证码错误");
            } else {
                try {
                    String data = HttpClientUtil.send(HttpClientUtil.formatUrl(apiHost, param.getUrl()), HttpClientUtil.POST, param.getReadTimeout(), param.getConnectTimeout(), form, null, null);
                    HttpClientUtil.render(response, request.getContentType(), data);
                } catch (Exception e) {
                    HttpClientUtil.render(response, request.getContentType(), e.getMessage());
                }
            }
        } catch (Exception e) {
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put("code", 1);
            ret.put("message", "验证码错误！");
            HttpClientUtil.render(response, request.getContentType(), JsonUtil.covertObj2Str(ret));
        }
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
