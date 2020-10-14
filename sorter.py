import csv
import xml.etree.ElementTree as ET

def getDB():
    ITEM_DATABASE = {}
    member_IDs = []
    
    with open('Groceries_dataset.csv', newline='') as csvfile:
        file = csv.reader(csvfile, delimiter=',')
        
        for row in file:
            if ITEM_DATABASE.get(int(row[0])) == None:  
                ITEM_DATABASE[int(row[0])] = [] 
            ITEM_DATABASE.get(int(row[0])).append([row[1], row[2]])
            member_IDs.append(int(row[0]))
        
    return (ITEM_DATABASE, member_IDs)

def sort(data):
    ITEM_DATABASE, member_IDs = data
    
    member_IDs.sort()
    SORTED_ITEM_DATABASE = {}
    for member_id in member_IDs:
        if SORTED_ITEM_DATABASE.get(member_id) == None:
            SORTED_ITEM_DATABASE[member_id] = ITEM_DATABASE.get(member_id)
        else: 
            continue
    
    return SORTED_ITEM_DATABASE
      
def parseXML(DATABASE):
    
    CustomersTag = ET.Element('Customers')
    
    for key in DATABASE.keys():
        CustomerTag = ET.SubElement(CustomersTag, 'Customer') 
        IDTag = ET.SubElement(CustomerTag, 'ID member_id="{}"'.format(key))
        ReceiptsTag = ET.SubElement(CustomerTag, 'Receipts')
        for transaction in DATABASE.get(key):
            ReceiptTag = ET.SubElement(ReceiptsTag, 'Receipt date="{}" item="{}"'.format(transaction[0], transaction[1]))
        
    tree = ET.tostring(CustomersTag)
    xmlFile = open('Transactions.xml', 'w')
    xmlFile.write(str(tree))
    
parseXML(sort(getDB()))