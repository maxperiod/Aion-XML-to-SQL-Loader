package MaxPeriod.AionXmlSqlLoader;

import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import MaxPeriod.AionXmlSqlLoader.TableStructure2.DataType;

public class AionXmlGetColumns4 extends AionXmlGetColumnsBase {

	private static final Logger logger = LogManager.getLogger(AionXmlGetColumns4.class);
	
	private boolean printingEnabled;

	private Map<String, TableStructure2> tableStructures;
	
	private Map<String, DataType> additionalColumns;
	
	private Stack<TableStructure2> currentTable;
	
	private StringBuilder columnContents;
	
	public AionXmlGetColumns4(Set<PotentialTable> potentialTables, Map<String, TableStructure2> tableStructures) {
		super(potentialTables);
		
		this.tableStructures = tableStructures;//new LinkedHashMap<String, TableStructure2>();
		currentTable = new Stack<TableStructure2>();
				
	}

	public void setAdditionalColumns(Map<String, DataType> additionalColumns){
		this.additionalColumns = additionalColumns;
	}
	
	@Override
	public void beginTable(String name) {
		if (printingEnabled) System.out.printf("getcolumns4 Begin table %s\n", name);
		
		if (tableStructures.containsKey(name)) {
			currentTable.push(tableStructures.get(name));
		}
		else {
			TableStructure2 newTable = new TableStructure2(name);
			
			for (String additionalColumn: additionalColumns.keySet()) {
				DataType additionalColumnDataType = additionalColumns.get(additionalColumn);
				newTable.addColumn(additionalColumn, additionalColumnDataType.type, additionalColumnDataType.arg1, additionalColumnDataType.arg2);
			}
			
			
			tableStructures.put(name, newTable);
			currentTable.push(newTable);
		}
		
		if (currentTable.size() >= 2) {
			//endColumn(currentTable.get(0).getUniqueKey());
			TableStructure2 lastTable = currentTable.get(currentTable.size() - 2); 
			String uniqueKey = lastTable.getUniqueKey();
			DataType uniqueKeyDataType = lastTable.getColumns().get(uniqueKey);
			currentTable.peek().addColumn(lastTable.getName() + '_' + uniqueKey, uniqueKeyDataType.type, uniqueKeyDataType.arg1, uniqueKeyDataType.arg2);
		}
		
		//if (!tableStructures.containsKey(name)) tableStructures.put(name, new TableStructure(name, null));
	}
	
	@Override
	public void beginRow(String name) {
		if (printingEnabled) System.out.printf("\tgetcolumns4 Begin row %s\n", name);
	}
	
	@Override
	public void beginColumn(String name) {
		if (printingEnabled) System.out.printf("\t\tgetcolumns4 Begin column %s\n", name);
	
		columnContents = new StringBuilder();
	}
	
	@Override
	public void processRowAttributes(String att, String value) {
		if (printingEnabled) System.out.printf("\t\tgetcolumns4 Attribute: %s / Value: %s\n", att, value);
		
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
		if (printingEnabled) System.out.printf("\tgetcolumns4 End row %s\n", name);
	}		
	
	@Override
	public void endColumn(String name) {
		if (!currentTable.isEmpty()) {
		
			String columnContent = columnContents.toString(); 
			
			boolean integerValid = false;
			boolean longValid = false;
			int decimalPrecision = 0;
			int decimalScale = 0;
			
			try {
				Integer.valueOf(columnContent);
				integerValid = true;
			}
			catch (NumberFormatException e) {}
			
			try {
				Long.valueOf(columnContent);
				longValid = true;
			}
			catch (NumberFormatException e) {}
			
			try {
				Double.valueOf(columnContent);
				
				decimalPrecision = columnContent.length() - 1;
				decimalScale = columnContent.length() - columnContent.lastIndexOf('.') - 1;
				
							
			}
			catch (NumberFormatException e) {}
			
			if (printingEnabled) System.out.printf("\t\tgetcolumns4 End column %s ", name);
			if (integerValid) {
				if (printingEnabled) System.out.printf("integer");
				currentTable.peek().addColumn(name, Types.INTEGER);
			}
			else if (longValid) {
				if (printingEnabled) System.out.printf("bigint");
				currentTable.peek().addColumn(name, Types.BIGINT);
			}
			else if (decimalPrecision >= 1 && decimalScale >= 1) {
				if (printingEnabled) System.out.printf("decimal(%d, %d)", decimalPrecision, decimalScale);
				currentTable.peek().addColumn(name, Types.DECIMAL, decimalPrecision, decimalScale);
			}
			else if (columnContent.length() >= 1){
				if (printingEnabled) System.out.printf("varchar");
				currentTable.peek().addColumn(name, Types.VARCHAR, columnContent.length() < 63 ? 63 : columnContent.length());
			}
			if (printingEnabled) System.out.printf(" - Value: %s\n", columnContent);
		}
		//if (!currentTable.isEmpty()) currentTable.peek().getColumns().put(name, "VARCHAR(255)");
	}
	
	@Override
	public void endTable(String name) {
		if (printingEnabled) System.out.printf("getcolumns4 End table %s\n", name);
		
		currentTable.pop();
	}
	
	public void enableSystemOutPrintln(){
		printingEnabled = true;
	}

	public Map<String, TableStructure2> getTableStructures() {
		// TODO Auto-generated method stub
		return tableStructures;
	}
}
