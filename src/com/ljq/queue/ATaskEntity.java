package com.ljq.queue;

/**
 * 任务实体处理类
 * User: Larry
 * Date: 2015-03-27
 * Time: 16:07
 * Version: 1.0
 */

public abstract class ATaskEntity {
    public static final int HANDLE_TYPE_QUERY = 1;

    public static final int HANDLE_TYPE_NORMAL = 2;
    /**
     * 获取任务实体处理类型
     * @return
     */
    public abstract int getTaskHandleType();
}
