import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int TOP_VIRTUAL_SITE = 0;
    private final int BOTTOM_VIRTUAL_SITE;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final WeightedQuickUnionUF weightedQuickUnionUFForPercolation;
    private final int count;
    private boolean[][] openSides;
    private int opened = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(n + " is biger then 0");
        }
        count = n;
        BOTTOM_VIRTUAL_SITE = n * n + 1;
        weightedQuickUnionUF = new WeightedQuickUnionUF(BOTTOM_VIRTUAL_SITE + 1);
        weightedQuickUnionUFForPercolation = new WeightedQuickUnionUF(BOTTOM_VIRTUAL_SITE + 1);
        openSides = new boolean[n][n];

        if (count > 1) {
            for (int i = 1; i <= count; i++) {
                weightedQuickUnionUF.union(TOP_VIRTUAL_SITE, i);
            }
            for (int i = 1; i <= count; i++) {
                weightedQuickUnionUFForPercolation.union(TOP_VIRTUAL_SITE, i);
            }
            for (int i = 1; i <= count; i++) {
                int q = getSideCoordinate(count, i);
                weightedQuickUnionUFForPercolation.union(BOTTOM_VIRTUAL_SITE, q);
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        if (openSides[row - 1][col - 1]) {
            return;
        }

        openSides[row - 1][col - 1] = true;
        opened++;
        int p = getSideCoordinate(row, col);

        if (row - 2 >= 0 && openSides[row - 2][col - 1]) {
            int q = getSideCoordinate(row - 1, col);
            weightedQuickUnionUF.union(p, q);
            weightedQuickUnionUFForPercolation.union(p, q);
        }

        if (col < count && openSides[row - 1][col]) {
            int q = getSideCoordinate(row, col + 1);
            weightedQuickUnionUF.union(p, q);
            weightedQuickUnionUFForPercolation.union(p, q);
        }

        if (row < count && openSides[row][col - 1]) {
            int q = getSideCoordinate(row + 1, col);
            weightedQuickUnionUF.union(p, q);
            weightedQuickUnionUFForPercolation.union(p, q);
        }

        if (col - 2 >= 0 && openSides[row - 1][col - 2]) {
            int q = getSideCoordinate(row, col - 1);
            weightedQuickUnionUF.union(p, q);
            weightedQuickUnionUFForPercolation.union(p, q);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return openSides[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);

        if (!isOpen(row, col)) {
            return false;
        }

        if (count == 1 && openSides[0][0]) {
            return true;
        }

        int q = getSideCoordinate(row, col);

        if (q < count) {
            return true;
        }

        return weightedQuickUnionUF.connected(TOP_VIRTUAL_SITE, q);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opened;
    }

    // does the system percolate?
    public boolean percolates() {
        if (count == 1 && openSides[0][0]) {
            return true;
        }
        return weightedQuickUnionUFForPercolation.connected(TOP_VIRTUAL_SITE, BOTTOM_VIRTUAL_SITE);
    }

    private int getSideCoordinate(int row, int col) {
        return count * (row - 1) + col;
    }

    private void validate(int row, int col) {
        if (row > count || row < 1) {
            throw new IllegalArgumentException("row " + row + " is not between 1 and " + count);
        }

        if (col > count || col < 1) {
            throw new IllegalArgumentException("col " + col + " is not between 1 and " + count);
        }
    }
}
