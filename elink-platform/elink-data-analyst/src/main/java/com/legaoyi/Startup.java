package com.legaoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @SpringBootApplication = @Configuration, @EnableAutoConfiguration and @ComponentScan
 * @author gaoshengbo
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class Startup extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Startup.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Startup.class, args);
	}
}
