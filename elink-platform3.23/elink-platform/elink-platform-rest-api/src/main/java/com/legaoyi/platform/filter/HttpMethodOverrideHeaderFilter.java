package com.legaoyi.platform.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

public class HttpMethodOverrideHeaderFilter extends OncePerRequestFilter {

    private static final String X_HTTP_METHOD_OVERRIDE_HEADER = "X-HTTP-Method-Override";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerValue = request.getHeader(X_HTTP_METHOD_OVERRIDE_HEADER);
        String method = request.getMethod();
        if (headerValue != null && headerValue.equals(RequestMethod.PATCH.name()) && RequestMethod.POST.name().equals(request.getMethod())) {
            method = headerValue.toUpperCase(Locale.ENGLISH);
        }
        HttpMethodRequestWrapper wrapper = new HttpMethodRequestWrapper(request, method);
        wrapper.addHeader("_enterpriseId", getCurrentUserEnterpriseId());
        wrapper.addHeader("_userAccount", getCurrentUserAccount());
        wrapper.addHeader("_userId", getCurrentUserId());
        wrapper.addHeader("_userType", String.valueOf(getCurrentUserType()));
        filterChain.doFilter(wrapper, response);
    }

    private String getCurrentUserEnterpriseId() {
        return (String) getCurrentUserDetails().get("enterpriseId");
    }

    private String getCurrentUserAccount() {
        return (String) getCurrentUserDetails().get("userAccount");
    }

    private String getCurrentUserId() {
        return (String) getCurrentUserDetails().get("userId");
    }

    private Short getCurrentUserType() {
        return (Short) getCurrentUserDetails().get("userType");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getCurrentUserDetails() {
        try {
            return (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getDetails();
        } catch (Exception e) {
        }
        return new HashMap<String, Object>();
    }

    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

        private final String method;

        private final Map<String, String> customHeaders;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method;
            this.customHeaders = new HashMap<String, String>();
        }

        @Override
        public String getMethod() {
            return this.method;
        }

        public void addHeader(String name, String value) {
            this.customHeaders.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = this.customHeaders.get(name);
            if (headerValue != null) {
                return headerValue;
            }
            return ((HttpServletRequest) getRequest()).getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Set<String> set = new HashSet<String>(this.customHeaders.keySet());
            Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
            while (e.hasMoreElements()) {
                String n = e.nextElement();
                set.add(n);
            }
            return Collections.enumeration(set);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> values = Collections.list(super.getHeaders(name));
            if (this.customHeaders.containsKey(name)) {
                values.add(this.customHeaders.get(name));
            }
            return Collections.enumeration(values);
        }
    }

}
