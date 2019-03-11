package MaxPeriod.AionXmlSqlLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLFinder{
	private String directory;
	
	private List<String> excludeDirectories;		
	private List<String> xmlFilesInFolder;
	
	public XMLFinder(String directory){
		excludeDirectories = new ArrayList<String>();
		xmlFilesInFolder = new ArrayList<String>();
		this.directory = directory;			
	}
	
	public void excludeDirectory(String directory){
		excludeDirectories.add(directory.toLowerCase());
	}
	
	public void doProcess(){
		lookForXmlFilesInFolder(xmlFilesInFolder, "");
	}
	
	public List<String> getXmlFilesInFolder(){			
		return xmlFilesInFolder;
	}
	
	private void lookForXmlFilesInFolder(List<String> xmlFilesList, String subdirectory){
		//System.out.println(subdirectory);
		File xmlsubdirectory = new File(directory + '/' + subdirectory);
		
		String[] xmlFiles = xmlsubdirectory.list();
		for (String xmlFile: xmlFiles){
			if (xmlFile.matches(".*[.]xml"))				
				//System.out.println(xmlsubdirectory + "/" + xmlFile);
				xmlFilesList.add(subdirectory + "/" + xmlFile); //(loadXmlFile(sqliteDatabaseFile, subdirectory, xmlFile, prefix);
			else if (new File(directory + '/' + subdirectory + "/" + xmlFile).isDirectory() && !excludeDirectories.contains(xmlFile.toLowerCase())){
				lookForXmlFilesInFolder(xmlFilesList, subdirectory + "/" + xmlFile);
			}
		}
	}

	public String getDirectory() {
		
		return directory;
	}
}