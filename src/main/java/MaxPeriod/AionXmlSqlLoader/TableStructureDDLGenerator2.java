package MaxPeriod.AionXmlSqlLoader;

import java.util.Map;
import java.util.Set;

import MaxPeriod.AionXmlSqlLoader.TableStructure2.DataType;

import java.sql.Types;

public class TableStructureDDLGenerator2 {
	public static String generateDDL(TableStructure2 table, Set<String> DBMSReservedWords){
		String name = table.getName();
		Map<String, DataType> columns = table.getColumns();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("CREATE TABLE ");
		/*
		if (prefix != null){
			sb.append(prefix);
			sb.append('_');
		}
		*/
		sb.append('\"');
		sb.append(name);
		sb.append('\"');
		sb.append(" (\n");
		columns.forEach((key, value)->{			
			sb.append('\"');
			sb.append(key);
			sb.append('\"');
			
			sb.append(' ');
			
			/*if (DBMSReservedWords != null && DBMSReservedWords.contains(value.toUpperCase())) sb.append(value + "_R");
			else*/
			
			//sb.append(value);
			switch(value.type) {
			case Types.INTEGER:
				sb.append(String.format("INTEGER"));
				break;
			case Types.BIGINT:
				sb.append(String.format("BIGINT"));
				break;	
			case Types.DECIMAL:
				sb.append(String.format("DECIMAL(%d, %d)", value.arg1, value.arg2));
				break;
			case Types.VARCHAR:
				sb.append(String.format("VARCHAR(%d)", value.arg1));			
				break;
				
			}
			
			sb.append("\n,");
		});
		sb.delete(sb.length() - 2, sb.length());
		sb.append("\n);");
		return sb.toString();
	}
}
