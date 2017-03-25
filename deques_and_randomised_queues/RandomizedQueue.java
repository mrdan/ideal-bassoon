/*----------------------------------------------------------------
 *  Author:        Daniel Doyle
 *  Written:       28/02/2017
 *  Last updated:  01/03/2017
 *  Score:         98/100
 *
 *  Compilation:   javac-algs4 RandomizedQueue.java
 *
 *  A class implmentation of a randomised queue using an expanding array
 *----------------------------------------------------------------*/
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;


public class RandomizedQueue<Item> implements Iterable<Item> {

    private int itemCount;      // track number of items in the queue
    private Item[] theQueue;    // the queue itself

    /**
     *   Constructor. Initialises variables, including setting array size
     *   to 2 initially.
     */
    public RandomizedQueue() {
        itemCount = 0;
        theQueue = (Item[]) new Object[2];
    }

    /**
     *   Returns an iterator for the queue
     */
    public Iterator<Item> iterator() { return new RandomIterator(); }

    /**
     *   Class that implements the iterator for the queue.
     *   Each iterator instance gets it's own random ordering
     *   and position
     */
    private class RandomIterator implements Iterator<Item> {

        private int current;    // iterator's current position in the queue
        private int[] order;    // iterator instance-distinct queue ordering

        /**
         *   Constructor for the queue iterator
         */
        public RandomIterator() {
            // initial iterator position is the start
            current = 0;
            // randomise the queue ordering on iterator creation
            order = StdRandom.permutation(itemCount);
        }

        /**
         *   Return true if there is at least one more element in the queue
         */
        public boolean hasNext() {
            return current < itemCount;
        }

        /**
         *   Not implemented
         *
         *   throws UnsupportedOperationException if called
         */
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        /**
         *   Constructor for the queue iterator
         *
         *   throws NoSuchElementException there are no more elements to return
         */
        public Item next() {
            if (current >= itemCount) {
                throw new java.util.NoSuchElementException();
            }

            // maintain iterator position
            current++;

            return theQueue[order[current - 1]];
        }
    }

    /**
     *   Resizes the queue array to newCapacity-elements by
     *   copying the old array's values into a new array of newCapacity-size
     *
     *   throws an IllegalArgumentException if newCapacity is smaller than
     *   the current queue itemCount
     */
    private void queueResize(int newCapacity) {
        if (newCapacity < itemCount)
            throw new IllegalArgumentException();

        Item[] newQueue = (Item[]) new Object[newCapacity];

        for (int i = 0; i < itemCount; i++) {
            newQueue[i] = theQueue[i];
        }

        theQueue = newQueue;
    }

    /**
     *  Return true if the queue is empty
     */
    public boolean isEmpty() {
        return itemCount == 0;
    }

    /**
     *  Return an int representing the number of items in the queue
     */
    public int size() {
        return itemCount;
    }

    /**
     *  Adds item to the end of queue, resizing the array if needed
     *
     *  throws NullPointerException if item is null
     */
    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException();

        // if array limit reached, double it's size
        if (theQueue.length == itemCount)
            queueResize(theQueue.length * 2);

        // on enqueue, add element to array
        theQueue[itemCount] = item; // it might just be swapped with itself, so it should be assigned
        itemCount++;
    }

    /**
     *  Removes and returns a random element from the queue,
     *  resizing the array if needed (i.e. if queue is less than 1/4 full)
     *
     *  throws NoSuchElementException if queue is empty
     */
    public Item dequeue() {
        // return and remove a random element
        if (itemCount < 1) {
            throw new java.util.NoSuchElementException();
        }

        // chose a random element
        int chosen = StdRandom.uniform(itemCount);

        // remove element, move whatever was last in q to that spot and null last spot
        Item output = theQueue[chosen];
        theQueue[chosen] = theQueue[itemCount - 1];
        theQueue[itemCount - 1] = null;
        itemCount--;

        // if removing the item makes the array 1/4 full, halve the size
        if (itemCount > 0 && itemCount <= (theQueue.length / 4))
            queueResize(theQueue.length / 2);

        return output;
    }

    /**
     *  Returns a random element from the queue without removing it
     *
     *  throws NoSuchElementException if queue is empty
     */
    public Item sample() {
        if (itemCount < 1) {
            throw new java.util.NoSuchElementException();
        }

        // sample return but do not remove a random element
        return theQueue[StdRandom.uniform(itemCount)];
    }

    private static void printQ(RandomizedQueue<Integer> rq) {
        System.out.print("Queue is: ");
        for (int x : rq) {
            System.out.print(x + " ");
        }
        System.out.println(" ");
    }
}
