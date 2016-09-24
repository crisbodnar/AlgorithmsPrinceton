//package com.crisbodnar.Puzzle;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] blocks;
    private final int SIZE;
    private final boolean isGoal;
    private final int hamming;
    private final int manhattan;

    private final int dif[] = {-1, 0, 1, 0};

    private int getGoalRow(int number) {
        if(number == 0)
            return SIZE - 1;

        return (number - 1) / SIZE;
    }

    private int getGoalCol(int number) {
        if(number == 0)
            return SIZE - 1;

        return (number - 1) % SIZE;
    }

    private int computeHamming() {
        int hammingScore = 0;

        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                if((i != getGoalRow(blocks[i][j]) || j != getGoalCol(blocks[i][j])) && (blocks[i][j] != 0)){
                    hammingScore++;
                }

        return hammingScore;
    }

    private int computeManhattan() {
        int manhattanScore = 0;

        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                if(blocks[i][j] != 0)
                    manhattanScore += Math.abs(getGoalRow(blocks[i][j]) - i)
                                    + Math.abs(getGoalCol(blocks[i][j]) - j);

        return manhattanScore;
    }

    public Board(int[][] blocks) {
        SIZE = blocks.length;

        this.blocks = new int[SIZE][SIZE];

        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                this.blocks[i][j] = blocks[i][j];

        hamming = computeHamming();
        manhattan = computeManhattan();
        isGoal = manhattan == 0;
    }

    public int dimension() {
        return SIZE;
    }

    public int hamming()   {
        return hamming;
    }

    public int manhattan()     {
        return manhattan;
    }

    public boolean isGoal() {
        if(isGoal)
            assert (manhattan() == hamming());

        return isGoal;
    }

    private void swap(int[][] matrix, int i, int j, int ii, int jj) {
        int temp = matrix[i][j];
        matrix[i][j] = matrix[ii][jj];
        matrix[ii][jj] = temp;
    }

    public Board twin() {
        int[][] twinBlocks = new int[SIZE][SIZE];

        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                twinBlocks[i][j] = blocks[i][j];

        for(int i = 2; i < SIZE * SIZE; i++)
            if(twinBlocks[getGoalRow(i)][getGoalCol(i)] != 0 && twinBlocks[getGoalRow(i - 2)][getGoalCol(i - 2)] != 0) {
                swap(twinBlocks, getGoalRow(i), getGoalCol(i), getGoalRow(i - 2), getGoalCol(i - 2));
                break;
            }

        return new Board(twinBlocks);
    }

    public boolean equals(Object y) {
        if(y == null)
            return false;
        else if(this == y)
            return true;
        else if(y instanceof Board){
            Board that = (Board) y;
            return toString().equals(that.toString());
        }
        else
            return super.equals(y);
    }

    public Iterable<Board> neighbors(){
        List<Board> neighbours = new ArrayList<>();

        int emptyi = 0, emptyj = 0;
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                if(blocks[i][j] == 0) {
                    emptyi = i;
                    emptyj = j;
                    break;
                }

        int[][] copy = new int[SIZE][SIZE];

        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                copy[i][j] = blocks[i][j];

        for(int d = 0; d < 4; d++) {
            int ii = emptyi + dif[d];
            int jj = emptyj + dif[(d + 1) % 4];

            if(ii >= 0 && ii < SIZE && jj >= 0 && jj < SIZE) {
                swap(copy, ii, jj, emptyi, emptyj);
                neighbours.add(new Board(copy));
                swap(copy, ii, jj, emptyi, emptyj);
            }
        }

        return neighbours;
    }

    public String toString()   {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(SIZE + "\n");

        for(int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++)
                stringBuilder.append(blocks[i][j] + " ");
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args){
        int[][] matrix = new int[2][2];
        matrix[0][0] = 1;
        matrix[0][1] = 0;
        matrix[1][0] = 3;
        matrix[1][1] = 2;

        Board b = new Board(matrix);
        Solver solver = new Solver(b);
        for (Board board : solver.solution())
            StdOut.println(board);
    }
}
