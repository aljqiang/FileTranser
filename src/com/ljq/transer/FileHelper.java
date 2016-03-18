package com.ljq.transer;

import com.ljq.LoggerDao.model.Tfilelog;
import com.ljq.common.ConstantKey;
import com.ljq.common.ProgramConfig;
import com.ljq.gateway.SendFileResult;
import com.ljq.queue.FileEntity;
import com.ljq.transer.iml.XTaskInfoCreator;
import com.ljq.transer.iml.YTaskInfoCreator;
import com.ljq.util.FileTypeHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件传输工具类
 * User: Larry
 * Date: 2015-03-30
 * Time: 16:10
 * Version: 1.0
 */

public class FileHelper {
    private static Log log = LogFactory.getLog(FileHelper.class);

    /**
     * 发送文件参数构造器
     */
    private static final Map<Integer, ACreateTaskInfo> taskInfoCreator = new HashMap<Integer, ACreateTaskInfo>();

    public final static DecimalFormat numFmt = new DecimalFormat("000000");  // 流水号格式

    public static final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");

    /**
     * 初始化固定的文件参数创建器
     */
    public static void init() {
        FileHelper.registerTaskInfoCreator(ProgramConfig.TASK_TYPE_X, new XTaskInfoCreator());
        FileHelper.registerTaskInfoCreator(ProgramConfig.TASK_TYPE_Y, new YTaskInfoCreator());
    }

    /**
     * 注册文件参数创建处理器
     *
     * @param taskType
     * @param creator
     */
    public static void registerTaskInfoCreator(int taskType, ACreateTaskInfo creator) {
        FileHelper.taskInfoCreator.put(taskType, creator);
    }

