package com.dianping.sso;

import javax.servlet.ServletException;

/**
 * Created by tenglinxiao on 13/8/15.
 */
public class SSOException {
    public static class RequiredMissingException extends ServletException {
        public RequiredMissingException(String msg) {
            super(msg);
        }

        public RequiredMissingException(Throwable throwable) {
            super(throwable);
        }
    }

    public static class MethodTypeException extends ServletException {
        public MethodTypeException(String msg) {
            super(msg);
        }

        public MethodTypeException(Throwable throwable) {
            super(throwable);
        }
    }

}
