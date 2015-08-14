package com.dianping.cricket.api.authority;

import com.dianping.cricket.api.cache.redis.RedisConnection;
import com.dianping.cricket.api.cache.redis.RedisConnectionPool;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by tenglinxiao on 7/8/15.
 * @author uknow.
 */
public class AuthorityFilter extends StrutsPrepareFilter {
    /**
     * Config key for urls included.
     */
    private final static String INCLUDE_KEY = "include";
    /**
     * Config key for urls excluded.
     */
    private final static String EXCLUDE_KEY = "exclude";
    /**
     * Cookie key for user login.
     */
    public final static String TOKEN_KEY = "token";
    /**
     * Parameter key for target url.
     */
    public final static String TARGET_KEY = "target";
    /**
     * Session key for user login.
     */
    public final static String USERNAME_KEY = "username";
    /**
     * Urls included.
     */
    private List<String> includeList = null;
    /**
     * Urls excluded.
     */
    private List<String> excludeList = null;
    /**
     * Whether filter all urls.
     */
    private boolean filterAll;
    // Sso service url.
    private String ssoService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String paths = null;
        if ((paths = filterConfig.getInitParameter(INCLUDE_KEY)) != null) {
            this.includeList = toList(paths);
        } else if ((paths = filterConfig.getInitParameter(EXCLUDE_KEY)) != null) {
            this.excludeList = toList(paths);
        } else {
            this.filterAll = true;
        }
    }

    protected void setSsoService(String ssoService) {
        this.ssoService = ssoService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;

        // First try to get token from request.
        String token = req.getParameter(TOKEN_KEY);

        // Secondly try to get the token from cookie.
        if (token == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(TOKEN_KEY)) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        if (token != null) {
            RedisConnection connection = RedisConnectionPool.getConnectionPool().getConnection();
            String username = connection.get(token);
            connection.release();
            if (username != null) {
                request.getSession().setAttribute(USERNAME_KEY, username);
                super.doFilter(req, res, chain);
            }
        }

        ((HttpServletResponse) res).sendRedirect(ssoService + "?" + TARGET_KEY + "=" + request.getRequestURL());
    }

    public List<String> toList(String pathList) {
        List<String> list = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(pathList, ",");
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken().trim());
        }
        return list;
    }
}
