import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int queueSize = 0;
    private int tail = -1;

    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return queueSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return queueSize;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("argument can't be null");
        }

        if (queueSize == queue.length) {
            resize(2 * queue.length);
        }

        queue[queueSize++] = item;
        tail++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }

        int itemNum = StdRandom.uniform(0, queueSize);
        queueSize--;
        Item item = queue[itemNum];
        queue[itemNum] = queue[tail];
        tail--;
        if (queueSize > 0 && queueSize == queue.length/4) {
            resize(queue.length/2);
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }

        int itemNum = StdRandom.uniform(0, queueSize);

        return queue[itemNum];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resize(int capacity)
    {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < queueSize; i++)
            copy[i] = queue[i];
        queue = copy;
    }
    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> d = new RandomizedQueue<>();
        d.enqueue(1);
        d.enqueue(2);
        d.enqueue(3);
        d.enqueue(4);

        for (int num: d) {
            StdOut.println(num);
        }

        StdOut.println(d.sample());
        StdOut.println(d.sample());
        StdOut.println(d.dequeue());
        StdOut.println(d.size());
        StdOut.println(d.dequeue());
        StdOut.println(d.dequeue());
        StdOut.println(d.dequeue());
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current = 0;
        private final int[] per = StdRandom.permutation(queueSize);

        public boolean hasNext() {
            return  current < queueSize;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove method not supported");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items in deque");
            }
            int itemNum = per[current];
            current++;

            return queue[itemNum];
        }
    }
}
