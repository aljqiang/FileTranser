package com.ljq.http;

import com.ljq.common.ProgramConfig;
import com.ljq.core.CoreTranser;
import com.ljq.core.TransHelper;
import com.ljq.schedule.ScheduleInfo;
import com.ljq.schedule.ScheduleManager;
import com.ljq.schedule.handler.DataFileCleanHandler;
import com.ljq.schedule.handler.PdfTaskHandlerImpl;
import com.ljq.schedule.handler.YTaskHandlerImpl;
import com.ljq.transer.FileHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * User: Larry
 * Date: 2015-03-30
 * Time: 16:33
 * Version: 1.0
 */

public class AppInit extends HttpServlet{
    private static Log log = LogFactory.getLog(AppInit.class);

    @Override
    public void init() throws ServletException {
        log.info("初始化：正在进行系统初始化......");

        // 初始化程序环境配置
        ProgramConfig.init();
        // 初始化通讯程序
        CoreTranser transer = CoreTranser.create();
        // 设置到工具类
        TransHelper.setTranser(transer);
        // 初始化固定的文件参数创建器
        FileHelper.init();

        // 设置http连接为多核模式
        HttpConnection.setRunningMode(HttpConnection.RUNNING_MODE_MULTI);

        // 关闭所有定时任务
        try {
            ScheduleManager.engine().shutdownJobs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 初始化通讯程序工作线程
        transer.runPdfSchedule();
//        transer.runYSchedule();

        // 装载X与Y任务处理器
        ScheduleManager.registerTaskHandler(new PdfTaskHandlerImpl());
        ScheduleManager.registerTaskHandler(new YTaskHandlerImpl());

        // 初始化定时清理作业
        ScheduleManager.registerTaskHandler(new DataFileCleanHandler());
//        ScheduleManager.engine().lauchTask(new ScheduleInfo(-999, ProgramConfig.TASK_TYPE_CLEAN, "0 06 17 * * ? *"));

        // 初始化定时文件迁移作业
        ScheduleManager.engine().lauchTask(new ScheduleInfo(999, ProgramConfig.TASK_TYPE_PDF, "0 25 15 * * ? *"));

        super.init();

        log.info("初始化：系统初始化完成!");
    }

    @Override
    public void destroy(){
        super.destroy();
    }
}
