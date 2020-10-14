import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

// Handles the XML data
public class XmlHandler extends DefaultHandler {

    private ArrayList<Customer> customers = new ArrayList<Customer>();      // ArrayList of all Customers
    private Customer currentCustomer;                                       // Reference to the current Customer being processed
    private String currentDate;                                             // Reference to the current Date being processed
    private String currentItem;                                             // Reference to the current Item being processed

    // Runs at each openning tag in the XML file
    // qName - the tag name
    // attributes - the attributes to that associated tag
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        int member_id;     // Stores the member_id for the current customer

            // Handle the ID element
            if (qName.equals("ID")) {

                //values
                member_id = Integer.parseInt(attributes.getValue("member_id"));

                //Customer object
                currentCustomer = new Customer(member_id);
            }
            
            // Handle the Receipt for each Customer
            if (qName.equals("Receipt")) {
                
                // Stores the Date and Item for each transaction
                currentDate = attributes.getValue("date");
                currentItem = attributes.getValue("item");

            }


    }

    // Runs at each closing tag in the XML file
    // qName - the tag name
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
            
            // Handle the ID tag
            if (qName.equals("ID")) {
                // Adds the customer to a list of customers
                customers.add(currentCustomer);
            }
            
            // Handle the Receipt tag
            if (qName.equals("Receipt")) {
                // Checks if the date for that transaction is already in the hashmap
                if (currentCustomer.transactions.containsKey(currentDate) == false) {
                    // If not then place the date into the hashmap with a value of a new initialized 
                    // array list for all the items purchased on that date by that customer
                    currentCustomer.transactions.put(currentDate, new ArrayList<String>());
                }
                // Append the item to the list of purchased items by that customer
                currentCustomer.transactions.get(currentDate).add(currentItem); 
            }
    }

    // Return a reference to array list
    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}
