package com.dianping.sso;

import com.dianping.cricket.api.cache.redis.RedisConnection;
import com.dianping.cricket.api.cache.redis.RedisConnectionPool;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.DefaultActionSupport;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;

/**
 * Created by tenglinxiao on 12/8/15.
 */
public class AuthenticationAction extends DefaultActionSupport {
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    public final static String TARGET = "target";
    public final static String TOKEN = "token";

    private InputStream inputStream;
    private String username;
    private String password;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String target = request.getSession().getAttribute(TARGET).toString();

        if (this.authenticate()) {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(username.getBytes());
            String token = this.toHex(digest.digest());

            Cookie cookie = new Cookie(TOKEN, token);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 3600);
            ServletActionContext.getResponse().addCookie(cookie);
            this.writeOutputStream(target);

            RedisConnection connection = RedisConnectionPool.getConnectionPool().getConnection();
            connection.set(token, username);
            connection.release();

            return DefaultActionSupport.SUCCESS;
        }

        this.addActionError("Invalid username / password!");
        this.addActionMessage("test");
        return DefaultActionSupport.LOGIN;
    }

    // Convert the md5 byte array to hex string.
    private String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b & 0xff));
        }
        return builder.toString();
    }

    public void writeOutputStream(String target) {
        this.inputStream = new ByteArrayInputStream(("<script>window.location.href = '" + target + "';</script>").getBytes());
    }

    public boolean authenticate() {
        return this.password.equals("angelfish");
    }
}
