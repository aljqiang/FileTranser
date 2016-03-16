package com.ljq.core;

import com.ljq.gateway.SendTaskWorker;
import com.ljq.queue.ATaskEntity;
import com.ljq.queue.SendTaskEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ljq.core.IQueueScheduleStrategy.QueueScheduleResult;

/**
 * 任务队列轮询线程类
 * User: Larry
 * Date: 2015-03-30
 * Time: 15:46
 * Version: 1.0
 */

public class TaskQueueScheduler extends Thread {
    private static Log log = LogFactory.getLog(TaskQueueScheduler.class);
    // 轮询频率(秒)
    private double send_loop_delay;
    // 任务类型
    private int task_type;
    // 超时配置
    private int ot;
    // 重发配置
    private int resendTime;
    // 重发间隔
    private int resendDelay;

    /**
     * 任务队列调度器
     * @param send_loop_delay
     * @param task_type
     * @param ot
     * @param resendTime
     */
    public TaskQueueScheduler(double send_loop_delay,int task_type,int ot,int resendTime,int resendDelay){
        this.send_loop_delay = send_loop_delay;
        this.task_type = task_type;
        this.ot = ot;
        this.resendTime = resendTime;
        this.resendDelay = resendDelay;
    }

    @Override
    public void run(){
        while(true){
            QueueScheduleResult result = TransHelper.getScheduleStrategy(task_type).canSend();
            // 判断发送还是等待
            if(result.canSend()){
                ATaskEntity task = result.getTaskEntity();
                if(task.getTaskHandleType() == ATaskEntity.HANDLE_TYPE_NORMAL){
                    SendTaskWorker sendTask = new SendTaskWorker((SendTaskEntity)task);
                    sendTask.setOverTime(this.ot);
                    sendTask.setResender(this.resendTime, this.resendDelay);
                    sendTask.start();
                }

                continue;
            }

            //轮询休眠
            try {
                Thread.sleep(((int)(send_loop_delay*1000)));
            } catch (InterruptedException e) {
                log.debug("轮询发送任务队列被中断.");
                throw new RuntimeException();
            }
        }
    }
}
