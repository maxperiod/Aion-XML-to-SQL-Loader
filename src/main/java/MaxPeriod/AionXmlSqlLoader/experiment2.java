package MaxPeriod.AionXmlSqlLoader;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import JDBC.DBConnection;
import sqliteJDBC.SqliteConnection;

public class experiment2 {

	public static void main(String[] args) throws SAXException, IOException, ClassNotFoundException, SQLException {

		{
			//Set<Triple<String, String, String>> potentialTables = new HashSet<Triple<String, String, String>>();
			Set<PotentialTable> potentialTables = new HashSet<PotentialTable>();
			
			AionXmlFindPotentialTables2 handler = new AionXmlFindPotentialTables2(potentialTables);
			
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(handler);		
					
			xr.parse("D:\\Aion\\extract_data\\Items\\client_item_enchanttable.xml");
			xr.parse("D:\\Aion\\extract_data\\World\\client_artifact.xml");			
			xr.parse("D:\\Aion\\extract_data\\World\\client_world_housing_lf_personal.xml");
			xr.parse("D:\\Aion\\extract_data\\animationmarkers\\divinecrusader.xml");
			handler.elementNameCannotBeTable();
			
			for (PotentialTable triple: potentialTables) {
				System.out.printf("tableName: %s / elementName: %s / parent: %s / level: %s\n", triple.tableName, triple.elementName, triple.parentElement, triple.level);
			}
			
			Map<String, TableStructure2> tableStructures = new LinkedHashMap<String, TableStructure2>();
			TableStructure2.addPrimaryKey("id");
			TableStructure2.addPrimaryKey("name");
			
			Map<String, TableStructure2.DataType> additionalColumns = new LinkedHashMap<String, TableStructure2.DataType>();
			additionalColumns.put("folder", new TableStructure2.DataType(Types.VARCHAR, 255));
			additionalColumns.put("file", new TableStructure2.DataType(Types.VARCHAR, 255));	
						 
			Map<String, String> additionalColumnsValue = new HashMap<String, String>();			
			
			//AionXmlGetColumnsBase handler2 = new AionXmlGetColumnsBase(potentialTables/*, null*/);
			AionXmlGetColumns4 handler2 = new AionXmlGetColumns4(potentialTables, tableStructures);
			//handler2.enableSystemOutPrintln();
			
			DBConnection connection = new SqliteConnection("C:\\Stuff\\hteht.db");
			connection.connect();
			if (connection != null) connection.executeUpdate("BEGIN;");
			
			handler2.setAdditionalColumns(additionalColumns);

			XMLReader xr2 = XMLReaderFactory.createXMLReader();
			xr2.setContentHandler(handler2);	
			xr2.parse("D:\\Aion\\extract_data\\Items\\client_item_enchanttable.xml");
			xr2.parse("D:\\Aion\\extract_data\\World\\client_artifact.xml");			
			xr2.parse("D:\\Aion\\extract_data\\World\\client_world_housing_lf_personal.xml");
			xr2.parse("D:\\Aion\\extract_data\\animationmarkers\\divinecrusader.xml");
			tableStructures = handler2.getTableStructures();
			
			for (String key: tableStructures.keySet()) {
				System.out.println(tableStructures.get(key));
				if (connection != null) {
					connection.executeUpdate("DROP TABLE IF EXISTS " + tableStructures.get(key).getName() + ";");
					connection.executeUpdate(tableStructures.get(key).toString());
				}
			}
			
			
			
			AionXmlLoadRecords3 handler3 = new AionXmlLoadRecords3(potentialTables, tableStructures);
			if (connection != null) handler3.setDBConnection(connection);
			
			//handler3.enableSystemOutPrintln();
			
			XMLReader xr3 = XMLReaderFactory.createXMLReader();
			xr3.setContentHandler(handler3);	
			
			additionalColumnsValue.put("folder", "D:\\Aion\\extract_data");
			additionalColumnsValue.put("file", "asdf.xml");
			handler3.setAdditionalColumnValues(additionalColumnsValue);
			
			xr3.parse("D:\\Aion\\extract_data\\Items\\client_item_enchanttable.xml");
			xr3.parse("D:\\Aion\\extract_data\\World\\client_artifact.xml");			
			xr3.parse("D:\\Aion\\extract_data\\World\\client_world_housing_lf_personal.xml");
			xr3.parse("D:\\Aion\\extract_data\\animationmarkers\\divinecrusader.xml");
			
			if (connection != null) connection.executeUpdate("COMMIT;");
		}
		/*
		{
			
			Set<Triple<String, String, String>> potentialTables = new HashSet<Triple<String, String, String>>();
			
			AionXmlFindPotentialTables2 handler = new AionXmlFindPotentialTables2(potentialTables);
			
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(handler);		
					
			//xr.parse("D:\\Aion\\extract_data\\World\\client_artifact.xml");
			xr.parse("D:\\Aion\\extract_data\\World\\client_world_housing_lf_personal.xml");
			
			for (Triple<String, String, String> triple: potentialTables) {
				System.out.printf("tableName: %s / elementName: %s / parent: %s\n", triple.first, triple.second, triple.third);
			}
			
			Map<String, TableStructure> tableStructures = new HashMap<String, TableStructure>();
			
			Map<String, String> additionalColumns = new LinkedHashMap<String, String>();
			additionalColumns.put("folder", "TEXT");
			additionalColumns.put("file", "TEXT");	
			
			AionXmlGetColumns4 handler2 = new AionXmlGetColumns4(potentialTables, null);
			handler2.enableSystemOutPrintln();
			handler2.setAdditionalColumns(additionalColumns);

			XMLReader xr2 = XMLReaderFactory.createXMLReader();
			xr2.setContentHandler(handler2);	
			xr2.parse("D:\\Aion\\extract_data\\World\\client_world_housing_lf_personal.xml");
		}
		*/
	}
}

