/*----------------------------------------------------------------
 *  Author:        Daniel Doyle
 *  Written:       26/02/2017
 *  Last updated:  26/02/2017
 *  Score:         100/100
 *
 *  Compilation:   javac-algs4 Percolation.java
 *
 *  Model a percolation grid using a 2D array for site-status storage
 *  and a WeightedQuickUnionUF for fast association searching
 *
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

        private int[][] siteGrid;                // array representing the site grid
                                                 // (0 == closed, 1 == open, 2 == open and full)
        private int gridSize;                    // amount of sites along one axis
        private int openSitesCount;              // number of 'open' sites
        private WeightedQuickUnionUF searchTree; // search tree for sites
        private int virtualTopSiteID;            // 'virtual' top site for search tree performance
        private int virtualBottomSiteID;         // 'virtual' bottom site for search tree performance

        /**
         *   Constructor. Initialises variables. Throws you a surprise party
         *   just when you think it's forgotten your birthday.
         *
         *   Throws a IllegalArgumentException if n is less than 1.
         */
        public Percolation(int n) {
            if (n < 1)
                throw new IllegalArgumentException(
                    "grid size must be at least 1 x 1"
                );

            gridSize = n;
            openSitesCount = 0;

            // 2 virtual rows added to searchTree to accommodate virtual sites
            searchTree = new WeightedQuickUnionUF((n + 2) * n);

            // siteGrid index starts at 1
            siteGrid = new int [n + 1][n + 1];

            // virtual sites location outside the grid proper
            virtualTopSiteID = xyTo1D(0, 0);
            virtualBottomSiteID = xyTo1D(gridSize + 1, (gridSize - 1));
        }

        /**
         *   Checks if row and col(umn) are within acceptable bounds
         *
         *   Throws a IndexOutOfBoundsException if otherwise
         */
        private void validateCoordinates(int row, int col) {
            // throw exception of either row or col is outside grid bounds
            if (row <= 0 || row > gridSize)
                throw new IndexOutOfBoundsException("Row is outside grid");
            if (col <= 0 || col > gridSize)
                throw new IndexOutOfBoundsException("Column is outside grid");
        }

        /**
         *   Generates a site id for row and col(umn) using
         *   the gridsize, effectively converting a 2D array
         *   position into a 1D value
         */
        private int xyTo1D(int row, int col) {
            return ((row * gridSize) + col);
        }

        /**
         *   Check if a neighbouring site is full or open.
         *   If neighbour is full, origin should be filled.
         *   If neighbour is open connect to origin site component.
         */
        private void checkNeighbour(
            int currentSiteID, int originRow, int originCol, int row, int col
        ) {
            // generate id for neighbour site
            int neighbourSiteID = xyTo1D(row, col);

            // if neighbour is full, this site should be too
            if (isFull(row, col)) {
                siteGrid[originRow][originCol] = 2;
            }

            // if site is open, but not already connected then connect
            if (
                isOpen(row, col) &&
                !searchTree.connected(currentSiteID, neighbourSiteID)
            )
                searchTree.union(currentSiteID, neighbourSiteID);
        }

        /**
         *   Opens a site on the siteGrid, connecting it to neighbouring sites
         *   if they are also open, and recursively filling neighbouring sites
         *   if appropriate
         *
         *   Throws a IndexOutOfBoundsException if row and col are invalid
         */
        public void open(int row, int col) {
            // check row and col are valid
            validateCoordinates(row, col);

            // check if already open
            if (isOpen(row, col))
                return;

            // open the site
            siteGrid[row][col] = 1;
            // add site to amount opened
            openSitesCount++;

            // generate an id for the site
            int currentSiteID = xyTo1D(row, col);

            // if in top row, then connect to virtual top
            if (row == 1) {
                searchTree.union(currentSiteID, virtualTopSiteID);
                siteGrid[row][col] = 2; // top row is auto-filled
            }

            // if in bottom row, then connect to virtual bottom
            if (row == gridSize) {
                searchTree.union(currentSiteID, virtualBottomSiteID);
            }

            // connect site to open neighbours
            // up
            if (row > 1)
                checkNeighbour(currentSiteID, row, col, row - 1, col);

            // down
            if (row < gridSize)
                checkNeighbour(currentSiteID, row, col, row + 1, col);

            // left
            if (col > 1)
                checkNeighbour(currentSiteID, row, col, row, col - 1);

            // right
            if (col < gridSize)
                checkNeighbour(currentSiteID, row, col, row, col + 1);

            // recursively fill open neighbours if appropriate
            if (siteGrid[row][col] == 2) {
                fillNeighbours(row, col);
            }
        }

        /**
         *   Check if a neighbouring site is full or open.
         *   If neighbour is open and not full, fill it and
         *   fill it's neighbours.
         */
        private void checkFillNeighbours(int row, int col) {
            // check if site is open and not already full
            if (isOpen(row, col) && !isFull(row, col)) {
                siteGrid[row][col] = 2;     // set to full
                fillNeighbours(row, col);   // fill neighbours of this site
            }
        }

        /**
         *   Fill the neighbours of site at (row, col) if
         *   they're open and not already full
         */
        private void fillNeighbours(int row, int col) {
            if (row > 1) checkFillNeighbours(row - 1, col);         // up

            if (row < gridSize) checkFillNeighbours(row + 1, col);  // down

            if (col > 1) checkFillNeighbours(row, col - 1);         // left

            if (col < gridSize) checkFillNeighbours(row, col + 1);  // right
        }

        /**
         *   Return true if site at (row, col) is open
         *
         *   Throws a IndexOutOfBoundsException if row and col are invalid
         */
        public boolean isOpen(int row, int col) {
            // check row and col are valid
            validateCoordinates(row, col);

            // if site is either 1 or 2 then it is open
            return siteGrid[row][col] != 0;
        }

        /**
         *   Return true if site at (row, col) is full
         *
         *   Throws a IndexOutOfBoundsException if row and col are invalid
         */
        public boolean isFull(int row, int col) {
            // check row and col are valid
            validateCoordinates(row, col);

            // if site is 2, it is full
            return siteGrid[row][col] == 2;
        }

        /**
         *   Return number of currently open sites on the grid
         */
        public int numberOfOpenSites() {
            return openSitesCount;
        }

        /**
         *   Return true if virtual top site and virtual bottom site are
         *   connected, indicating that percolation is possible
         */
        public boolean percolates() {
            // return true if percolation is possible
            return searchTree.connected(
                virtualTopSiteID,
                virtualBottomSiteID
            );
        }
}
