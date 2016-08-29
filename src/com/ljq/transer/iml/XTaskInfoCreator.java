package com.ljq.transer.iml;

import com.ljq.common.ConstantKey;
import com.ljq.common.ProgramConfig;
import com.ljq.transer.ACreateTaskInfo;
import com.ljq.transer.FileTaskInfo;

import java.io.File;
import java.util.Date;

/**
 * 创建文件目录和文件
 * User: Larry
 * Date: 2015-03-30
 * Time: 16:35
 * Version: 1.0
 */

public class XTaskInfoCreator extends ACreateTaskInfo {
    @Override
    public int getTaskType() {
        return ProgramConfig.TASK_TYPE_X;
    }

    public XTaskInfoCreator(){
        String dir = ProgramConfig.X.getProperty(ConstantKey.transer_send_dir, "Z:/aimDir/X/save/") +
                ProgramConfig.X.getProperty(ConstantKey.transer_task_seq, "90001") + "/";
        File dirFolder = new File(dir);
        if(!dirFolder.exists())
            dirFolder.mkdirs();
    }

    @Override
    public FileTaskInfo createTaskInfo() {
        FileTaskInfo info = new FileTaskInfo();
        //创建当前日期标识
        String dateStr = this.dateFmt.format(new Date());
        //构造源系统发送路径
        String srcDir = ProgramConfig.X.getProperty(ConstantKey.transer_send_dir, "D:/aimDir/X/save/") +
                ProgramConfig.X.getProperty(ConstantKey.transer_task_seq, "90001") + "/" +
                dateStr + "/";
        //判断是否需要创建发送目录
        synchronized(this.dateFmt){
            File srcDirFolder = new File(srcDir);
            if(!srcDirFolder.exists())
                srcDirFolder.mkdirs();
            this.dateFmt.notifyAll();
        }
        info.setSrcDir(srcDir);
        //构造目标系统接收路径
        String aimDir = ProgramConfig.X.getProperty(ConstantKey.transer_save_dir, "D:/aimDir/X/save/") +
                ProgramConfig.X.getProperty(ConstantKey.transer_task_seq, "90001") + "/" +
                dateStr + "/";
        info.setAimDir(aimDir);
        //构造源系统发送文件名
        synchronized(this.numFmt){
            if(this.seqNum >= this.MAX_SEQ_NUM)
                this.seqNum = 0;
            this.seqNum++;

            String srcFileName = ProgramConfig.X.getProperty(ConstantKey.transer_file_name, "TRANSER_X_")+
                    ProgramConfig.X.getProperty(ConstantKey.transer_task_seq, "90001") + "_" + dateStr;
            info.setSrcFile(srcFileName);
            info.setAimFile(srcFileName);

            this.numFmt.notifyAll();
        }
        return info;
    }
}
