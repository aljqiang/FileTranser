package com.ljq.schedule;

import java.util.Date;

/**
 * User: Larry
 * Date: 2015-04-01
 * Time: 10:50
 * Version: 1.0
 */

public class ScheduleInfo {
    /**
     * 任务ID
     */
    private long task_id;

    /**
     * 渠道类型
     */
    private int task_type;
    /**
     * 触发时间
     */
    private Date startTime;
    /**
     * 时间格式
     */
    private String timeExp;

    public static final String FLAG_TASK_ID = "task_id";

    public static final String FLAG_TASK_TYPE = "task_type";

    public ScheduleInfo(){

    }

    public ScheduleInfo(long task_id,int task_type){
        this.task_id=task_id;
        this.task_type = task_type;
    }

    public ScheduleInfo(long task_id,int task_type,Date time){
        this.task_id=task_id;
        this.task_type = task_type;
        this.startTime = time;
        this.timeExp = CronExpHelper.date2Exp(time);
    }

    public ScheduleInfo(long task_id,int task_type,String exp){
        this.task_id=task_id;
        this.task_type = task_type;
        this.timeExp = exp;
    }

    public int getTask_type() {
        return task_type;
    }
    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getTimeExp() {
        return timeExp;
    }

    public void setTimeExp(String timeExp) {
        this.timeExp = timeExp;
    }

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }
}
