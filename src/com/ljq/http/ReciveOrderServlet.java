package com.ljq.http;

import com.ljq.common.ProgramConfig;
import com.ljq.core.TransHelper;
import com.ljq.queue.SendTaskEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接收任务接口
 * User: Larry
 * Date: 2015-03-31
 * Time: 15:22
 * Version: 1.0
 */

public class ReciveOrderServlet extends HttpServlet {
    private static Log log= LogFactory.getLog(ReciveOrderServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    public void doGet(HttpServletRequest req,HttpServletResponse rep) throws ServletException {
        this.doPost(req,rep);
    }

    @Override
    public void doPost(HttpServletRequest req,HttpServletResponse rep) throws ServletException{
        int orderType=Integer.parseInt(req.getParameter("orderType"));
        String dataFilePath=req.getParameter("dataFilePath");

        if(!checkOrderType(orderType)){
            System.out.println();
        }

        // 封装任务发送实体类
        SendTaskEntity entity=new SendTaskEntity();
        entity.setTaskType(orderType);
        entity.setSrcDir(dataFilePath);

        // 加入任务队列
        TransHelper.getScheduleStrategy(orderType).addTaskEntity(entity);
        // 写入日志
        log.info("添加任务(taskType:"+orderType+" srcDir:"+dataFilePath+")完成.");
    }

    /**
     * 检测是否合法的任务类型
     * @param orderTypeId
     * @return
     */
    public static boolean checkOrderType(int orderTypeId){
        for(Integer type: ProgramConfig.TASK_TYPE){
            if(orderTypeId==type){
                return true;
            }
        }
        return false;
    }
}
