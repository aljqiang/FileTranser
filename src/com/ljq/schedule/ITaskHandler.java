package com.ljq.schedule;

/**
 * User: Larry
 * Date: 2015-04-01
 * Time: 10:58
 * Version: 1.0
 */

public interface ITaskHandler {
    /**
     * 处理任务类型
     * @return
     */
    public Integer getTaskType();
    /**
     * 处理逻辑
     * @param info
     */
    public void handle(ScheduleInfo info);
}
