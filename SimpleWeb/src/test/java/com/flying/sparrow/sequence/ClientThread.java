package com.flying.sparrow.sequence;

/**
 * Created by wangjianchun on 2017/11/14.
 */
public class ClientThread extends Thread {

    private Sequence sequence;

    public ClientThread(Sequence sequence){
        this.sequence = sequence;
    }

    @Override
    public void run() {
        for(int i=0; i<3; i++){
            System.out.println(Thread.currentThread().getName()+" => "+sequence.getNumber());
        }
    }
}
