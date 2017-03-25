/*----------------------------------------------------------------
 *  Author:        Daniel Doyle
 *  Written:       26/02/2017
 *  Last updated:  26/02/2017
 *  Score:         100/100
 *
 *  Compilation:   javac-algs4 PercolationStats.java
 *  Execution:     java-algs4 PercolationStats
 *
 *  Repeatedly execute a "Monte Carlo simulation" on a percolation
 *  grid of 'n' size, 'trials' number of times
 *
 *  % java-algs4 PercolationStats <n> <trials>
 *  Sample Mean: 0.665825
 *  Sample Std Dev: 0.1181512392932665
 *  95% confidence interval: [0.6426673570985197, 0.6889826429014803]
 *
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

        private int trialsCount;     // the number of trials to execute
        private double[] percPoints; // the fraction of open to closed sites at which percolation was achieved

        /**
         *   Constructor. Initialised variables and executes 'trials'
         *   number of percolation trials.
         *
         *   Throws a IllegalArgumentException if n or trials is less than 1.
         */
        public PercolationStats(int n, int trials) {
            if (n <= 0 || trials <= 0)
                throw new IllegalArgumentException(
                    "Either n or trials is too small"
                );

            // initialise vars
            trialsCount = trials;
            percPoints = new double[trials];

            // run the trials
            for (int i = 0; i < trials; i++) {
                performTrial(n, i);
            }
        }

        /**
         *   Perform a 'trial' with ID trialNumber, using a n * n grid
         */
        private void performTrial(int n, int trialNumber) {
            // create percolation model
            Percolation perc = new Percolation(n);

            // open random sites in the grid until it percolates
            while (!perc.percolates())
            {
                int col = StdRandom.uniform(1, n + 1);
                int row = StdRandom.uniform(1, n + 1);

                if (!perc.isOpen(row, col))
                    perc.open(
                        row, col
                    );
            }

            // record the proportion of open sites to closed at percolation
            percPoints[trialNumber] = (double) perc.numberOfOpenSites() /  (double) (n * n);
        }

        /**
         *   Calculate and return the mean sample from the current trial results
         */
        public double mean() {
            return StdStats.mean(percPoints);
        }

        /**
         *   Calculate and return the standard deviation sample
         *   from the current trial results
         */
        public double stddev() {
            return StdStats.stddev(percPoints);
        }

        /**
         *   Calculate and return the low endpoint of 95% confidence interval
         */
        public double confidenceLo() {
            return mean() - ((1.96 * stddev()) / Math.sqrt(trialsCount));
        }

        /**
         *   Calculate and return the high endpoint of 95% confidence interval
         */
        public double confidenceHi() {
            return mean() + ((1.96 * stddev()) / Math.sqrt(trialsCount));
        }

        /**
         *   Writes program usage text to stdout
         */
        private static void printUsage() {
            System.out.println(
                "usage: java-algs4 PercolationStats " +
                "<int:single grid axis size (n)> <int:number of trials>"
            );
        }

        /**
         *   Accept 2 integer arguments representing grid size and
         *   number of trials. Create PercolationStats and output
         *   the results to stdout.
         *
         *   Throws IllegalArgumentException if not supplied enough arguments
         */
        public static void main(String[] args) {
            // check that there are at least two arguments
            if (args.length < 2) {
                System.out.println("Not enough arguments!");
                printUsage();
                return;

            }

            int n = 0;          // size of one axis in the grid
            int trials = 0;     // number of trials to perform

            try
            {
                // try and parse integers from the arguments
                n = Integer.parseInt(args[0]);
                trials = Integer.parseInt(args[1]);
            }
            catch (java.lang.NumberFormatException e)
            {
                System.out.println("Invalid argument(s)!");
                printUsage();
                return;
            }

            // Create PercolationStats and execute trials
            PercolationStats perc = new PercolationStats(n, trials);

            // Output results
            System.out.println("Sample Mean: " + perc.mean());
            System.out.println("Sample Std Dev: " + perc.stddev());
            System.out.println(
                "95% confidence interval: [" +
                perc.confidenceLo() + ", " + perc.confidenceHi() + "]"
            );
        }
}
