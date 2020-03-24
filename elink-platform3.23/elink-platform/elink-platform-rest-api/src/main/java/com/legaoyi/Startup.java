package com.legaoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @SpringBootApplication = @Configuration, @EnableAutoConfiguration and @ComponentScan
 * @author gaoshengbo
 */
// 2.0的start中默认也有一个spring-boot-autoconfigure-2.0..RELEASE.jar，如果你还引用了activiti的activiti-spring-boot-starter-rest-api.jar包，需要将两个包中的
// SecurityAutoConfiguration.class 都排除
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableCaching
@EnableScheduling
public class Startup extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Startup.class, args);
    }


    /**
     * 如此配置打包后可以war包才可在tomcat下使用
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Startup.class);
    }

    @Bean("appApplicationListener")
    public AppApplicationListener appApplicationListener() {
        return new AppApplicationListener();
    }
}
