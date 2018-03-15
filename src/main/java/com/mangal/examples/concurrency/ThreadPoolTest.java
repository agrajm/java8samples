package com.mangal.examples.concurrency;

import java.util.concurrent.TimeoutException;

public class ThreadPoolTest {

    public static void main(String[] args) throws TimeoutException, InterruptedException {

        ThreadPool threadPool = ThreadPool.getInstance(Runtime.getRuntime().availableProcessors());
        for(int i=0;i<20;i++){
            MyTask myTask = new MyTask(i);
            System.out.print("Adding task "+i);
            threadPool.executeTask(myTask);
        }

        Thread.sleep(50000);

        threadPool.shutdown();
        threadPool.awaitTermination(50000);
    }

    private static class MyTask implements Runnable{

        public MyTask(int index){
            this.index = index;
        }
        private int index;
        @Override
        public void run() {
            System.out.println("Executing Heavy Task "+index);
        }
    }
}
