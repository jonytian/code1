package com.reacheng.rc.core;

import com.reacheng.rc.dao.MenuRepository;
import com.reacheng.rc.dao.SystemRepository;
import com.reacheng.rc.dao.UserRepository;
import com.reacheng.rc.dao.AppRepository;
import com.reacheng.rc.dao.DeviceRepository;
import com.reacheng.rc.entity.*;
import com.reacheng.rc.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StartApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected SystemRepository systemRepository;

    @Autowired
    protected AppRepository appRepository;

    @Autowired
    protected DeviceRepository deviceRepository;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("====系统初始化开始====");
//        //用户初始化
//        if (userRepository.count() <= 0) {
//            User user = new User();
//            user.setUserName("admin");
//            user.setPassword(Tools.Md5("123456"));
//            user.setIsSuper(true);
//            user.setEmail("upadate@reacheng.com");
//            user.setCreatedAt(new Date());
//
//            userRepository.save(user);
//            System.out.println("==初始化账号密码完毕==");
//        }
//
//        //系统设置初始化
//        if (systemRepository.count() <= 0) {
//            Systems system = new Systems();
//            system.setName("应用升级管理系统");
//            system.setKeywords("系统");
//            system.setDescription("系统描述");
//            system.setUrl("http://www.rechx.com");
//            system.setAddress("上海市浦东新区");
//            system.setEmail("upadate@reacheng.com");
//            system.setMobile("021-666666666");
//            system.setIpc("备案号");
//            system.setCompany("上海锐承通讯技术有限公司");
//            system.setVersion("0.0.1");
//            systemRepository.save(system);
//            System.out.println("==初始化系统设置==");
//        }
//
//        //菜单初始化
//        if (menuRepository.count() <= 0) {
//            /***************系统菜单*******************/
//            Menu menu = new Menu();
//            menu.setMenuName("系统管理");
//            menu.setMenuIcon("fa-home");
//            Menu menu1 = menuRepository.save(menu);
//
//            Menu menu100 = new Menu();
//            menu100.setMenuName("应用管理");
//            menu100.setMenuIcon("fa-desktop");
//            Menu menu111 = menuRepository.save(menu100);
//
//            Menu menu2 = new Menu();
//            menu2.setMenuName("角色管理");
//            menu2.setMenuLink("admin/role");
//            menu2.setMenuIcon("fa-info-circle");
//            menu2.setParentId(menu1.getMenuId());
//
//            Menu menu3 = new Menu();
//            menu3.setMenuName("用户管理");
//            menu3.setMenuLink("admin/user");
//            menu3.setMenuIcon("fa-home");
//            menu3.setParentId(menu1.getMenuId());
//
//            Menu menu4 = new Menu();
//            menu4.setMenuName("菜单管理");
//            menu4.setMenuLink("admin/menu");
//            menu4.setMenuIcon("fa-envelope");
//            menu4.setParentId(menu1.getMenuId());
//
//            Menu menu5 = new Menu();
//            menu5.setMenuName("系统设置");
//            menu5.setMenuLink("admin/system");
//            menu5.setMenuIcon("fa-desktop");
//            menu5.setParentId(menu1.getMenuId());
//
//            Menu menu6 = new Menu();
//            menu6.setMenuName("系统日志");
//            menu6.setMenuLink("admin/admin-log");
//            menu6.setMenuIcon("fa-magic");
//            menu6.setParentId(menu1.getMenuId());
//
//            /***************应用菜单*******************/
//            Menu menu112 = new Menu();
//            menu112.setMenuName("项目列表");
//            menu112.setMenuLink("/admin/pro");
//            menu112.setMenuIcon("fa-magic");
//            menu112.setParentId(menu111.getMenuId());
//
//            Menu menu113 = new Menu();
//            menu113.setMenuName("设备列表");
//            menu113.setMenuLink("/admin/dev");
//            menu113.setMenuIcon("fa-picture-o");
//            menu113.setParentId(menu111.getMenuId());
//
//            Menu menu114 = new Menu();
//            menu114.setMenuName("配置应用");
//            menu114.setMenuLink("/admin/app-info");
//            menu114.setMenuIcon("fa-edit");
//            menu114.setParentId(menu111.getMenuId());
//
//            Menu menu115 = new Menu();
//            menu115.setMenuName("应用列表");
//            menu115.setMenuLink("/admin/app");
//            menu115.setMenuIcon("fa-bar-chart-o");
//            menu115.setParentId(menu111.getMenuId());
//
//            Menu menu116 = new Menu();
//            menu116.setMenuName("白名单");
//            menu116.setMenuLink("/admin/white");
//            menu116.setMenuIcon("fa-cutlery");
//            menu116.setParentId(menu111.getMenuId());
//
//            Menu menu117 = new Menu();
//            menu117.setMenuName("应用日志");
//            menu117.setMenuLink("/admin/app-log");
//            menu117.setMenuIcon("fa-cutlery");
//            menu117.setParentId(menu111.getMenuId());
//
//            menuRepository.save(menu2);
//            menuRepository.save(menu3);
////            menuRepository.save(menu4);
////            menuRepository.save(menu5);
//            menuRepository.save(menu6);
//
//            menuRepository.save(menu112);
//            menuRepository.save(menu113);
//            menuRepository.save(menu114);
//            menuRepository.save(menu115);
//            menuRepository.save(menu116);
//            menuRepository.save(menu117);
//            System.out.println("==初始化菜单完毕==");
//        }
//
//        // 初始化应用数据
//        if (appRepository.count() <= 0){
//            Application application = new Application();
//            application.setDownloadUrl("download/qqlite.apk");
//            application.setMd5("5a30323df0e43bafa711d422782a08e4");
//            application.setPackageName("com.tencent.qqlite");
//            application.setVersionCode(999);
//            application.setType(0);
//            application.setAppName("QQ轻聊版");
//            application.setStatus("待审核");
//            application.setClassName("com.tencent.mobileqq.activity.SplashActivity");
//            Project project =new Project();
//            project.setProjectName("Test1");
//            project.setCreateTime(new Date());
//            application.setProject(project);
//
//
//            Application application1 = new Application();
//            application1.setDownloadUrl("download/mobileqq_android.apk");
//            application1.setMd5("55d93485ebee76dc6787df43bc577f94");
//            application1.setPackageName("com.tencent.mobileqq");
//            application1.setVersionCode(2000);
//            application1.setAppName("QQ应用版");
//            application1.setStatus("待审核");
//            application1.setType(1);
//            application1.setClassName("com.tencent.mobileqq.activity.SplashActivity");
//            Project project1 =new Project();
//            project1.setProjectName("Test2");
//            project1.setCreateTime(new Date());
//            application1.setProject(project1);
//
//            appRepository.save(application);
//            appRepository.save(application1);
//            System.out.println("==初始化默认应用完毕==");
//
//        }
//
//        // 初始化设备数据
//        if (deviceRepository.count() <= 0){
//            Device device = new Device();
//            device.setImei("865847038000533");
//            device.setCreateTime(new Date());
//            Project project = new Project();
//            project.setProjectName("MIRROR_CMCC_CM02_A");
//            project.setCreateTime(new Date());
//            device.setProject(project);
//            deviceRepository.save(device);
//            System.out.println("==初始化设备数据完毕==");
//        }
        System.out.println("====系统初始化结束====");
    }
}
