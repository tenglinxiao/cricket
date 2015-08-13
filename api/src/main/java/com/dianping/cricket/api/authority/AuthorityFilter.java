package com.dianping.cricket.api.authority;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    public final static String COOKIE_KEY = "token";
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

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_KEY)) {

            }
        }
        super.doFilter(req, res, chain);
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
