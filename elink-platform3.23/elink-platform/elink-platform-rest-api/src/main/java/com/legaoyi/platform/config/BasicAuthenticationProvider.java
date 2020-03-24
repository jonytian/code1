package com.legaoyi.platform.config;

import java.util.HashMap;
import java.util.Map;

import com.legaoyi.common.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.legaoyi.common.util.PasswordHelper;
import com.legaoyi.platform.service.SecurityService;

@Component("basicAuthenticationProvider")
public class BasicAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationProvider.class);

    @Autowired
    @Qualifier("securityService")
    private SecurityService securityService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userAccount = authentication.getName();
        String password = authentication.getCredentials().toString();
        Map<String, Object> userInfo = new HashMap<String, Object>();

        try {
            Map<?, ?> user = this.securityService.getUserAccount(userAccount);
            if (user != null) {
                Short state = (Short) user.get("state");
                if (state != 1) {
                    throw new DisabledException("user has been disabled ");
                }
                // 传入的是加密后的密码或者未经加密
                String pwd = (String) user.get("password");
//                System.out.println("password:"+password);
//                System.out.println("pwd:"+pwd);
//                System.out.println("salt:"+(String) user.get("salt"));
//                System.out.println("password:"+PasswordHelper.SHA1(password, (String) user.get("salt")));

                if (password.equals(pwd) || pwd.equals(PasswordHelper.SHA1(password, (String) user.get("salt")))) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userAccount, password, null);
                    userInfo.put("userId", user.get("id"));
                    userInfo.put("userType", user.get("type"));
                    userInfo.put("userAccount", userAccount);
                    userInfo.put("userName", user.get("name"));
                    userInfo.put("enterpriseId", user.get("enterpriseId"));
                    userInfo.put("orgId", user.get("orgId"));
                    userInfo.put("deptId", user.get("deptId"));
                    userInfo.put("roleId", user.get("roleId"));
                    token.setDetails(userInfo);
                    return token;
                }
            }
        } catch (Exception e) {
            logger.error("authentication errror", e);
            throw new AuthenticationServiceException("authentication errror", e);
        }
        throw new BadCredentialsException("Authentication failed for this username and password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
