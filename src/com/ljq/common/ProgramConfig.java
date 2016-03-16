package com.ljq.common;

import java.io.IOException;
import java.util.Properties;

/**
 * 程序环境配置
 * User: Larry
 * Date: 2015-03-27
 * Time: 15:56
 * Version: 1.0
 */

public class ProgramConfig {
    public static Properties GATEWAY = null;
    public static Properties X = null;
    public static Properties Y = null;

    public static void init(){
        GATEWAY = new Properties();
        X = new Properties();
        Y = new Properties();
        try {
            /**
             * 通信服务器配置
             */
            GATEWAY.load(ProgramConfig.class.getResourceAsStream("/gateway.properties"));

            /**
             * X系统通信配置
             */
            X.load(ProgramConfig.class.getResourceAsStream("/gateway.x.properties"));

            /**
             * Y系统通信配置
             */
            Y.load(ProgramConfig.class.getResourceAsStream("/gateway.y.properties"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 系统接收任务类型
     */
    public static final Integer TASK_TYPE_X=1;
    public static final Integer TASK_TYPE_Y=2;
    public static final Integer TASK_TYPE_CLEAN=0;
    public static final Integer[] TASK_TYPE={TASK_TYPE_X,TASK_TYPE_Y,TASK_TYPE_CLEAN};
}
