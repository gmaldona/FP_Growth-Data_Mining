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
    private ArrayList<String> receiptItemList = new ArrayList<String>();

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
            
            // Handles the Date for each Receipt
            if (qName.equals("Date")) {
                currentDate = attributes.getValue("date");
            }

            // Handles the Item for each Receipt
            if (qName.equals("Item")) {
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
                currentCustomer.transactions.put(currentDate, receiptItemList);
                receiptItemList = new ArrayList<String>(); 
            }

            if (qName.equals("Item")) {
                receiptItemList.add(currentItem);
            }
    }

    // Return a reference to array list
    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}
