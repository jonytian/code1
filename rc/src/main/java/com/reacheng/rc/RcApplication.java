package com.reacheng.rc;

import com.reacheng.rc.core.BaseRepositoryFactoryBean;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@EnableRedisHttpSession
@EnableCaching
public class RcApplication {

    public static void main(String[] args) {
        SpringApplication.run(RcApplication.class, args);
    }


//    /**
//     * 防止文件大于10M时Tomcat连接重置
//     *
//     * @return
//     */
//    @Bean
//    public TomcatServletWebServerFactory tomcatEmbedded() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
//            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
//                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
//            }
//        });
//        return tomcat;
//    }
    /**
     * 配置上传文件大小的配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize("1024MB");
        //  总上传数据大小
        factory.setMaxRequestSize("1024MB");
        return factory.createMultipartConfig();
    }



}
