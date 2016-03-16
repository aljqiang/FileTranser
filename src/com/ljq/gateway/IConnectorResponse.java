package com.ljq.gateway;

/**
 * User: Larry
 * Date: 2015-03-30
 * Time: 16:26
 * Version: 1.0
 */

public interface IConnectorResponse {
    public static final int RESULT_SUCCESS = 1;

    public static final int RESULT_FAIL = 0;

    public static final int RESULT_NONE = -1;

    /**
     * 获取对应类型
     * @return
     */
    public int getType();
    /**
     * 获取回复结果
     * @return
     */
    public int getResult();
    /**
     * 获取错误码
     * @return
     */
    public String getErrorCode();
}
