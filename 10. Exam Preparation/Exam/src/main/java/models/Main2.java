package models;

import java.util.ArrayDeque;

public class Main2 {
    public static void main(String[] args) {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.offer(null);
        System.out.println(deque.isEmpty());
    }
}
