import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {
    private Picture image;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        image = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(image);
    }

    // width of current picture
    public int width() {
        return image.width();
    }

    // height of current picture
    public int height() {
        return image.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException();
        }
        double energy = 1000;

        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return energy;
        }

        int colorX1 = image.getRGB(x - 1, y);
        int colorX2 = image.getRGB(x + 1, y);
        int colorY1 = image.getRGB(x, y - 1);
        int colorY2 = image.getRGB(x, y +1);

        double xSum = getPointEnergySum(colorX1, colorX2);
        double ySum = getPointEnergySum(colorY1, colorY2);

        energy = Math.sqrt(xSum + ySum);

        return energy;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if(height() == 1) {
            return new int[1];
        }

        double[][] energies = new double[width()][height()];
        double[][] distTo = new double[width()][height()];
        Point [][] edgeTo = new Point[width()][height()];

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energies[x][y] = energy(x, y);
            }
        }

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        // set base destinations
        for (int x = 0; x < width(); x++) {
            distTo[x][0] = 1000;
        }

        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                int yTo = y + 1;
                Point from = new Point(x, y);
                Point to = new Point(x, yTo);
                double newDist = distTo[x][y] + energies[to.x][to.y];
                if (Double.compare(distTo[to.x][to.y], newDist) > 0) {
                    distTo[to.x][to.y] = newDist;
                    edgeTo[to.x][to.y] = from;
                }

                if (x - 1 >= 0) {
                    to = new Point(x - 1, yTo);
                    newDist = distTo[x][y] + energies[to.x][to.y];
                    if (Double.compare(distTo[to.x][to.y], newDist) > 0) {
                        distTo[to.x][to.y] = newDist;
                        edgeTo[to.x][to.y] = from;
                    }
                }

                if (x + 1 < width() - 1) {
                    to = new Point(x + 1, yTo);
                    newDist = distTo[x][y] + energies[to.x][to.y];
                    if (Double.compare(distTo[to.x][to.y], newDist) > 0) {
                        distTo[to.x][to.y] = newDist;
                        edgeTo[to.x][to.y] = from;
                    }
                }
            }
        }

        Point minDistPoint = getMinDistPoint(distTo);

        int[] seam = getSeam(edgeTo, minDistPoint);

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!isHorizontalSeamValid(seam)) {
            throw new IllegalArgumentException();
        }
        transpose();
        removeSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (!isVerticalSeamValid(seam)) {
            throw new IllegalArgumentException();
        }

        removeSeam(seam);
    }

    private void removeSeam(int[] seam) {
        Picture newImage = new Picture(width() - 1, height());

        for (int y = 0; y < height(); y++) {
            boolean xToRemoveFound = false;
            for (int x = 0; x < width() - 1; x++) {
                if (seam[y] == x) {
                    xToRemoveFound = true;
                }

                if (xToRemoveFound) {
                    newImage.setRGB(x, y, image.getRGB(x + 1, y));
                } else {
                    newImage.setRGB(x, y, image.getRGB(x, y));
                }
            }
        }

        image = newImage;
    }

    private double getPointEnergySum(int point1, int point2) {
        int xR = ((point1 >> 16) & 0xFF) - ((point2 >> 16) & 0xFF);
        int xG = ((point1 >> 8) & 0xFF) - ((point2 >> 8) & 0xFF);
        int xB = ((point1 >> 0) & 0xFF) - ((point2 >> 0) & 0xFF);

        return Math.pow(xR, 2) +  Math.pow(xG, 2) +  Math.pow(xB, 2);
    }

    private Point getMinDistPoint(double[][] distTo) {
        double minDist = Double.POSITIVE_INFINITY;
        int minDistX = 0;
        int minDistY = height() - 1;
        for (int x = 0; x < width(); x++) {
            if (distTo[x][minDistY] < minDist) {
                minDist = distTo[x][minDistY];
                minDistX = x;
            }
        }

        return new Point(minDistX, minDistY);
    }

    private int[] getSeam(Point[][] edgeTo, Point point) {
        int[] seam = new int[height()];

        Stack<Point> stack = new Stack<>();
        stack.push(edgeTo[point.x][point.y]);
        seam[point.y] = point.x;

        while (!stack.isEmpty()) {
            Point from = stack.pop();
            seam[from.y] = from.x;
            if (edgeTo[from.x][from.y] != null) {
                stack.push(edgeTo[from.x][from.y]);
            }
        }

        return seam;
    }

    private boolean isVerticalSeamValid(int[] seam) {
        if (seam == null || seam.length != height() || width() == 1) {
            return false;
        }

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width()) {
                return false;
            }

            if (i + 1 < seam.length) {
                int nextIntDiff = seam[i] - seam[i + 1];

                if (nextIntDiff < -1 || nextIntDiff > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isHorizontalSeamValid(int[] seam) {
        if (seam == null || seam.length != width() || height() == 1) {
            return false;
        }

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height()) {
                return false;
            }

            if (i + 1 < seam.length) {
                int nextIntDiff = seam[i] - seam[i + 1];

                if (nextIntDiff < -1 || nextIntDiff > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    // rotate image
    private void transpose() {
        Picture transposedIm = new Picture(height(), width());

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                transposedIm.setRGB(y, x, image.getRGB(x, y));
            }
        }

        image = transposedIm;
    }

    private class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
