package com.mangal.examples.futures;

import java.util.concurrent.*;

public class BlockingFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<String> blockingFuture =
                executorService.submit(
                        () -> {
                            Thread.sleep(5000);
                            return "Hello Blocking World";
                        }
                );

        String result = blockingFuture.get();
        System.out.println(result);

        System.out.print("Blocked. This statement does not executes until the future returns");

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

    }


}
