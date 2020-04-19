import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private final SET<Point2D> pointsSet;

    // construct an empty set of points
    public PointSET() {
        pointsSet = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointsSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointsSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        pointsSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return pointsSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : pointsSet) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> result = new ArrayList<>();

        for (Point2D point : pointsSet) {
            if (rect.contains(point)) {
                result.add(point);
            }
        }

        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }

        Point2D result = null;
        double smallestDistance = Double.POSITIVE_INFINITY;

        for (Point2D point : pointsSet) {
            double distance = p.distanceSquaredTo(point);
            if (Double.compare(distance, smallestDistance) < 0 && !p.equals(point)) {
                smallestDistance = distance;
                result = point;
            }
        }

        return result;
    }
}
