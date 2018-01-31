package com.mangal.examples.futures;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {

    public static void main(String[] args){

        CompletableFuture<String> completableFuture =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException ex){
                        ex.printStackTrace();
                        throw new IllegalArgumentException(ex);
                    }
                    return "Hello World";
                });

        completableFuture.thenAccept(name->{
            System.out.println(name);
        });

        System.out.println("Not blocked. This statement executes before the future returns");
        completableFuture.join();
        System.out.println("Blocked here - waiting until the future is completed " +
                "otherwise the main method would exit");
    }
}
