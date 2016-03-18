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
     * PDF文件传输工作线程记录量
     */
    private Integer pdfWorkerCnt = 0;
    private Object pdfWorkerCntLock = new Object();

    /**
     * PDF文件传输工作线程最大限制
     */
    private int PDF_WORKER_LOAD;

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
        this.PDF_WORKER_LOAD =Integer.parseInt(ProgramConfig.PDF.getProperty(ConstantKey.send_worker_thread, "5"));
        this.Y_WORKER_LOAD =Integer.parseInt(ProgramConfig.Y.getProperty(ConstantKey.send_worker_thread,"5"));
    }

    /**
     * 开始轮询PDF文件传输任务队列
     */
    public void runPdfSchedule(){
        log.info("##########开始轮询结单文件传输任务队列##########");
        TaskQueueScheduler pdfScheduler = new TaskQueueScheduler(
                Double.parseDouble(ProgramConfig.PDF.getProperty(ConstantKey.send_loop_delay,"3")),
                ProgramConfig.TASK_TYPE_PDF,
                Integer.parseInt(ProgramConfig.PDF.getProperty(ConstantKey.send_ot,"300")),
                Integer.parseInt(ProgramConfig.PDF.getProperty(ConstantKey.resend_times,"5")),
                Integer.parseInt(ProgramConfig.PDF.getProperty(ConstantKey.resend_wait_time, "10000")));
        pdfScheduler.setDaemon(true);
        pdfScheduler.setName("PdfQueueScheduler");
        pdfScheduler.start();
    }

    /**
     * 开始轮询Y任务队列
     */
    public void runYSchedule(){
        log.info("##########开始轮询Y任务队列##########");
        TaskQueueScheduler yScheduler = new TaskQueueScheduler(
                Double.parseDouble(ProgramConfig.Y.getProperty(ConstantKey.send_loop_delay,"3")),
                ProgramConfig.TASK_TYPE_Y,
                Integer.parseInt(ProgramConfig.Y.getProperty(ConstantKey.send_ot,"300")),
                Integer.parseInt(ProgramConfig.Y.getProperty(ConstantKey.resend_times,"5")),
                Integer.parseInt(ProgramConfig.Y.getProperty(ConstantKey.resend_wait_time,"10000")));
        yScheduler.setDaemon(true);
        yScheduler.setName("YQueueScheduler");
        yScheduler.start();
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
     * 判断PDF文件传输负载情况
     * @return
     */
    public boolean checkPdfLoad(){
        synchronized(transer.pdfWorkerCntLock){
            boolean result;
            if(transer.pdfWorkerCnt+1>transer.PDF_WORKER_LOAD){
                result = false;
            }else{
                result = true;
                transer.pdfWorkerCnt++;
            }
//            log.debug("当前系统有["+(((transer.xWorkerCnt-1)<=0)?0:(transer.xWorkerCnt-1))+"]个X发送任务正在执行.");
            transer.pdfWorkerCntLock.notifyAll();
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
     * 改动当前PDF文件传输工作线程数记录
     */
    public void decreasePdfCnt(){
        synchronized(transer.pdfWorkerCntLock){
            if(transer.pdfWorkerCnt > 0)
                transer.pdfWorkerCnt--;
            transer.pdfWorkerCntLock.notifyAll();
        }
    }

    /**
     * 显示当前PDF文件传输执行情况.
     */
    public int showPdfWorkerCnt(){
        int cntResult = 0;
        synchronized(transer.pdfWorkerCntLock){
            cntResult = (((transer.pdfWorkerCnt-1)<0)?0:(transer.pdfWorkerCnt-1));
            log.debug("当前系统还有["+cntResult+"]个结单文件发送任务正在执行.");
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
