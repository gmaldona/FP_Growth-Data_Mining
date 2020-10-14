import java.util.ArrayList; 

public class Main { 

    public static void main(String[] args) { 

        XmlReader reader = new XmlReader("Transactions.xml");

        ArrayList<Customer> customers = reader.getCustomers();

        customers.get(0).getTransaction("27-05-2015");

    }

}