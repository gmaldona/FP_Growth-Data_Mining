import java.util.ArrayList; 

public class Main { 

    public static void main(String[] args) { 

        XmlReader reader = new XmlReader("Transactions.xml");

        ArrayList<Customer> customers = reader.getCustomers();

        System.out.println(customers.get(420).getAllTransactions());

    }

}