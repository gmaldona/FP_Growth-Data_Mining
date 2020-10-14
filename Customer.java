import java.util.HashMap;
import java.util.ArrayList;

// Stores all the information for a customer 
public class Customer {
    
    // Member ID 
    private int member_id; 
    // HashMap of all the transactions made by this Customer
    // The key is the date of purchase, the value is a list of all the items purchased on that date
    HashMap<String, ArrayList<String>> transactions = new HashMap<>();

    // Initializes
    public Customer(int member_id) { this.member_id = member_id; }

    // Total number of transactions made by this customer
    public int totalTransactions() { return transactions.size(); }

    // Gets a list of items in the transaction of a parameterized date (String)
    public ArrayList<String> getTransaction(String date) {
        
        // Checks if customer made a purchase on that date
        try {
            // Return the transaction made on that date
            return transactions.get(date); 
        }
        // if customer did not make a purchase on that date
        catch (Exception e) {
            System.out.println("Date doesn't exist: " + e); 
        }
        return null; 
    }

    // Method that returns all of the transactions made by this customer
    public ArrayList<ArrayList<String>> getAllTransactions() {
        
        // List of a List of items purchased by this customer
        ArrayList<ArrayList<String>> allTransactions = new ArrayList<>(); 

        try {
            // For each transaction on a day 
            for (String key : transactions.keySet()) {
                // Add that list to a running list of transactions made by this customer
                allTransactions.add(transactions.get(key));
            }
            // Return list of all transactions
            return allTransactions;
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return null;

    }

    // Returns the member_ID
    public int getMemberID() { return this.member_id; }

}
