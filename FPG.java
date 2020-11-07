import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Collection;

/*
The FPG class contains the algorithm for the Frequent Pattern Growth data mining
*/

public class FPG extends Miner {
    
    // Contains the entire Trie for all of the transactions
    Trie fpTree = new Trie();
    // Contains each FP Trie for each item
    HashMap<String, Trie> conditionalFPTree = new HashMap<>();
    // The linked list for all the leaf nodes in the entire Trie
    HashMap<String, LinkedList<Node>> conditionalFPTreeLeaves = new HashMap<>(); 
    // Contains all the transactions processed
    ArrayList<ArrayList<String>> transactions;
    // Contains a list of all unique items in all of the transactions
    ArrayList<String> uniqueItems;
    // Contains the frequency of all items in the transactions
    HashMap<Integer, ArrayList<String>> frequencyTable;
    // Contains a list of only the frequent items. SupportCOunt > minimum
    String[] frequentItems;
    // Reversed list of frequentItems
    String[] reversedFrequentItems; 

    ArrayList<Object[]> GeneratedPatterns = new ArrayList<>(); 

    ArrayList<ArrayList<String>> orderedItemSet = new ArrayList<>();
    // Contains the head node for each linked list for all the nodes in the entire Trie
    HashMap<String, Node> headerTable = new HashMap<>();

    ArrayList<ArrayList<String>> frequentItemsets = new ArrayList<>();
    //HashMap<String, ArrayList<Object[]>> frequentItemsets = new HashMap<>();
    

    public FPG(ArrayList<ArrayList<String>> transactions, ArrayList<String> uniqueItems) {
        this.transactions = transactions;
        this.uniqueItems = uniqueItems;
        this.frequencyTable = new HashMap<>();
    }

    public void start() {
        getItemSupportCount();
        getFrequentItems();
        getReversedFrequentItems();
        getOrderItemSet();
        constructTree();
        getConditionalFPTree();
        generateFrequentPattern();
        getAssociateRules();
        
    }   
    
    /**
    * <h1> Support Count Method </h1>
    *
    * This method calculates the support count for each unique item
    *
    * This method enters support count values for each items into a HashMap frequencyTable
    * At the end, a fully completed frequency table shall stored
    **/
    private void getItemSupportCount() {
        
        // Loops through all the items in unique items
        for (String item : uniqueItems) {
            // Gets the support count for each item
            int supportCount = calculateSupportCount(item, transactions);
            // If the support count is greater than the minimum
            if (supportCount >= this.minSupportCount) {
                // If that key is not already stored in the frequency table
                if (!frequencyTable.containsKey(supportCount)) {
                    // Initialize the table with the key of the support count
                    frequencyTable.put(supportCount, new ArrayList<String>());
                }
                // Add the item to the frequency table with the key being it's support count
                frequencyTable.get(supportCount).add(item);
                // Sort the items 
                Collections.sort(frequencyTable.get(supportCount));
            }

        }

    }
    
    /**
    * <h1> Get Frequent Items Method </h1>
    * 
    * This method only stores the frequent items found in all the transactions
    * At the end, an array of just the frequent items shall be stored
    **/
    private void getFrequentItems() {

        ArrayList<String> items;
        ArrayList<String> sortedItems = new ArrayList<>();
        HashMap<Integer, ArrayList<String>> fTable = new HashMap<>(frequencyTable);

        int largest = 0; 

        // Sorts through all the items stored in the frequncy table
        for (int i = 0; i < frequencyTable.size(); i++) {
            // Loops through all the keys stored in the table
            for (Integer key : fTable.keySet()) {
                // if a value is found to have a larger value, then that value is stored
                if (key > largest) largest = key;
            } 
            // Gets the items with the largest support count
            items = frequencyTable.get(largest); 
            // For each item in items, add the items to a list
            for (String item : items) sortedItems.add(item);
            // Remove that key from the frequency table
            fTable.remove(largest);
            // Reset the largest value, and start over again
            largest = 0;
        } 

        int index = 0;
        // New array to store all the frequent items only
        frequentItems = new String[sortedItems.size()];
        // Loops through all items and stores them in the array frequentItems
        for (String item : sortedItems) {
            frequentItems[index] = item; 
            index ++; 
        }
    }   

