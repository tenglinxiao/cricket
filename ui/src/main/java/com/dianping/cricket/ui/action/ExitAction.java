package com.dianping.cricket.ui.action;

import com.dianping.cricket.api.cache.redis.RedisConnection;
import com.dianping.cricket.api.cache.redis.RedisConnectionPool;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.DefaultActionSupport;

import javax.servlet.http.HttpSession;

/**
 * Created by tenglinxiao on 17/8/15.
 */
public class ExitAction extends DefaultActionSupport {
    public final static String USERNAME = "username";
    public final static String TOKEN = "token";

    @Override
    public String execute() throws Exception {
        // Remove username key from session.
        HttpSession session = ServletActionContext.getRequest().getSession();

        RedisConnection connection = RedisConnectionPool.getConnectionPool().getConnection();
        if (session.getAttribute(TOKEN) != null) {
            connection.del(session.getAttribute(TOKEN).toString());
        }
        connection.release();
        session.removeAttribute(TOKEN);
        session.removeAttribute(USERNAME);
        return DefaultActionSupport.SUCCESS;
    }
}
