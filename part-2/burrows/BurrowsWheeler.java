import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String in = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(in);

        for (int i = 0; i < in.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        for (int i = 0; i < in.length(); i++) {
            int index = circularSuffixArray.index(i) - 1;
            if (index < 0) {
                index = in.length() - 1;
            }

            BinaryStdOut.write(in.charAt(index));
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int[] next = new int[s.length()];

        int N = s.length();
        int R = 256;
        char[] sortedIn = new char[N];
        int[] count = new int[R + 1];

        for (int i = 0; i < N; i++)
            count[s.charAt(i) + 1]++;

        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];

        for (int i = 0; i < N; i++) {
            sortedIn[count[s.charAt(i)]] = s.charAt(i);
            next[count[s.charAt(i)]++] = i;
        }

        int current = first;
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(sortedIn[current]);
            current = next[current];
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
