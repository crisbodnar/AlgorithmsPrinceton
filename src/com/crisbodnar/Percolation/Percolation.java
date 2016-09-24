package com.crisbodnar.Percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    //The matrix of the percolation system
    private boolean[][] matrix;

    private final int MATRIX_SIZE;
    private final int NUMBER_OF_SITES;
    private final int TOP;
    private final int BOTTOM;

    private final int[] dif = {1, 0, -1, 0};

    private final WeightedQuickUnionUF unionFind;
    private final WeightedQuickUnionUF unionFindWithoutBottom;

    public Percolation(int n) {
        if(n <= 0) throw new IllegalArgumentException("Matrix dimension should be greater than 0");

        MATRIX_SIZE = n;
        NUMBER_OF_SITES = MATRIX_SIZE * MATRIX_SIZE + 2;
        TOP = NUMBER_OF_SITES - 2;
        BOTTOM = NUMBER_OF_SITES - 1;
        unionFind = new WeightedQuickUnionUF(NUMBER_OF_SITES);
        unionFindWithoutBottom = new WeightedQuickUnionUF(NUMBER_OF_SITES);

        matrix = new boolean[n + 1][n + 1];

        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++)
                matrix[i][j] = false;
    }

    private boolean inMatrix(int i, int j) {
        return i > 0 && i <= MATRIX_SIZE && j > 0 && j <= MATRIX_SIZE;
    }

    public void open(int i, int j) {
        if(!inMatrix(i, j)) throw new IndexOutOfBoundsException("Can't open site out of matrix");

        matrix[i][j] = true;

        //Visit the neighbours
        for(int idx = 0; idx < 4; idx++) {
            int ii = i + dif[idx], jj = j + dif[(idx + 1) % 4];
            if (inMatrix(ii, jj) && isOpen(ii, jj)) {
                unionFind.union(getSiteNumber(i, j), getSiteNumber(ii, jj));
                unionFindWithoutBottom.union(getSiteNumber(i, j), getSiteNumber(ii, jj));
            }
        }

        //Union with the fake nodes
        if(i == 1) {
            unionFind.union(getSiteNumber(i, j), TOP);
            unionFindWithoutBottom.union(getSiteNumber(i, j), TOP);
        }
        if(i == MATRIX_SIZE)
            unionFind.union(getSiteNumber(i, j), BOTTOM);
    }

    private int getSiteNumber(int i, int j) {
        int res =  (i - 1) * MATRIX_SIZE + j - 1;
        assert (res >= 0 && res < MATRIX_SIZE * MATRIX_SIZE);
        return res;
    }

    public boolean isOpen(int i, int j) {
        if(!inMatrix(i, j)) throw new IndexOutOfBoundsException("Site out of the matrix");

        return matrix[i][j];
    }

    public boolean isFull(int i, int j) {
        if(!inMatrix(i, j)) throw new IndexOutOfBoundsException("Site out of the matrix");

        return matrix[i][j] && unionFindWithoutBottom.connected(getSiteNumber(i, j), TOP);
    }

    public boolean percolates() {
        return unionFind.connected(TOP, BOTTOM);
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(1);
        assert(percolation.isFull(1, 1));

        percolation.open(1, 1);
        assert(percolation.isOpen(1, 1));
        assert(!percolation.isFull(1, 1));

        assert(percolation.percolates());
    }
}
