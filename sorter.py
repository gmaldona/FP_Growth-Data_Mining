import csv
import xml.etree.ElementTree as ET


def getDB() -> (dict, list):
    
    '''

    Description: Parses the csv file and organizes the data
    
    ITEM_DATABASE (type dictionary): 
            - key -> member_ID (type Integer)
            - Value -> [ [Date Purchased, Item Purchased], [Date Purchased, Item Purchased], ... ] (type 2D list) 
    
            The value for each key are all the purchases made by that member_id
            
    member_IDs (type list):
            - list of Integers that stores each member_ID
    
    Returns: A tuple containing a dictionary of all the items (ITEM_DATABASE) & an array of all of the member IDs (member_IDs)
    
    '''
    
    ITEM_DATABASE = {}
    member_IDs = []
    
    # Parses the csv file   
    with open('Groceries_dataset.csv', newline='') as csvfile:
        file = csv.reader(csvfile, delimiter=',')
        
        # For each row in the file, organize the data by putting the data in it's respecitive data structure
        for row in file:
            # If the member_ID doesn't exist in the dictionary
            if ITEM_DATABASE.get(int(row[0])) == None:  
                # Creates an empty list for transactions made by that member_ID
                ITEM_DATABASE[int(row[0])] = [] 
                
            # Places Item into 2D for the value at the key of the member ID
            ITEM_DATABASE.get(int(row[0])).append([row[1], row[2]])
            # Places member_ID into a list
            member_IDs.append(int(row[0]))
    
    # Returns the database and all of the member IDs    
    return (ITEM_DATABASE, member_IDs)


def sortDB(data: (dict, list)) -> dict:
    '''
    
    Description: Groups (and sorts) all of the purchases made by one customer 
    
    Returns: A new sorted and more organized ITEM_DATABASE
    
    '''
    ITEM_DATABASE, member_IDs = data
    
    # Sorted all of the member ID's from least to greatest
    member_IDs.sort()
    
    SORTED_ITEM_DATABASE = {}
    
    # Loops through all of the keys in the dictionary 
    for member_id in member_IDs:
        # Checks if a key with the member_id already exists
        if SORTED_ITEM_DATABASE.get(member_id) == None:
            # If the key doesn't exist then place the key in the dictionary set it to the parameter dictionary at the key member_id
            SORTED_ITEM_DATABASE[member_id] = ITEM_DATABASE.get(member_id)
        else: 
            continue
    
    # Returns the new Database
    return SORTED_ITEM_DATABASE
      
      
def parseXML(DATABASE: dict):
    
    '''
    
    description: Turns the old csv file into an organized XML file that groups all purchases together with associated member_ID
    
    '''
    
    # Alls the receipts for a customer
    receipts = {}
    
    for key in DATABASE.keys(): 
        receipts[key] = {}
        purchases = {}
        
        for purchase in DATABASE[key]:
            if purchases.__contains__(purchase[0]) == False:
                purchases[purchase[0]] = []
            purchases[purchase[0]].append(purchase[1]) 
            
        receipts[key] = purchases
        
    print(receipts)
        
        
     
    
    # Root Tag
    CustomersTag = ET.Element('Customers')
    
    # For each member ID in the Database, a customer tag needs to be creaeted
    for key in DATABASE.keys():
        # Creates a customer tag
        CustomerTag = ET.SubElement(CustomersTag, 'Customer') 
        # Creates an ID tag for that customer
        IDTag = ET.SubElement(CustomerTag, 'ID member_id="{}"'.format(key))
        # Creates a Receipts tag to house all of the transactions that member_ID processed
        ReceiptsTag = ET.SubElement(CustomerTag, 'Receipts')
        for date in receipts.get(key).keys():
            ReceiptTag = ET.SubElement(ReceiptsTag, 'Receipt')
            DateTag = ET.SubElement(ReceiptTag, 'Date date="{}"'.format(date))
            
            for item in receipts.get(key).get(date):
                itemTag = ET.SubElement(ReceiptTag, 'Item item="{}"'.format(item))
        
    # Creates the XML structure    
    tree = ET.tostring(CustomersTag)
    # Writes to the XML file
    xmlFile = open('Transactions.xml', 'w')
    xmlFile.write(str(tree))
    

if __name__ == '__main__':
    Database, member_IDs = getDB()
    Database = sortDB((Database, member_IDs))
    parseXML(Database)
    print('Finished.')