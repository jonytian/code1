package com.legaoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

/**
 * @SpringBootApplication = @Configuration, @EnableAutoConfiguration and @ComponentScan
 * @author gaoshengbo
 */
@SpringBootApplication
@ServletComponentScan  
public class Startup extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Startup.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Startup.class);
    }

    @Bean
    public AppApplicationListener appApplicationListener() {
        return new AppApplicationListener();
    }
}
