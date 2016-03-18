package com.ljq.core;

import com.ljq.common.ProgramConfig;
import com.ljq.core.strategy.PdfQueueScheduleStrategy;
import com.ljq.core.strategy.YQueueScheduleStrategy;
import com.ljq.gateway.strategy.SimpleTransStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 通讯工具类
 * User: Larry
 * Date: 2015-03-27
 * Time: 16:23
 * Version: 1.0
 */

public class TransHelper {
    private static final Map<Integer, ITransStrategy> transStrategy = new HashMap<Integer,ITransStrategy>();

    private static final Map<Integer,IQueueScheduleStrategy> scheduleStrategy = new HashMap<Integer,IQueueScheduleStrategy>();

    private static CoreTranser transer;
    /**
     * 设置通讯主程序
     * @param transer
     */
    public static void setTranser(CoreTranser transer){
        TransHelper.transer =transer;
    }
    /**
     * 获取主程序
     * @return
     */
    public static CoreTranser transer(){
        return transer;
    }

    static{
        if(transStrategy.size() == 0 || scheduleStrategy.size() == 0)
            TransHelper.init();
    }

    /**
     * 初始化
     */
    public static void init(){
        TransHelper.registerTransStrategy(ProgramConfig.TASK_TYPE_PDF, new SimpleTransStrategy(ProgramConfig.TASK_TYPE_PDF));
        TransHelper.registerTransStrategy(ProgramConfig.TASK_TYPE_Y, new SimpleTransStrategy(ProgramConfig.TASK_TYPE_Y));

        TransHelper.registerScheduleStrategy(ProgramConfig.TASK_TYPE_PDF, new PdfQueueScheduleStrategy());
        TransHelper.registerScheduleStrategy(ProgramConfig.TASK_TYPE_Y, new YQueueScheduleStrategy());
    }

    /**
     * 添加通讯策略
     * @param taskType
     * @param strategy
     */
    public static void registerTransStrategy(Integer taskType,ITransStrategy strategy){
        transStrategy.put(taskType, strategy);
    }

    /**
     * 获取通讯策略
     * @param taskType
     * @return
     */
    public static ITransStrategy getTransStrategy(int taskType){
        return TransHelper.transStrategy.get(taskType);
    }

    /**
     * 添加任务队列调度策略
     * @param taskType
     * @param strategy
     */
    public static void registerScheduleStrategy(Integer taskType,IQueueScheduleStrategy strategy){
        scheduleStrategy.put(taskType,strategy);
    }

    /**
     * 获取任务队列调度策略
     * @param taskType
     * @return
     */
    public static IQueueScheduleStrategy getScheduleStrategy(int taskType){
        return TransHelper.scheduleStrategy.get(taskType);
    }
}
