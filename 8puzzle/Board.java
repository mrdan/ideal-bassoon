/*----------------------------------------------------------------
 *  Author:        Daniel Doyle
 *  Written:       19/03/2017
 *  Last updated:  19/03/2017
 *  Score:         98/100
 *
 *  Compilation:   javac-algs4 Board.java
 *  Execution:     java Board
 *
 *  Represent an 8-puzzle game board
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Board {

    private int n;
    private int[] board;
    private int blankPosition;

    /**
     *   Constructor. Initialises variables etc.
     *
     *   Construct a board from an n-by-n array of blocks
     *   (where blocks[i][j] = block in row i, column j)
     *
     *   throws NullPointerException xx
     */
    public Board(int[][] blocks)
    {
        // error checking
            // check blocks is not null
            // check no element of blocks is null
            // check exactly one block is 0
            // check array is n * n

        // initialise
        n = blocks.length;              // n = size of one axis
        board = new int[(n * n) + 1];   // 1d array (index starting at 1)
        blankPosition = 0;

        // copy array
        int position = 1;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {

                if (blocks[i][j] == 0)
                    blankPosition = position;

                board[position] = blocks[i][j];
                position++;
            }
        }
    }

    /**
     *  Private constructor for twinning
     */
    private Board(int size, int zeroPos, int[] blocks)
    {
        board = blocks;
        n = size;
        this.blankPosition = zeroPos;
    }

    /**
     *  Private constructor for iteration
     */
    private Board(
        int size, int zeroPos, int[] blocks, int swapPos1, int swapPos2
    )
    {
        this.board = blocks.clone();
        this.n = size;
        this.blankPosition = zeroPos;

        int tmp = board[swapPos1];
        board[swapPos1] = board[swapPos2];
        board[swapPos2] = tmp;
    }

    /**
     *  Board dimension n
     */
    public int dimension(){ return n; }

    /**
     *  Number of blocks out of place (plus moves made so far)
     */
    public int hamming()
    {
        int score = 0;

        for (int i = 1; i < board.length; i++) {
            if (board[i] != 0 && board[i] != i)
                score++;
        }

        return score;
    }

    /**
     *  Sum of Manhattan distances between blocks and goal
     */
    public int manhattan()
    {
        int score = 0;
        for (int i = 1; i < board.length; i++)
        {
            if (board[i] != 0 && board[i] != i)
            {
                int x1 = i % n;
                int x0 = board[i] % n;

                if (x1 == 0) x1 = n;
                if (x0 == 0) x0 = n;

                int y1 = i % n  == 0 ? (i / n) : (i / n) + 1;
                int y0 = board[i] % n == 0 ? (board[i] / n) : (board[i] / n) + 1;

                int curX = (x1 - x0);
                int curY = (y1 - y0);

                curX = (curX < 0) ? curX * -1 : curX;
                curY = (curY < 0) ? curY * -1 : curY;

                score += (curX + curY);
            }
        }
        return score;
    }

    /**
     *  Is this board the goal board?
     */
    public boolean isGoal()
    {
        for (int i = 1; i < board.length; i++) {
            if (board[i] != 0 && board[i] != i) {
                return false;
            }
        }

        return true;
    }

    /**
     *  a board that is obtained by exchanging any pair of blocks
     */
    public Board twin()
    {
        int[] deVitoBoard = this.board.clone();

        // swap 2 elements
        int block1 = 0;
        int block2 = 0;

        // get random block, can't be zero
        while (deVitoBoard[block1] == 0) {
            block1 = StdRandom.uniform(1, deVitoBoard.length);
        }

        // get 2nd block, can't be zero or the same as block1
        while (block2 == block1 || deVitoBoard[block2] == 0) {
            block2 = StdRandom.uniform(1, deVitoBoard.length);
        }

        int temp = deVitoBoard[block1];
        deVitoBoard[block1] = deVitoBoard[block2];
        deVitoBoard[block2] = temp;

        return new Board(n, blankPosition, deVitoBoard);
    }

    /**
     *  does this board equal y?
     */
    public boolean equals(Object y)
    {
        // error checking
        if (y == null) throw new java.lang.NullPointerException();

        Board other = (Board) y;
        // check n dimension
        if (other.dimension() != n)
            return false;

        // check elements
        for (int i = 1; i < this.board.length; i++) {
            if (this.board[i] != other.board[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     *  all neighboring boards
     */
    public Iterable<Board> neighbors()
    {
        ArrayList<Board> order = new ArrayList<>();

        // create neighbours for all valid directions
        //
        // up
        if (blankPosition > n){
            Board neighbor = new Board(
                n,
                blankPosition - n,
                this.board,
                blankPosition,
                blankPosition - n
            );

            order.add(neighbor);
        }
        // down
        if (blankPosition <= n * (n - 1)) {
            Board neighbor = new Board(
                n,
                blankPosition + n,
                this.board,
                blankPosition,
                blankPosition + n
            );

            order.add(neighbor);
        }
        // left
        if ((blankPosition - 1) % n != 0) {
            Board neighbor = new Board(
                n,
                blankPosition - 1,
                this.board,
                blankPosition,
                blankPosition - 1
            );

            order.add(neighbor);
        }
        // right
        if (blankPosition % n != 0) {
            Board neighbor = new Board(
                n,
                blankPosition + 1,
                this.board,
                blankPosition,
                blankPosition + 1
            );

            order.add(neighbor);
        }

        return order;
    }

    /**
     *  String representation of this board
     */
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");

        for (int i = 1; i < board.length; i++) {
            s.append(String.format("%2d ", board[i]));

            if (i % n == 0)
                s.append("\n");
        }

        return s.toString();
    }
}