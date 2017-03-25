/*----------------------------------------------------------------
 *  Author:        Daniel Doyle
 *  Written:       28/02/2017
 *  Last updated:  28/02/2017
 *  Score:         98/100
 *
 *  Compilation:   javac-algs4 Permutation.java
 *
 *  Accept an int k and sequence of space-seperated strings, printing out
 *  a k-number random sampling of strings from the sequence
 *----------------------------------------------------------------*/
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    /**
     *  Main program loop. Accept int:k and space-seperated string
     *  arguments and output k-number of elements from string[]
     *  with uniform randomness
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough arguments!");
            return;
        }

        int k = 0;

        try
        {
            k = Integer.parseInt(args[0]);
        }
        catch (java.lang.NumberFormatException e)
        {
            System.out.println("Invalid argument(s)!");
            return;
        }

        if (k == 0)
            return;

        RandomizedQueue<String> test = new RandomizedQueue<String>();

        String x = StdIn.readString();
        while (x != null) {
            test.enqueue(x);
            try {
                x = StdIn.readString();
            } catch (java.util.NoSuchElementException err) {
                x = null;
            }
        }

        // for(String y : test) {
        //     System.out.println(y);
        // }

        Iterator<String> z = test.iterator();
        for (int i = 0; i < k; i++) {
            System.out.println(z.next());
        }
    }
}