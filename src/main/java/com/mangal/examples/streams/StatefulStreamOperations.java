package com.mangal.examples.streams;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Example taken from Stuart Mark's post on Writing Stateful Stream operations
 *
 * https://stuartmarks.wordpress.com/2015/01/09/writing-stateful-stream-operations/
 *
 * Read the comments below to understand the flow if not clear
 * */
public class StatefulStreamOperations {

    public static void main(String[] args) {

        List<Book> list = Arrays.asList(
            new Book("This Side of Paradise", "F. Scott Fitzgerald"),
            new Book("The Beautiful and Damned", "F. Scott Fitzgerald"),
            new Book("The Great Gatsby", "F. Scott Fitzgerald"),
            new Book("Tender is the Night", "F. Scott Fitzgerald"),
            new Book("The Sound and the Fury", "William Faulkner"),
            new Book("Absalom, Absalom!", "William Faulkner"),
            new Book("Intruder in the Dust", "William Faulkner"),
            new Book("The Sun Also Rises", "Ernest Hemingway"),
            new Book("A Farewell to Arms", "Ernest Hemingway"),
            new Book("The Old Man and the Sea", "Ernest Hemingway"),
            new Book("For Whom the Bell Tolls", "Ernest Hemingway"),
            new Book("A Moveable Feast", "Ernest Hemingway")
        );

        list.stream()
                .filter(distinctByKey(Book::getAuthor))
                .forEach(System.out::println);

    }

    /**
     * Need a stateful operation as it needs to keep a state of what all elements have been
     * processed so far
     *
     * The second insight is that lambdas can capture local variables from their enclosing
     * lexical environment. These local variables cannot be mutated, but if they are
     * references to mutable objects, ​those objects​ can be mutated. Thus we can write a
     * higher-order function whose local variables contain references to the state objects,
     * and we can have our higher-order function return a lambda that captures those
     * locals and does its processing based on the captured, mutable state.
     *
     *
     * <code>seen</code> is such a local variable that is a reference to a mutable object
     * a concurrent data-structure (set) which can be used to contain the elements seen
     * so far
     *
     * <code>keyExtractor</code> is a function that would be evaluated by the stateful function
     * to get the actual value of the element in the stream. The stateful function then
     * tries to put this value in the set and returns false if the value is already present
     * else returns true (default behavior of set.add)
     *
     * since this returns a boolean so it can be used in filter operation on streams
     *
     * The set is backed by a ConcurrentHashMap to support parallel streams
     * */
    static<T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor){
        Set<Object> seen = Collections.newSetFromMap(new ConcurrentHashMap<>());
        return t -> seen.add(keyExtractor.apply(t));
    }

    static class Book{
        public String getName() {
            return name;
        }

        public Book(String name, String author) {
            this.name = name;
            this.author = author;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthor() {
            return author;
        }

        @Override
        public String toString() {
            return "Book{" +
                    "name='" + name + '\'' +
                    ", author='" + author + '\'' +
                    '}';
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        private String name;
        private String author;
    }
}
