package com.flying.sparrow.sequence;

import javax.sound.midi.*;

/**
 * Created by wangjianchun on 2017/11/14.
 */
public class SequenceA implements Sequence {

    private static int number = 0;

    @Override
    public int getNumber() {
        number = number + 1;
        return number;
    }

    public static void main(String[] args) {
        Sequence sequence = new SequenceA();

        ClientThread thread1 = new ClientThread(sequence);
        ClientThread thread2 = new ClientThread(sequence);
        ClientThread thread3 = new ClientThread(sequence);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
