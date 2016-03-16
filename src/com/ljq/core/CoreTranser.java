package com.ljq.core;

import com.ljq.common.ConstantKey;
import com.ljq.common.ProgramConfig;
import com.ljq.queue.TaskEntityQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 主程序
 * 该类是发送任务线程池以及等待队列的容器，是一个单例类。包含了很多容器的引用
 * User: Larry
 * Date: 2015-03-30
 * Time: 15:24
 * Version: 1.0
 */

public class CoreTranser {
    private static Log log = LogFactory.getLog(CoreTranser.class);

    private static CoreTranser transer;

    /**
     * X工作线程记录量
     */
    private Integer xWorkerCnt = 0;
    private Object xWorkerCntLock = new Object();

    /**
     * X工作线程最大限制
     */
    private int X_WORKER_LOAD;

    /**
     * Y工作线程记录量
     */
    private Integer yWorkerCnt = 0;
    private Object yWorkerCntLock = new Object();

    /**
     * Y工作线程最大限制
     */
    private int Y_WORKER_LOAD;

    /**
     * 发送任务队列
     */
    private TaskEntityQueue taskQueue;

    /**
     * 单例构造函数.
     */
    private CoreTranser(){
        // 初始化发送任务队列
        this.taskQueue = TaskEntityQueue.queue();
        // 初始化各任务类型处理任务线程最大限度
        this.X_WORKER_LOAD =Integer.parseInt(ProgramConfig.X.getProperty(ConstantKey.send_worker_thread,"5"));
        this.Y_WORKER_LOAD =Integer.parseInt(ProgramConfig.Y.getProperty(ConstantKey.send_worker_thread,"5"));
    }

    /**
     * 开始轮询X任务队列
     */
    public void runXSchedule(){
        log.info("##########开始轮询X任务队列##########");
        TaskQueueScheduler xScheduler = new TaskQueueScheduler(
                Double.parseDouble(ProgramConfig.X.getProperty(ConstantKey.send_loop_delay,"3")),
                ProgramConfig.TASK_TYPE_X,
                Integer.parseInt(ProgramConfig.X.getProperty(ConstantKey.send_ot,"300")),
                Integer.parseInt(ProgramConfig.X.getProperty(ConstantKey.resend_times,"5")),
                Integer.parseInt(ProgramConfig.X.getProperty(ConstantKey.resend_wait_time,"10000")));
        xScheduler.setDaemon(true);
        xScheduler.setName("XQueueScheduler");
        xScheduler.start();
    }

    /**
     * 开始轮询Y任务队列
     */
    public void runYSchedule(){
        log.info("##########开始轮询Y任务队列##########");
        TaskQueueScheduler xScheduler = new TaskQueueScheduler(
                Double.parseDouble(ProgramConfig.Y.getProperty(ConstantKey.send_loop_delay,"3")),
                ProgramConfig.TASK_TYPE_Y,
                Integer.parseInt(ProgramConfig.Y.getProperty(ConstantKey.send_ot,"300")),
                Integer.parseInt(ProgramConfig.Y.getProperty(ConstantKey.resend_times,"5")),
                Integer.parseInt(ProgramConfig.Y.getProperty(ConstantKey.resend_wait_time,"10000")));
        xScheduler.setDaemon(true);
        xScheduler.setName("YQueueScheduler");
        xScheduler.start();
    }

    /**
     * 创建发送任务调度器
     * @return
     */
    public static synchronized CoreTranser create(){
        if(null == transer)
            transer = new CoreTranser();
        return transer;
    }

    /**
     * 获取任务队列
     * @return
     */
    public TaskEntityQueue taskQueue(){
        return transer.taskQueue;
    }

    /**
     * 判断X负载情况
     * @return
     */
    public boolean checkXLoad(){
        synchronized(transer.xWorkerCntLock){
            boolean result;
            if(transer.xWorkerCnt+1>transer.X_WORKER_LOAD){
                result = false;
            }else{
                result = true;
                transer.xWorkerCnt++;
            }
//            log.debug("当前系统有["+(((transer.xWorkerCnt-1)<=0)?0:(transer.xWorkerCnt-1))+"]个X发送任务正在执行.");
            transer.xWorkerCntLock.notifyAll();
            return result;
        }
    }

    /**
     * 判断Y负载情况
     * @return
     */
    public boolean checkYLoad(){
        synchronized(transer.yWorkerCntLock){
            boolean result;
            if(transer.yWorkerCnt+1>transer.Y_WORKER_LOAD){
                result = false;
            }else{
                result = true;
                transer.yWorkerCnt++;
            }
//            log.debug("当前系统有["+(((transer.yWorkerCnt-1)<=0)?0:(transer.yWorkerCnt-1))+"]个Y发送任务正在执行.");
            transer.yWorkerCntLock.notifyAll();
            return result;
        }
    }

    /**
     * 改动当前X工作线程数记录
     */
    public void decreaseXCnt(){
        synchronized(transer.xWorkerCntLock){
            if(transer.xWorkerCnt > 0)
                transer.xWorkerCnt--;
            transer.xWorkerCntLock.notifyAll();
        }
    }

    /**
     * 显示当前X执行情况.
     */
    public int showXWorkerCnt(){
        int cntResult = 0;
        synchronized(transer.xWorkerCntLock){
            cntResult = (((transer.xWorkerCnt-1)<0)?0:(transer.xWorkerCnt-1));
            log.debug("当前系统还有["+cntResult+"]个X发送任务正在执行.");
        }
        return cntResult;
    }

    /**
     * 改动当前Y工作线程数记录
     */
    public void decreaseYCnt(){
        synchronized(transer.yWorkerCntLock){
            if(transer.yWorkerCnt > 0)
                transer.yWorkerCnt--;
            transer.yWorkerCntLock.notifyAll();
        }
    }

    /**
     * 显示当前Y执行情况.
     */
    public int showYWorkerCnt(){
        int cntResult = 0;
        synchronized(transer.yWorkerCntLock){
            cntResult = (((transer.yWorkerCnt-1)<0)?0:(transer.yWorkerCnt-1));
            log.debug("当前系统还有["+cntResult+"]个Y发送任务正在执行.");
        }
        return cntResult;
    }
}
