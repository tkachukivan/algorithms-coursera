import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final boolean isBoardSolvable;
    private final int moves;
    private final Stack<Board> solutionBoards;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        Board twin = initial.twin();
        boolean solvable = false;
        MinPQ<Node> initionalBoardPQ = new MinPQ<>();
        MinPQ<Node> twinBoardPQ = new MinPQ<>();
        initionalBoardPQ.insert(new Node(initial, null, 0));
        twinBoardPQ.insert(new Node(twin, null, 0));

        Node current = null;
        Node currentTwin = null;

        while (!initionalBoardPQ.isEmpty()) {
            current = initionalBoardPQ.delMin();
            currentTwin = twinBoardPQ.delMin();

            if (currentTwin.board.isGoal()) break;

            if (current.board.isGoal()) {
                solvable = true;
                break;
            }


            for (Board neighbor: current.board.neighbors()) {
                if (current.prev != null && neighbor.equals(current.prev.board)) continue;
                initionalBoardPQ.insert(new Node(neighbor, current, current.moves + 1));
            }

            for (Board neighbor: currentTwin.board.neighbors()) {
                if (currentTwin.prev != null && neighbor.equals(currentTwin.prev.board)) continue;
                twinBoardPQ.insert(new Node(neighbor, currentTwin, currentTwin.moves + 1));
            }
        }

        if (solvable) {
            isBoardSolvable = true;
            moves = current.moves;
            solutionBoards = new Stack<>();
            collectSolutionBoards(current);
        } else {
            moves = -1;
            isBoardSolvable = false;
            solutionBoards = null;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isBoardSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solutionBoards;
    }

    private class Node implements Comparable<Node> {
        public final Board board;
        public final Node prev;
        public final int moves;
        public final int priorityM;

        public Node(Board board, Node prev, int moves) {
            this.board = board;
            this.moves = moves;
            this.priorityM = board.manhattan() + moves;
            this.prev = prev;
        }

        public int compareTo(Node node) {
            return priorityM - node.priorityM;
        }
    }

    private void collectSolutionBoards(Node node) {
        Node current = node;
        while (current != null) {
            solutionBoards.push(current.board);
            current = current.prev;
        }
    }

    // test client (see below) 
    public static void main(String[] args) {
        // create initial board from file
        In in = new In("puzzle4x4-15.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}