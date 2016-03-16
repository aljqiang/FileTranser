package com.ljq.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * User: Larry
 * Date: 2015-03-27
 * Time: 15:47
 * Version: 1.0
 */

public class HttpConnection {
    private static Log log = LogFactory.getLog(HttpConnection.class);

    public final static int RUNNING_MODE_MULTI = 1;

    public final static int RUNNING_MODE_SINGLE = 0;
    /**
     * 运行模式
     */
    private static int RUNNING_MODE;
    /**
     * 单核模式的连接超时上限(50秒)
     */
    private static int connectOt = 50000;
    /**
     * 单核模式的等待回复上限(50秒)
     */
    private static int waitOt = 50000;
    /**
     * 重发上限(30次)
     */
    private static int resendTimes = 30;
    /**
     * 重发等待时间(20秒)
     */
    private static int resendWait = 20000;
    /**
     * 多核模式的超时限制
     */
    private final static ThreadLocal<OvertimeSetting> localOtSetting = new ThreadLocal<OvertimeSetting>();
    /**
     * 超时限制MODEL
     * @author 翻书侠
     *
     */
    private static class OvertimeSetting{
        private int connectOt;
        private int waitOt;
        public int getConnectOt() {
            return connectOt;
        }
        public void setConnectOt(int connectOt) {
            this.connectOt = connectOt;
        }
        public int getWaitOt() {
            return waitOt;
        }
        public void setWaitOt(int waitOt) {
            this.waitOt = waitOt;
        }
    }

    /**
     * 初始化
     */
    static{
        HttpConnection.RUNNING_MODE = HttpConnection.RUNNING_MODE_SINGLE;
    }

    /**
     * 更改运行模式
     * @param mode
     */
    public static void setRunningMode(int mode){
        HttpConnection.RUNNING_MODE = mode;
    }
}
