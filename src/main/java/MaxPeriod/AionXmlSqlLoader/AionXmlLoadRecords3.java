package MaxPeriod.AionXmlSqlLoader;

import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import JDBC.DBConnection;
import MaxPeriod.AionXmlSqlLoader.TableStructure2.DataType;

public class AionXmlLoadRecords3 extends AionXmlGetColumnsBase {
	private static final Logger logger = LogManager.getLogger(AionXmlLoadRecords3.class);
	
	private boolean printingEnabled;

	private DBConnection connection;
	
	private Map<String, TableStructure2> tableStructures;
	
	private Map<String, String> additionalColumnValues;
	
	private Stack<TableStructure2> currentTable;
	private Stack<TableRecord> currentTableRecord;
	
	//private Map<String, String> currentColumnValues;
	
	private StringBuilder columnContents;
	
	public AionXmlLoadRecords3(Set<PotentialTable> potentialTables, Map<String, TableStructure2> tableStructures) {
		super(potentialTables);
		
		this.tableStructures = tableStructures;//new LinkedHashMap<String, TableStructure2>();
		currentTable = new Stack<TableStructure2>();
		currentTableRecord = new Stack<TableRecord>();
				
		//currentColumnValues = new LinkedHashMap<String, String>();
	}
	
	public void setDatabaseConnection(DBConnection connection) {
		this.connection = connection;
	}

	public void setAdditionalColumnValues(Map<String, String> additionalColumnValues){
		this.additionalColumnValues = additionalColumnValues;
	}
	
	@Override
	public void beginTable(String name) {
		if (printingEnabled) System.out.printf("Current Table: %s\n", name);
		
		if (tableStructures.containsKey(name)) {
			currentTable.push(tableStructures.get(name));
		}
		
		
		//if (!tableStructures.containsKey(name)) tableStructures.put(name, new TableStructure(name, null));
	}
	
	@Override
	public void beginRow(String name) {
		if (printingEnabled) System.out.printf("\tLoadRecords3 Begin row %s\n", name);
		//currentColumnValues.clear();
		
		TableRecord record = new TableRecord(currentTable.peek());
		currentTableRecord.push(record);
		//currentTable.peek().getColumns().forEach((String columnName, DataType dataType) -> currentColumnValues.put(columnName, null));
	}
	
	@Override
	public void beginColumn(String name) {
		if (printingEnabled) System.out.printf("\t\tLoadRecords3 Begin column %s\n", name);
	
		columnContents = new StringBuilder();
	}
	
	@Override
	public void processRowAttributes(String att, String value) {
		
		if (printingEnabled) System.out.printf("\t\tLoadRecords3 Attribute: %s / Value: %s\n", att, value);
		
		columnContents = new StringBuilder();
		columnContents.append(value);
		endColumn(att);
	}
	
	@Override
	public void columnContents(String ch) {
		if (columnContents != null) columnContents.append(ch);
	}
	
	@Override
	public void endRow(String name) {
		if (printingEnabled) System.out.printf("\tLoadRecords3 End row %s\n", name);
		
		if (currentTableRecord.size() >= 2) {
			TableRecord parentTableRecord = currentTableRecord.get(currentTableRecord.size() - 2);
			String parentUniqueKeyName = parentTableRecord.getUniqueKeyColumnName();
			String parentUniqueKeyValue = parentTableRecord.getColumnValue(parentUniqueKeyName);
			currentTableRecord.peek().setColumnValue(parentTableRecord.getTableName() + '_' + parentUniqueKeyName, parentUniqueKeyValue);
		}
		if (printingEnabled) System.out.println(currentTableRecord.peek().toString());
		
		
		if (connection != null && currentTableRecord.size() >= 1) {
			String insertionQuery = currentTableRecord.peek().toString();
			
			try {
				connection.executeUpdate(insertionQuery);
			} catch (SQLException e) {
				
				logger.error("Insertion failed: " + e.getMessage() + "\n" + insertionQuery);
			}
		}
		
		
		currentTableRecord.pop();
		//connection.
	}		
	
	@Override
	public void endColumn(String name) {
		if (!currentTable.isEmpty()) {
		
			String columnContent = columnContents.toString(); 
			//if (currentColumnValues.containsKey(name)) currentColumnValues.put(name, columnContent);
			currentTableRecord.peek().setColumnValue(name, columnContent);
			
			for (String s: additionalColumnValues.keySet()) {
				currentTableRecord.peek().setColumnValue(s, additionalColumnValues.get(s));
			}
			
			if (printingEnabled) System.out.printf("Value: %s\n", columnContent);
		}
		//if (!currentTable.isEmpty()) currentTable.peek().getColumns().put(name, "VARCHAR(255)");
	}
	
	@Override
	public void endTable(String name) {
		if (printingEnabled) System.out.printf("LoadRecords3 End table %s\n", name);
		
		currentTable.pop();
	}
	
	public void enableSystemOutPrintln(){
		printingEnabled = true;
	}

	public void setDBConnection(DBConnection connection) {
		
		this.connection = connection;
	}
}
