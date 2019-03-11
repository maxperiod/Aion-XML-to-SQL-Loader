
package MaxPeriod.AionXmlSqlLoader;

import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableStructure2 {
	

	
	private String name;
	//private String elementName;
	private Map<String, DataType> columns;
	private Set<String> subtables;
	
	private String firstAddedColumn;

	public TableStructure2(String name){
		this.name = name;
		//this.elementName = elementName;
		columns = new LinkedHashMap<String, DataType>();
		subtables = new LinkedHashSet<String>();
	}
	
	public String getName() {
		return name;
	}
/*	
	public String getElementName() {
		return elementName;
	}
*/

	public Map<String, DataType> getColumns() {
		return columns;
	}

	/*
	public Set<String> getSubtables() {
		return subtables;
	}
*/

	
	
	public void addColumn(String name){
		if (firstAddedColumn == null) firstAddedColumn = name;
		columns.put(name, new DataType(Types.VARCHAR, 255));
	}
	
	public void addColumn(String name, int dataType) {
		addColumn(name, dataType, 0, 0);
	}
	
	public void addColumn(String name, int dataType, int arg1) {
		addColumn(name, dataType, arg1, 0);
	}
		
	public void addColumn(String name, int dataType, int arg1, int arg2){
		addColumn(name, dataType, arg1, arg2, false);
	}
	
	private void addColumn(String name, int dataType, int arg1, int arg2, boolean doNotRecognizeAsPrimaryKey){
		if (firstAddedColumn == null&& !doNotRecognizeAsPrimaryKey) firstAddedColumn = name;
		
		if (!columns.containsKey(name)) columns.put(name, new DataType(dataType, arg1, arg2));
		else {
			DataType existingColumn = columns.get(name);
			if (existingColumn.type == dataType) {
				if (arg1 > existingColumn.arg1) existingColumn.arg1 = arg1;
				if (arg2 > existingColumn.arg2) existingColumn.arg2 = arg2;
			}
			
			else if ((existingColumn.type == Types.NULL && dataType != Types.NULL)
					||(existingColumn.type == Types.INTEGER && dataType == Types.BIGINT)
					|| (existingColumn.type == Types.BIGINT && dataType == Types.DECIMAL) 
					|| (existingColumn.type != Types.VARCHAR && dataType == Types.VARCHAR)
					) {
				existingColumn.type = dataType;
				existingColumn.arg1 = arg1;
				existingColumn.arg2 = arg2;
			}
		}
	}
	
	public void addHeaderColumn(String name, int dataType) {
		addHeaderColumn(name, dataType, 0, 0);
	}
	
	public void addHeaderColumn(String name, int dataType, int arg1) {
		addHeaderColumn(name, dataType, arg1, 0);
	}
	
	public void addHeaderColumn(String name, int dataType, int arg1, int arg2) {
		addColumn(name, dataType, arg1, arg2, true);
	}
	
	public String getUniqueKey(){
		//if (columns.containsKey("id")) return "id";
		//if (columns.containsKey("name")) return "name";
		for (String primaryKey: primaryKeyNames) {
			if (columns.containsKey(primaryKey)) return primaryKey;
		}
		
		return firstAddedColumn;
	}

	public void addSubtable(String name) {
		subtables.add(name);
		
	}
	
	public String toString(){
		
		/*
		StringBuilder sb = new StringBuilder();
		
		sb.append("CREATE TABLE ");
		
		sb.append(name);
		sb.append(" (");
		columns.forEach((key, value)->{			
			sb.append(key);
			sb.append(' ');
			sb.append(value);
			sb.append(", ");
		});
		sb.delete(sb.length() - 2, sb.length());
		sb.append(");");
		return sb.toString();
		*/
		return TableStructureDDLGenerator2.generateDDL(this, null);
	}

	private static List<String> primaryKeyNames = new ArrayList<String>();
	
	public static void addPrimaryKey(String primaryKeyName) {
		primaryKeyNames.add(primaryKeyName);
	}
	
	
	public static class DataType{
		public int type;
		public int arg1;
		public int arg2;
		
		public DataType(int type, int arg1, int arg2) {
			this.type = type;
			this.arg1 = arg1;
			this.arg2 = arg2;
		}
		
		public DataType(int type, int arg1) {
			this.type = type;
			this.arg1 = arg1;
			this.arg2 = 0;
		}
		
		public DataType(int type) {
			this.type = type;
			this.arg1 = 0;
			this.arg2 = 0;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + arg1;
			result = prime * result + arg2;
			result = prime * result + type;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DataType other = (DataType) obj;
			if (arg1 != other.arg1)
				return false;
			if (arg2 != other.arg2)
				return false;
			if (type != other.type)
				return false;
			return true;
		}
	}
}
