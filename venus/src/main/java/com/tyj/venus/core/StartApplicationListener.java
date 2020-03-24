package com.tyj.venus.core;


import com.tyj.venus.dao.MenuRepository;
import com.tyj.venus.dao.NewsRepository;
import com.tyj.venus.dao.SystemRepository;
import com.tyj.venus.dao.UserRepository;
import com.tyj.venus.entity.Menu;
import com.tyj.venus.entity.News;
import com.tyj.venus.entity.Systems;
import com.tyj.venus.entity.User;
import com.tyj.venus.tools.Tools;
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
    protected NewsRepository newsRepository;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("====系统初始化====");
        //用户初始化
        if (userRepository.count() <= 0) {
            User user = new User();
            user.setUserName("admin");
            user.setPassword(Tools.Md5("12345"));
            user.setAdminIsSuper(true);
            user.setEmail("123@qq.com");
            userRepository.save(user);
            System.out.println("==初始化账号密码完毕==");
        }

        //系统设置初始化
        if (systemRepository.count() <= 0) {
            Systems system = new Systems();
            system.setName("venus");
            system.setKeywords("SEO-关键字");
            system.setDescription("SEO优化的描述");
            system.setUrl("http://www.tyj.com");
            system.setAddress("上海市闵行区");
            system.setEmail("123@qq.com");
            system.setMobile("4123131");
            system.setIpc("备案号");
            system.setCompany("上海venus科技");
            systemRepository.save(system);
            System.out.println("==初始化系统设置==");
        }

        //菜单初始化
        if (menuRepository.count() <= 0) {

            Menu menu = new Menu();
            menu.setMenuName("系统管理");
            menu.setMenuIcon("fa-bar-chart-o");
            Menu menu1 = menuRepository.save(menu);

            Menu menu2 = new Menu();
            menu2.setMenuName("角色管理");
            menu2.setMenuLink("admin/role");
            menu2.setParentId(menu1.getMenuId());

            Menu menu3 = new Menu();
            menu3.setMenuName("用户管理");
            menu3.setMenuLink("admin/user");
            menu3.setParentId(menu1.getMenuId());

            Menu menu4 = new Menu();
            menu4.setMenuName("菜单管理");
            menu4.setMenuLink("admin/menu");
            menu4.setParentId(menu1.getMenuId());

            Menu menu5 = new Menu();
            menu5.setMenuName("系统设置");
            menu5.setMenuLink("admin/admin-log");
            menu5.setParentId(menu1.getMenuId());

            Menu menu6 = new Menu();
            menu6.setMenuName("管理员日志");
            menu6.setMenuLink("admin/system");
            menu6.setParentId(menu1.getMenuId());

            menuRepository.save(menu2);
            menuRepository.save(menu3);
            menuRepository.save(menu4);
            menuRepository.save(menu5);
            menuRepository.save(menu6);

            System.out.println("==初始化菜单完毕==");
        }


         // 初始化主页
        if (newsRepository.count() <= 0) {
            News news = new News();
            news.setImageUrl("../res/static/img/paging_img7.jpg");
            news.setDescription("国产手机迎来了最好的局面，除了苹果、三星之外，似乎就是国产手机的天下了。但是，我们不得不承认一个事实是...");
            news.setContent("国产手机迎来了最好的局面，除了苹果、三星之外，似乎就是国产手机的天下了。但是，我们不得不承认一个事实是...");
            news.setPromulgator("爱科学1");
            news.setReleaseDate(new Date());
            news.setCategory("科技1");
            news.setTitle("手机就啊手机就啊手机就啊手机就啊手机就啊");
            news.setComment("这是个好文章");
            news.setCommentCount(1);
            news.setCreatedAt(new Date());
            newsRepository.save(news);
            System.out.println("==初始化主页完毕==");
        }





    }
}
