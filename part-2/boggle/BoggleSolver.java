import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashSet;

public class BoggleSolver {
    private final Trie dictionary = new Trie();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (int i = 0; i < dictionary.length; i++) {
            this.dictionary.add(dictionary[i]);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> words = new HashSet<>();
        int cols = board.cols();
        int rows = board.rows();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                collectWorlds("", words, board, i, j, new boolean[rows][cols]);
            }
        }

        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (this.dictionary.contains(word)) {
            switch (word.length()) {
                case 1:
                case 2:
                    return 0;
                case 3:
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;
            }
        } else {
            return 0;
        }
    }

    private void collectWorlds(String word, HashSet<String> words, BoggleBoard board, int row, int col, boolean[][] marked) {
        marked[row][col] = true;
        char letter = board.getLetter(row, col);

        if (letter == 'Q') {
            word += "QU";
        } else {
            word += letter;
        }

        int cols = board.cols();
        int rows = board.rows();

        if (word.length() > 2) {
            if (!this.dictionary.hasKeysWithPrefix(word)) {
                return;
            }

            if (this.dictionary.contains(word)) {
                words.add(word);
            }
        }

        // upper row
        if (row - 1 >= 0) {
            if (!marked[row - 1][col]) {
                collectWorlds(word, words, board, row - 1, col, copyMarked(marked, rows, cols));
            }

            if (col - 1 >= 0 && !marked[row - 1][col - 1]) {
                collectWorlds(word, words, board, row - 1, col - 1, copyMarked(marked, rows, cols));
            }

            if (col + 1 < cols && !marked[row - 1][col + 1]) {
                collectWorlds(word, words, board, row - 1, col + 1, copyMarked(marked, rows, cols));
            }
        }

        // lower row
        if (row + 1 < rows) {
            if (!marked[row + 1][col]) {
                collectWorlds(word, words, board, row + 1, col, copyMarked(marked, rows, cols));
            }

            if (col - 1 >= 0 && !marked[row + 1][col - 1]) {
                collectWorlds(word, words, board, row + 1, col - 1, copyMarked(marked, rows, cols));
            }

            if (col + 1 < cols && !marked[row + 1][col + 1]) {
                collectWorlds(word, words, board, row + 1, col + 1, copyMarked(marked, rows, cols));
            }
        }

        // same row
        if (col - 1 >= 0 && !marked[row][col - 1]) {
            collectWorlds(word, words, board, row, col - 1, copyMarked(marked, rows, cols));
        }

        if (col + 1 < cols && !marked[row][col + 1]) {
            collectWorlds(word, words, board, row, col + 1, copyMarked(marked, rows, cols));
        }
    }

    private boolean[][] copyMarked(boolean[][] marked, int rows, int cols) {
        boolean[][] copy = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            copy[i] = Arrays.copyOf(marked[i], cols);
        }

        return copy;
    }

    public static void main(String[] args) {
        In in = new In("dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-q.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
