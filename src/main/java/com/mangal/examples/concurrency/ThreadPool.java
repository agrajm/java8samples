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
    private ConcurrentLinkedQueue<Runnable> runnables;
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

    public void executeTask(Runnable runnable){
        if(!this.execute.get()){
            throw new IllegalStateException("The threadpool has been stopped. Restart to submit task");
        }
        this.runnables.add(runnable);
    }

    public void shutdown(){
        this.runnables.clear();
        stop();
    }

    public void stop(){
        this.execute.set(false);
    }

    public void awaitTermination(long timeout) throws TimeoutException{
        if(this.execute.get()){
            throw new IllegalStateException("Threadpool should be stopped before awaiting termination");
        }
        else{
            long currentTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTime < timeout){
                boolean flag = true;
                for (Thread worker: this.workers){
                    if(worker.isAlive()){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    return;
                }
                try {
                    Thread.sleep(1);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            throw new TimeoutException("Unable to shutdown pool within specified time "+timeout);
        }
    }

    private class ThreadPoolWorker extends Thread{

        private AtomicBoolean execute;
        private ConcurrentLinkedQueue<Runnable> runnables;

        public ThreadPoolWorker(AtomicBoolean execute, ConcurrentLinkedQueue<Runnable> runnables){
            this.execute = execute;
            this.runnables = runnables;
        }

        @Override
        public void run(){
            System.out.print("Inside Run of ThreadPoolWorker "+Thread.currentThread().getName());
            while (this.execute.get() && !this.runnables.isEmpty()){
                Runnable runnable;
                while ((runnable = runnables.poll()) !=null ){
                    System.out.print("Thread name "+Thread.currentThread().getName()+" executing ");
                    runnable.run();
                }
                try {
                    // Simulate delay and avoid busy wait
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
