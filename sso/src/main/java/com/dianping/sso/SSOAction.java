package com.dianping.sso;

import com.dianping.cricket.api.cache.redis.RedisConnection;
import com.dianping.cricket.api.cache.redis.RedisConnectionPool;
import org.apache.http.client.utils.URIBuilder;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.DefaultActionSupport;

import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by tenglinxiao on 12/8/15.
 */
public class SSOAction extends DefaultActionSupport {
    public final static String TOKEN = "token";
    public final static String TARGET = "target";

    // Target url after the sso is done.
    private String target;

    private InputStream inputStream;

    public void setTarget(String target) {
        this.target = target;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;

        if (cookies != null) {
            // Find cookie that stores token.
            for (Cookie c : cookies) {
                if (c.getName().equals(TOKEN)) {
                    cookie = c;
                    break;
                }
            }
        }

        // If cookie is null, then go to the login page.
        if (cookie != null) {
            RedisConnection connection = RedisConnectionPool.getConnectionPool().getConnection();

            // Check the token value in redis cache, it's valid only if it's found.
            if (connection.get(cookie.getValue()) != null) {
                // Release connection to pool.
                connection.release();

                HttpServletResponse response = ServletActionContext.getResponse();

                // Extend the cookie expired time.
                cookie.setPath("/");
                cookie.setMaxAge(7 * 24 * 3600);
                response.addCookie(cookie);

                // Write output stream for url forward.
                writeOutputStream(this.target, cookie.getValue());

                return DefaultActionSupport.SUCCESS;
            }

            if (!connection.isIdle()) {
                connection.release();
            }
        }

        request.getSession().setAttribute(TARGET, target);
        return DefaultActionSupport.LOGIN;
    }

    public void writeOutputStream(String target, String token) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(target);
        builder.clearParameters().addParameter(TOKEN, token);
        this.inputStream = new ByteArrayInputStream(("<script>window.location.href = '" + builder + "';</script>").getBytes());
    }
}
