//package com.crisbodnar.RandomizedQueuesDeques;


import edu.princeton.cs.algs4.StdIn;

public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue  = new RandomizedQueue<>();

        String[] strings = StdIn.readAllStrings();
        for(String str: strings) {
            queue.enqueue(str);
        }

        for (int i = 0; i < k; i++) {
            String el = queue.dequeue();
            System.out.println(el);
        }
    }
}
