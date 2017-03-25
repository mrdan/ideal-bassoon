/*----------------------------------------------------------------
 *  Author:        Daniel Doyle
 *  Written:       28/02/2017
 *  Last updated:  01/03/2017
 *  Score:         98/100
 *
 *  Compilation:   javac-algs4 Deque.java
 *
 *  A class implmentation of a Double ended queue using a linked list
 *----------------------------------------------------------------*/
import java.util.Iterator;


public class Deque<Item> implements Iterable<Item> {

    private Node first;     // pointer to the beginning of the queue
    private Node last;      // pointer to the end of the queue
    private int itemCount;  // amount of items currently in the queue

    /**
     *   Constructor. Initialises variables.
     */
    public Deque() {
        itemCount = 0;  // initial queue state is empty
    }

    /**
     *   Class for a linked list node.
     */
    private class Node {
        private Item item;  // the item this node represents in the queue
        private Node next;  // the next item in the queue
    }

    /**
     *   Returns an iterator for the linked list that holds the queue
     */
    public Iterator<Item> iterator() { return new NodeIterator(); }

    /**
     *   Class that implements the iterator for the queue
     */
    private class NodeIterator implements Iterator<Item> {

        private Node current = first;   // set the current position to the first

        /**
         *   Return true if there is another item in the queue
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         *   Not implemented
         */
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        /**
         *   Return the next item in the queue and update the current position
         */
        public Item next() {
            if (current == null) {
                throw new java.util.NoSuchElementException();
            }

            // return the next item for the iterator and move the position up
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    /**
     *   Return true if the queue is empty
     */
    public boolean isEmpty() {
        return itemCount == 0;
    }

    /**
     *   Return an int representing the number of items in the queue
     */
    public int size() {
        return itemCount;
    }

    /**
     *   Add an item to the start of the queue
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        // if first is null the queue is empty, so set first and last to item
        if (first == null)
        {
            first = new Node();
            first.item = item;
            last = first;
        }
        else
        {
            // item is the new first, and the old first should be first.next
            Node oldFirst = first;
            first = new Node();
            first.next = oldFirst;
            first.item = item;
        }

        itemCount++; // track number of items in queue
    }

    /**
     *   Add an item to the end of the queue
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        // first is null, so item should really be first in the queue
        if (first == null)
        {
            addFirst(item);
            return;
        }
        else
        {
            Node oldLast = last;    // oldLast is no longer the last item
            last = new Node();
            last.item = item;       // last should now point to item
            oldLast.next = last;    // and oldLast.next points to last
        }
        itemCount++;    // track number of items in queue
    }

    /**
     *   Remove and return the first item from the queue
     */
    public Item removeFirst() {
        // if first exists, set first to equal first.next and reduce item count
        if (first != null)
        {
            Node outputNode = first;
            first = first.next;
            itemCount--;

            return outputNode.item;
        }
        else
        {
            throw new java.util.NoSuchElementException();
        }
    }

    /**
     *   Remove and return the last item from the queue
     */
    public Item removeLast() {
        // if last exists
        if (last != null)
        {
            // go down the queue and set the second to last to be last
            Node outputNode;
            Node tmpNode = first;

            // check if last == first
            if (itemCount > 1)
            {
                while (tmpNode.next.next != null)
                {
                    tmpNode = tmpNode.next;
                }
                outputNode = tmpNode.next;
                tmpNode.next = null;
                last = tmpNode;

                itemCount--;

                return outputNode.item;
            }
            else
            {
                return removeFirst();
            }
        }
        else
        {
            throw new java.util.NoSuchElementException();
        }
    }

    private static void printQ(Deque<Integer> deque) {
        System.out.print("Queue is: ");
        for (int x : deque) {
            System.out.print(x + " ");
        }
        System.out.println(" ");
    }
}