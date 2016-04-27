package cl.toeska.sqlitedb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MainDB {
	
	public static boolean dbUpdate (String dbUpdateString) { 
		
		String sDriverName = "org.sqlite.JDBC";
        try {
			Class.forName(sDriverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
 
        
        String dbName = "thesisProtDB";
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + dbName;
       
        Connection connection = null; 
        
        try {
			connection = DriverManager.getConnection(sDbUrl);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Statement stmnt = connection.createStatement();
			stmnt.executeUpdate(dbUpdateString);
		
			connection.close();
			
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return false;
			
		}
		
	}
	

}
