package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectTestDB {
	public static void main(String [] args) {
		Connection conn;
		Statement stmt;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			String jdbcUrl = "jdbc:mysql://localhost:3306/SNL";
			String userId = "root";
			String userPass = "root";
			
			conn = DriverManager.getConnection(jdbcUrl, userId, userPass);
			stmt = conn.createStatement();
			
			System.out.println("연결 완료");
			stmt.close();
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
