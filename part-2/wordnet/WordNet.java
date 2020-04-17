/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private final SAP sap;
    private final HashMap<String, ArrayList<Integer>> nounsMap = new HashMap<String, ArrayList<Integer>>();
    private final HashMap<Integer, String> synsetsMap = new HashMap<Integer, String>();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("no synsets or hypernyms provided");
        }
        int graphSize = 0;
        In synsetsIn = new In(synsets);
        while (!synsetsIn.isEmpty()) {
            String synsetsLine = synsetsIn.readLine();
            String[] synsetsLineArray = synsetsLine.split(",");
            int id = Integer.parseInt(synsetsLineArray[0]);

            synsetsMap.put(id, synsetsLineArray[1]);

            for (String name : synsetsLineArray[1].split(" ")) {
                if (nounsMap.containsKey(name)) {
                    nounsMap.get(name).add(id);
                } else {
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    list.add(id);
                    nounsMap.put(name, list);
                }

            }
            graphSize++;
        }

        Digraph dg = new Digraph(graphSize);

        In hypernymsIn = new In(hypernyms);

        while (!hypernymsIn.isEmpty()) {
            String hypernymsLine = hypernymsIn.readLine();
            String[] hypernymsLineArray = hypernymsLine.split(",");
            int id = Integer.parseInt(hypernymsLineArray[0]);

            for (int i = 1; i < hypernymsLineArray.length; i++) {
                dg.addEdge(id, Integer.parseInt(hypernymsLineArray[i]));
            }
        }

        Topological tp = new Topological(dg);

        if (!tp.hasOrder()) {
            throw new IllegalArgumentException("cycled Digraph");
        }

        int numberOfRoots = 0;

        for (int i = 0; i < dg.V(); i++) {
            if (dg.outdegree(i) == 0) {
                numberOfRoots++;
            }

            if (numberOfRoots > 1) {
                throw new IllegalArgumentException("More then one root");
            }
        }

        sap = new SAP(dg);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounsMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!nounsMap.containsKey(nounA) || !nounsMap.containsKey(nounB)) {
            throw new IllegalArgumentException();
        }
        ArrayList<Integer> root = nounsMap.get(nounA);
        ArrayList<Integer> wTarget = nounsMap.get(nounB);

        return sap.length(root, wTarget);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!nounsMap.containsKey(nounA) || !nounsMap.containsKey(nounB)) {
            throw new IllegalArgumentException();
        }

        ArrayList<Integer> root = nounsMap.get(nounA);
        ArrayList<Integer> wTarget = nounsMap.get(nounB);

        int ancestor = sap.ancestor(root, wTarget);

        return synsetsMap.get(ancestor);
    }

    public static void main(String[] args) {
        WordNet wn = new WordNet("./synsets15.txt", "./hypernyms15Path.txt");

        StdOut.print(wn.distance("a", "b"));
    }
}
