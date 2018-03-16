package com.mangal.examples.concurrency;

import java.util.concurrent.TimeoutException;

public class ThreadPoolTest {

    public static void main(String[] args) throws TimeoutException, InterruptedException {

        ThreadPool threadPool = ThreadPool.getInstance(Runtime.getRuntime().availableProcessors()/2);
        for(int i=0;i<20;i++){
            MyTask myTask = new MyTask(i);
            threadPool.executeTask(myTask);
        }
        threadPool.stop();
        threadPool.awaitTermination(10000);
    }

    private static class MyTask implements Task{

        public MyTask(int index){
            this.index = index;
        }
        private int index;

        @Override
        public void execute() {
            System.out.println("Thread "+Thread.currentThread().getName()+" Executing Heavy Task "+index);
        }
    }
}
