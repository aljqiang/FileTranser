package com.ljq.core.strategy;

import com.ljq.core.IQueueScheduleStrategy;
import com.ljq.core.TransHelper;
import com.ljq.queue.ATaskEntity;
import com.ljq.queue.SendTaskEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * X调度策略
 * User: Larry
 * Date: 2015-03-30
 * Time: 15:21
 * Version: 1.0
 */

public class XQueueScheduleStrategy implements IQueueScheduleStrategy {
    private static Log log= LogFactory.getLog(XQueueScheduleStrategy.class);

    @Override
    public void addTaskEntity(ATaskEntity entity) {
        TransHelper.transer()
                .taskQueue()
                .xTaskQueue()
                .addQueueElement((SendTaskEntity) entity);
    }

    @Override
    public QueueScheduleResult canSend() {
        QueueScheduleResult result = new QueueScheduleResult();
        // 从队列获取任务
        if(TransHelper.transer().checkXLoad()){
            SendTaskEntity task = TransHelper.transer()
                    .taskQueue()
                    .xTaskQueue()
                    .getQueueElement();
            result.setStatus((task!=null));
            if(!result.canSend()){
                TransHelper.transer().decreaseXCnt();
            }else{
                result.setTaskEntity(task);
            }
        }
        return result;
    }
}
