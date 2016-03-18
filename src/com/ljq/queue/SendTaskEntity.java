package com.ljq.queue;

import com.ljq.common.ProgramConfig;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * User: Larry
 * Date: 2015-03-27
 * Time: 16:08
 * Version: 1.0
 */

public class SendTaskEntity extends ATaskEntity{
    private String srcDir;  // 源系统的文件路径
    private String aimDir;  // 目标系统的文件路径
    private String fileName;  // 传送文件名
    private int taskType;  // 任务类型
    private Date planSendTime;  // 计划发送时间
    private Date sendTime;  // 实际发送时间
    private Date endTime;  // 实际结束时间
    private int resendTime;  // 重发次数
    private int result;  // 发送结果
    private String errCode;  // 错误代码
    private String errCause;  // 错误描述
    private String respCode;  // 活动跟中代码

    public SendTaskEntity() {
        this.fileName = "";
        this.errCause = "";
        this.respCode = "";
        this.errCode = "";
    }

    @Override
    public String toString(){
        String msg = "\r\n========================\r\n";
        String taskType=null;

        if(this.taskType== ProgramConfig.TASK_TYPE_X){
            taskType="X";
        }else{
            taskType="Y";
        }

        msg += "传输任务类型:" +taskType + "\r\n";
        msg += "执行结果:" + ((this.result == 0) ? "成功" : "失败") + "\r\n";
        msg += "错误描述:" + this.errCode + "," + this.errCause + "\r\n";
        msg += "源路径:" + this.srcDir + "\r\n";
//        msg += "文件名称:" + this.fileName + "\r\n";
        msg += "========================\r\n";

        return msg;
    }

    public String getSrcDir() {
        return srcDir;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public Date getPlanSendTime() {
        return planSendTime;
    }

    public void setPlanSendTime(Date planSendTime) {
        this.planSendTime = planSendTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getResendTime() {
        return resendTime;
    }

    public void setResendTime(int resendTime) {
        this.resendTime = resendTime;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCause() {
        return errCause;
    }

    public void setErrCause(String errCause) {
        this.errCause = errCause;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getAimDir() {
        return aimDir;
    }

    public void setAimDir(String aimDir) {
        this.aimDir = aimDir;
    }

    @Override
    public int getTaskHandleType() {
        return super.HANDLE_TYPE_NORMAL;
    }
}
