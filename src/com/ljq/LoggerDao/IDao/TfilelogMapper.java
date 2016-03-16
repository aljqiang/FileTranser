package com.ljq.LoggerDao.IDao;

import java.util.Map;

public interface TfilelogMapper {
    /**
     * 记录文件迁移日志
     */
    boolean insertFilelog(Map params);
}