    /**
     * <h1> Get Reversed Frequent Items Method </h1>
     * 
     * Method that gets a reversed list of the frequent items
     * At the end, the method shall store an array of the frequent items list but reversed
     *
     **/
    private void getReversedFrequentItems() {
        
        ArrayList<String> reversedList = new ArrayList<>(); 
        reversedFrequentItems = new String[frequentItems.length];

        // Loops backwards the frequentItems list
        for (int i = frequentItems.length - 1; i >= 0; i-- ) {
            // Stores each value into reversed list
            reversedList.add(frequentItems[i]);
        }

        int index = 0;   
        // Stores each items reversed into the reversedFrequentItems Array
        for (String item : reversedList) {
            reversedFrequentItems[index] = item;
            index ++;
        }

    }
    
    /**
     * <h1> Get ordered item set method </h1>
     * 
     * Method that extracts only the frequent items from each transaction
     * At the end, an ArrayList is generated that contains the sets of frequent items from all transactions
     * 
     **/
    private void getOrderItemSet() {

        //List<String> list = Arrays.asList(frequentItems);

        // // Loops through each transaction
        // for (ArrayList<String> receipt : transactions) {
        //     ArrayList<String> set = new ArrayList<String>();
        //     // Loops through all the items in the transaction
        //     for (String item : receipt) {
        //         // If an item is found that is also found to be frequent, then that item is stored
        //         if (list.contains(item)) set.add(item);
        //     }
        //     // Adds the set to the entire arraylist
        //     orderedItemSet.add(set);
        // }

        for (ArrayList<String> receipt : transactions) {
            ArrayList<String> set = new ArrayList<>();
            for (String item : frequentItems) {
                if (receipt.contains(item)) {
                    set.add(item);
                }
            }
            orderedItemSet.add(set);
        }

        // Loops through all the sets in the orderedItemset Arraylist
        for(int i = 0; i < orderedItemSet.size(); i++) {
            // If the set is found to be empty, then remove the set entirely
            if (orderedItemSet.get(i).size() == 0) {
                orderedItemSet.remove(orderedItemSet.get(i));
            }
        }

    }

    /**
     * <h1> Construct Tree Method </h1>
     * 
     * This method constructs the entire Trie for all the frequent items for each transaction
     * At the end, a fully constructed Trie for all the transactions shall be stored
     **/
    private void constructTree() {
        
        HashMap<String, ArrayList<Node>> tempTable = new HashMap<>();

        for (ArrayList<String> receipt : orderedItemSet) {
            fpTree.insertSet(receipt); 

            ArrayList<Node> nodes = fpTree.getNodeSet(receipt);
            
            for (Node node : nodes) {
                if (!tempTable.containsKey(node.value())) {
                    tempTable.put(node.value(), new ArrayList<>());
                }
                tempTable.get(node.value()).add(node);

            }

        }

        for (String key : tempTable.keySet()) {
            
            for (int i = 0; i < tempTable.get(key).size() - 1; i++) {
                Node currentNode = tempTable.get(key).get(i);
                Node nextNode = tempTable.get(key).get(i+1);
                
                currentNode.setNext(nextNode);
            }
        }

        for (String key : tempTable.keySet()) {
            Node head = tempTable.get(key).get(0);
            headerTable.put(key, head);
        }

    }

    // //TODO: 
    // private void getConditionalPatternBase() {
        
    //     for (String item : reversedFrequentItems) {

    //         ArrayList<Object[]> patterns = new ArrayList<>();
    //         Node node = headerTable.get(item);

    //         do {
    //             // pattern[0] = set
    //             // pattern[1] = count 
    //             Object[] pattern = new Object[2];

    //             ArrayList<String> set = new ArrayList<>();
    //             ArrayList<Node> nodePath = fpTree.getPath(node);
    //             nodePath.remove(nodePath.size() - 1);

