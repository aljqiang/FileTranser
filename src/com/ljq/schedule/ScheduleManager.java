package com.ljq.schedule;

import com.ljq.schedule.impl.QuartzScheduleEngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Larry
 * Date: 2015-04-01
 * Time: 10:58
 * Version: 1.0
 */

public class ScheduleManager {
    private static Log log = LogFactory.getLog(ScheduleManager.class);
    /**
     * 调度器引擎
     */
    private static IScheduleEngine engine = new QuartzScheduleEngine();
    /**
     * 调度任务处理器
     */
    private static final Map<Integer,ITaskHandler> taskHandlers = new HashMap<Integer,ITaskHandler>();
    /**
     * 等待任务队列
     */
    private static final List<TranserScheduler> taskQueue = new ArrayList<TranserScheduler>();
    /**
     * 正在执行任务池
     */
    private static final Map<Long,TranserScheduler> runningTaskPool = new HashMap<Long,TranserScheduler>();
    /**
     * 任务调度监控线程
     */
    private static final TranserScheduleMonitor monitor = new TranserScheduleMonitor();
    /**
     * 初始化调度管理器
     */
    static{
        monitor.start();
    }

    /**
     * 返回调度引擎
     * @return
     */
    public static IScheduleEngine engine(){
        return engine;
    }

    /**
     * 添加等待执行任务
     * @param ScheduleInfo
     */
    public static void addScheduler(ScheduleInfo info){
        synchronized(taskQueue){
            ScheduleManager.taskQueue.add(new TranserScheduler(monitor,info));
        }
    }

    /**
     * 获取最早等待执行任务
     * @return
     */
    protected static TranserScheduler popScheduler(){
        TranserScheduler scheduler = null;
        synchronized(taskQueue){
            if(taskQueue.size()!=0)
                scheduler = taskQueue.remove(0);
        }
        return scheduler;
    }

    /**
     * 执行任务池是否存在相同任务
     * @param info
     * @return
     */
    public static boolean isRunningPoolExist(ScheduleInfo info){
        boolean result = false;
        synchronized(runningTaskPool){
            result = runningTaskPool.containsKey(info.getTask_id());
        }
        return result;
    }

    /**
     * 添加到任务执行池
     * @param scheduler
     */
    protected static void addRunningPool(TranserScheduler scheduler){
        synchronized(runningTaskPool){
            runningTaskPool.put(scheduler.getInfo().getTask_id(),scheduler);
            scheduler.start();
        }
    }

    /**
     * 移除任务执行池任务
     * @param info
     */
    protected static void removeRunningTask(ScheduleInfo info){
        synchronized(runningTaskPool){
            if(runningTaskPool.containsKey(info.getTask_id()))
                runningTaskPool.remove(info.getTask_id());
        }
    }

    /**
     * 中断执行池任务
     * @param info
     */
    public static boolean interruptRunningTask(ScheduleInfo info){
        boolean result = false;
        synchronized(runningTaskPool){
            if(runningTaskPool.containsKey(info.getTask_id())){
                runningTaskPool.get(info.getTask_id()).interrupt();
                result = true;
            }
        }
        return result;
    }

    /**
     * 添加任务处理器
     * @param handler
     */
    public static void registerTaskHandler(ITaskHandler handler){
        taskHandlers.put(handler.getTaskType(), handler);
    }

    /**
     * 根据任务类型获取任务处理器
     * @param task_type
     */
    protected static ITaskHandler getTaskHandler(int task_type){
        return taskHandlers.get(task_type);
    }
}
