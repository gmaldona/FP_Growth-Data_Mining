import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Collections;

public class Main { 

    private static XmlReader reader = new XmlReader("Transactions.xml");

    private static ArrayList<Customer> customers = reader.getCustomers();

    public static void main(String[] args) { 
        
        // Gets a list of all the items in all the transactions without repeating an item
        ArrayList<String> uniqueItems = getUniqueItems();

        // HashMap to store the binary representation  of all the items bought
        HashMap<String, Integer> itemsIndices = new HashMap<>(); 

        // Stores all the items names in the hashmap along with that index of the item
        for (int i = 0; i < uniqueItems.size(); i++) itemsIndices.put(uniqueItems.get(i), i);
        
        // Creates an empty array with the size of all the possible items found in all of the transactions
        Integer[] uniqueItemsArray = new Integer[itemsIndices.size()];
        // Sets all the values in the array to zero to start
        for (int j = 0; j < uniqueItemsArray.length; j++) uniqueItemsArray[j] = 0;
        
        //Creates a new empty Array List of Integer Arrays to store the list of binary transactions
        ArrayList<Integer[]> binaryTransactions = new ArrayList<>();
        
        // Loops through all the customers in the array list
        for (Customer c : customers) {
            // Grabs all the transactions made by that customer
            ArrayList<ArrayList<String>> transactions = c.getAllTransactions();
            // Loops through all the receipts in all of the given transaction
            for (ArrayList<String> receipt : transactions) {
                // Clones the unique item array that contains a binary array for if an item is found in a receipt
                // Starts off with all the values in the array being equal to zero
                Integer[] itemsArray = uniqueItemsArray.clone();
                // Loops through each item in each receipt
                for (String item : receipt) {
                    // Gets the encoding index for that item
                    int itemIndex = itemsIndices.get(item);
                    // Changes the 0 to a 1 at the index of the encoded item
                    itemsArray[itemIndex] = 1;
                }
                // Adds the entire array of items in the array list of binary arrays
                binaryTransactions.add(itemsArray);
            }
        }


        // Decoding an array of binary representing the items in a receipt
        Integer[] testBinaryRow = binaryTransactions.get(0);
        // String array of all the decoded items
        ArrayList<String> testItemRow = new ArrayList<>();
        // Loops through all the binary in the array
        for (int j = 0; j < testBinaryRow.length; j++ ) {
            // A value of 1 means that the item in is the receipt
            if (testBinaryRow[j] == 1) {
                // Checks all the keys in the itemIndices HashMap
                for (String key : itemsIndices.keySet()) {
                    // Checks if the key has the same value as the index in the array where a value of 1 was found
                    if (itemsIndices.get(key) == j ) {
                        // Adds that item into the list of decoded items in the array
                        testItemRow.add(key);
                    }
                }
            }
        }

        // Comparing the two test cases to see if the decoding works
        for (Integer binary : testBinaryRow) {
            System.out.print(binary.intValue());
        }
        Collections.sort(testItemRow);
        Collections.sort(customers.get(0).getAllTransactions().get(0));
        System.out.println();
        System.out.println(testItemRow);
        System.out.println();
        System.out.println(customers.get(0).getAllTransactions().get(0));
        System.out.println(); 
        System.out.println(testItemRow.containsAll(customers.get(0).getAllTransactions().get(0)) ? true : false);
        
        // Print out the whole binary representation array
        /*
        for (Integer[] transaction : binaryTransactions) {
            for (Integer binaryItem : transaction) {
                System.out.print(binaryItem.intValue() + " | ");
            }
            System.out.println("");
        }
        */
    



    }


    public static ArrayList<String> getUniqueItems() {

        ArrayList<String> uniqueItems = new ArrayList<>();

        for (Customer c : customers) {
            ArrayList<ArrayList<String>> transactions = c.getAllTransactions(); 
            for (ArrayList<String> receipt : transactions ) {
                for (String item : receipt) {
                    if (!uniqueItems.contains(item)) {
                        uniqueItems.add(item);
                    }    
                }
            }
        } 

        Collections.sort(uniqueItems);
        return uniqueItems;

    }

}