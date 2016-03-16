package com.ljq.LoggerDao;

import com.ljq.LoggerDao.model.Tfilelog;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * User: Larry
 * Date: 2016-03-16
 * Time: 11:24
 * Version: 1.0
 */

public class FilelogService {
    public static void main(String[] args) throws IOException {
        String resource = "com/ljq/loggerDao/mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder()
                .build(reader);
        SqlSession session = sessionFactory.openSession();
//        //²éÑ¯
//        Book book = (Book) session.selectOne(
//                "com.bookstore.app.bookMapper.getBookByName", "Spring In Action");
//        System.out.println(book.toString());
//        //ÐÞ¸Ä
//        book.setName("Spring In Action 2ed Edition");
//        book.setPrice("59");
//        session.update("com.bookstore.app.bookMapper.updateBook", book);
        //²åÈë
        Tfilelog tfilelog=new Tfilelog();
        tfilelog.setXh(3);
        tfilelog.setKhh(1003);
        tfilelog.setRq("20160315");
        tfilelog.setWjlj("D:\\TEST");
        tfilelog.setBz("test");

        int i=session.insert("com.ljq.LoggerDao.mapping.TfilelogMapper.insertFilelog", tfilelog);
        System.out.println(i);
//        //É¾³ý
//        session.delete("com.bookstore.app.bookMapper.deleteBook", "Struts In Action");
        session.commit();
        session.close();
    }
}
