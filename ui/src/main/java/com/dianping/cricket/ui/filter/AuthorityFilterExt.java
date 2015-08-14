package com.dianping.cricket.ui.filter;

import com.dianping.cricket.api.authority.AuthorityFilter;
import com.dianping.cricket.ui.ServiceEndPointConf;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * Created by tenglinxiao on 14/8/15.
 */
public class AuthorityFilterExt extends AuthorityFilter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        setSsoService(ServiceEndPointConf.getConf().getSsoService());
    }
}
