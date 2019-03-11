package MaxPeriod.AionXmlSqlLoader;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import JDBC.DBConnection;
import MaxPeriod.AionXmlSqlLoader.TableStructure2.DataType;
import sqliteJDBC.SqliteConnection;

public class TestGetDDLStructure2 {
	@Test
	public void test1() throws SAXException, IOException{
		String[] files = {"src/tests/client_item_random_option_test.xml"};
				
		
		Set<PotentialTable> potentialTables = new HashSet<PotentialTable>();
		
		AionXmlFindPotentialTables2 handler = new AionXmlFindPotentialTables2(potentialTables);
		
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(handler);		
						
		for (String file: files) xr.parse(file);
		
		handler.elementNameCannotBeTable();
		
		//assertTrue(potentialTables.contains(new PotentialTable("item_enchants", null, 0, "item_enchant")));
		//assertTrue(potentialTables.contains(new PotentialTable("enchant_attr_list", "item_enchant", 2, "data")));
		
		Map<String, TableStructure2> tableStructures = new LinkedHashMap<String, TableStructure2>();
					
		AionXmlGetColumns4 handler2 = new AionXmlGetColumns4(potentialTables, tableStructures);
		handler2.enableSystemOutPrintln();
		
		XMLReader xr2 = XMLReaderFactory.createXMLReader();
		xr2.setContentHandler(handler2);
		
		Map<String, DataType> additionalColumns = new LinkedHashMap<String, DataType>();
		additionalColumns.put("folder", new DataType(Types.VARCHAR, 255));
		additionalColumns.put("file", new DataType(Types.VARCHAR, 255));	
		handler2.setAdditionalColumns(additionalColumns);
		
		TableStructure2.addPrimaryKey("id");
		TableStructure2.addPrimaryKey("name");
		
		for (String file: files) xr2.parse(file);
			
		
		tableStructures = handler2.getTableStructures();
		
		assertTrue(tableStructures.containsKey("item_random_options"));
		assertTrue(tableStructures.get("item_random_options").getColumns().containsKey("folder"));
		
		DataType dataType;
		assertTrue(tableStructures.get("item_random_options").getColumns().containsKey("file"));
		dataType = tableStructures.get("item_random_options").getColumns().get("file");
		assertEquals(Types.VARCHAR, dataType.type);
		
		assertTrue(tableStructures.get("item_random_options").getColumns().containsKey("id"));
		dataType = tableStructures.get("item_random_options").getColumns().get("id");
		assertEquals(Types.INTEGER, dataType.type);
		
		assertTrue(tableStructures.get("item_random_options").getColumns().containsKey("name"));		
		assertFalse(tableStructures.get("item_random_options").getColumns().containsKey("random_attr_group_list"));			
		assertFalse(tableStructures.get("item_random_options").getColumns().containsKey("random_attr_group_list_id"));
		
		//assertEquals("item_random_option", tableStructures.get("item_random_options").getElementName());
		assertEquals("id", tableStructures.get("item_random_options").getUniqueKey());
		
		
		//assertEquals("CREATE TABLE item_random_options (src_folder TEXT, src_file TEXT, id TEXT, name TEXT);", tableStructures.get("item_random_options").toString());
		
		assertFalse(tableStructures.containsKey("item_random_options_id"));
		assertFalse(tableStructures.containsKey("item_random_options_prob"));
		
		//System.out.println(tableStructures.get("item_random_options_random_attr_group_list").toString());
		assertTrue(tableStructures.containsKey("item_random_options_random_attr_group_list"));
				
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("folder"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("file"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("item_random_options_id"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("attr_group_id"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("prob"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("random_attr1"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("random_attr2"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("random_attr3"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("random_attr4"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("random_attr5"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("random_attr6"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("random_attr7"));
		assertTrue(tableStructures.get("item_random_options_random_attr_group_list").getColumns().containsKey("random_attr8"));

		//assertEquals("data", tableStructures.get("item_random_options_random_attr_group_list").getElementName());
		
	}
	
	@Test
	public void test2() throws SAXException, IOException{
		
		

		Set<PotentialTable> potentialTables = new HashSet<PotentialTable>();
		
		AionXmlFindPotentialTables2 handler = new AionXmlFindPotentialTables2(potentialTables);
		
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(handler);		
		
				
		xr.parse("D:\\Aion\\extract_data\\World\\client_artifact.xml");
		xr.parse("D:\\Aion\\extract_data\\World\\client_world_housing_lf_personal.xml");
		
		handler.elementNameCannotBeTable();
		
		assertTrue(potentialTables.contains(new PotentialTable("client_artifacts", null, 0, "client_artifact")));
		assertTrue(potentialTables.contains(new PotentialTable("artifact_fee", "client_artifact", 2, "data")));
		
		assertTrue(potentialTables.contains(new PotentialTable("subzones", "clientzones", 1, "subzone")));
		
		assertTrue(potentialTables.contains(new PotentialTable("points", "points_info", 4, "data")));
		
		assertFalse(potentialTables.contains(new PotentialTable("subzone", "subzones", 2, "key_link")));
		
		assertTrue(potentialTables.contains(new PotentialTable("attributes", "clientzones", 1, "radar_area")));
		
	}
	

}
