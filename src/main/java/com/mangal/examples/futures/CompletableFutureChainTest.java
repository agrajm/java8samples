package com.mangal.examples.futures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CompletableFutureChainTest {
    public static void main(String[] args){
        composingFuturesUsingThenCompose();
    }
    
    private static void composingFuturesUsingThenCompose(){

        String userId = "userId";

        CompletableFutureChainTest test = new CompletableFutureChainTest();

        CompletableFuture details = test.getUserDetails(userId).thenCompose(user -> {
            return test.getFriends(user).thenCompose(users ->
                    CompletableFuture.allOf(
                            users.parallelStream().map(user1 -> {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Sending email to "+user1+Thread.currentThread().getName());
                                return test.emailSend(user1);
                            }).collect(Collectors.toList()).toArray(new CompletableFuture[users.size()])
                    ));
        });
        details.thenAccept((res)->{
            System.out.println("Sent all emails");
        });
        details.join();

    }

    private void composingFuturesUsingThenAccept(){

        String userId = "userId";

        CompletableFutureChainTest object = new CompletableFutureChainTest();
        System.out.println(Thread.currentThread().getName());

        CompletableFuture details = object.getUserDetails(userId).thenAccept(user -> {
            System.out.println(Thread.currentThread().getName());
            object.getFriends(user).thenAccept(users -> {
                users.stream().forEach(user1 -> {
                    System.out.println("Sending email to "+user+Thread.currentThread().getName());
                    object.emailSend(user1);
                });
            });
        });
        details.join();

    }


    private CompletableFuture<Boolean> emailSend(User user){
        return CompletableFuture.supplyAsync(() -> {
            return true; // always return true implying email was sent
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return false;
        });
    }

    private CompletableFuture<User> getUserDetails(String userId){

        CompletableFuture<User> userFuture = new CompletableFuture<>();
        CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new IllegalArgumentException(e);
            }
            User user = new User("test","Name1","Add1","email1");
            userFuture.complete(user);
            return null;
        });
        return userFuture;
    }

    private CompletableFuture<List<User>> getFriends(User user){

        List<User> friends = new ArrayList<User>(){
            {
                add(new User("fr1", "Ravi","addfr1","friend1@gmail.com"));
                add(new User("fr2", "Shanker","addfr2","friend2@gmail.com"));
                add(new User("fr3", "Prasad","addfr3","friend3@gmail.com"));
            }
        };

        return CompletableFuture.completedFuture(friends);
    }

    class User{
        public User(){}

        @Override
        public String toString() {
            return "User{" +
                    "userId='" + userId + '\'' +
                    ", name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }

        public String getUserId() {
            return userId;
        }
        public void setUserId(String userId) {
            this.userId = userId;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        private String userId;
        private String name;
        private String address;

        public User(String userId, String name, String address, String email) {
            this.userId = userId;
            this.name = name;
            this.address = address;
            this.email = email;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        private String email;

    }
}
