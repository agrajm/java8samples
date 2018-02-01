package com.mangal.examples.futures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CombiningFuturesTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "Futuristic");

        CompletableFuture<String> combinedFuture = f1.thenCombine(f2, (s1,s2) -> {
            return s1+s2;
        });
        System.out.println(combinedFuture.get());

        // thenAccept takes a Consumer does not need to return anything just combine and do
        // whatever you want to
        f1.thenAcceptBoth(f2, (s1, s2)-> {
            System.out.println(s1+s2);
        });

    }
}
