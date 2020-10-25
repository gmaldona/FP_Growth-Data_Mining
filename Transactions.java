import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

// Class for transaction manipulation
public class Transactions {
    
    ArrayList<Customer> customers;
    ArrayList<String> uniqueItems; 
    HashMap<String, Integer> itemBinaryID;
    ArrayList<Integer[]> transactionListInBinary;

    // Setup
    public Transactions(ArrayList<Customer> customers) {
        this.customers = customers; 
        this.uniqueItems = listOfUniqueItems();
        this.itemBinaryID = binaryRepresentationOfItem();
        this.transactionListInBinary = encodeToBinary();
    }

    // Returns the list of all the unique items in all the transactions (no repeats)
    private ArrayList<String> listOfUniqueItems() {
        // Stores the items here
        ArrayList<String> uniqueItems = new ArrayList<>();

        // Checks through all the transactions for all customers to pull all unique items
        for (Customer c : customers) {
            ArrayList<ArrayList<String>> transactions = c.getAllTransactions(); 
            for (ArrayList<String> receipt : transactions ) {
                for (String item : receipt) {
                    // If the item is already not in the list 
                    if (!uniqueItems.contains(item)) {
                        // Then add that item to the list
                        uniqueItems.add(item);
                    }    
                }
            }
        } 

        // Sort before returning
        Collections.sort(uniqueItems);
        return uniqueItems;
    }

    // The encoding definition of all items 
    // Put in a string and it is defined by an integer
    private HashMap<String, Integer> binaryRepresentationOfItem() {
        // HashMap to store the binary representation  of all the items bought
        HashMap<String, Integer> itemsIndices = new HashMap<>(); 

        // Stores all the items names in the hashmap along with that index of the item
        for (int i = 0; i < uniqueItems.size(); i++) itemsIndices.put(uniqueItems.get(i), i);

        return itemsIndices;
    }

    // Returns the string form of all the transactions with the items
    public ArrayList<ArrayList<String>> ToString() {

        ArrayList<ArrayList<String>> allTransactions = new ArrayList<>();

        // Grabs all transactions made by a customer
        for (Customer c : customers) {
            ArrayList<ArrayList<String>> customerTransactions = c.getAllTransactions();

            // Adds each item to a transaction and then appends the whole transaction to one list of all transactions
            for (ArrayList<String> receipt : customerTransactions) {
                ArrayList<String> items = new ArrayList<>();
                for (String item : receipt) { 
                    items.add(item);
                }
                allTransactions.add(items);
            }
        }

        // Returns a list of all transactions
        return allTransactions;

    }

    // Encodes the entire list of transactions to binary representation
    public ArrayList<Integer[]> encodeToBinary() {
        // Creates an empty array with the size of all the possible items found in all of the transactions
        Integer[] uniqueItemsArray = new Integer[itemBinaryID.size()];
        // Sets all the values in the array to zero to start
        for (int j = 0; j < uniqueItemsArray.length; j++) uniqueItemsArray[j] = 0;
        
        // Encoding
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
                    int itemIndex = itemBinaryID.get(item);
                    // Changes the 0 to a 1 at the index of the encoded item
                    itemsArray[itemIndex] = 1;
                }
                // Adds the entire array of items in the array list of binary arrays
                binaryTransactions.add(itemsArray);
            }
        }

        return binaryTransactions;
    }

    // Returns one transaction in the entire list of transactions but encoded in binary (For Debugging)
    public Integer[] getEncode(int atIndex) { return transactionListInBinary.get(atIndex); }

    // Returns one transaction in the entire list of transactions (For Debugging)
    public ArrayList<String> getDecode(int atIndex) {
        
        Integer[] transaction = transactionListInBinary.get(atIndex);
        ArrayList<String> decodedItems = new ArrayList<>();

        for (int i = 0; i < transaction.length; i++) {
            if (transaction[i] == 1) {
                for (String key : itemBinaryID.keySet()) {
                    if (itemBinaryID.get(key) == i) {
                        decodedItems.add(key);
                    }
                }
            }
        }

        return decodedItems;

    }

    // Method just prints to the std out all the transactions (For Debugging)
    public void printAllTransactions() {
        ArrayList<ArrayList<String>> transactions = ToString();
        for (ArrayList<String> receipt : transactions) {
            for (String item : receipt) System.out.print(item + " | ");
            System.out.println(); 
        }
    }

    // Method just prints to the std out all the transactions in binary (For Debugging)
    public void printAllBinaryTransactions() {
        for (Integer[] binaryList : transactionListInBinary) {
            for (Integer binary : binaryList) System.out.print(binary);
            System.out.println();
        }  
    }

    // Method that returns the total amount of transactions
    public int size() { return this.transactionListInBinary.size(); }

    //Method that returns the item representation in binary for the mining
    public ArrayList<Integer[]> get() { return this.transactionListInBinary; }
}
