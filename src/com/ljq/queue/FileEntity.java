package com.ljq.queue;

/**
 * 文件实体类
 * User: Larry
 * Date: 2016-03-17
 * Time: 10:35
 * Version: 1.0
 */

public class FileEntity {
    /**
     * 客户号
     */
    private String khh;

    /**
     * 日期
     */
    private String rq;

    /**
     * 资金账号
     */
    private String khzh;

    public String getKhh() {
        return khh;
    }

    public void setKhh(String khh) {
        this.khh = khh;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getKhzh() {
        return khzh;
    }

    public void setKhzh(String khzh) {
        this.khzh = khzh;
    }
}
