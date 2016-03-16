package com.ljq.queue;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务队列
 * User: Larry
 * Date: 2015-03-27
 * Time: 15:29
 * Version: 1.0
 */

public class BlockQueue<T> {
    private final List<T> queue=new ArrayList<T>();

    public BlockQueue(){

    }

    /**
     * 添加队列元素
     */
    public void addQueueElement(T entity){
        synchronized(this.queue){
            this.queue.add(entity);
            this.queue.notifyAll();
        }
    }

    /**
     * 获取队列元素
     */
    public T getQueueElement(){
        synchronized(this.queue){
            T entity;
            if(0 == this.queue.size())
                entity = null;
            else
                entity = this.queue.remove(0);
            this.queue.notifyAll();
            return entity;
        }
    }

    public static void main(String[] args) {
//        BlockQueue<String> testQueue=new BlockQueue<String>();
//        testQueue.addQueueElement("a");
//        testQueue.addQueueElement("b");
//        testQueue.addQueueElement("c");
//
//        System.out.println(testQueue.getQueueElement());
//        System.out.println(testQueue.getQueueElement());
//        System.out.println(testQueue.getQueueElement());
    }
}
