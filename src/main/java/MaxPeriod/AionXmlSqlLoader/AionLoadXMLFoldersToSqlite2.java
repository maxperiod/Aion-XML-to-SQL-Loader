package MaxPeriod.AionXmlSqlLoader;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import JDBC.DBConnection;
import MaxPeriod.AionXmlSqlLoader.XMLFinder;
import sqliteJDBC.SqliteConnection;
import sqliteJDBC.SqliteReservedWordsList;

public class AionLoadXMLFoldersToSqlite2 {

	private static final Logger logger = LogManager.getLogger(AionLoadXMLFoldersToSqlite2.class);

	private String dbFile;
	private Map<String, XMLFinder> folders;
	
	private List<String> excludedFolders;
	
	private boolean printingEnabled = false;
	
	private String loggerFile;
	
	private Set<String> DBMSReservedWords = SqliteReservedWordsList.sqliteReservedWords;
	
	public AionLoadXMLFoldersToSqlite2(String sqliteDatabaseFile){
		dbFile = sqliteDatabaseFile;
		folders = new LinkedHashMap<String, XMLFinder>();
		excludedFolders = new ArrayList<String>();
	}
	
	public void addFolder(String folder){
		folders.put(folder, new XMLFinder(folder));
	}
	
	public void excludeFolder(String excludeFolder) {
		excludedFolders.add(excludeFolder);
	}
	
	public void enableSystemOutPrintln(){
		printingEnabled = true;
	}
	
	public void setLogFile(String file){		
		loggerFile = file;					 
		  
	}
	
