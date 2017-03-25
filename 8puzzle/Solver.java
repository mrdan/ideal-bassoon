/*----------------------------------------------------------------
 *  Author:        Daniel Doyle
 *  Written:       19/03/2017
 *  Last updated:  19/03/2017
 *  Score:         98/100
 *
 *  Compilation:   javac-algs4 Solver.java
 *  Execution:     java Solver
 *
 *  Solve an 8-puzzle game board
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Collections;

public class Solver {

    private Node solutionStart;
    private int solutionMoves;

    /**
     *   Constructor. Initialises variables etc.
     */
    private class Node implements Comparable<Node>
    {
        private Board board;
        private int movesMade;
        private Node previousNode;

        /**
         *   Constructor. Initialises variables etc.
         *
         */
        public Node(Board board, int movesMade, Node prev) {
            this.board = board;
            this.movesMade = movesMade;
            this.previousNode = prev;
        }

        // hamming
        // public int compareTo(Node other) {
        //     if (other == null)
        //         throw new java.lang.NullPointerException();

        //     int ourHamming = this.board.hamming() + this.movesMade;
        //     int theirHamming = other.board.hamming() + other.movesMade;

        //     if (ourHamming < theirHamming)
        //         return -1;
        //     else if (ourHamming > theirHamming)
        //         return 1;
        //     else
        //         return 0;
        // }

        /**
         *  String representation of this board
         */
        public int compareTo(Node other) {
            if (other == null)
                throw new java.lang.NullPointerException();

            int ourHamming = this.board.manhattan() + this.movesMade;
            int theirHamming = other.board.manhattan() + other.movesMade;
            if (ourHamming < theirHamming)
                return -1;
            else if (ourHamming > theirHamming)
                return 1;
            else
                return 0;
        }

        /**
         *  String representation of this board
         */
        public Iterable<Node> neighbors() {
            ArrayList<Node> neighbors = new ArrayList<>();

            for (Board n : board.neighbors()) {

                Node tmp = new Node(n, movesMade + 1, this);
                neighbors.add(
                    tmp
                );
            }

            return neighbors;
        }

        /**
         *  String representation of this board
         */
        public boolean isGoal() {
            return board.isGoal();
        }

        /**
         *  String representation of this board
         */
        public String toString() {
            return board.toString();
        }

        /**
         *  String representation of this board
         */
        public boolean validate(Node a) {
            if (previousNode == null)
                return false;

            return a.board.equals(previousNode.board);
        }

        /**
         *  String representation of this board
         */
        public int movesMade()
        {
            return movesMade;
        }
    }

    /**
     *  Find a solution to the initial board (using the A* algorithm)
     */
    public Solver(Board initial)
    {
        solutionMoves = -1;
        Board twin = initial.twin();

        // We define a search node of the game to be a board, the number of
        // moves made to reach the board, and the previous search node.
        MinPQ<Node> originalPQ = new MinPQ<>();
        MinPQ<Node> twinPQ = new MinPQ<>();
        Node root = new Node(initial, 0, null);
        Node twinRoot = new Node(twin, 0, null);

        // First, insert the initial search node
        // (the initial board, 0 moves, and a null previous search node)
        // into a priority queue.
        originalPQ.insert(root);
        twinPQ.insert(twinRoot);

        while(!root.isGoal() && !twinRoot.isGoal()) {
            // Then, delete from the priority queue the search node
            // with the minimum priority,
            root = originalPQ.delMin();

            for (Node x : root.neighbors())
            {
                if (!root.validate(x)) {
                    originalPQ.insert(x);
                }
                // and insert onto the priority queue all neighboring search
                // nodes (those that can be reached in one move from the
                // dequeued search node).
            }
            // Repeat this procedure until the search node
            // dequeued corresponds to a goal board.

            twinRoot = twinPQ.delMin();

            for (Node x : twinRoot.neighbors())
            {
                if (!twinRoot.validate(x))
                    twinPQ.insert(x);
            }
        }

        if (root.isGoal()) {
            solutionStart = root;
            solutionMoves = root.movesMade();
        }
    }

    /**
     *  Is the initial board solvable?
     */
    public boolean isSolvable()
    {
        if (solutionStart != null)
            return true;
        else
            return false;
    }

    /**
     *  Min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves()
    {
        return solutionMoves;
    }

    /**
     *  Sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution()
    {
        ArrayList<Board> sweetMoves = new ArrayList<>();
        Node x = solutionStart;
        while ( x != null) {
            sweetMoves.add(x.board);
            x = x.previousNode;
        }
        Collections.reverse(sweetMoves);

        return sweetMoves;
    }

    // test client
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
