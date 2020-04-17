import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.function.Consumer;

public class SAP {
    private final Digraph dg;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("No digraph provided");
        }
        dg = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Node n = find(v, w);

        return n.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Node n = find(v, w);

        return n.ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return find(v, w).length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return find(v, w).ancestor;
    }

    private class Node {
        public int length;
        public int ancestor;

        public Node(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }
    }

    private Node find(int v, int w) {
        if (v >= dg.V() || v < 0 || w < 0 || w >= dg.V()) {
            throw new IllegalArgumentException("not in range");
        }

        if (v == w) {
            return new Node(0, w);
        }

        Queue<Integer> queue = new Queue<Integer>();
        boolean[] markedV = new boolean[dg.V()];
        boolean[] markedW = new boolean[dg.V()];
        int[] distToV = new int[dg.V()];
        int[] distToW = new int[dg.V()];

        queue.enqueue(v);
        markedV[v] = true;
        distToV[v] = 0;
        while (!queue.isEmpty())
        {
            int k = queue.dequeue();

            for (int j : dg.adj(k)) {
                if (!markedV[j]) {
                    distToV[j] = distToV[k] + 1;
                    markedV[j] = true;
                    queue.enqueue(j);
                }
            }
        }

        queue.enqueue(w);
        distToW[w] = 0;
        markedW[w] = true;
        int shortestPath = Integer.MAX_VALUE;
        int ancestor = -1;

        while (!queue.isEmpty())
        {
            int k = queue.dequeue();

            if (markedV[k]) {
                int dist = distToV[k] + distToW[k];
                if (dist < shortestPath) {
                    shortestPath = dist;
                    ancestor = k;
                }
            }

            for (int j : dg.adj(k)) {
                if (!markedW[j]) {
                    distToW[j] = distToW[k] + 1;
                    markedW[j] = true;

                    queue.enqueue(j);
                }

                if (markedV[j]) {
                    int dist = distToV[j] + distToW[j];
                    if (dist < shortestPath) {
                        shortestPath = dist;
                        ancestor = j;
                    }
                }
            }
        }

        return new Node(shortestPath == Integer.MAX_VALUE ? -1 : shortestPath, ancestor);
    }

    private Node find(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        v.forEach(new Consumer<Integer>() {
            public void accept(Integer integer) {
                checkNull(integer);
            }
        });

        w.forEach(new Consumer<Integer>() {
            public void accept(Integer integer) {
                checkNull(integer);
            }
        });

        int shortestPath = Integer.MAX_VALUE;
        int commonAncestor = -1;
        for (int i: v) {
            for (int j: w) {
                Node n = find(i, j);
                if (n.length != -1 && n.length < shortestPath) {
                    shortestPath = n.length;
                    commonAncestor = n.ancestor;
                }
            }
        }
        return new Node(shortestPath == Integer.MAX_VALUE ? -1 : shortestPath, commonAncestor);
    }

    private void checkNull(Integer i) {
        if (i == null) {
            throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("./digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
