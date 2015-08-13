package com.dianping.sso;

import com.dianping.cricket.api.cache.redis.RedisConnection;
import com.dianping.cricket.api.cache.redis.RedisConnectionPool;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareFilter;

import javax.servlet.*;
import java.io.IOException;

/**
 * SSO filter to verify whether the parameter target is offered.
 * @author uknow.
 * @since 0.0.1
 */
public class SSOFilter extends StrutsPrepareFilter {
    public final static String TARGET = "target";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // Target is a MUST-HAVE OPTION.
        String target = req.getParameter(TARGET);
        if (target != null) {
            super.doFilter(req, res, chain);
            return;
        }

        throw new SSOException.RequiredMissingException("Parameter [target] is required!");
    }
}
