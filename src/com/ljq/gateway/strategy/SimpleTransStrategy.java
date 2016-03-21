package com.ljq.gateway.strategy;

import com.ljq.common.ConstantKey;
import com.ljq.common.ProgramConfig;
import com.ljq.core.ITransStrategy;
import com.ljq.core.TransHelper;
import com.ljq.gateway.IConnectorResponse;
import com.ljq.gateway.SendFileResult;
import com.ljq.queue.SendTaskEntity;
import com.ljq.transer.FileHelper;
import com.ljq.transer.FileTaskInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * 传输策略
 * User: Larry
 * Date: 2015-03-30
 * Time: 16:02
 * Version: 1.0
 */

public class SimpleTransStrategy implements ITransStrategy {
    private static Log log = LogFactory.getLog(SimpleTransStrategy.class);

    private int taskType;
    private String recvUri;

    public SimpleTransStrategy(int taskType){
        this.taskType = taskType;
        this.recvUri = ProgramConfig.GATEWAY.getProperty(ConstantKey.gateway_recvuri, "");
    }

    @Override
    public synchronized SendFileResult sendDatafile(SendTaskEntity entity) throws IOException { // 如果发送文件是同一个文件，加同步锁防止资源抢占，不加同步锁则多线程同步作业，会出现资源抢占问题
        // 初始化发送结果
        SendFileResult sendResult = new SendFileResult();

        // 发送文件到文件服务器
        log.info("开始调用API发送,任务类型为:" + entity.getTaskType() + ",文件目录为:" + entity.getSrcDir() + "");
        FileTaskInfo fileTaskInfo = FileHelper.createTaskInfo(entity.getTaskType(), entity.getSrcDir(),sendResult);

        entity.setFileName(fileTaskInfo.getSrcFile());
        entity.setAimDir(ProgramConfig.PDF.getProperty(ConstantKey.transer_save_dir, "D:/CRM_JD_FILE/"));

        // 返回结果
        return sendResult;
    }

    @Override
    public void release() {
        if(taskType == ProgramConfig.TASK_TYPE_PDF){
            TransHelper.transer().decreasePdfCnt();
            TransHelper.transer().showPdfWorkerCnt();
        }else if(taskType == ProgramConfig.TASK_TYPE_Y){
            TransHelper.transer().decreaseYCnt();
            TransHelper.transer().showYWorkerCnt();
        }
    }

    @Override
    public void complete(SendTaskEntity entity) throws Exception {
        String taskType=null;

        if(this.taskType==ProgramConfig.TASK_TYPE_PDF){
            taskType="结单文件传输";
        }else if(this.taskType==ProgramConfig.TASK_TYPE_Y){
            taskType="Y";
        }

        String msg = "\r\n==========发送完成信息==============\r\n";
        msg += "传输任务类型:"+taskType+"\r\n";
        msg += "执行结果:"+((entity.getResult()== IConnectorResponse.RESULT_SUCCESS)?"成功":"失败")+"\r\n";
        msg += "目标文件发送路径:"+entity.getSrcDir()+"\r\n";
        msg += "源文件存放路径:" + entity.getAimDir()+ "\r\n";
        msg += "====================================\r\n";
        log.info(msg);
    }
}
