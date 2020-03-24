package com.legaoyi.platform.config;

/**
 * @author gaoshengbo
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.legaoyi.platform.filter.HttpMethodOverrideHeaderFilter;

@Configuration("securityConfiguration")
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired(required = false)
    private AuthenticationProvider[] aps;

    @Value("${security.ignore.matchers}")
    private String securityIgnoreMatchers;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if ((null != this.aps) && (this.aps.length > 0)) {
            for (AuthenticationProvider ap : this.aps) {
                auth.authenticationProvider(ap);
            }
        }
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        String[] antMatchers = null;
        if (this.securityIgnoreMatchers != null && !this.securityIgnoreMatchers.equals("")) {
            antMatchers = this.securityIgnoreMatchers.split(",");
        }
        if (antMatchers != null) {
            for (String Matcher : antMatchers) {
                webSecurity.ignoring().antMatchers(Matcher);
            }
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 允许所有用户访问”/”和”/home” 
        // http.authorizeRequests()// .antMatchers("/", "/home").permitAll()
        // 其他地址的访问均需验证权限  
        // .anyRequest().authenticated();// .and().formLogin();
        // 指定登录页是”/login”   
        // .loginPage("/login").permitAll();
        // 登录成功后可使用loginSuccessHandler()存储用户信息，可选。   
        // .successHandler(loginSuccessHandler())//code3   .and()   .logout() 
        // 退出登录后的默认网址是”/home”   .logoutSuccessUrl("/home")   .permitAll() 
        // .invalidateHttpSession(true).and()

        http.authorizeRequests().anyRequest().fullyAuthenticated();
        http.httpBasic();
        http.csrf().disable();
    }

    @Bean("filterRegistration")
    public FilterRegistrationBean<HttpMethodOverrideHeaderFilter> filterRegistration() {// 过滤器例子
        FilterRegistrationBean<HttpMethodOverrideHeaderFilter> registration = new FilterRegistrationBean<HttpMethodOverrideHeaderFilter>();
        registration.setFilter(new HttpMethodOverrideHeaderFilter());
        registration.addUrlPatterns("*");
        registration.setName("httpMethodOverrideHeaderFilter");
        return registration;
    }
}
