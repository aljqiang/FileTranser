package com.ljq.gateway;

import com.ljq.core.ITransStrategy;
import com.ljq.core.TransHelper;
import com.ljq.queue.SendTaskEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * 发送任务工作线程
 * User: Larry
 * Date: 2015-03-30
 * Time: 15:49
 * Version: 1.0
 */

public class SendTaskWorker extends Thread {
    public static Log log = LogFactory.getLog(SendTaskWorker.class);
    // 发送任务信息
    private SendTaskEntity entity;
    // 传输策略
    private ITransStrategy transStrategy;
    // 超时监控线程
    private OverTimeChecker ot_checker;
    // 重发监控
    private Resender resender;

    public SendTaskWorker(){

    }

    /**
     * 创建发送柜面网关任务
     * @param entity
     */
    public SendTaskWorker(SendTaskEntity entity) {
        this.entity = entity;
        // 初始化传输策略
        this.transStrategy = TransHelper.getTransStrategy(entity.getTaskType());
        // 初始化超时监控
        this.ot_checker = new OverTimeChecker(0,entity,this);
        // 初始化重发监控
        this.resender = new Resender(0,0);
    }

    /**
     * 超时设置
     * @param overtime
     */
    public void setOverTime(int overtime){
        this.ot_checker.update(overtime);
    }
    /**
     * 重连设置
     * @param resendTime
     */
    public void setResender(int resendTime,int waitTime){
        this.resender.update(resendTime, waitTime);
    }


    /**
     * 发送逻辑
     */
    @Override
    public void run(){
        try{
            // 设置实际发送时间
            entity.setSendTime(new Date());
            // 发送数据文件
            SendFileResult sendResult = transStrategy.sendDatafile(entity);
            // 判断是否数据文件发送成功
            if(!sendResult.isSuccess()){
                // 记录发送错误结果到日志
                log.info("传送数据文件失败[传送方式为:"+sendResult.getType()+"]."+"错误代码:"+sendResult.getCode()+" 错误描述:"+sendResult.getMsg());
            }else{
                // 记录发送结果成功信息到日志
                log.info("[传送方式为:" + sendResult.getType() + "]传送任务:" + entity.getTaskType() + ",文件目录为:" + entity.getSrcDir() + "完成.]");
                entity.setResult(1);
                transStrategy.complete(entity);
            }

        }catch(Exception ex){
            // 中断超时监控线程
            this.ot_checker.interrupt();
            // 日志记录
            log.debug(ex.getMessage(),ex);
        }finally{
            // 最后释放资源
            this.release();
        }

    }

    /**
     * 释放结束发送任务
     *
     */
    public void release(){
        this.transStrategy.release();
    }

    /**
     * 超时检测
     * @author 翻书侠
     *
     */
    public static class OverTimeChecker implements Runnable{

        private int overtime;
        private long beginTime;
        private SendTaskEntity entity;
        private SendTaskWorker worker;
        private Thread thread;
        /**
         * 计时器构造函数
         * @param overtime 超时限制
         * @param entity 任务实例
         */
        public OverTimeChecker(int overtime,SendTaskEntity entity,SendTaskWorker worker){
            this.overtime = overtime;
            this.entity = entity;
            this.worker = worker;
        }
        /**
         * 记录开始时间
         */
        public void prepareBeginTime(){
            this.beginTime = System.currentTimeMillis();
        }

        /**
         * 设置新的超时上限
         * @param overtime
         */
        public void update(int overtime){
            this.overtime = overtime;
        }

        /**
         * 开启监控
         */
        public void start(){
            if(this.overtime == 0)
                return;
            //建立线程
            this.thread = new Thread(this);
            //设置标识
            this.thread.setName(worker.getName()+"-otChecker");
            //启动线程
            this.thread.start();
        }

        /**
         * 停止监控
         */
        public void interrupt(){
            if(this.overtime == 0)
                return;
            if(this.thread != null)
                try{
                    this.thread.interrupt();
                }catch(Exception e){
                    log.error("中断"+this.thread.getName()+"失败.");
                }
        }

        /**
         * 监控逻辑
         */
        @Override
        public void run() {
            while(true)
            {
                //检测当前时间与开始时间差是否大于超时限制
                if((System.currentTimeMillis() - this.beginTime)/1000 >= this.overtime){
//                    log.error("与柜面网关通信超过"+this.overtime+"秒时间限制,中断本次通讯.",new SendTaskException(entity,"传输超时"));
//                    this.worker.exceptionProcess(ProgramConfig.SEND_ERR_OVERTIME,"");
                    this.worker.interrupt();
                    break;
                }

                //休眠
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    log.info("中断超时检测线程.");
                    break;
                }
            }
        }
    }

    /**
     * 重发控制器
     * @author 翻书侠
     *
     */
    public static class Resender{
        private int resend_cnt;
        private int waitTime;
        private int resend_time;

        public Resender(int resend_time,int waitTime){
            this.resend_cnt = 0;
            this.resend_time = resend_time;
            this.waitTime = waitTime;
        }

        public void update(int resend_time,int waitTime){
            this.resend_time = resend_time;
            this.waitTime = waitTime;
        }

        /**
         * 判断是否重发
         * @return
         */
        public boolean isOvertimes(){
            boolean result = (resend_cnt>=resend_time);
            if(!result){
                this.resend_cnt++;
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    log.info("重发睡眠等待被中断.");
                }
            }
            return result;
        }
    }
}
