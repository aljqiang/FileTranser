package com.ljq.queue;

/**
 * 任务队列管理容器
 * User: Larry
 * Date: 2015-03-27
 * Time: 16:32
 * Version: 1.0
 */

public class TaskEntityQueue {
    private static TaskEntityQueue taskQueue;

    private final BlockQueue<SendTaskEntity> pdftaskQueue = new BlockQueue<SendTaskEntity>();
    private final BlockQueue<SendTaskEntity> ytaskQueue = new BlockQueue<SendTaskEntity>();

    private TaskEntityQueue(){

    }
    /**
     * 获取任务队列管理容器
     * @return
     */
    public static synchronized TaskEntityQueue queue(){
        if(null == taskQueue)
            taskQueue = new TaskEntityQueue();
        return taskQueue;
    }

    /**
     * 获取结单任务队列任务队列
     * @return
     */
    public BlockQueue<SendTaskEntity> pdfTaskQueue(){
        return this.pdftaskQueue;
    }

    /**
     * 获取Y任务队列
     * @return
     */
    public BlockQueue<SendTaskEntity> yTaskQueue(){
        return this.ytaskQueue;
    }
}
