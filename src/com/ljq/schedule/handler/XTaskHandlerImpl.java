package com.ljq.schedule.handler;

import com.ljq.common.ConstantKey;
import com.ljq.common.ProgramConfig;
import com.ljq.core.TransHelper;
import com.ljq.http.TestReciveOrderServlet;
import com.ljq.queue.SendTaskEntity;
import com.ljq.schedule.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Larry
 * Date: 2015-04-01
 * Time: 11:18
 * Version: 1.0
 */

public class XTaskHandlerImpl implements ITaskHandler {
    private static Log log = LogFactory.getLog(XTaskHandlerImpl.class);

    @Override
    public Integer getTaskType() {
        return ProgramConfig.TASK_TYPE_X;
    }

    @Override
    public void handle(ScheduleInfo info) {
        int orderType = 1;
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFmt.format(new Date());
        String dataFilePath = ProgramConfig.X.getProperty(ConstantKey.transer_send_dir, "Z:/file_jd/") + dateStr + "/";

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
