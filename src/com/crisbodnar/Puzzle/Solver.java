package com.crisbodnar.Puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;

public class Solver {

    private boolean isSolvable = false;
    private LinkedList<Board> finalMoves = new LinkedList<>();

    public Solver(Board initial) {
        if(initial == null)
            throw new NullPointerException();


        MinPQ<SearchNode> pq = new MinPQ<>(new SearchNodeComparator());

        pq.insert(new SearchNode(initial, new MyList<Board>()));
        pq.insert(new SearchNode(initial.twin(), new MyList<Board>()));

        SearchNode top;
        do {
            top = pq.delMin();
            for (Board neighbour: top.getBoard().neighbors()) {
                if(!top.getMoves().isEmpty()) {
                    if(!top.getMoves().head.equals(neighbour)) {
                        MyList<Board> newMoves = new MyList<>(top.getBoard(), top.getMoves());
                        pq.insert(new SearchNode(neighbour, newMoves));
                    }
                }
                else {
                    MyList<Board> newMoves = new MyList<>(top.getBoard());
                    pq.insert(new SearchNode(neighbour, newMoves));
                }
            }
        }while(!top.getBoard().isGoal());

        isSolvable = top.getMoves().isEmpty() || top.getMoves().getLast().equals(initial);
        LinkedList<Board> reversedMoves = top.getMoves();
        reversedMoves.push(top.getBoard());

        while(!reversedMoves.isEmpty())
            finalMoves.push(reversedMoves.pop());

    }

    public boolean isSolvable()   {
        return isSolvable;
    }

    public int moves() {
        if(!isSolvable)
            return -1;
        else
            return finalMoves.size() - 1;

    }
    public Iterable<Board> solution()  {
        if(isSolvable)
            return finalMoves;
        else
            return null;
    }

    public static void main(String[] args) {
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

    private class SearchNode {

        private final Board board;
        private final MyList<Board> moves;

        public SearchNode(Board b, MyList<Board> moves) {
            this.board = b;
            this.moves = moves;
        }

        public Board getBoard() {
            return board;
        }

        public MyList<Board> getMoves() {
            return moves;
        }
    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return a.getBoard().manhattan() + a.getMoves().size - b.getBoard().manhattan() - b.getMoves().size;
        }
    }

    private class MyList<T> {
        private MyList next = null;
        public MyList prev = null;
        public T head;

        public int size = 0;

        public MyList() {
            size = 0;
        }

        public MyList(T elem) {
            head = elem;
            size = 1;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public T getLast()

        public MyList(T elem, MyList<T> other) {
            this(elem);
            if(other.size > 0) {
                next = other;
                size = other.size + 1;
            }
        }
    }
}