    //             for (Node n : nodePath) {
    //                 if (n != null) {
    //                     set.add(n.value());
    //                 }
    //             }
    //             if (set != null) {
    //                 pattern[0] = set; 
    //             }
    //             pattern[1] = node.getCount();
                

    //             node = node.getNext();

    //             patterns.add(pattern);

    //         } while (node != null);

    //         //conditionalPatternBase.add(patterns);

    //     }

    // }
 
    public void getConditionalFPTree() {

        for (String item : reversedFrequentItems) { 
            Node node = null; 

            if (headerTable.get(item) != null) {
                node = headerTable.get(item);
            }

            do {

                if (!conditionalFPTree.containsKey(item)) {
                    conditionalFPTree.put(item, new Trie());
                    conditionalFPTreeLeaves.put(item, new LinkedList<>());
                }

                ArrayList<Node> nodePath = fpTree.getPath(node);
                ArrayList<String> path = new ArrayList<>();
                
                for (int i = 0; i < nodePath.size(); i++) {
                    if (nodePath.get(i).value() != null) {
                        path.add(nodePath.get(i).value());
                    }
                    
                    // if (i == 0) {
                    //     conditionalFPTreeLeaves.get(item).add(nodePath.get(i));
                    // }
                }   
                ArrayList<Node> nodeSet = new ArrayList<>();
                for (int j = 0; j < node.getCount(); j++) {
                    ArrayList<String> set = new ArrayList<>();
                    for (String i : frequentItems) {
                        if (path.contains(i)) {
                            set.add(i);
                        }
                    } 
                    
                    conditionalFPTree.get(item).insertSet(set);
                    nodeSet = conditionalFPTree.get(item).getNodeSet(set);
                }
                 
                // boolean foundleaf = false; 
                for (String i : reversedFrequentItems) {
                //     if (!foundleaf) {
                        for (Node n : nodeSet) {
                            if (n.value().equals(i)) {
                                if (n.isLeaf()) {
                                    conditionalFPTreeLeaves.get(item).add(n);
                //                 foundleaf = true;
                                    break;
                                }
                            }
                        }
                //     }
                //     else {
                //         break;
                //     }
                 }



                node = node.getNext();

            } while (node != null) ;

            
        }
        
        int index = 0; 
        for (String item : reversedFrequentItems) {
            //HashMap<String, LinkedList<Node>> leaves = (HashMap) conditionalFPTreeLeaves.clone();
            //HashMap<String, LinkedList<Node>> leaves = new HashMap<>(conditionalFPTreeLeaves);
            LinkedList<Node> leaves = new LinkedList<>(conditionalFPTreeLeaves.get(item));
            if (leaves.size() == 0) {
                break;
            }
            Node node = leaves.getFirst(); 

            /*
            do {
                
                ArrayList<Node> nodePath = conditionalFPTree.get(item).getPath(node); 
                ArrayList<String> stringPath = new ArrayList<>();
                nodePath.add(0, node);
                for (Node n : nodePath) {
                    stringPath.add(n.value());
                }
                
                System.out.println();
                for (Node n : nodePath) {
                    if (n.value() != null) { 
                        if (n.getCount() < minSupportCount) {
                            if (n.getChildren().size() > 0) {
                                for (Node child : n.getChildren()) {
                                    if (n.getParent() != null) {
                                        child.setParent(n.getParent());
                                        n.getParent().add(child);
                                        n.getParent().getChildren().remove(n); 
                                    }
                                }
                            }
                            else {
                                n.getParent().getChildren().remove(n);
                                conditionalFPTreeLeaves.get(item).remove(index);
                                if (n.getParent().getChildren().size() == 0) {
                                    n.getParent().isLeaf(true);
                                    n.isLeaf(false);
                                    conditionalFPTreeLeaves.get(item).add(index, n.getParent());
                                    System.out.println();
                                }
                                else {
                                    n.isLeaf(false);
                                }
                            }   
                        }
                    }
                }
                // boolean found = false; 
                // for (String i : reversedFrequentItems) {
                //     if (!found){
                //         for (Node n : nodePath) {
                //             if (n.value() != null) {
                //                 if (n.value().equals(i) && n.isLeaf()) {
                //                     conditionalFPTreeLeaves.get(item).remove(index); 
                //                     conditionalFPTreeLeaves.get(item).add(index, n);
                //                     found = true;
                //                     break; 
                //                 }
                //             }
                //         }
                //     }
                // }
                // index ++; 

                if (leaves.size() > 0) {
                    leaves.remove(0);
                    if (leaves.size() > 0) {
                        node = leaves.getFirst();
                    }
                    else {
                        node = null;
                    }
                }
                else {
                    node = null;
                }

                if (node == null) {
                    index = 0;
                }

            } while (node != null); 
            */
                
        }

    }

