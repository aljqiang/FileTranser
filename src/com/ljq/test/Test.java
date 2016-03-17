package com.ljq.test;

import com.ljq.queue.FileEntity;
import com.ljq.util.FileTypeHelper;

import java.io.File;

/**
 * User: Larry
 * Date: 2015-03-31
 * Time: 19:24
 * Version: 1.0
 */

public class Test {
    public static void main(String[] args) {
        String FileDir ="Z:\\JD\\20160316";

        String[] str = Test.fileReader(FileDir);

        FileEntity fileEntity=new FileEntity();

        for (int i = 0; i < str.length; i++) {
            File tempFile =new File(str[i].trim());

            // 判断文件是否为指定类型文件
            if(tempFile.exists() && "pdf".equals(FileTypeHelper.getFileByFile(tempFile)) ){
                String fileName = tempFile.getName();

                int dot = fileName.lastIndexOf('.');

                if ((dot >-1) && (dot < (fileName.length()))) {
                    String subFileName=fileName.substring(0, dot);
                    String[] fileNameSplit=subFileName.split("-");

                    for (int j = 0; j < fileNameSplit.length; j++) {
                        if(j==0){
                            fileEntity.setKhh(fileNameSplit[j]);
                        }else if (j==1){
                            fileEntity.setRq(fileNameSplit[j]);
                        }else{
                            fileEntity.setKhzh(fileNameSplit[j]);
                        }
                    }

                    System.out.println(fileEntity.getKhh()+"-"+fileEntity.getRq()+"-"+fileEntity.getKhzh());
                }
            }

        }

    }

    /**
     * 遍历文件目录
     */
    private static String[] fileReader(String pathInput) {
        File file = new File(pathInput);
        File[] files = file.listFiles();

        String[] filepathList = null;

        try {
            int k = 0;
            String filePath = "";

            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
//                    log.info("=============================文件路径：\n" + files[i].getPath().replace("\\","/"));
                    if (k != 0) {
                        filePath += ",";
                    }

                    filePath += files[i].getPath().replace("\\", "/");
                    k++;

                } else if (files[i].isDirectory()) {
                    System.out.println("找不到文件.");
                }
            }

            filepathList = filePath.split(",");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return filepathList;
    }
}
