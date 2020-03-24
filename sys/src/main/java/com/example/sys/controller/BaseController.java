package com.example.sys.controller;

import com.alibaba.fastjson.JSON;
import com.example.sys.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseController {

    protected final static String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/uploads/";


    @Autowired
    private HttpServletRequest request;
    /**
     * 提示消息
     *
     * @param msg 提示文字
     * @return
     */
    public String showMsg(String msg) {
        String temp_url = "redirect:/show-msg?msg=" + msg;
        return temp_url;
    }


    /**
     * 提示消息
     *
     * @param msg 提示文字
     * @param url 跳转结果
     * @return
     */
    public String showMsg(String msg, String url) {
        String temp_url = "redirect:/show-msg?msg=" + URLEncoder.encode(msg) + "&url=" + url;
        System.out.println(temp_url);
        return temp_url;
    }

    public String outPutData(Object data) {
        Message msg = new Message(data.toString(), 0);
        return JSON.toJSONString(msg);
    }

    public String outPutErr(String err) {
        Message msg = new Message(err, -1);
        return JSON.toJSONString(msg);
    }

    /**
     * @param format yyyy-MM-dd HH:mm:ss
     * @return 当前字符串时间格式
     */
    public static String getStringDate(String format) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * @Description: 获取客户端IP地址
     */
    public String getIpAddr() {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

}
