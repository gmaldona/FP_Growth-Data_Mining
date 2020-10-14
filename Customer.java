import java.util.HashMap;
import java.util.ArrayList;

public class Customer {
    
    private int member_id; 
    HashMap<String, ArrayList<String>> transactions = new HashMap<>();

    public Customer(int member_id) {
        this.member_id = member_id;
    }

    public int totalTransactions() {
        return transactions.size();
    }

    public ArrayList<String> getTransaction(String date) {
        try {
            return transactions.get(date); 
        }
        catch (Exception e) {
            System.out.println("Date doesn't exist: " + e); 
        }
        return null; 
    }

    public ArrayList<String[]> getAllTransactions() {
        
        ArrayList<ArrayList<String>> allTransactions = new ArrayList<>(); 

        try {
            for (String key : transactions.keySet()) {
                allTransactions.add(transactions.get(key));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return null;

    }

    public int getMemberID() {
        return this.member_id;
    }

}