    /**
     * 创建需要的文件路径和文件目录
     *
     * @param dataFilePath
     * @return
     */
    public static FileTaskInfo createTaskInfo(int taskType, String dataFilePath,SendFileResult sendResult) throws IOException {

        // 获取文件参数
        FileTaskInfo taskInfo = null;

        FileEntity fileEntity=new FileEntity();

        // 初始化数据库连接
        String resource = "/com/ljq/LoggerDao/mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder()
                .build(reader);
        SqlSession session = sessionFactory.openSession();

        if (taskType == ProgramConfig.TASK_TYPE_X) {

            String[] str = FileHelper.fileReader(dataFilePath);

            String[] fileName = new String[str.length];

            for (int i = 0; i < str.length; i++) {

                File dataFile = new File(str[i]);

                // 判断文件是否为指定类型文件
                if(dataFile.exists() && "pdf".equals(FileTypeHelper.getFileByFile(dataFile)) ){

                    String fName = dataFile.getName();

                    int dot = fName.lastIndexOf('.');

                    if ((dot >-1) && (dot < (fName.length()))) {
                        String subFileName = fName.substring(0, dot);
                        String[] fileNameSplit = subFileName.split("-");

                        for (int j = 0; j < fileNameSplit.length; j++) {
                            if (j == 0) {
                                fileEntity.setKhh(fileNameSplit[j]);
                            } else if (j == 1) {
                                fileEntity.setRq(fileNameSplit[j]);
                            } else {
                                fileEntity.setKhzh(fileNameSplit[j]);
                            }
                        }
                    }

                    taskInfo = FileHelper.taskInfoCreator.get(taskType).createTaskInfo(fileEntity);

                    int seqNum;  // 任务类型流水号

                    int fileCount = new File(taskInfo.getSrcDir()).listFiles().length;

                    if (fileCount > 0) {
                        seqNum = FileHelper.fileReader(taskInfo.getSrcDir()).length + 1;
                    } else {
                        seqNum = 1;
                    }

                    File destFile = new File(taskInfo.getSrcDir() + taskInfo.getSrcFile() + "_" + numFmt.format(seqNum) + ".pdf");
                    fileName[i] = taskInfo.getSrcFile() + "_" + numFmt.format(seqNum) + ".pdf";

                    log.info("开始将数据文件:[" + dataFile.getAbsolutePath() + "]转移到目录[" + destFile.getAbsolutePath() + "]");
                    //dataFile.renameTo(destFile);
                    boolean result = FileHelper.copy(dataFile, destFile);
                    if (!result) {
                        sendResult.setType("Transer传输");
                        sendResult.setMsg("转移数据文件:[" + dataFile.getAbsolutePath() + "]失败");
                        log.debug("转移数据文件:[" + dataFile.getAbsolutePath() + "]失败");
                        log.debug(sendResult.getMsg());
                    }else{
                        // 数据库日记记录
                        try{
                            Tfilelog tfilelog=new Tfilelog();

                            tfilelog.setKhh(fileEntity.getKhh());
                            tfilelog.setRq(fileEntity.getRq());
                            tfilelog.setWjlj(taskInfo.getSrcDir() + taskInfo.getSrcFile() + "_" + numFmt.format(seqNum) + ".pdf");
                            tfilelog.setBz("");

                            session.insert("com.ljq.LoggerDao.mapping.TfilelogMapper.insertFilelog", tfilelog);
                            session.commit();
                            log.info("写入数据库日志信息成功!");
                        }catch (Exception e){
                            log.error("写入数据库日志信息出错，错误为:"+e);
                        }

                        sendResult.setSuccess(true);
                        sendResult.setType("Transer传输");
                    }

                    seqNum++;
                }
            }

            taskInfo.setSrcFiles(fileName);
            taskInfo.setAimFiles(fileName);

            session.close();
        } else if (taskType == ProgramConfig.TASK_TYPE_Y) {
            File dataFile = new File(dataFilePath);
            File destFile = new File(taskInfo.getSrcDir() + taskInfo.getSrcFile());
            log.info("开始将数据文件:[" + dataFile.getAbsolutePath() + "]转移到目录[" + destFile.getAbsolutePath() + "]");

            boolean result = FileHelper.copy(dataFile, destFile);
            try {
                String dateStr = dateFmt.format(new Date());
                String srcDir = ProgramConfig.Y.getProperty("transer_send_dir", "Z:/aimDir/Y/save/")
                        +ProgramConfig.Y.getProperty(ConstantKey.transer_task_seq, "90001") + "/" + dateStr + "/";

                File markerFile = new File(srcDir, "FLG.END");
                if (!markerFile.exists())
                    markerFile.createNewFile();
            } catch (IOException ex) {
                log.debug("============================生成.END文件失败" + ex.getMessage());
            }

            if (!result) {
                sendResult.setType("Transer传输");
                sendResult.setMsg("转移数据文件:[" + dataFile.getAbsolutePath() + "]失败");
                log.debug("转移数据文件:[" + dataFile.getAbsolutePath() + "失败]");
            }else{
                sendResult.setSuccess(true);
                sendResult.setType("Transer传输");
            }
        } else {
            // 复制移动文件到发送目录
            File dataFile = new File(dataFilePath);
            File destFile = new File(taskInfo.getSrcDir() + taskInfo.getSrcFile());
            log.info("开始将数据文件:[" + dataFile.getAbsolutePath() + "]转移到目录[" + destFile.getAbsolutePath() + "]");
            //dataFile.renameTo(destFile);
            boolean result = FileHelper.copy(dataFile, destFile);
            if (!result) {
                sendResult.setType("Transer传输");
                sendResult.setMsg("转移数据文件:[" + dataFile.getAbsolutePath() + "]失败");
                log.debug("转移数据文件:[" + dataFile.getAbsolutePath() + "]失败");
            }else{
                sendResult.setSuccess(true);
                sendResult.setType("Transer传输");
            }
        }

        return taskInfo;
    }

    /**
     * 遍历文件目录
     */
    private static String[] fileReader(String pathInput) {
        File file = new File(pathInput);
        File[] files = file.listFiles();

        String[] filepathList = null;

        try {
            int k = 0;
            String filePath = "";

            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
//                    log.info("=============================文件路径：\n" + files[i].getPath().replace("\\","/"));
                    if (k != 0) {
                        filePath += ",";
                    }

                    filePath += files[i].getPath().replace("\\", "/");
                    k++;

                } else if (files[i].isDirectory()) {
                    log.error("找不到文件.");
                }
            }

            filepathList = filePath.split(",");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return filepathList;
    }

    /**
     * 文件复制
     *
     * @param src
     * @param dist
     * @return
     */
    public static boolean copy(File src, File dist) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dist);
            byte[] buf = new byte['?'];
            int length = in.read(buf);
            while (length > 0) {
                out.write(buf, 0, length);

                length = in.read(buf);
            }
            out.close();
            in.close();

            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
}
