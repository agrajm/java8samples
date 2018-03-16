package com.mangal.examples.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * A basic thread pool
 * */
public class ThreadPool {

    private static ThreadPool instance;

    public static ThreadPool getInstance(int threadsCount){
        return new ThreadPool(threadsCount);
    }

    private AtomicBoolean execute;
    private ConcurrentLinkedQueue<Task> runnables;
    private List<ThreadPoolWorker> workers;
    private int threadsCount;

    private ThreadPool(int threadsCount){
        this.threadsCount = threadsCount;
        this.execute = new AtomicBoolean();
        this.execute.set(true);
        this.runnables = new ConcurrentLinkedQueue<>();
        this.workers = new ArrayList<>();

        for(int index=0; index<threadsCount; index++){
            ThreadPoolWorker threadPoolWorker = new ThreadPoolWorker(execute, runnables);
            threadPoolWorker.start();
            this.workers.add(threadPoolWorker);
        }
    }

    public void executeTask(Task runnable){
        if(!this.execute.get()){
            throw new IllegalStateException("The threadpool has been stopped. Restart to submit task");
        }
        this.runnables.add(runnable);
    }

    public void stop(){
        this.execute.set(false);
    }

    public void awaitTermination(long timeout) throws TimeoutException{
        for (Thread worker: this.workers){
            try {
                worker.join(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ThreadPoolWorker extends Thread{

        private AtomicBoolean execute;
        private ConcurrentLinkedQueue<Task> runnables;

        public ThreadPoolWorker(AtomicBoolean execute, ConcurrentLinkedQueue<Task> runnables){
            this.execute = execute;
            this.runnables = runnables;
        }

        @Override
        public void run(){
            while (this.execute.get() || !this.runnables.isEmpty()){
                Task task = runnables.poll();
                if(task!=null){
                    task.execute();
                }
                try {
                    // Simulate delay and avoid busy wait
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
