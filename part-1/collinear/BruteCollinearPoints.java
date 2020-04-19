import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (Point p: points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Point[] pointsCopy = points.clone();

        Arrays.sort(pointsCopy);

        for (int i = 0; i < pointsCopy.length - 3; i++) {
            int[] usedIndexes = new int[3];

            for (int j = i + 1; j < pointsCopy.length; j++) {
                int collinear = 0;

                usedIndexes[collinear++] = j;
                double baseSlope = pointsCopy[i].slopeTo(pointsCopy[j]);
                for (int k = j + 1; k < pointsCopy.length; k++) {
                    double currentSlope = pointsCopy[i].slopeTo(pointsCopy[k]);
                    if (Double.compare(baseSlope, currentSlope) == 0) {
                        usedIndexes[collinear++] = k;
                    }
                    if (collinear == 3) {
                        break;
                    }
                }
                if (collinear == 3) {
                    lineSegments.add(new LineSegment(pointsCopy[i], pointsCopy[usedIndexes[2]]));
                }
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
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
