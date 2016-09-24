package com.crisbodnar.Collinear;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private Point[] points;
    private int numberOfSeg = 0;
    private LineSegment seg[] = new LineSegment[12];

    private void checkUnique(Point[] array) {
        for(int i = 1; i < array.length; i++)
            if(array[i - 1].slopeTo(array[i]) == Double.NEGATIVE_INFINITY)
                throw new IllegalArgumentException("The provided points have to be unique");
    }

    private void expandArray() {
        LineSegment seg2[] = new LineSegment[seg.length * 2];

        for(int i = 0; i < numberOfSeg; i++)
            seg2[i] = seg[i];

        seg = seg2;
    }

    private void addSegment(Point p1, Point p2) {
        seg[numberOfSeg++] = new LineSegment(p1, p2);

        if(numberOfSeg == seg.length)
            expandArray();
    }

    public BruteCollinearPoints(Point[] givenPoints) {
        if(givenPoints == null)
            throw new NullPointerException("The provided array is null");

        for(Point p: givenPoints)
            if(p == null)
                throw new NullPointerException();

        points = givenPoints;
        Arrays.sort(points);
        checkUnique(points);

        LineSegment seg[] = new LineSegment[12];
        int numberOfSegments = 0;

        for(int i = 0; i < points.length - 3; i++)
            for(int j = i + 1; j < points.length - 2; j++)
                for(int k = j + 1; k < points.length - 1; k++)
                    for(int l = k + 1; l < points.length; l++)
                        if(points[i].slopeTo(points[j]) == points[j].slopeTo(points[k])
                           && points[j].slopeTo(points[k]) == points[k].slopeTo(points[l]))
                            addSegment(points[i], points[l]);

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
