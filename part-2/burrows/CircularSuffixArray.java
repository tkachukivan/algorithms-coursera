import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final int len;
    private final String str;
    private final CircularSuffix[] circularSuffixes;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        str = s;
        len = s.length();

        circularSuffixes = new CircularSuffix[len];

        for (int i = 0; i < len; i++) {
            circularSuffixes[i] = new CircularSuffix(i);
        }

        Arrays.sort(circularSuffixes);
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > len - 1) {
            throw new IllegalArgumentException();
        }

        return circularSuffixes[i].suffixStart;
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        public int suffixStart;

        public CircularSuffix(int suffixStart) {
            this.suffixStart = suffixStart;
        }

        public int compareTo(CircularSuffix circularSuffix) {
            for (int i = 0; i < len; i++) {
                int currentIndex = this.suffixStart + i;
                int compareToIndex = circularSuffix.suffixStart + i;

                if (currentIndex >= len) {
                    currentIndex = currentIndex - len;
                }

                if (compareToIndex >= len) {
                    compareToIndex = compareToIndex - len;
                }

                char current = str.charAt(currentIndex);
                char compareTo = str.charAt(compareToIndex);

                if (current > compareTo) {
                    return 1;
                } else if (current < compareTo) {
                    return -1;
                }
            }

            return 0;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");

        StdOut.println(circularSuffixArray.length());
        StdOut.println(circularSuffixArray.index(0));
        StdOut.println(circularSuffixArray.index(1));
    }
}
