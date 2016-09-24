package com.crisbodnar.Percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] thresholds;
    private final int TRIALS;

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    public PercolationStats(int n, int trials) {
        if(n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Trials and matrix dimension should be greater than 0");

        TRIALS = trials;
        thresholds = new double[TRIALS];

        for(int trial = 0; trial < trials; trial++) {
            Percolation percolation = new Percolation(n);

            int opened = 0;
            while(!percolation.percolates()) {
                int ii = StdRandom.uniform(n) + 1, jj = StdRandom.uniform(n) + 1;

                //Find a full cel randomly
                while(percolation.isOpen(ii, jj)) {
                    ii = StdRandom.uniform(n) + 1;
                    jj = StdRandom.uniform(n) + 1;
                }

                //Open the cell
                percolation.open(ii, jj);
                opened++;
            }

            thresholds[trial] = (double)opened / (double)(n * n);
        }

        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        confidenceLo = mean - stddev * 1.96 / Math.sqrt(TRIALS);
        confidenceHi = mean + stddev * 1.96 / Math.sqrt(TRIALS);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidenceLo;
    }

    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        if(args.length != 2) throw new IllegalArgumentException("2 command line arguments should be provided");

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        System.out.println(stats.mean());
        System.out.println(stats.stddev());
        System.out.println(stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
