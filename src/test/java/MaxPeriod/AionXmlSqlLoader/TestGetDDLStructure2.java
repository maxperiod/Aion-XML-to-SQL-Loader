package MaxPeriod.AionXmlSqlLoader;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

		Set<PotentialTable> potentialTables = new HashSet<PotentialTable>();
		
		AionXmlFindPotentialTables2 handler = new AionXmlFindPotentialTables2(potentialTables);
		
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(handler);		
		
				
		xr.parse("D:\\Aion\\extract_data\\animationmarkers\\bug_fluid.xml");
		xr.parse("D:\\Aion\\extract_data\\animationmarkers\\butterfly.xml");
		xr.parse("D:\\Aion\\extract_data\\animationmarkers\\agrint_summer.xml");
		
		handler.elementNameCannotBeTable();
		
		Map<String, TableStructure2> tableStructures = new HashMap<String, TableStructure2>();
		
		AionXmlGetColumnsBase handler2 = new AionXmlGetColumnsBase(potentialTables); 
		//AionXmlGetColumns4 handler2 = new AionXmlGetColumns4(potentialTables, tableStructures);
		Map<String, DataType> additional = new HashMap<String, DataType>();
		handler2.setAdditionalColumns(additional);
		
		xr.setContentHandler(handler2);		
		//handler2.enableSystemOutPrintln();
		
		xr.parse("D:\\Aion\\extract_data\\animationmarkers\\bug_fluid.xml");
		xr.parse("D:\\Aion\\extract_data\\animationmarkers\\butterfly.xml");
		xr.parse("D:\\Aion\\extract_data\\animationmarkers\\agrint_summer.xml");
		
		AionXmlGetColumns4 handler2a = new AionXmlGetColumns4(potentialTables, tableStructures);		
		handler2a.setAdditionalColumns(additional);
		
		xr.setContentHandler(handler2a);		
		//handler2a.enableSystemOutPrintln();
		
		xr.parse("D:\\Aion\\extract_data\\animationmarkers\\bug_fluid.xml");
		xr.parse("D:\\Aion\\extract_data\\animationmarkers\\butterfly.xml");
		xr.parse("D:\\Aion\\extract_data\\animationmarkers\\agrint_summer.xml");
		
		fail("manual check");
	}
	
	//D:\Aion\extract_l10n\ENU\data\strings\client_strings_msg.xml
	
	@Test
	public void testmsg() throws SAXException, IOException{

		Set<PotentialTable> potentialTables = new HashSet<PotentialTable>();
		
		AionXmlFindPotentialTables2 handler = new AionXmlFindPotentialTables2(potentialTables);
		
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(handler);		
		
				
		xr.parse("E:\\eclipse workspace\\AionXmlSqlLoader\\testcasedata\\teststring\\client_strings_msg_short.xml");
				
		handler.elementNameCannotBeTable();
		
		Map<String, TableStructure2> tableStructures = new HashMap<String, TableStructure2>();
		
		AionXmlGetColumnsBase handler2 = new AionXmlGetColumnsBase(potentialTables); 
		//AionXmlGetColumns4 handler2 = new AionXmlGetColumns4(potentialTables, tableStructures);
		Map<String, DataType> additional = new HashMap<String, DataType>();
		handler2.setAdditionalColumns(additional);
		
		xr.setContentHandler(handler2);		
		handler2.enableSystemOutPrintln();
		
		xr.parse("E:\\eclipse workspace\\AionXmlSqlLoader\\testcasedata\\teststring\\client_strings_msg_short.xml");		
		
		fail("manual check");
	}
	
	
	@Test
	public void testgather() throws SAXException, IOException{

		Set<PotentialTable> potentialTables = new HashSet<PotentialTable>();
		
		AionXmlFindPotentialTables2 handler = new AionXmlFindPotentialTables2(potentialTables);
		
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(handler);		
		
				
		xr.parse("D:\\Aion\\extract_data\\Gather\\gather_src.xml");
				
		handler.elementNameCannotBeTable();
		
		Map<String, TableStructure2> tableStructures = new HashMap<String, TableStructure2>();
		
		AionXmlGetColumnsBase handler2 = new AionXmlGetColumnsBase(potentialTables); 
		//AionXmlGetColumns4 handler2 = new AionXmlGetColumns4(potentialTables, tableStructures);
		Map<String, DataType> additional = new HashMap<String, DataType>();
		handler2.setAdditionalColumns(additional);
		
		xr.setContentHandler(handler2);		
		handler2.enableSystemOutPrintln();
		
		xr.parse("D:\\Aion\\extract_data\\Gather\\gather_src.xml");		
		
		fail("manual check");
	}
	
	
	@Test
	public void testMission() throws SAXException, IOException{

		Set<PotentialTable> potentialTables = new HashSet<PotentialTable>();
		
		AionXmlFindPotentialTables2 handler = new AionXmlFindPotentialTables2(potentialTables);
		
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(handler);		
		
				
		xr.parse("D:\\Aion\\extract_levels\\lf1\\mission_mission0.xml");
				
		handler.elementNameCannotBeTable();
		
		Map<String, TableStructure2> tableStructures = new HashMap<String, TableStructure2>();
		
		AionXmlGetColumnsBase handler2 = new AionXmlGetColumnsBase(potentialTables); 
		//AionXmlGetColumns4 handler2 = new AionXmlGetColumns4(potentialTables, tableStructures);
		Map<String, DataType> additional = new HashMap<String, DataType>();
		handler2.setAdditionalColumns(additional);
		
		xr.setContentHandler(handler2);		
		//handler2.enableSystemOutPrintln();
		
		xr.parse("D:\\Aion\\extract_levels\\lf1\\mission_mission0.xml");		
		
		AionXmlGetColumns4 handler2a = new AionXmlGetColumns4(potentialTables, tableStructures);		
		handler2a.setAdditionalColumns(additional);
		
		xr.setContentHandler(handler2a);		
		//handler2a.enableSystemOutPrintln();
		
		xr.parse("D:\\Aion\\extract_levels\\lf1\\mission_mission0.xml");
		
		AionXmlLoadRecords3 handler3 = new AionXmlLoadRecords3(potentialTables, tableStructures);
		Map<String, String> additionalValues = new HashMap<String, String>(); 
		
		handler3.setAdditionalColumnValues(additionalValues);
		handler3.setDatabaseConnection(mock(DBConnection.class));
		
		xr.setContentHandler(handler3);
		handler3.enableSystemOutPrintln();
		
		xr.parse("D:\\Aion\\extract_levels\\lf1\\mission_mission0.xml");
		
		fail("manual check");
	}
	
	//D:\Aion\extract_data\Gather\gather_src.xml
	
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
