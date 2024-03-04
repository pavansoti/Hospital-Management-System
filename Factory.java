package project2;

import java.sql.Connection;
import java.sql.DriverManager;

public class Factory {
	
	private static Connection con=null;
	
	public static Connection connectionProvider() {
		
		if(con==null) {
			
			try {
				//load the driver 
				Class.forName("com.mysql.cj.jdbc.Driver");
				//creating connection
				String url="jdbc:mysql://localhost:3306/hospital";
				con=DriverManager.getConnection(url, "root", "root");
				
			}catch(Exception e) {
				
				e.printStackTrace();
				
			}
		}
		return con;
	}

}
