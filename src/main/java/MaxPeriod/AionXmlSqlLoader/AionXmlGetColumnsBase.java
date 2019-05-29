package MaxPeriod.AionXmlSqlLoader;

//import java.io.IOException;
//import java.util.HashMap;
//import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
//import java.util.TreeSet;

import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import MaxPeriod.AionXmlSqlLoader.TableStructure2.DataType;


public class AionXmlGetColumnsBase extends DefaultHandler {

	//private enum mode {TABLE,ELEMENT,COLUMN};
	
	//private String COLUMN_NAME_INSERTION_ORDER = "maxperiod_insertion_order";
	
	private boolean printingEnabled = false;
	
	private int level = 0;
	//private Stack<Integer> tableLevel;
	//private Stack<String> currentTable;
	private Stack<Boolean> isTopLevelChecker;
	
	private Set<PotentialTable> potentialTables;
	private String mainElementName;
	
	//private Map<String, TableStructure> tableStructures;
	
	//private String currentElement;
	//private String lastFinishedElement;
	
	private Stack<String> elementStack;
	//private Stack<mode> modeStack;
	private Stack<PotentialTable> potentialTablesStack; 
	//private Stack<TableStructure> tableStructureStack;
	
	Map<String, DataType> additionalColumns;

	public AionXmlGetColumnsBase(Set<PotentialTable> potentialTables/*, Map<String, TableStructure> tableStructures*/){
		
		this.potentialTables = potentialTables;
		//this.tableStructures = tableStructures;
		
		elementStack = new Stack<String>();
		
		//tableLevel = new Stack<Integer>();
		//currentTable = new Stack<String>();
		//Stack<mode> modeStack = new Stack<mode>();
		potentialTablesStack = new Stack<PotentialTable>();
		//tableStructureStack = new Stack<TableStructure>();
		isTopLevelChecker = new Stack<Boolean>();
	}
	
	public void setAdditionalColumns(Map<String, DataType> additionalColumns){
		this.additionalColumns = additionalColumns;
	}
	
	public void enableSystemOutPrintln(){
		printingEnabled = true;
	}
	/*
	private String generateSubTablePrefix(){
		StringBuilder sb = new StringBuilder();
		currentTable.forEach(table -> {
			if (!table.equals(currentTable.peek())){
				sb.append(table);
				sb.append('_');
			}
		});
		return sb.toString();
	}
	*/
	/*
	public void startDocument(){	
	}
	
	public void endDocument(){
		
	}
	*/
	
	private String getLastLastElement() {
		return level - 2 >= 0 ? elementStack.get(level - 2) : null;
	}
	
	private String getLastElement() {
		return level - 1 >= 0 ? elementStack.get(level - 1) : null;
	}
	
	private PotentialTable getCurrentTable() {
		return !potentialTablesStack.isEmpty() ? potentialTablesStack.peek() : null;
	}
	
	private PotentialTable isTable(String name) {
		String lastElement = getLastElement();
		//String lastlastElement = level - 2 >= 0 ? elementStack.get(level - 2) : null;
		
		for (PotentialTable table: potentialTables) {
			if (table.level == level 
				&& table.tableName.equals(name)
				&& (lastElement != null && table.parentElement != null && table.parentElement.equals(lastElement)
					|| lastElement == null && table.parentElement == null)
			){
				return table;	
			}
		}
		
		return null;
	}
	

	
	private String getCurrentTableFullName(String name) {
		StringBuilder sb = new StringBuilder();
		int startLevel = 0;
		
		PotentialTable lastTable = null;
		
		for (Iterator<PotentialTable> iter = potentialTablesStack.iterator(); iter.hasNext();) {
			//sb.append(iter.next();
			PotentialTable table = iter.next();
			startLevel = lastTable == null ? 0 : lastTable.level + 2;
						
			for (int i = startLevel; i <= table.level; i ++) {
				if (i < level) {
					sb.append(elementStack.get(i));
					/*if (i < table.level - 1)*/ sb.append('_');
				}
			}
			/*
			if (sb.length() > 0 && iter.hasNext()) {
				sb.append('_');				
			}
			*/
			lastTable = table;
		}
		//if (sb.length() > 0) sb.append('_');
		sb.append(name);
		return sb.toString();
	}
	
	private String getCurrentColumnFullName(String name) {
		StringBuilder sb = new StringBuilder();
		
		
		PotentialTable table = getCurrentTable();
		int startLevel = table == null ? 0 : table.level + 2;
		
		for (int i = startLevel; i < elementStack.size(); i ++) {
			sb.append(elementStack.get(i));
			if (i < elementStack.size() - 1) sb.append('_');
		}		
		
		if (sb.length() > 0) sb.append('_');
		sb.append(name);
		return sb.toString();
	}
	
