import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;

// No changes are required for this class

public class XmlReader {
    private final String inputFileName;
    private XmlHandler myXmlHandler = null;
    private ArrayList<Customer> customers;

    public XmlReader(String inputFileName) {
        this.inputFileName = inputFileName;

        try {
            // Declare a File object
            File inputFile = new File(inputFileName);

            // Create a SAX parser factory and parser
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();

            // Create an XML handler
            myXmlHandler = new XmlHandler();

            // Parse the file
            saxParser.parse(inputFile, myXmlHandler);

        } catch (Exception e) {

            System.out.println("Exception: " + e.getMessage());

        }
    }

    public ArrayList<Customer> getCustomers() {
        return myXmlHandler.getCustomers();
    }
}
