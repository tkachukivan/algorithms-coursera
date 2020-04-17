import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int[] chars = getCharsArray();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int charIndex = 0;
            while (chars[charIndex] != c) {
                charIndex++;
            }

            BinaryStdOut.write(charIndex, 8);

            moveFront(chars, charIndex);
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int[] chars = getCharsArray();

        while (!BinaryStdIn.isEmpty()) {
            int c = BinaryStdIn.readChar();

            BinaryStdOut.write(chars[c], 8);

            moveFront(chars, c);
        }

        BinaryStdOut.close();
    }

    private static int[] getCharsArray() {
        int[] chars = new int[R];

        for (int i = 0; i < R; i++) {
            chars[i] = i;
        }

        return chars;
    }

    private static void moveFront(int[] chars, int index) {
        int temp = chars[index];
        for (int i = index; i > 0; i--) {
            chars[i] = chars[i - 1];
        }
        chars[0] = temp;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