	public void doProcess() throws ClassNotFoundException, SQLException, SAXException, IOException{

		/*
		Logger logger = Logger.getLogger(this.getClass().getName());
		FileHandler fileHandler = null;
		
		if (loggerFile != null){
			fileHandler = new FileHandler(loggerFile);
			fileHandler.setFormatter(new SimpleFormatter());
			//logger.setUseParentHandlers(false);
			logger.addHandler(fileHandler);
		}
*/
		
		Set<PotentialTable> potentialTables = new HashSet<PotentialTable>();
		
		Map<String, TableStructure2> tableStructures = new LinkedHashMap<String, TableStructure2>();
		TableStructure2.addPrimaryKey("id");
		TableStructure2.addPrimaryKey("name");
		
		//Map<String, String> additionalColumns = new LinkedHashMap<String, String>();
		Map<String, TableStructure2.DataType> additionalColumns = new LinkedHashMap<String, TableStructure2.DataType>();
		additionalColumns.put("_folder", new TableStructure2.DataType(Types.VARCHAR, 255));
		additionalColumns.put("_file", new TableStructure2.DataType(Types.VARCHAR, 255));	
					 
		//AionXmlFindPotentialTables handler = new AionXmlFindPotentialTables(tree);
		AionXmlFindPotentialTables2 handler = new AionXmlFindPotentialTables2(potentialTables);
				
		XMLReader xr = XMLReaderFactory.createXMLReader();
		
		AionXmlGetColumns4 handler2 = new AionXmlGetColumns4(potentialTables, tableStructures);
		

		handler2.setAdditionalColumns(additionalColumns);
		
		DBConnection connection = new SqliteConnection(dbFile);
		AionXmlLoadRecords3 handler3 = new AionXmlLoadRecords3(potentialTables, tableStructures);
		handler3.setDBConnection(connection);
		
		xr.setContentHandler(handler3);	
		
		if (printingEnabled){
			handler2.enableSystemOutPrintln();
			handler3.enableSystemOutPrintln();
			
		}
		
		folders.forEach((folder, xmlFinder) -> {
			
			excludedFolders.forEach((excludeFolder) -> xmlFinder.excludeDirectory(excludeFolder));
			/*
			xmlFinder.excludeDirectory("animationmarkers");
			xmlFinder.excludeDirectory("Animations");
			xmlFinder.excludeDirectory("cutscene");
			xmlFinder.excludeDirectory("Help");
			xmlFinder.excludeDirectory("Lite");
			xmlFinder.excludeDirectory("skybox");
			xmlFinder.excludeDirectory("skydome");
			xmlFinder.excludeDirectory("custompreset");
			xmlFinder.excludeDirectory("ui");
			*/
			System.out.printf("Searching for xml files in %s\n", folder);
			xmlFinder.doProcess();
		});
		
		//XMLFinder xmlFinder = new XMLFinder("E:/GameforgeLive/Games/GBR_eng/AION/Download/extract_l10n");
		
		//lookForXmlFilesInFolder(xmlFilesInFolder, "D:\\Aion\\extract_l10n");
		
		
		
		//xmlFinder2.excludeDirectory("cutscene");
		//xmlFinder2.doProcess();
		
		

		//LearnedXMLTree tree = new LearnedXMLTree();
		
		xr.setContentHandler(handler);		
		
		Set<String> keyset = folders.keySet();
		
		for (String folder: keyset){
			XMLFinder xmlFinder = folders.get(folder);
			
			for (String file: xmlFinder.getXmlFilesInFolder()){
				System.out.printf("Finding table elements from %s\n", file);
				try {
					xr.parse(xmlFinder.getDirectory() + '\\' + file);
				}
				catch(Exception e){
					logger.error(String.format("Failed to parse file %s - %s\n", file, e.getMessage()));
				}
			}
		}
		handler.elementNameCannotBeTable();
		
		/*
		folders.forEach((folder, xmlFinder) -> {
			for (String file: xmlFinder.getXmlFilesInFolder()){
				System.out.printf("Finding table elements from %s\n", file);
				xr.parse(xmlFinder.getDirectory() + '\\' + file);
			}
		});
		*/
		
		
		xr.setContentHandler(handler2);		
		System.out.println("Generating DDL");		
		
		for (String folder: keyset){
			XMLFinder xmlFinder = folders.get(folder);
			
			for (String file: xmlFinder.getXmlFilesInFolder()){
				System.out.printf("Generating DDL from %s\n", file);
				try {
					xr.parse(xmlFinder.getDirectory() + '\\' + file);
				}
				catch(Exception e){
					logger.error(String.format("Failed to parse file %s - %s\n", file, e.getMessage()));
				}
			}
		}
		/*
		folders.forEach((folder, xmlFinder) -> {
			for (String file: xmlFinder.getXmlFilesInFolder()){
				System.out.printf("Generating DDL from %s\n", file);
				xr.parse(xmlFinder.getDirectory() + '\\' + file);
			}
		});
		*/
		
		
		
	
		
		connection.connect();
		connection.executeUpdate("BEGIN;");		
		
		System.out.println("Aplying DDL into database");
		
		tableStructures.forEach((table, tableStructure) -> {
			logger.info(String.format("Creating table %s\n", table));
			try {
				connection.executeUpdate("DROP TABLE IF EXISTS " + table + ';');
				//connection.executeUpdate(DDL.toString());
				String DDL = tableStructure.toString();
				//connection.executeUpdate(TableStructureDDLGenerator2.generateDDL(DDL, DBMSReservedWords));
				connection.executeUpdate(DDL);
				logger.info(String.format("Table created:\n%s", DDL));
			} catch (SQLException e) {
				
				logger.error(String.format("DDL execution failed: %s\n%s", tableStructure, e.getMessage()));				
			}
		});
		

		xr.setContentHandler(handler3);	
		//handler3.setLogger(logger);
		System.out.println("Inserting into database");
		
		//handler3.setDBMSReservedWords(DBMSReservedWords);
		
		for (String folder: keyset){
			XMLFinder xmlFinder = folders.get(folder);
			
			for (String file: xmlFinder.getXmlFilesInFolder()){
				System.out.printf("Inserting records from %s\n", file);
				Map<String, String> additionalColumnValues = new LinkedHashMap<String, String>();
				
				int lastSlash = file.lastIndexOf('/', file.length());
				
				additionalColumnValues.put("_folder", file.substring(0, lastSlash));
				additionalColumnValues.put("_file", file.substring(lastSlash + 1, file.length()));			
				handler3.setAdditionalColumnValues(additionalColumnValues);
				
				try {
					xr.parse(xmlFinder.getDirectory() + '\\' + file);
				}
				catch(Exception e){
					logger.error(String.format("Failed to parse file %s - %s\n", file, e.getMessage()));
				}
			}
	
		}


		
		
		connection.executeUpdate("COMMIT;");
				
		connection.executeUpdate("create index index_strings on strings (folder, name);");
		
		connection.disconnect();
		
		//logger.removeHandler(fileHandler);
		//if (fileHandler != null) fileHandler.close();		
		
	}
}
