package com.pengcheng.nioserver.bootx.base;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 17-2-20 下午2:14.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class GenericException extends RuntimeException {

    private int code;
    private String message;
    private Map<String, Object> response = new HashMap();

    protected GenericException(int code, String message) {
        this.code = code;
        this.message = message;
        this.response.put("status", Integer.valueOf(this.code));
        this.response.put("message", this.message);
    }

    protected GenericException(int code, String message, Throwable cause) {
        super(cause);
        this.code = code;
        this.message = message;
        this.response.put("status", Integer.valueOf(this.code));
        this.response.put("message", this.message);
    }

    public final int getCode() {
        return this.code;
    }

    public final String getMessage() {
        return this.message;
    }

    Map<String, Object> getAttributes() {
        return this.response;
    }

    protected void setAttribute(String responseKey, Object responseValue) {
        this.response.put(responseKey, responseValue);
    }

}
