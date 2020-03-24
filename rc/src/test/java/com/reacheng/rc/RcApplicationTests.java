package com.reacheng.rc;

import com.reacheng.rc.core.MapCache;
import com.reacheng.rc.entity.Application;
import com.reacheng.rc.entity.Device;
import com.reacheng.rc.entity.Project;
import com.reacheng.rc.entity.Whitelist;
import com.reacheng.rc.service.AppService;
import com.reacheng.rc.service.DeviceService;
import com.reacheng.rc.service.ProjectService;
import com.reacheng.rc.service.WhitelistService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RcApplicationTests {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private WhitelistService whitelistService;


    @Autowired
    private ProjectService projectService;

    @Autowired
    private AppService appService;

    //Update<update@reacheng.com>
    @Test
    public void contextLoads() {
        System.out.println("####################开始发送###################");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("update@reacheng.com");
        message.setTo("yaojiang.tian@reacheng.com");
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");
        mailSender.send(message);
        System.out.println("#############发送完毕##########################");
    }

    @Test
    public void insertW() {

        Project p = projectService.findByName("MIRROR_CMCC_CM02_B");
//        p.setId(22);
        Application a = new Application();
        a.setId(30);
        List<Device> pubListExp = deviceService.findAllDevice(p);



        if(pubListExp.size()>0) {
            for (int i = 0; i < pubListExp.size(); i++) {
                String imei =  pubListExp.get(i).getImei();
                Whitelist whitelist = whitelistService.findByImeiAndAppId(imei,30);
                if(whitelist != null){
                    whitelist.setImei(imei);
                    whitelist.setStatus("安装");
                    whitelist.setAppId(30);
                    whitelist.setApplication(a);
                    whitelist.setCreateTime(new Date());
                    whitelist.setProjectId(22);
                    Whitelist whitelist_new = whitelistService.update(whitelist);
                   System.out.println("更新安装成功"+i);

                }else{
                    //新增
                    Whitelist whitelist1 = new Whitelist();
                    whitelist1.setImei(imei);
                    whitelist1.setStatus("安装");
                    whitelist1.setAppId(30);
                    whitelist1.setCreateTime(new Date());
                    whitelist1.setProjectId(22);
                    whitelist1.setApplication(a);
                    Whitelist whitelist_new = whitelistService.save(whitelist1);
                    System.out.println("报存安装成功"+i);

                }
            }
            }
        }



    @Test
    public void testCache(){
            MapCache mapCache = new MapCache();
            mapCache.add("uid_10001", "{1}", 5 * 1000);
            mapCache.add("uid_10002", "{2}", 5 * 1000);
            mapCache.add("uid_10003", "{3}", 5 * 1000);
            System.out.println("从缓存中取出值:" + mapCache.get("uid_10001"));
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("5秒钟过后");
            System.out.println("从缓存中取出值:" + mapCache.get("uid_10001"));
            // 5秒后数据自动清除了
        }




}
