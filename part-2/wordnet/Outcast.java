import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int distMax = Integer.MIN_VALUE;
        String result = "";

        for (String nounA : nouns) {
            int distSum = 0;

            for (String nounB : nouns) {
                distSum = distSum + wordNet.distance(nounA, nounB);
            }

            if (distMax < distSum) {
                distMax = distSum;
                result = nounA;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("./synsets.txt", "./hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        In in = new In("./outcast11.txt");
        String[] nouns = in.readAllStrings();
        StdOut.println(outcast.outcast(nouns));
    } // see test client below
}
