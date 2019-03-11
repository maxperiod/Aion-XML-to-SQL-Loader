package MaxPeriod.AionXmlSqlLoader;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;


public class AionXmlFindPotentialTables2 extends DefaultHandler {

	private static final Logger logger = LogManager.getLogger(AionXmlFindPotentialTables2.class);
	
	int level = 0;
	Stack<Integer> tableLevel;
	Stack<String> currentTable;
	
	//First: table name / second: element name for each record / third: parent element name
	private Set<PotentialTable> potentialTables;
	private String mainElementName;
	
	private String currentElement;
	private String lastFinishedElement;
	
	private Stack<String> elementStack;
	

	public AionXmlFindPotentialTables2(Set<PotentialTable> potentialTables){
		this.potentialTables = potentialTables;
		
		elementStack = new Stack<String>();
		
		
		tableLevel = new Stack<Integer>();
		currentTable = new Stack<String>();
	}

	
	/*
	public void startDocument(){	
	}
	
	public void endDocument(){
		
	}
	*/
	public void startElement (String uri, String name,
			String qName, Attributes atts){		
		name = name.toLowerCase();
		if (lastFinishedElement != null && lastFinishedElement.equals(name) && !elementStack.isEmpty()){
		
		
			String tableName = elementStack.peek();
			String elementName = name;
			String parent = null;
			if (level - 2 >= 0) parent = elementStack.get(level - 2);
			potentialTables.add(new PotentialTable(tableName, parent, level - 1, elementName));
			//potentialTables.put(elementStack.peek(), name);
			
		
			
	
		}
		else lastFinishedElement = null;
		
		elementStack.push(name);
		level ++;
	}	
	
	public void endElement (String uri, String name, String qName){
		name = name.toLowerCase();
	
		level --;
		elementStack.pop();
		lastFinishedElement = name;
	
    }
		
	//Append characters to current attribute
	public void characters (char ch[], int start, int length){
		/*
		if (attributeInProgress){
			characters.append(ch, start, length);
		}
		*/
		//System.out.print("characters");
	}

	public void elementNameCannotBeTable() {
		/*
		List<PotentialTable> potentialTablesList = new LinkedList<PotentialTable>();
				
		
		Iterator<PotentialTable> iter = potentialTables.iterator();
		*/
		List<PotentialTable> tablesToRemove = new ArrayList<PotentialTable>(); 
		for (PotentialTable table: potentialTables) {
						
			for (PotentialTable otherTable: potentialTables) {
				if (table.parentElement != null && table.parentElement.equals(otherTable.tableName)
						&& table.level - 1 == otherTable.level) {
					tablesToRemove.add(table);
					
				}
					
			}
		}
		for (PotentialTable table: tablesToRemove) {
			potentialTables.remove(table);
		}
		
	}

	public String getMainElementName() {
		return mainElementName;
	}

}



