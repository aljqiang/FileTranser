package com.ljq.transer;

import com.ljq.queue.FileEntity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 创建文件目录信息
 * User: Larry
 * Date: 2015-03-30
 * Time: 16:11
 * Version: 1.0
 */

public abstract class ACreateTaskInfo {
    /**
     * 流水号最大值(6位)
     */
    public final int MAX_SEQ_NUM = 999999;
    /**
     * 任务类型流水号
     */
    protected Integer seqNum = 0;
    /**
     * 时间日期格式
     */
    public final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
    /**
     * 流水号格式
     */
    public final DecimalFormat numFmt = new DecimalFormat("000000");
    /**
     * 获取创建任务的类型
     * @return
     */
    public abstract int getTaskType();
    /**
     * 生成任务信息
     */
    public abstract FileTaskInfo createTaskInfo(FileEntity fileEntity);
}
