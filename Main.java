import java.util.ArrayList; 

public class Main { 

    private static XmlReader reader = new XmlReader("Transactions.xml");

    private static ArrayList<Customer> customers = reader.getCustomers();

    private static Transactions allTransactions = new Transactions(customers);

    public static void main(String[] args) { 
        
        //allTransactions.printBinaryTransactions();

        Integer[] encodedItems = allTransactions.getEncode(0);

        for (Integer binary : encodedItems) System.out.print(binary);

        ArrayList<String> decodedItems = allTransactions.getDecode(0);
        
        for (String item : decodedItems) System.out.print(item + " | ");
        
    }

}