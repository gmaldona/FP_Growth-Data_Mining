import java.util.ArrayList;
import java.util.HashMap;

// Node class for the Trie Data Structure
public class Node {

    // References to all the children nodes
    private ArrayList<Node> children;
    private boolean isLeaf;
    private String value;
    private int count;
    private Node next = null;
    private Node parent;
    private boolean isReferenced = false;

    

    // Constructor to set up the node
    public Node(String value) { 
        this.value = value;
        this.children = new ArrayList<>();
        count = 0;
    }

    public void add(Node node) {
        this.children.add(node); 
    }

    // Method that adds a set to the trie
    public void add(ArrayList<String> transaction) {
        
        ArrayList<String> trans = new ArrayList<>(transaction);
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
                // Adds this to child's parent
                child.parent = this;
                //Leaf node
                this.isLeaf = false;
                child.isLeaf = true;
                // Increase the frequecy count
                child.increaseCount();
                // Recursively add the rest of items in the transaction, starting with the child node
                child.add(trans);
                //Adds the node to the header table
                //child.addPointer();
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
                        //Adds the node to the header table
                        //children.get(i).addPointer();
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
                        // Adds this to child's parent
                        child.parent = this;
                        this.isLeaf = false; 
                        child.isLeaf = true;
                        // Increase the frequency count for the new node
                        child.increaseCount();
                        // Recursively add the rest of items in the transaction, starting with the child node
                        child.add(trans);
                        //Adds the node to the header table
                        //child.addPointer();
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
                    //Adds the node to the header table
                    //children.get(i).addPointer();
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
                    // Adds this to child's parent
                    this.isLeaf = false;
                    child.isLeaf = true;
                    child.parent = this;
                    // Recursively add the rest of items
                    child.add(trans);
                    //Adds the node to the header table
                    //child.addPointer();
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
                // Adds this to child's parent
                child.parent = this;
                this.isLeaf = false;
                child.isLeaf = true;
                // increase the frequency
                child.increaseCount();
                // Recursively add the rest of the items
                child.add(trans);
                //Adds the node to the header table
                //child.addPointer();
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
    
    // Gets the set of nodes with a certain set of items
    public ArrayList<Node> getNodeSet(ArrayList<String> items) {

        ArrayList<Node> nodes = new ArrayList<>();
        Node node;

        for (String item : items) {
            if (nodes.size() == 0) {
                node = this.nextPointerNode(item);
                nodes.add(node);
            }
            else {  
                node = nodes.get(nodes.size() - 1).nextPointerNode(item);
                nodes.add(node); 
            }
        }

        if (items.size() == nodes.size() ) {
            return nodes;
        }
        else { 
            return null;
        }

    }

    public ArrayList<Node> getPath(Node node) { 

        ArrayList<Node> nodes = new ArrayList<>();
        Node pointer = node;

        while (pointer.parent != null ) {
            nodes.add(pointer.parent); 
            pointer = pointer.parent; 
        }

        return nodes;
    }

    public Node getParent() { return this.parent; } 

    public Node getParent(Node node) { return node.parent; }

    public void setParent(Node node) { this.parent = node; } 

    // Returns the node with next pointer equal to null
    private Node nextPointerNode(String item) {

        for (Node child : this.children) {
            if (child.value.equals(item)) {
                return child;
            }
        }
        return null;
    }

    public void setNext(Node node) { 
        if (node.isReferenced == false) {
            if (this != node) { 
                this.next = node; 
                node.isReferenced = true;
                this.isReferenced = true; 
            }
        } 
    }

    public Node getNext() { return this.next; }

    public String value() { return value; }

    public Node find(String item) {
        if (this.value.equals(item)) {
            return this;
        }
        else {
            if (this.parent != null) {
                return this.parent.find(item);
            }
        }
        return null;
    }
}
