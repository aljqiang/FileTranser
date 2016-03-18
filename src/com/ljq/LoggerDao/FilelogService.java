package com.ljq.LoggerDao;

import com.ljq.LoggerDao.model.Tfilelog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static Log log = LogFactory.getLog(FilelogService.class);

    public static void main(String[] args) throws IOException {
        String resource = "com/ljq/loggerDao/mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder()
                .build(reader);
        SqlSession session = sessionFactory.openSession();

        try{
            //        //��ѯ
//        Book book = (Book) session.selectOne(
//                "com.bookstore.app.bookMapper.getBookByName", "Spring In Action");
//        System.out.println(book.toString());
//        //�޸�
//        book.setName("Spring In Action 2ed Edition");
//        book.setPrice("59");
//        session.update("com.bookstore.app.bookMapper.updateBook", book);
            //����
            Tfilelog tfilelog=new Tfilelog();
            tfilelog.setXh(3);
            tfilelog.setKhh("1008");
            tfilelog.setRq("20160318");
            tfilelog.setWjlj("D:\\TEST");
            tfilelog.setBz("test");

            int i=session.insert("com.ljq.LoggerDao.mapping.TfilelogMapper.insertFilelog", tfilelog);
            System.out.println(i);
//        //ɾ��
//        session.delete("com.bookstore.app.bookMapper.deleteBook", "Struts In Action");
            session.commit();
        }catch (Exception e){
            log.info("д�����ݿ���־��Ϣ��������Ϊ:"+e);
        }finally {
            session.close();
        }
    }
}
