package com.ljq.transer;

/**
 * User: Larry
 * Date: 2015-03-30
 * Time: 16:10
 * Version: 1.0
 */

public class FileTaskInfo {
    private String srcFile;
    private String srcDir;
    private String aimFile;
    private String aimDir;

    private String[] srcFiles;
    private String[] aimFiles;

    public String getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(String srcFile) {
        this.srcFile = srcFile;
    }

    public String getSrcDir() {
        return srcDir;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public String getAimFile() {
        return aimFile;
    }

    public void setAimFile(String aimFile) {
        this.aimFile = aimFile;
    }

    public String getAimDir() {
        return aimDir;
    }

    public void setAimDir(String aimDir) {
        this.aimDir = aimDir;
    }

    public String[] getSrcFiles() {
        return srcFiles;
    }

    public void setSrcFiles(String[] srcFiles) {
        this.srcFiles = srcFiles;
    }

    public String[] getAimFiles() {
        return aimFiles;
    }

    public void setAimFiles(String[] aimFiles) {
        this.aimFiles = aimFiles;
    }
}
