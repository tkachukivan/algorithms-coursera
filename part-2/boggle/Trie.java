public class Trie {
    private static final int R = 26;
    private Node root;      // root of trie

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    /**
     * Initializes an empty set of strings.
     */
    public Trie() {
    }

    /**
     * Does the set contain the given key?
     * @param key the key
     * @return {@code true} if the set contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(key);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(String key) {
        int i = 0;
        Node currentNode = root;

        while (currentNode != null) {
            if (i == key.length()) {
                return currentNode;
            }
            int c = key.charAt(i) - 65;
            i++;
            currentNode = currentNode.next[c];
        }

        return null;
    }

    /**
     * Adds the key to the set if it is not already present.
     * @param key the key to add
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.isString = true;
        }
        else {
            int c = key.charAt(d) - 65;
            x.next[c] = add(x.next[c], key, d+1);
        }
        return x;
    }

    /**
     * Returns if dictionary has keys with prefix {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     */
    public boolean hasKeysWithPrefix(String prefix) {
        Node x = get(prefix);

        return x != null;
    }
}
