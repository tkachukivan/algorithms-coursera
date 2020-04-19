import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double meanValue;
    private final double stddevValue;
    private final double confidenceLoValue;
    private final double confidenceHiValue;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(n + " is biger then 0");
        }

        double[] thresholds = new double[trials];
        double size = n * n;

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int cel = StdRandom.uniform(1, n + 1);
                p.open(row, cel);
            }

            thresholds[i] = p.numberOfOpenSites() / size;
        }

        meanValue = StdStats.mean(thresholds);
        stddevValue = StdStats.stddev(thresholds);
        confidenceLoValue = meanValue - CONFIDENCE_95 * stddevValue / Math.sqrt(trials);
        confidenceHiValue = meanValue + CONFIDENCE_95 * stddevValue / Math.sqrt(trials);
    }

    // sample mean of percolation threshold
    public double mean() {
        return meanValue;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddevValue;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLoValue;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHiValue;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, t);

        StdOut.println("mean = " + percolationStats.mean());
        StdOut.println("stddev = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                               + percolationStats.confidenceHi() + "]");
    }
}
