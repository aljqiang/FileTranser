package com.ljq.core;

import com.ljq.queue.ATaskEntity;

/**
 * 调度策略接口
 * User: Larry
 * Date: 2015-03-27
 * Time: 16:28
 * Version: 1.0
 */

public interface IQueueScheduleStrategy {
    public void addTaskEntity(ATaskEntity entity);

    public QueueScheduleResult canSend();

    public class QueueScheduleResult{

        private boolean status;

        private ATaskEntity taskEntity;

        public QueueScheduleResult(){
            this.status = false;
            this.taskEntity = null;
        }

        public boolean canSend() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public ATaskEntity getTaskEntity() {
            return taskEntity;
        }

        public void setTaskEntity(ATaskEntity taskEntity) {
            this.taskEntity = taskEntity;
        }

    }
}
