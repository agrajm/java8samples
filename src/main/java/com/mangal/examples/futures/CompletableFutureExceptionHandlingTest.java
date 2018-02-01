package com.mangal.examples.futures;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureExceptionHandlingTest {

    public static void main(String[] args) {

        CompletableFuture<String> completableFuture =
            new CompletableFuture<>().supplyAsync(() -> {
                throw new IllegalArgumentException("some runtime exception");
            });

        // with handle, either one of the two arguments is null
        String result = completableFuture.handle( (res, ex) -> {
            if(ex!=null){
                // some exception occurred
                return "Unknown error occurred";
            }
            return res;
        }).join();

        System.out.println(result);

    }
}
