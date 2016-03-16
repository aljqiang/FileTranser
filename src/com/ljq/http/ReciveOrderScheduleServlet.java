package com.ljq.http;

import com.ljq.common.ProgramConfig;
import com.ljq.schedule.CronExpHelper;
import com.ljq.schedule.ScheduleInfo;
import com.ljq.schedule.ScheduleManager;
import com.ljq.schedule.ScheduleResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * User: Larry
 * Date: 2015-04-01
 * Time: 14:39
 * Version: 1.0
 */

public class ReciveOrderScheduleServlet extends HttpServlet{
    private static Log log= LogFactory.getLog(ReciveOrderScheduleServlet.class);

    @Override
    public void init() throws ServletException{
        super.init();
    }

    @Override
    public void doGet(HttpServletRequest req,HttpServletResponse rep) throws ServletException{
        this.doPost(req,rep);
    }

    @Override
    public void doPost(HttpServletRequest req,HttpServletResponse rep) throws ServletException{
        String date=req.getParameter("date");
        String time=req.getParameter("time");
        try {
            ScheduleInfo info = new ScheduleInfo(-9999, ProgramConfig.TASK_TYPE_X,CronExpHelper.format(date,time));

            if(!ScheduleManager.engine().isTaskPlanExsit(info)){
                ScheduleResult result = ScheduleManager.engine().lauchTask(info);
                if(!result.isSucc())
                    throw new Exception("关联规划到调度器时出错:添加新调度计划(ID为:"+info.getTask_id()+")失败.");
            }
        } catch (Exception e) {
            log.debug("关联规划到调度器时出错:添加新调度计划失败.");
        }
    }
}
