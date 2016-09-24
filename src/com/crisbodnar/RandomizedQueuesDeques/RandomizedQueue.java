//package com.crisbodnar.RandomizedQueuesDeques;


import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public class RandomizedQueue<Item> implements Iterable<Item>{

    private static final int INITIAL_SIZE = 12;
    private static final int INCR_RESIZE_FACTOR = 2;
    private static final int DECR_RESIZE_FACTOR = 2;

    //Array implementation of the queue
    private Item[] array;

    private int size;
    private int top;

    public RandomizedQueue() {
        array = (Item[]) new Object[INITIAL_SIZE];
        top = -1;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void expandArray() {
        Item[] newArray = (Item[]) new Object[array.length * INCR_RESIZE_FACTOR];
        for(int i = 0; i < size; i++)
            newArray[i] = array[i];

        array = newArray;
    }

    private void shrinkArray() {
        Item[] newArray = (Item[]) new Object[array.length / DECR_RESIZE_FACTOR];
        for(int i = 0; i < size; i++)
            newArray[i] = array[i];

        array = newArray;
    }

    public void enqueue(Item item) {
        if(item == null) throw new NullPointerException("Can't add null item to the queue");

        size++;
        array[++top] = item;

        if(size() == array.length)
            expandArray();

    }

    private void swap(int i, int j) {
        Item temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public Item dequeue() {
        if(isEmpty()) throw new NoSuchElementException("Can't dequeue an empty queue");
        else {
            int randomIndex = StdRandom.uniform(size);
            swap(randomIndex, top);

            Item dequeuedItem = array[top];

            //Make it null in order to be deleted by the garbage collector
            array[top] = null;

            //Decrease the top and size
            top--;
            size--;

            if(size() <= array.length / 4)
                shrinkArray();

            return dequeuedItem;
        }
    }

    public Item sample() {
        if(isEmpty()) throw new NoSuchElementException("Can't dequeue an empty queue");
        else {
            int randomIndex = StdRandom.uniform(size);
            return array[randomIndex];
        }
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<>(array, size());
    }

    private static class RandomizedQueueIterator<T> implements Iterator<T>{

        private T[] array;
        private int size;
        private int index;

        private void swap(int i, int j) {
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        private void shuffleArray() {
            for(int i = 1; i < size; i++) {
                int randomPosition = StdRandom.uniform(i + 1);
                swap(randomPosition, i);
            }
        }

        public RandomizedQueueIterator(T[] array, int size) {
            this.array = (T[]) new Object[size];
            for(int i = 0; i < size; i++)
                this.array[i] = array[i];

            this.size = size;
            this.index = 0;
            shuffleArray();
        }

        @Override
        public T next() {
            if(hasNext()) {
                return array[index++];
            }
            else {
                throw new NoSuchElementException("No next element for the iterator");
            }
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        assert (queue.isEmpty());

        queue.enqueue(1);

        assert (!queue.isEmpty());
        assert (queue.size() == 1);
        assert (queue.sample() == 1);

        int el = queue.dequeue();
        assert (queue.isEmpty());
        assert (el == 1);

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);

        Iterator<Integer> it = queue.iterator();
        while(it.hasNext()) {
            //System.out.println(it.next());
        }
    }
}
