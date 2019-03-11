package MaxPeriod.Process;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


import org.xml.sax.SAXException;

import MaxPeriod.AionXmlSqlLoader.AionLoadXMLFoldersToSqlite2;


public class LoadAionXmlToSqlite2 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, SAXException, IOException {
		// TODO Auto-generated method stub
		
		
		new Thread(() -> {
			AionLoadXMLFoldersToSqlite2 loader = new AionLoadXMLFoldersToSqlite2("C:/stuff/aionsqlite62NA.db");
			//AionLoadXMLFoldersToSqlite loader = new AionLoadXMLFoldersToSqlite("C:/stuff/aionEU62PTS.db");
			
			loader.setLogFile("failure.log");

			loader.addFolder("D:/Aion/extract_data");
			//loader.addFolder("E:/GameforgeLive/Games/GBR_eng/AION/Download/extract_data");
			loader.addFolder("D:/Aion/extract_L10N");
			loader.addFolder("E:/GameforgeLive/Games/GBR_eng/AION/Download/extract_l10n");
			loader.addFolder("E:\\AION_TW\\extract_l10n");
			
			//loader.enableSystemOutPrintln();
			try {
				loader.doProcess();
			} catch (ClassNotFoundException | SQLException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//failures: animations, Dialog, Tutorials
			
		}).start();
		
		new Thread(() -> {
			AionLoadXMLFoldersToSqlite2 loader = new AionLoadXMLFoldersToSqlite2("C:/stuff/aionsqlite65TW.db");
			//AionLoadXMLFoldersToSqlite loader = new AionLoadXMLFoldersToSqlite("C:/stuff/aionEU62PTS.db");
			
			loader.setLogFile("failure.log");

			loader.addFolder("E:\\AION_TW\\extract_data");
			//loader.addFolder("E:/GameforgeLive/Games/GBR_eng/AION/Download/extract_data");
			loader.addFolder("D:/Aion/extract_L10N");
			loader.addFolder("E:/GameforgeLive/Games/GBR_eng/AION/Download/extract_l10n");
			loader.addFolder("E:\\AION_TW\\extract_l10n");
			
			//loader.enableSystemOutPrintln();
			try {
				loader.doProcess();
			} catch (ClassNotFoundException | SQLException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//failures: animations, Dialog, Tutorials
			
		}).start();
		
	}

}
