package com.ljq.schedule.impl;

import com.ljq.schedule.ScheduleInfo;
import com.ljq.schedule.ScheduleManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * User: Larry
 * Date: 2015-04-01
 * Time: 11:12
 * Version: 1.0
 */

public class BaseJob implements Job {
    @Override
    public void execute(JobExecutionContext cxt) throws JobExecutionException {
        long task_id = cxt.getJobDetail().getJobDataMap().getLong(ScheduleInfo.FLAG_TASK_ID);
        int task_type = cxt.getJobDetail().getJobDataMap().getInt(ScheduleInfo.FLAG_TASK_TYPE);
        ScheduleInfo scheduleInfo = new ScheduleInfo(task_id,task_type);
        ScheduleManager.addScheduler(scheduleInfo);
    }
}
