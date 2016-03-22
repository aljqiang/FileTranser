package com.ljq.core.strategy;

import com.ljq.core.IQueueScheduleStrategy;
import com.ljq.core.TransHelper;
import com.ljq.queue.ATaskEntity;
import com.ljq.queue.SendTaskEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 结单文件传输调度策略
 * User: Larry
 * Date: 2015-03-30
 * Time: 15:21
 * Version: 1.0
 */

public class PdfQueueScheduleStrategy implements IQueueScheduleStrategy {
    private static Log log= LogFactory.getLog(PdfQueueScheduleStrategy.class);

    @Override
    public void addTaskEntity(ATaskEntity entity) {
        TransHelper.transer()
                .taskQueue()
                .pdfTaskQueue()
                .addQueueElement((SendTaskEntity) entity);
    }

    @Override
    public QueueScheduleResult canSend() {
        QueueScheduleResult result = new QueueScheduleResult();
        // 从队列获取任务
        if(TransHelper.transer().checkPdfLoad()){
            SendTaskEntity task = TransHelper.transer()
                    .taskQueue()
                    .pdfTaskQueue()
                    .getQueueElement();
            result.setStatus((task!=null));
            if(!result.canSend()){
                TransHelper.transer().decreasePdfCnt();
            }else{
                result.setTaskEntity(task);
            }
        }
        return result;
    }
}
