package com.ljq.transer.iml;

import com.ljq.common.ConstantKey;
import com.ljq.common.ProgramConfig;
import com.ljq.queue.FileEntity;
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

public class PdfTaskInfoCreator extends ACreateTaskInfo {
    @Override
    public int getTaskType() {
        return ProgramConfig.TASK_TYPE_PDF;
    }

    public PdfTaskInfoCreator(){
//        String dir = ProgramConfig.X.getProperty(ConstantKey.transer_send_dir, "Z:/aimDir/X/save/") +
//                ProgramConfig.X.getProperty(ConstantKey.transer_task_seq, "90001") + "/";
//        File dirFolder = new File(dir);
//        if(!dirFolder.exists())
//            dirFolder.mkdirs();
    }

    @Override
    public FileTaskInfo createTaskInfo(FileEntity fileEntity) {
        FileTaskInfo info = new FileTaskInfo();
        // 创建当前日期标识
        String dateStr = this.dateFmt.format(new Date());
        // 构造源文件存放目录
        String srcDir = ProgramConfig.PDF.getProperty(ConstantKey.transer_save_dir, "D:/CRM_JD_FILE/") +
                fileEntity.getKhh() + "/" +
                fileEntity.getRq() + "/";
        // 判断是否需要创建发送目录
        synchronized(this.dateFmt){
            File srcDirFolder = new File(srcDir);
            if(!srcDirFolder.exists())
                srcDirFolder.mkdirs();
            this.dateFmt.notifyAll();
        }
        info.setSrcDir(srcDir);
        // 构造目标系统发送目录
        String aimDir = ProgramConfig.PDF.getProperty(ConstantKey.transer_send_dir, "Z:/JD/") + dateStr + "/";
        info.setAimDir(aimDir);
        // 构造源系统存放文件名
        synchronized(this.numFmt){
            if(this.seqNum >= this.MAX_SEQ_NUM)
                this.seqNum = 0;
            this.seqNum++;

            String srcFileName = ProgramConfig.PDF.getProperty(ConstantKey.transer_file_name, "ZTGJ")+
                    fileEntity.getKhh() + "_" + fileEntity.getRq();
            info.setSrcFile(srcFileName);
            info.setAimFile(srcFileName);

            this.numFmt.notifyAll();
        }
        return info;
    }
}
