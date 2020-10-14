import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XmlHandler extends DefaultHandler {

    private ArrayList<Customer> customers = new ArrayList<Customer>();  // ArrayList of all Customers
    private Customer currentCustomer; // Reference to the current Customer being processed
    private String currentDate;
    private String currentItem;

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("Start element:" + qName);

        int member_id; 

            // Handle the customer element
            if (qName.equals("ID")) {

                //values
                member_id = Integer.parseInt(attributes.getValue("member_id"));

                //Customer object
                currentCustomer = new Customer(member_id);
            }

            if (qName.equals("Receipt")) {
                
                currentDate = attributes.getValue("date");
                currentItem = attributes.getValue("item");

            }


    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

            if (qName.equals("ID")) {
                customers.add(currentCustomer);
            }

            if (qName.equals("Receipt")) {
                if (currentCustomer.transactions.containsKey(currentDate) == false) {
                    currentCustomer.transactions.put(currentDate, new ArrayList<String>());
                }
                currentCustomer.transactions.get(currentDate).add(currentItem); 
            }
    }

    // Return a reference to array list
    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}