    public void generateFrequentPattern() {


        for (String key : conditionalFPTreeLeaves.keySet()) {

            Trie fp = conditionalFPTree.get(key);
            if (conditionalFPTreeLeaves.get(key).size() == 0) {
                continue;
            }
            Node node = conditionalFPTreeLeaves.get(key).getFirst();

            //ArrayList<Object[]> patterns = new ArrayList<>();
            //ArrayList<String> patterns = 
            ArrayList<ArrayList<String>> subsets = new ArrayList<>();
            do {
                ArrayList<Node> nodePath = fp.getPath(node);
                nodePath.add(0, node); 
                ArrayList<String> stringPath = new ArrayList<>();
                for (Node n : nodePath) {
                    if (n.value() != null) stringPath.add(n.value());
                }
                for (int i = 0; i < (1<<stringPath.size()); i++) {
                    int m = 1;
                    ArrayList<String> set = new ArrayList<>();
                    for (int j = 0; j < stringPath.size(); j++) {
                        if ((i & m) > 0)  {
                            set.add(stringPath.get(j));
                            Collections.sort(set);
                        }
                        m = m << 1;
                        if (set.size() > 0) {
                            if (subsets.size() == 0) {
                                subsets.add(set);
                                Collections.sort(set); 
                            }
                            else {
                                Collections.sort(set);
                                boolean similar = false; 
                                for (ArrayList<String> s : subsets) {
                                    s.remove(key);
                                    if(s.equals(set)) {
                                       similar = true; 
                                    }
                                    s.add(key);
                                }
                                if (!similar) {
                                    subsets.add(set);
                                }
                            }
                        }
                    }
                }

                // // Adding the key to each subset
                for (ArrayList<String> subset : subsets) {
                    if (!subset.contains(key)) {
                        subset.add(key); 
                    }
                }

            

                if (conditionalFPTreeLeaves.get(key).size() > 0) {
                    conditionalFPTreeLeaves.get(key).remove(0);
                    if (conditionalFPTreeLeaves.get(key).size() > 0) {
                        node = conditionalFPTreeLeaves.get(key).getFirst();
                    }
                    else {
                        node = null;
                    }
                }
                else {
                    node = null;
                }

            } while (node != null);

            for (ArrayList<String> set : subsets) {
                frequentItemsets.add(set);
            }



        }
    

    }

