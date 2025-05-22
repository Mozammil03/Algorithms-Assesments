package UnionFind;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final boolean[][] grid;
    private int openSites;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF fullUF;
    private final int virtualTop;
    private final int virtualBottom;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        this.n = n;
        grid = new boolean[n][n];
        openSites = 0;

        int size = n * n;
        uf = new WeightedQuickUnionUF(size + 2);
        fullUF = new WeightedQuickUnionUF(size + 1);
        virtualTop = size;
        virtualBottom = size + 1;
    }

    private int xyTo1D(int row, int col) {
        validate(row, col);
        return (row - 1) * n + (col - 1);
    }

    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("row or col out of bounds");
    }

    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) return;

        grid[row - 1][col - 1] = true;
        openSites++;

        int current = xyTo1D(row, col);

        // connect to virtual top or bottom
        if (row == 1) {
            uf.union(current, virtualTop);
            fullUF.union(current, virtualTop);
        }
        if (row == n) {
            uf.union(current, virtualBottom);
        }

        // connect to open neighbors
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : directions) {
            int r = row + d[0];
            int c = col + d[1];
            if (r >= 1 && r <= n && c >= 1 && c <= n && isOpen(r, c)) {
                int neighbor = xyTo1D(r, c);
                uf.union(current, neighbor);
                fullUF.union(current, neighbor);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) return false;
        int index = xyTo1D(row, col);
        return fullUF.find(index) == fullUF.find(virtualTop);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }
}
