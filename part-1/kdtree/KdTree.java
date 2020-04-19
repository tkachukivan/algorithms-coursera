import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private final PointsBST pointsBST;

    // construct an empty set of points
    public KdTree() {
        pointsBST = new PointsBST();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointsBST.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointsBST.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        pointsBST.insert(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return pointsBST.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        pointsBST.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        return pointsBST.range(rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }

        return pointsBST.nearest(p);
    }

    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();
        KdTree kdtree = new KdTree();

        kdtree.insert(new Point2D(0.372, 0.497));
        kdtree.insert(new Point2D(0.564, 0.413));
        kdtree.insert(new Point2D(0.226, 0.577));
        kdtree.insert(new Point2D(0.144, 0.179));
        kdtree.insert(new Point2D(0.083, 0.510));
        kdtree.insert(new Point2D(0.320, 0.708));
        kdtree.insert(new Point2D(0.417, 0.362));
        kdtree.insert(new Point2D(0.862, 0.825));
        kdtree.insert(new Point2D(0.785, 0.725));
        kdtree.insert(new Point2D(0.499, 0.208));

        kdtree.nearest(new Point2D(0.155, 0.160));
    }
}

