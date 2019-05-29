package MaxPeriod.AionXmlSqlLoader;

import java.sql.Types;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import MaxPeriod.AionXmlSqlLoader.TableStructure2.DataType;

public class TableRecord {
	private TableStructure2 tableStructure;	
	private Map<String, String> currentColumnValues;
	
	public TableRecord(TableStructure2 tableStructure) {
		this.tableStructure = tableStructure;
		currentColumnValues = new LinkedHashMap<String, String>();
		
		tableStructure.getColumns().forEach((String columnName, DataType dataType) -> currentColumnValues.put(columnName, null));
	}
	
	public String getTableName() {
		return tableStructure.getName();
	}
	
	public String getColumnValue(String column) {
		return currentColumnValues.get(column);
	}
	
	public void setColumnValue(String column, String value) {
		if (currentColumnValues.containsKey(column)) currentColumnValues.put(column, value);
	}
	
	public String getUniqueKeyColumnName() {
		return tableStructure.getUniqueKey();
	}
	
	public String getUniqueKeyColumnValue() {
		String uniqueKey = tableStructure.getUniqueKey();
		
		return uniqueKey != null ? currentColumnValues.get(uniqueKey) : null;
	}
	
	@Override
	public String toString() {				
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(tableStructure.getName());
		sb.append(" VALUES (");
		
		for (Iterator<String> iter = currentColumnValues.keySet().iterator(); iter.hasNext(); ) {
			String columnName = iter.next();
			String value = currentColumnValues.get(columnName);
			
			boolean addSingleQuotes = false;
			switch(tableStructure.getColumns().get(columnName).type) {
			case Types.CHAR:
			case Types.DATE:
			case Types.LONGNVARCHAR:
			case Types.LONGVARCHAR:
			case Types.NCHAR:
			case Types.NVARCHAR:
			case Types.VARCHAR:
				addSingleQuotes = true;
			}
			
			if (addSingleQuotes && value != null) sb.append('\'');
			
			if (!addSingleQuotes && value != null) {
				value = value.replace("f", "");
				value = value.replace("d", "");
			}
			
			if (value != null) {
				if (value.trim().length() == 0 && !addSingleQuotes) sb.append("NULL");
				else sb.append(value.replace("'",  "''"));
			}
			else sb.append("NULL");
			
			if (addSingleQuotes && value != null) sb.append('\'');
			if (iter.hasNext()) sb.append(", ");
		}
		sb.append(");");
		return sb.toString();
	}
}
