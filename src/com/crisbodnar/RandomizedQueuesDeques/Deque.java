//package com.crisbodnar.RandomizedQueuesDeques;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    //Pointers to first and last elements
    private Node<Item> first;
    private Node<Item> last;

    //Size of the deque
    private int size ;


    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if(item == null) throw new NullPointerException("Can't add null item");

        size++;
        Node<Item> oldFirst = first;
        first = new Node<>(item, null, oldFirst);
        if(oldFirst != null)
            oldFirst.setPrevious(first);
        else
            last = first;
    }

    public void addLast(Item item) {
        if(item == null) throw new NullPointerException("Can't add null item");

        size++;
        Node<Item> oldLast = last;
        last = new Node<>(item, oldLast, null);
        if(oldLast != null)
            oldLast.setNext(last);
        else
            first = last;
    }

    public Item removeFirst() {
        if(size == 0) throw new NoSuchElementException("Can't remove first element from an empty stack");
        else {
            Item removedItem = first.getItem();
            first.cleanItem();

            if (size == 1) {
                first = last = null;
            }
            else {
                first = first.next();
                first.setPrevious(null);
            }

            size--;
            return removedItem;
        }
    }

    public Item removeLast() {
        if(size == 0) throw new NoSuchElementException("Can't remove last element from an empty stack");
        else {
            Item removedItem = last.getItem();
            last.cleanItem();

            if (size == 1) {
                first = last = null;
            }
            else {
                last = last.previous();
                last.setNext(null);
            }

            size--;
            return removedItem;
        }
    }

    public Iterator<Item> iterator() {
       return new DequeIterator<>(first, last);
    }

    private static class DequeIterator<Item> implements Iterator<Item> {

        private Node<Item> iterator;
        private Node<Item> last;

        public DequeIterator(Node<Item> first, Node<Item> last) {
            this.iterator = first;
            this.last = last;
        }

        @Override
        public boolean hasNext() {
            return iterator != null;
        }

        @Override
        public Item next() {
            if(hasNext()) {
                Item item = iterator.getItem();
                iterator = iterator.next();
                return item;
            }
            else {
                throw new NoSuchElementException("No more items to return");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    private static class Node<T> {
        private T item;
        private Node<T> previous;
        private Node<T> next;

        public Node(T item, Node<T> left, Node<T> right) {
            this.item = item;
            this.previous = left;
            this.next = right;
        }

        public Node(T item) {
            this(item, null, null);
        }

        public Node<T> previous() {
            return this.previous;
        }

        public Node<T> next() {
            return this.next;
        }

        public T getItem() {
            return item;
        }

        public void setPrevious(Node<T> prev) {
            previous = prev;
        }

        public void setNext(Node<T> nex) {
            next = nex;
        }

        public void cleanItem() {
            item = null;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        int item = deque.removeFirst();
        assert (item == 1);

        deque.addLast(2);
        item = deque.removeLast();
        assert (item == 2);

        deque.addLast(3);
        item = deque.removeFirst();
        assert (item == 3);

        deque.addFirst(4);
        item = deque.removeLast();
        assert (item == 4);

        assert (deque.isEmpty());
        assert (deque.size() == 0);

        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        assert (deque.size() == 4);

        Iterator<Integer> it = deque.iterator();
        item = it.next();
        assert (item == 4);

        item = it.next();

        assert (item == 3);
        it.next(); it.next();

        assert (!it.hasNext());
    }
}
