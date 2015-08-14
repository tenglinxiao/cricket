package com.dianping.sso;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareFilter;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by tenglinxiao on 13/8/15.
 */
public class AuthenticationFilter extends StrutsPrepareFilter {
    private final static String POST = "post";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        if (request.getMethod().equalsIgnoreCase(POST)) {
            super.doFilter(req, res, chain);
            return;
        }

        throw new SSOException.MethodTypeException("Request in [" + request.getMethod() + "] type is not allowed for this url!");
    }
}
