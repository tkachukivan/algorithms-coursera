import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int queueSize = 0;
    private Node head = null;
    private Node tail = null;

    // is the deque empty?
    public boolean isEmpty() {
        return queueSize == 0;
    }

    // return the number of items on the deque
    public int size() {
        return queueSize;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("argument can't be null");
        }
        queueSize++;
        Node node = new Node();
        node.item = item;

        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("argument can't be null");
        }

        queueSize++;
        Node node = new Node();
        node.item = item;

        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }

        queueSize--;
        Item item = head.item;

        head = head.next;

        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }

        queueSize--;
        Item item = tail.item;

        tail = tail.prev;

        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<>();
        d.addFirst(1);
        d.addFirst(2);
        d.addLast(3);
        d.addLast(4);

        for (int num: d) {
            StdOut.println(num);
        }

        StdOut.println(d.removeFirst());
        StdOut.println(d.size());
        StdOut.println(d.removeLast());
        StdOut.println(d.removeFirst());
        StdOut.println(d.removeLast());
    }

    private class Node {
        Item item;
        Node next;
        Node prev;
    };

    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        public boolean hasNext() {
            return  current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove method not supported");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items in deque");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
