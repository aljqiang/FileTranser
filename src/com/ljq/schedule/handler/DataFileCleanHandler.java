package com.ljq.schedule.handler;


import com.ljq.common.ConstantKey;
import com.ljq.common.ProgramConfig;
import com.ljq.schedule.ITaskHandler;
import com.ljq.schedule.ScheduleInfo;
import com.ljq.util.FileWalker;
import com.ljq.util.FileWalker.ICatchHandler;
import com.ljq.util.FileWalker.IScanChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Larry
 * Date: 2015-04-01
 * Time: 11:18
 * Version: 1.0
 */

public class DataFileCleanHandler implements ITaskHandler {

	private static Log log = LogFactory.getLog(DataFileCleanHandler.class);

	private List<String> dataFileDirs;

	public DataFileCleanHandler(){

		String x_dir = ProgramConfig.X.getProperty(ConstantKey.transer_send_dir, "D:/aimDir/X/save/") +
                ProgramConfig.X.getProperty(ConstantKey.transer_task_seq, "90001") + "/";
		String y_dir = ProgramConfig.Y.getProperty(ConstantKey.transer_send_dir, "D:/aimDir/Y/save/") +
                ProgramConfig.Y.getProperty(ConstantKey.transer_task_seq, "90002") + "/";
		
		this.dataFileDirs = new ArrayList<String>();
		this.dataFileDirs.add(x_dir);
		this.dataFileDirs.add(y_dir);
	}

	@Override
	public Integer getTaskType() {
		return 0;
	}

	@Override
	public void handle(ScheduleInfo info) {
		
		for(String dataFileDir:dataFileDirs)
		{
			FileWalker dataFilesWalker = new FileWalker(dataFileDir,
																			  new DatafilesScaner(dataFileDir),
																			  new DatafilesCatchHandler());
			dataFilesWalker.walkAndScan();
		}
	}
	
	/**
	 * UFS文件特征搜索器
	 * @author 翻书侠
	 *
	 */
	private static class DatafilesScaner implements IScanChecker{
		
		private String baseDir;
		private int currDate;
		private DateFormat fmt;
		
		public DatafilesScaner(String baseDir){
			try{
				this.baseDir = baseDir;
				this.fmt = new SimpleDateFormat("yyyyMMdd");
				this.currDate = Integer.parseInt(fmt.format(new Date()));
			}catch(Exception ex){
				log.error("过期数据文件初始化当前日期出错.",ex);
			}
		}
		
		@Override
		public boolean checkFile(File aimFile) {
			//不识别文件名
			return false;
		}

		@Override
		public boolean checkFolder(File aimFolder) {
			try{
				int dateMark = Integer.parseInt(aimFolder.getName());
				if(currDate>dateMark)
					return true;
			}catch(Exception ex){
				log.error("判断文件:"+aimFolder.getName()+"是否冗余文件出错.",ex);
			}
			return false;
		}
		
	}
	
	/**
	 * UFS数据文件处理器
	 * @author 翻书侠
	 *
	 */
	private static class DatafilesCatchHandler implements ICatchHandler{
		@Override
		public void fileOperation(File aimFile) {
			aimFile.delete();			
		}

		@Override
		public void folderOperation(File folder) {
			folder.delete();
		}
		
	}
}
