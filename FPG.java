import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class FPG extends Miner {
    
    Trie fpTree = new Trie();
    ArrayList<ArrayList<String>> transactions;
    ArrayList<String> uniqueItems;
    
    HashMap<Integer, ArrayList<String>> frequencyTable;
    String[] freqentItems;

    ArrayList<ArrayList<String>> orderedItemSet = new ArrayList<>();

    public FPG(ArrayList<ArrayList<String>> transactions, ArrayList<String> uniqueItems) {
        this.transactions = transactions;
        this.uniqueItems = uniqueItems;
        this.frequencyTable = new HashMap<>();
    }

    public void start() {
        getItemSupportCount();
        getFrequentItems();
        getOrderItemSet();
        constructTree();
        System.out.println();
    }

    private void getItemSupportCount() {

        for (String item : uniqueItems) {

            int supportCount = calculateSupport(item, transactions);
            
            if (supportCount >= this.minSupport) {
                if (!frequencyTable.containsKey(supportCount)) {
                    frequencyTable.put(supportCount, new ArrayList<String>());
                }
                frequencyTable.get(supportCount).add(item);
                Collections.sort(frequencyTable.get(supportCount));
            }

        }

    }

    private void getFrequentItems() {
        ArrayList<String> items;
        freqentItems = new String[frequencyTable.size()]; 
        HashMap<Integer, ArrayList<String>> fTable = new HashMap<>(frequencyTable);

        int largest = 0;
        int index = 0; 

        for (int i = 0; i < frequencyTable.size(); i++) {
            for (Integer key : fTable.keySet()) {
                if (key > largest) largest = key;
            } 
            items = frequencyTable.get(largest); 
            for (String item : items) freqentItems[index] = item;
            index ++ ;
            fTable.remove(largest);
            largest = 0;
        } 
    }   

    private void getOrderItemSet() {

        List<String> list = Arrays.asList(freqentItems);

        for (ArrayList<String> receipt : transactions) {
            ArrayList<String> set = new ArrayList<String>();
            for (String item : receipt) {
                if (list.contains(item)) set.add(item);
            }
            orderedItemSet.add(set);
        }

    }

    private void constructTree() {

        for (ArrayList<String> receipt : orderedItemSet) {
            fpTree.insertSet(receipt); 
        }

    }
}