    public void getAssociateRules() {

        int maxSize = 0;
        ArrayList<ArrayList<String>> sets = new ArrayList<>(frequentItemsets);
        
        for (ArrayList<String> set : sets) {
            if (set.size() > maxSize) maxSize = set.size();
        }

        for (int i = 0; i < frequentItemsets.size(); i++) {
            
            if (frequentItemsets.get(i).size() == maxSize) {
                continue;
            }

            for (int j = 0; j < frequentItemsets.size(); j++) { 

                if (j == i) { continue; }

                if (frequentItemsets.get(j).containsAll(frequentItemsets.get(i))) {
                    sets.remove(frequentItemsets.get(i));
                    break; 
                }

            }

        }

        for (ArrayList<String> set : sets) {
            
            for (int n = 1; n < set.size(); n++) {
                if (n == 1) {
                    for (int i = 0; i < set.size() - 1; i++) {
                        for (int j = i + 1; j < set.size(); j++) {
                            ArrayList<String> A = new ArrayList<>(Arrays.asList(set.get(i))); 
                            ArrayList<String> B = new ArrayList<>(Arrays.asList(set.get(j)));
                            
                            int count = calculateCount(A, B, transactions);
                            double support = calculateSupport(A, B, transactions);
                            double confidence = calculateConfidence(A, B, transactions);
                            
                            if (support > minSupport) {
                                if (confidence > minConfidence) {
                                    System.out.println(A + "->" + B);
                                    System.out.println("Count: " + count + " | Support: " + support + " | Confidence: " + confidence);
                                    System.out.println();
                                }
                            }

                            count = calculateCount(B, A, transactions);
                            support = calculateSupport(B, A, transactions);
                            confidence = calculateConfidence(B, A, transactions);

                            if (support > minSupport) {
                                if (confidence > minConfidence) { 
                                    System.out.println(B + "->" + A);
                                    System.out.println("Count: " + count + " | Support: " + support + " | Confidence: " + confidence);
                                    System.out.println();
                                }
                            }

                        }
                    }
                }
                else if (n == 2) {
                    for (int i = 0; i < set.size() - 1; i++) {
                        for (int j = i + 1; j < set.size(); j++) {
                            for (int z = 0; z < set.size(); z++) {
                                if (z == i || z == j) { continue; }
                                //System.out.println(set.get(i) + ", " + set.get(j) + "->" + set.get(z));

                                ArrayList<String> A = new ArrayList<>(Arrays.asList(set.get(i), set.get(j)));
                                ArrayList<String> B = new ArrayList<>(Arrays.asList(set.get(z)));

                                int count = calculateCount(A, B, transactions);
                                double support = calculateSupport(A, B, transactions);
                                double confidence = calculateConfidence(A, B, transactions);

                                if (support > minSupport) {
                                    if (confidence > minConfidence) {
                                        System.out.println(A + "->" + B);
                                        System.out.println("Count: " + count + " | Support: " + support + " | Confidence: " + confidence);
                                        System.out.println();
                                    }
                                }

                                count = calculateCount(B, A, transactions);
                                support = calculateSupport(B, A, transactions);
                                confidence = calculateConfidence(B, A, transactions);
                                
                                if (support > minSupport) {
                                    if (confidence > minConfidence) { 
                                        System.out.println(B + "->" + A);
                                        System.out.println("Count: " + count + " | Support: " + support + " | Confidence: " + confidence);
                                        System.out.println();
                                    }
                                }

                            }
                        }
                    }

                }
                else if (n == set.size() - 1) {
                    for (int i = 0; i < set.size(); i++) {
                        ArrayList<String> A = new ArrayList<>(set);
                        A.remove(set.get(i));
                        //System.out.println(temp + "->" + set.get(i));
                        ArrayList<String> B = new ArrayList<>(Arrays.asList(set.get(i))); 
                        
                        int count = calculateCount(A, B, transactions);
                        double support = calculateSupport(A, B, transactions);
                        double confidence = calculateConfidence(A, B, transactions);

                        if (support > minSupport) {
                            if (confidence > minConfidence) {
                                System.out.println(A + "->" + B);
                                System.out.println("Count: " + count + " | Support: " + support + " | Confidence: " + confidence);
                                System.out.println();
                            }
                        }

                        count = calculateCount(B, A, transactions);
                        support = calculateSupport(B, A, transactions);
                        confidence = calculateConfidence(B, A, transactions);

                        if (support > minSupport) {
                            if (confidence > minConfidence) { 
                                System.out.println(B + "->" + A);
                                System.out.println("Count: " + count + " | Support: " + support + " | Confidence: " + confidence);
                                System.out.println();
                            }
                        }
                    }
                }
            }

        }


    }

    public HashMap<String, Node> getHeaderTable() { return this.headerTable; }


}
