import java.util.ArrayList;

// Node class for the Trie Data Structure
public class Node {

    // References to all the children nodes
    private ArrayList<Node> children;
    private boolean isLeaf;
    private String value;
    private int count;

    // Constructor to set up the node
    public Node(String value) {
        this.value = value;
        this.children = new ArrayList<>();
        count = 0;
    }

    // Method that adds a set to the trie
    public void add(ArrayList<String> transaction) {
        
        ArrayList<String> trans = transaction;
        String item = null;

        // If the transaction list is greater than 0, then pull the first item in the list
        if (trans.size() > 0) { item = trans.get(0); }
        //If the transaction list is empty then return nothing
        else { return; }
        
        //If the first node is the root node, null
        if (this.value == null) {

            // If the root node doesn't have any children
            if (this.children.size() == 0) {
                // Make the first child for the root node
                Node child = new Node(item);
                // Remove the item from the transaction
                trans.remove(item);
                // Add the new node to the list of children nodes
                this.children.add(child);
                // Increase the frequecy count
                child.increaseCount();
                // Recursively add the rest of items in the transaction, starting with the child node
                child.add(trans);
            }
            
            // If the root node does have children
            else {
                // The variable found is a bool that tells if the value of a node is already there
                boolean found = false;
                // Loops through all the children nodes
                for (int i = 0; i < children.size(); i++) {
                    // Checks if a node with the same value exist already 
                    if (children.get(i).getValue().equals(item))  { 
                        // If true, then that child node's frequency count is increased by one
                        children.get(i).increaseCount();
                        // Remove the item from the transaction
                        trans.remove(item); 
                        // Recursively add the rest of items in the transaction, starting with the child node
                        children.get(i).add(trans);
                        // found is now true
                        found = true;
                        // break out of the loop so items dont get counted twice
                        break;
                    }
                    // If a node with this value doesn't already exist
                    if (i == children.size() - 1 && found == false) {
                        // Make a new node containing that value
                        Node child = new Node(item);
                        // Remove the item from the transaction
                        trans.remove(item);
                        // Add the new child node to the list of all children nodes
                        this.children.add(child);
                        // Increase the frequency count for the new node
                        child.increaseCount();
                        // Recursively add the rest of items in the transaction, starting with the child node
                        child.add(trans);
                        // break out the loop so items dont get counted twice
                        break; 
                    }
                }
            }
        }
        
        // If the starting node is not the root node (not null)
        else {
            // The variable found is a bool that tells if the value of a node is already there 
            boolean found = false;
            // Loops through all the children of this node
            for (int i = 0; i < children.size(); i++) {
                // Tries to find a node that already exist with this value
                if (children.get(i).getValue().equals(item))  {
                    // If true, increase the frequency count by one
                    children.get(i).increaseCount();
                    // remove the item from the transaction
                    trans.remove(item); 
                    // Node is found
                    found = true;
                    // Recursively add the rest of items in the transaction, starting with the child node
                    children.get(i).add(trans);
                    // break out the loop so items dont get counted twice
                    break;
                }
                // If there is no existing node with the same value
                if (i == children.size() - 1 && found == false) {
                    // Make a new node with that value
                    Node child = new Node(item);
                    // Remove the item from the transaction
                    trans.remove(item);
                    // Add the new node to the list of children nodes 
                    this.children.add(child);
                    // Increase the frequency count
                    child.increaseCount(); 
                    // Recursively add the rest of items
                    child.add(trans);
                    // Break out so items dont get counted twice
                    break;
                }
            }
            // If the node doesnt have any children nodes
            if (children.size() == 0) {
                // Make a new child node
                Node child = new Node(item);
                // Remove the item from the transaction
                trans.remove(item);
                // Add the child to a list of children
                this.children.add(child);
                // increase the frequency
                child.increaseCount();
                // Recursively add the rest of the items
                child.add(trans);
            }
        }

    }

    // Method that returns a bool if the node is a leaf
    public void isLeaf(boolean bool) { this.isLeaf = bool; }

    //Changes the node to a leaf or not a leaf
    public boolean isLeaf() { return this.isLeaf; }

    // Method that gets the value of the node
    public String getValue() { return this.value; }

    // Method that gets the children of the node
    public ArrayList<Node> getChildren() { return this.children; }

    //Method that increases the frequency count of the node
    public void increaseCount() { this.count ++; }

    // Method that returns the count of the node
    public int getCount() { return this.count; }
    
}
