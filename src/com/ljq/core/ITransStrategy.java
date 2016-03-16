package com.ljq.core;

import com.ljq.gateway.SendFileResult;
import com.ljq.queue.SendTaskEntity;

/**
 * 通讯策略接口
 * User: Larry
 * Date: 2015-03-27
 * Time: 16:24
 * Version: 1.0
 */

public interface ITransStrategy {
    /**
     * 发送数据文件
     * @param entity
     * @return
     */
    public SendFileResult sendDatafile(SendTaskEntity entity);

    /**
     * 传输结束后的释放资源
     */
    public void release();

    /**
     * 完成任务处理逻辑
     * @throws Exception
     */
    public void complete(SendTaskEntity entity) throws Exception;
}
