package com.crisbodnar.Collinear;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private Point[] points;
    private int numberOfSeg = 0;
    private LineSegment[] seg = new LineSegment[12];

    private void checkUnique(Point[] array) {
        for(int i = 1; i < array.length; i++)
            if(array[i - 1].slopeTo(array[i]) == Double.NEGATIVE_INFINITY)
                throw new IllegalArgumentException("The provided points have to be unique");
    }

    private void expandArray() {

    }

    private void addSegment(Point p1, Point p2, Point p3) {


        if(numberOfSeg == seg.length)
            expandArray();
    }

    public FastCollinearPoints(Point[] givenPoints) {
        if(givenPoints == null)
            throw new NullPointerException("The provided array is null");

        for(Point p: givenPoints)
            if(p == null)
                throw new NullPointerException();

        points = givenPoints;
        Arrays.sort(points);
        checkUnique(points);

        Point[] copy = new Point[points.length];
        System.arraycopy(points, 0, copy, 0, points.length);

        for(int i = 0; i < copy.length; i++) {

            System.arraycopy(copy, 0, points, 0, copy.length);
            Arrays.sort(points, copy[i].slopeOrder());

            int collinearPoints = 2;
            for(int j = 2; j < points.length; j++) {
                if(points[j - 2].slopeTo(points[j - 1]) == points[j - 1].slopeTo(points[j])) {
                    collinearPoints++;
                }
                else {
                    if(collinearPoints > 3)
                        addSegment(points[j - collinearPoints], points[j - 1], points[j - 2]);
                    collinearPoints = 2;
                }
            }

            if(collinearPoints > 3)
                addSegment(points[points.length - collinearPoints +  1], points[points.length - 1],
                           points[points.length - 2]);
        }

    }

    public int numberOfSegments() {
        return numberOfSeg;
    }

    public LineSegment[] segments() {
        LineSegment seg2[] = new LineSegment[numberOfSeg];

        for(int i = 0; i < numberOfSeg; i++)
            seg2[i] = seg[i];

        return seg2;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
