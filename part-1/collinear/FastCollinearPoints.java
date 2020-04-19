import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
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
        Point[] pointsCopySlopeOrder = pointsCopy.clone();

        for (int i = 0; i < pointsCopy.length - 3; i++) {
            int collinear = 2;
            ArrayList<Point> segment = new ArrayList<>();
            Arrays.sort(pointsCopySlopeOrder, pointsCopy[i].slopeOrder());

            double baseSlope = pointsCopySlopeOrder[0].slopeTo(pointsCopySlopeOrder[1]);
            segment.add(pointsCopySlopeOrder[1]);

            for (int j = 2; j < pointsCopySlopeOrder.length; j++) {
                double currentSlope = pointsCopySlopeOrder[0].slopeTo(pointsCopySlopeOrder[j]);
                if (Double.compare(baseSlope, currentSlope) == 0) {
                    segment.add(pointsCopySlopeOrder[j]);
                    collinear++;
                } else if (collinear < 4) {
                    collinear = 2;
                    baseSlope = currentSlope;
                    segment = new ArrayList<>();
                    segment.add(pointsCopySlopeOrder[j]);
                } else {
                    segment.add(pointsCopy[i]);
                    Point[] s = segment.toArray(new Point[0]);
                    Arrays.sort(s);
                    if (pointsCopy[i].compareTo(s[0]) == 0) {
                        lineSegments.add(new LineSegment(s[0], s[s.length - 1]));
                    }
                    baseSlope = currentSlope;
                    collinear = 2;
                    segment = new ArrayList<>();
                    segment.add(pointsCopySlopeOrder[j]);
                }
            }

            if (collinear >= 4) {
                segment.add(pointsCopy[i]);
                Point[] s = segment.toArray(new Point[0]);
                Arrays.sort(s);
                if (pointsCopy[i].compareTo(s[0]) == 0) {
                    lineSegments.add(new LineSegment(s[0], s[s.length - 1]));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
