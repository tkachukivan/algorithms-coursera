import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] tilesArray;
    private final int n;
    private final int hamming;
    private final int manhattan;
    private int blankX;
    private int blankY;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        tilesArray = new int[n][n];
        int h = 0;
        int m = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tilesArray[i][j] = tiles[i][j];

                if (tiles[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                    continue;
                }

                int numberAtPosition = n * i + j + 1;

                if (numberAtPosition != tilesArray[i][j]) {
                    h++;
                    int targetX = (tiles[i][j] - 1) / n;
                    int targetY = (tiles[i][j] - 1) % n;
                    m += Math.abs(i - targetX) + Math.abs(j - targetY);
                }
            }
        }

        hamming = h;
        manhattan = m;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tilesArray[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;

        return Arrays.deepEquals(this.tilesArray, that.tilesArray);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> result = new ArrayList<>();
        if (blankX - 1 >= 0) {
            int[][] tilesCopy = getTilesArrayCopy();

            tilesCopy[blankX][blankY] = tilesCopy[blankX - 1][blankY];
            tilesCopy[blankX - 1][blankY] = 0;
            result.add(new Board(tilesCopy));
        }

        if (blankX + 1 < n) {
            int[][] tilesCopy = getTilesArrayCopy();

            tilesCopy[blankX][blankY] = tilesCopy[blankX + 1][blankY];
            tilesCopy[blankX + 1][blankY] = 0;
            result.add(new Board(tilesCopy));
        }

        if (blankY - 1 >= 0) {
            int[][] tilesCopy = getTilesArrayCopy();

            tilesCopy[blankX][blankY] = tilesCopy[blankX][blankY - 1];
            tilesCopy[blankX][blankY - 1] = 0;
            result.add(new Board(tilesCopy));
        }

        if (blankY + 1 < n) {
            int[][] tilesCopy = getTilesArrayCopy();

            tilesCopy[blankX][blankY] = tilesCopy[blankX][blankY + 1];
            tilesCopy[blankX][blankY + 1] = 0;
            result.add(new Board(tilesCopy));
        }

        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] tilesCopy = getTilesArrayCopy();

        if (blankX == 0 && blankY == 0) {
            int temp = tilesCopy[1][1];
            tilesCopy[1][1] = tilesCopy[0][1];
            tilesCopy[0][1] = temp;
        } else {
            if (blankX == 0 && blankY == 1) {
                int temp = tilesCopy[1][0];
                tilesCopy[1][0] = tilesCopy[0][0];
                tilesCopy[0][0] = temp;
            } else {
                int temp = tilesCopy[0][1];
                tilesCopy[0][1] = tilesCopy[0][0];
                tilesCopy[0][0] = temp;
            }
        }

        return new Board(tilesCopy);
    }

    private int[][] getTilesArrayCopy() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = tilesArray[i][j];
            }
        }
        return copy;
    }

    public static void main(String[] args) {
        In in = new In("puzzle2x2-unsolvable1.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // solve the slider puzzle
        Board initial = new Board(tiles);
        initial.neighbors();
    }
}
