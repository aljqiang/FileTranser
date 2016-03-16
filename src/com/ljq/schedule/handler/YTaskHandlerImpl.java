package com.ljq.schedule.handler;

import com.ljq.common.ProgramConfig;
import com.ljq.core.TransHelper;
import com.ljq.http.TestReciveOrderServlet;
import com.ljq.queue.SendTaskEntity;
import com.ljq.schedule.ITaskHandler;
import com.ljq.schedule.ScheduleInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Larry
 * Date: 2015-04-01
 * Time: 14:14
 * Version: 1.0
 */

public class YTaskHandlerImpl implements ITaskHandler {
    private static Log log= LogFactory.getLog(YTaskHandlerImpl.class);

    @Override
    public Integer getTaskType() {
        return ProgramConfig.TASK_TYPE_Y;
    }

    @Override
    public void handle(ScheduleInfo info) {
        int orderType = 2;
        String dataFilePath = "D:/test";

        for (int i = 0; i < 11; i++) {
            if (!TestReciveOrderServlet.checkOrderType(orderType)) {
                log.debug("不能识别的发送任务类型.");
            }

            // 封装任务发送实体类
            SendTaskEntity entity = new SendTaskEntity();
            entity.setTaskType(orderType);
            entity.setSrcDir(dataFilePath);

            // 加入任务队列
            TransHelper.getScheduleStrategy(orderType).addTaskEntity(entity);
            // 写入日志
            log.info("添加任务(taskType:" + orderType + " srcDir:" + dataFilePath + ")完成.");
        }
    }
}
