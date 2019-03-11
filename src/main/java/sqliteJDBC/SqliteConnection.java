package sqliteJDBC;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.sqlite.SQLiteConfig;

import JDBC.DBConnection;

public class SqliteConnection implements DBConnection {
		
	private String path;
	private Connection conn;
		
	public SqliteConnection(String filename) throws ClassNotFoundException, SQLException{
       
		Class.forName("org.sqlite.JDBC");
		path = "jdbc:sqlite:" + filename;
		SQLiteConfig config = new SQLiteConfig();
		config.setEncoding(SQLiteConfig.Encoding.UTF16);
	    
	}
	
	
	@Override
	public void close() throws SQLException{
        if (conn != null) {
        	conn.close();            
        }
		
	}

	@Override
	public void executeUpdate(String sql) throws SQLException {		
		conn.createStatement().executeUpdate(sql);		
	}
	
	@Override
	public void executeUpdate(String sql, String[] params) throws SQLException {
				
		//Statement statement = conn.createStatement();
		PreparedStatement statement = conn.prepareStatement(sql);
		for (int i = 1; i <= params.length; i ++){
			statement.setString(i, params[i-1]);
		}
		//statement.executeUpdate(sql);
		statement.executeUpdate();
	}
	
	@Override
	public void executeUpdateFromFile(String sql) throws SQLException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(sql));
		StringBuilder sb = new StringBuilder();
		while(true){
			String line = br.readLine();
			if (line == null) break;
			
			if (!line.replace('\t', ' ').trim().startsWith("--")) sb.append(line);
			sb.append(' ');			
		}
		br.close();			
		
		conn.createStatement().executeUpdate(sb.toString());
		//conn.prepareStatement(sb.toString()).executeUpdate();
	}
	
	@Override
	public ResultSet executeQuery(String sql) throws SQLException{
		return conn.createStatement().executeQuery(sql);
		//return conn.prepareStatement(sql).executeQuery();
	
	}	

	@Override
	public ResultSet executeQuery(String sql, String[] params) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(sql);
		
		for (int i = 1; i <= params.length; i ++){
			statement.setString(i, params[i-1]);
		}
		return statement.executeQuery();
		
	}
	
	@Override
	public ResultSet executeQueryFromFile(String sqlFile) throws SQLException, IOException{
		/*
		BufferedReader br = new BufferedReader(new FileReader(sqlFile));
		StringBuilder sb = new StringBuilder();
		while(true){
			String line = br.readLine();
			if (line == null) break;
			if (!line.replace('\t', ' ').trim().startsWith("--")) sb.append(line);
			sb.append(' ');	
		}
		br.close();
		return executeQuery(sb.toString());
		*/
		return executeQueryFromFile(sqlFile, null);
		//return conn.prepareStatement(sb.toString()).executeQuery();
	
	}
	


	@Override
	public void executeUpdateFromFile(String sql, String[] params) throws SQLException, IOException {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public ResultSet executeQueryFromFile(String sqlFile, String[] params) throws SQLException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(sqlFile));
		StringBuilder sb = new StringBuilder();
		while(true){
			String line = br.readLine();
			if (line == null) break;
			if (!line.replace('\t', ' ').trim().startsWith("--")) sb.append(line);
			sb.append(' ');	
		}
		br.close();
		
		if (params != null) return executeQuery(sb.toString(), params);
		else return executeQuery(sb.toString());
		//return conn.prepareStatement(sb.toString()).executeQuery();
	}

	@Override
	public boolean connect() throws SQLException {
		conn = DriverManager.getConnection(path);
	    if (conn != null) return true;
	    else return false;
	}
	
	@Override
	public void disconnect() throws SQLException {
		conn.close();
		
	}















}
