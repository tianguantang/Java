package org.sparrow.thread.pause;

/**
 * @author wangjianchun
 * @create 2018/3/17
 */
public class Run {

    public static void main(String[] args) {
        try {
            MyThread thread = new MyThread();
            thread.start();
            Thread.sleep(5000);
            //A 段
            thread.suspend();
            System.out.println("A= "+System.currentTimeMillis()+" i="+thread.getI());
            Thread.sleep(5000);
            System.out.println("A= "+System.currentTimeMillis()+" i="+thread.getI());
            //B 段
            thread.resume();
            Thread.sleep(5000);
            //C 段
            thread.suspend();
            System.out.println("B= "+System.currentTimeMillis()+" i="+thread.getI());
            Thread.sleep(5000);
            System.out.println("B= "+System.currentTimeMillis()+" i="+thread.getI());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
