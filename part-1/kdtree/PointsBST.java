import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class PointsBST {
    private int bstSize = 0;

    private Node root;

    public void insert(Point2D p) {
        root = insert(root, p, true, 0, 1, 0, 1);
    }

    private Node insert(Node node, Point2D p, boolean isEven, double xmin, double xmax, double ymin, double ymax)
    {
        if (node == null) {
            this.bstSize++;

            return new Node(p, isEven, xmin, xmax, ymin, ymax);
        }

        int cmp = comparePoints(p, node.point, isEven);
        double nxmin;
        double nxmax;
        double nymin;
        double nymax;

        if (cmp < 0) {
            if (isEven) {
                nymin = ymin;
                nymax = ymax;
                nxmin = xmin;
                nxmax = node.point.x();
            } else {
                nxmin = xmin;
                nxmax = xmax;
                nymin = ymin;
                nymax = node.point.y();
            }

            node.left = insert(node.left, p, !isEven, nxmin, nxmax, nymin, nymax);
        } else if (cmp > 0) {
            if (isEven) {
                nxmin = node.point.x();
                nxmax = xmax;
                nymin = ymin;
                nymax = ymax;
            } else {
                nymin = node.point.y();
                nymax = ymax;
                nxmin = xmin;
                nxmax = xmax;
            }

            node.right = insert(node.right, p, !isEven, nxmin, nxmax, nymin, nymax);
        }

        return node;
    }

    private int comparePoints(Point2D p1, Point2D p2, boolean isEven) {
        int cmp;
        if (isEven) {
            cmp = Double.compare(p1.x(), p2.x());
            if (cmp == 0) {
                cmp = Double.compare(p1.y(), p2.y());
            }
        } else {
            cmp = Double.compare(p1.y(), p2.y());
            if (cmp == 0) {
                cmp = Double.compare(p1.x(), p2.x());
            }
        }
        return cmp;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return this.bstSize;
    }

    public boolean contains(Point2D p) {
        Node node = root;
        while (node != null)
        {
            int cmp = comparePoints(p, node.point, node.isEven);

            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return true;
        }
        return false;
    }

    public void draw()
    {
        draw(root);
    }

    private void draw(Node node)
    {
        if (node == null) return;
        draw(node.left);
        node.point.draw();
        double x1;
        double y1;
        double x2;
        double y2;

        if (node.isEven) {
            StdDraw.setPenColor(StdDraw.RED);
            x1 = node.point.x();
            y1 = node.ymin;
            x2 = node.point.x();
            y2 = node.ymax;
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            x1 = node.xmin;
            y1 = node.point.y();
            x2 = node.xmax;
            y2 = node.point.y();

        }

        StdDraw.line(x1, y1, x2, y2);
        draw(node.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> result = new ArrayList<>();

        range(root, rect, result);

        return result;
    }

    private void range(Node node, RectHV rect, ArrayList<Point2D> result) {
        if (node == null) return;

        if (rect.intersects(node.rect)) {
            if (rect.contains(node.point)) {
                result.add(node.point);
            }

            range(node.left, rect, result);
            range(node.right, rect, result);
        } else {
            int cmp;
            if (node.isEven) {
                cmp = Double.compare(rect.xmin(), node.xmin);
            } else {
                cmp = Double.compare(rect.ymin(), node.ymin);
            }

            if (cmp > 0) {
                range(node.right, rect, result);
            } else if (cmp < 0) {
                range(node.left, rect, result);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        return nearest(root, p, Double.POSITIVE_INFINITY, null);
    }

    private Point2D nearest(Node node, Point2D p, double minDistance, Point2D closest) {
        if (node == null) return closest;
        if (node.rect.distanceSquaredTo(p) > minDistance) {
            return closest;
        }
        double distance = node.point.distanceSquaredTo(p);
        if (Double.compare(distance, minDistance) < 0) {
            minDistance = distance;
            closest = node.point;
        }

        int cmp = comparePoints(p, node.point, node.isEven);
        if (cmp < 0) {
            closest = nearest(node.left, p, minDistance, closest);
            minDistance = closest.distanceSquaredTo(p);
            closest = nearest(node.right, p, minDistance, closest);
        } else if (cmp > 0) {
            closest = nearest(node.right, p, minDistance, closest);
            minDistance = closest.distanceSquaredTo(p);
            closest = nearest(node.left, p, minDistance, closest);
        }

        return closest;
    }

    private static final class Node {
        public Point2D point;
        public boolean isEven;
        public Node left;
        public Node right;
        public double xmin;
        public double xmax;
        public double ymin;
        public double ymax;
        public RectHV rect;

        public Node(Point2D p, boolean even, double xmin, double xmax, double ymin, double ymax) {
            this.point = p;
            this.isEven = even;
            this.xmin = xmin;
            this.xmax = xmax;
            this.ymin = ymin;
            this.ymax = ymax;

            this.rect = new RectHV(xmin, ymin, xmax, ymax);
        }
    }
}