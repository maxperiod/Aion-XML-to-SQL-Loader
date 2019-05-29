package MaxPeriod.Process;

import java.io.IOException;
import java.sql.SQLException;

import org.xml.sax.SAXException;

import JDBC.DBConnection;
import MaxPeriod.AionXmlSqlLoader.AionLoadXMLFoldersToSqlite2;
import sqliteJDBC.SqliteConnection;

public class LoadAionXmlToSqliteSeparate {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, SAXException, IOException {
		
		new Thread(() -> {
				try {
				DBConnection connection = new SqliteConnection("C:/stuff/Aion65NA.db");
				AionLoadXMLFoldersToSqlite2 loader = new AionLoadXMLFoldersToSqlite2(connection);
				//AionLoadXMLFoldersToSqlite loader = new AionLoadXMLFoldersToSqlite("C:/stuff/aionEU62PTS.db");
				
				loader.setLogFile("failure.log");
		
				loader.addFolder("D:/Aion/extract_data");	
				loader.excludeFolder("cutscene");
				loader.excludeFolder("dialog");
				loader.excludeFolder("help");
				loader.excludeFolder("Strings");
				loader.excludeFolder("ui");
				
				//loader.enableSystemOutPrintln();
				
				loader.doProcess();
			} catch (ClassNotFoundException | SQLException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//failures: animations, Dialog, Tutorials
			
		}).start();
		/*
		new Thread(() -> {
			try {
				DBConnection connection = new SqliteConnection("C:/stuff/Aion65TW.db");
				AionLoadXMLFoldersToSqlite2 loader = new AionLoadXMLFoldersToSqlite2(connection);
				//AionLoadXMLFoldersToSqlite loader = new AionLoadXMLFoldersToSqlite("C:/stuff/aionEU62PTS.db");
				
				loader.setLogFile("failure.log");
		
				loader.addFolder("E:\\AION_TW\\extract_data");
				loader.excludeFolder("cutscene");
				loader.excludeFolder("dialog");
				loader.excludeFolder("help");
				loader.excludeFolder("Strings");
				loader.excludeFolder("ui");
				
				//loader.enableSystemOutPrintln();
			
				loader.doProcess();
			} catch (ClassNotFoundException | SQLException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//failures: animations, Dialog, Tutorials
			
		}).start();
		*/
		new Thread(() -> {
			try {
				DBConnection connection = new SqliteConnection("C:/stuff/AionL10N.db");
				AionLoadXMLFoldersToSqlite2 loader = new AionLoadXMLFoldersToSqlite2(connection);
				
				
				loader.setLogFile("failure.log");
		
				loader.addFolder("D:/Aion/extract_L10N");
				loader.addFolder("E:/GameforgeLive/Games/GBR_eng/AION/Download/extract_l10n");
				loader.addFolder("E:\\AION_TW\\extract_l10n");
				
				//loader.enableSystemOutPrintln();
				
				loader.doProcess();
				
				//connection.executeUpdate("create index index_strings on strings (_folder, name);");
			} catch (ClassNotFoundException | SQLException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//failures: animations, Dialog, Tutorials
			
		}).start();
		
		new Thread(() -> {
			try {
				DBConnection connection = new SqliteConnection("C:/stuff/AionLevels65.db");
				AionLoadXMLFoldersToSqlite2 loader = new AionLoadXMLFoldersToSqlite2(connection);
				
				
				loader.setLogFile("failure.log");	

				loader.addFolder("D:/Aion/extract_levels");
		
				//loader.enableSystemOutPrintln();
				
				loader.doProcess();
				
				//connection.executeUpdate("create index index_strings on strings (_folder, name);");
			} catch (ClassNotFoundException | SQLException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//failures: animations, Dialog, Tutorials
			
		}).start();
	}
}
