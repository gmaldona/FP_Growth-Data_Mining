import java.util.ArrayList;

// Class for the data structure TRIE
public class Trie {

    // Root node
    Node root;

    // Sets up the trie, starting with the root node being null
    public Trie() {
        root = new Node(null); 
        root.isLeaf(true);
    }

    //Inserting method for new nodes
    public void insertSet(ArrayList<String> transaction) {
        this.root.add(transaction);
    }

}