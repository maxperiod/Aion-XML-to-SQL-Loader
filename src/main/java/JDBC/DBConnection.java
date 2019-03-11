package JDBC;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBConnection {
							
		void close() throws SQLException;

		void executeUpdate(String sql) throws SQLException;
		void executeUpdate(String sql, String[] params) throws SQLException;
		
		void executeUpdateFromFile(String sql) throws SQLException, IOException;
		void executeUpdateFromFile(String sql, String[] params) throws SQLException, IOException;
		
		ResultSet executeQuery(String sql) throws SQLException;
		ResultSet executeQuery(String sql, String[] params) throws SQLException;
		
		ResultSet executeQueryFromFile(String sqlFile) throws SQLException, IOException;
		ResultSet executeQueryFromFile(String sqlFile, String[] params) throws SQLException, IOException;

		boolean connect() throws SQLException;
		void disconnect() throws SQLException;

}
