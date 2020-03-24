package com.legaoyi;

import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.spring.DwrSpringServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {"classpath*:applicationContext*.xml"})
public class AppConfiguration {

    /**
     * dwr消息推送
     * @return
     */
    @Bean
    public ServletRegistrationBean<DwrSpringServlet> dwrSpringServlet() {
        DwrSpringServlet servlet = new DwrSpringServlet();
        ServletRegistrationBean<DwrSpringServlet> registrationBean = new ServletRegistrationBean<DwrSpringServlet>(servlet, "/dwr/*");
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("debug", "true");
        initParameters.put("activeReverseAjaxEnabled","true");
        initParameters.put("pollAndCometEnabled","true");
        initParameters.put("crossDomainSessionSecurity","false");
        initParameters.put("allowScriptTagRemoting","true");
        initParameters.put("disconnectedTime","10000");
        initParameters.put("org.directwebremoting.extend.ServerLoadMonitor","org.directwebremoting.impl.PollingServerLoadMonitor");
        registrationBean.setInitParameters(initParameters);
        return registrationBean;
    }
}
