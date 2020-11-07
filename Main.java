import java.util.ArrayList; 
import java.util.List;
import java.util.Collections;

public class Main { 

    private static XmlReader reader = new XmlReader("Transactions.xml");

    private static ArrayList<Customer> customers = reader.getCustomers();

    private static Transactions allTransactions = new Transactions(customers);

    public static void main(String[] args) { 
        
        //allTransactions.printBinaryTransactions();

        //Integer[] encodedItems = allTransactions.getEncode(0);

        //for (Integer binary : encodedItems) System.out.print(binary);

        //ArrayList<String> decodedItems = allTransactions.getDecode(0);
        
        //for (String item : decodedItems) System.out.print(item + " | ");

        ArrayList<ArrayList<String>> t = allTransactions.ToString();
        ArrayList<String> u = allTransactions.listOfUniqueItems();

        ArrayList<ArrayList<String>> transactions = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            transactions.add(new ArrayList<>());
        }

        ArrayList<String> uniqueItems = new ArrayList<>();

        // String[] list1 = new String[]{"Bread", "Salt", "Soup"};
        // String[] list2 = new String[]{"Apple", "Bread", "Soup"};
        // String[] list3 = new String[]{"Apple", "Bread", "Crackers"};
        // String[] list4 = new String[]{"Apple", "Meat"}; 
        // String[] list5 = new String[]{"Salt", "Soup"};

        // for (String item : list1) transactions.get(0).add(item);
        // for (String item : list2) transactions.get(1).add(item);
        // for (String item : list3) transactions.get(2).add(item);
        // for (String item : list4) transactions.get(3).add(item);
        // for (String item : list5) transactions.get(4).add(item);

        // String[] list1 = new String[]{"E", "K", "M", "N", "O", "Y"};
        // String[] list2 = new String[]{"D", "E", "K", "N", "O", "Y"};
        // String[] list3 = new String[]{"A", "E", "K", "M"};
        // String[] list4 = new String[]{"C", "K", "M", "U", "Y"};
        // String[] list5 = new String[]{"C", "E", "I", "K", "O"};

        // for (String item : list1) transactions.get(0).add(item);
        // for (String item : list2) transactions.get(1).add(item);
        // for (String item : list3) transactions.get(2).add(item);
        // for (String item : list4) transactions.get(3).add(item);
        // for (String item : list5) transactions.get(4).add(item);

        // String[] items = new String[]{"A", "C", "D", "E", "I", "K", "M", "N", "O", "U", "Y"};
        
        //for (String item : items) uniqueItems.add(item);
        FPG fpg = new FPG(t, u);
        //FPG fpg = new FPG(transactions, uniqueItems);
        fpg.start();
    

    }

}