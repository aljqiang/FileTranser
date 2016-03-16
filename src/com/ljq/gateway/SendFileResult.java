package com.ljq.gateway;

/**
 * User: Larry
 * Date: 2015-03-27
 * Time: 16:27
 * Version: 1.0
 */

public class SendFileResult {
    private boolean success;
    private String code;
    private String msg;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