	public final void startElement (String uri, String name,
			String qName, Attributes atts){		
	
		name = name.toLowerCase();
		/*
		String lastElement = null;
		String lastlastElement = null;
		
		if (level - 1 >= 0) lastElement = elementStack.get(level - 1);
		if (level - 2 >= 0) lastlastElement = elementStack.get(level - 2);
		*/
		//Triple<String, String, String> currentTriple = new Triple<String, String, String>(lastElement, name, lastlastElement); 
		
		//if (printingEnabled) System.out.printf("%s %s\n", name, qName);
		PotentialTable table = isTable(name);
		PotentialTable currentTable = getCurrentTable(); 
		
		if (table != null) {
			potentialTablesStack.push(table);
			//if (printingEnabled) System.out.printf("Begin table %s at level %d. Expected %s as elements\n", getCurrentTableFullName(name), level, table.elementName);
			beginTable(getCurrentTableFullName(name));
		}
		
		//else if (name.equals(potentialTablesStack.peek().elementName) && getLastElement().equals(potentialTablesStack.peek().tableName)){
		else if (currentTable != null && getLastElement() != null && getLastElement().equals(currentTable.tableName)){
			beginRow(name);
			//if (printingEnabled) System.out.printf("\tBegin row %s\n", name);
			
			for (int i = 0; i < atts.getLength(); i ++) {
				processRowAttributes(atts.getQName(i).toLowerCase(), atts.getValue(i));
				//System.out.printf("\t\tRow attribute: %s\n", atts.getQName(i));
			}
			
			
		}
		
		else {
			
			//if (printingEnabled) System.out.printf("\t\tBegin column %s\n", name);
			for (int i = 0; i < atts.getLength(); i ++) {
				StringBuilder sb = new StringBuilder();
				sb.append(name);
				sb.append('_');
				sb.append(atts.getQName(i));
				processRowAttributes(sb.toString().toLowerCase(), atts.getValue(i));
				//System.out.printf("\t\t\tColumn attribute: %s_%s\n", name, atts.getQName(i));
			}
			
			beginColumn(name);
		}
		
		elementStack.push(name);
		if (!isTopLevelChecker.isEmpty()){
			isTopLevelChecker.pop();
			isTopLevelChecker.push(false);
		}
		isTopLevelChecker.push(true);
		
		level ++;
	}	
	
	public final void endElement (String uri, String name, String qName){
		
		name = name.toLowerCase();
		
		level --;
		elementStack.pop();
		boolean isTopLevel = isTopLevelChecker.pop();
		
		
		PotentialTable table = !potentialTablesStack.isEmpty() ?  potentialTablesStack.peek() : null;
		
		
		//if (name.equals(table.elementName) && getLastElement() != null && getLastElement().equals(table.tableName)){
		if (table != null && getLastElement() != null && getLastElement().equals(table.tableName)) {
			endRow(name);
			//if (printingEnabled) System.out.printf("\tEnd row %s\n", name);
		}
		else if (table != null && name.equals(table.tableName) && (getLastElement() == null || getLastElement().equals(table.parentElement))){
			//if (printingEnabled) System.out.printf("End table %s\n", getCurrentTableFullName(name));
			endTable(getCurrentTableFullName(name));
			potentialTablesStack.pop();
		}
		else {
			if (isTopLevel) endColumn(getCurrentColumnFullName(name));
			/*
			if (printingEnabled) {
				System.out.printf("\t\tEnd column %s", getCurrentColumnFullName(name));
				if (isTopLevel) System.out.printf("(top level)");
				System.out.println();
			}
			*/
			
		}
	
		

    }
	
	public void beginTable(String name) {
		if (printingEnabled) System.out.printf("Begin table %s at level %d\n", name, level);
	}
	
	public void beginRow(String name) {
		if (printingEnabled) System.out.printf("\tBegin row %s\n", name);
	}
	
	public void beginColumn(String name) {
		if (printingEnabled) System.out.printf("\t\tBegin column %s\n", name);
		
	}
	
	public void processRowAttributes(String att, String value) {
		if (printingEnabled) System.out.printf("\t\tAttribute: %s / value: %s\n", att, value);
	}
	
	public void columnContents(String ch) {
		if (printingEnabled) System.out.printf("\t\t\tcharacters: %s\n", ch);			
		
	}
	
	public void endRow(String name) {
		if (printingEnabled) System.out.printf("\tEnd row %s\n", name);
	}
	
	public void endColumn(String name) {
		if (printingEnabled) System.out.printf("\t\tEnd column %s\n", name);
		
	}
	
	public void endTable(String name) {
		if (printingEnabled) System.out.printf("End table %s\n", name);
	}
	
	@Override
	public void endDocument() {
		
	}
		
	//Append characters to current attribute
	public final void characters (char ch[], int start, int length){
		
		String string = new String(ch, start, length);//.trim();
		if (string.length() > 0) columnContents(string);
		/*
		if (printingEnabled) {			
			if (string.length() > 0) System.out.printf("\t\t\tcharacters: %s\n", string);			
		}
		*/
		/*
		if (attributeInProgress){
			characters.append(ch, start, length);
		}
		*/
		//System.out.print("characters");
	}

	

	public String getMainElementName() {
		return mainElementName;
	}

	
}



