package com.ljq.schedule;

/**
 * User: Larry
 * Date: 2015-04-01
 * Time: 10:50
 * Version: 1.0
 */

public class ScheduleResult {
    private boolean succ;
    private String cause;

    public ScheduleResult(){
        this.succ = true;
        this.cause = "";
    }

    public ScheduleResult(String cause){
        this.succ = false;
        this.cause = cause;
    }

    public boolean isSucc() {
        return succ;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.succ = false;
        this.cause += cause + "\r\n";
    }
}
