package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseController {
	
	private static Connection connectToDatabase() {
		 try {
             //Loads the driver.
             Class.forName("org.sqlite.JDBC").newInstance();
             //returns the connection to the resource.
             return DriverManager.getConnection("jdbc:sqlite::resource:" + DatabaseController.class.getResource("dataBase.sqlite"));
     } catch (ClassNotFoundException e) {
     
     } catch (SQLException e) {
     
     } catch (InstantiationException e) {
             e.printStackTrace();
     } catch (IllegalAccessException e) {
             e.printStackTrace();
     }
     return null;
	}
	
	public static boolean validateUserName(String userName) throws InterruptedException {
		//TODO Delete delay simulation;
		Thread.sleep(1000);
		Boolean correct = false;
		Connection connection = connectToDatabase();
		try {
			PreparedStatement statement = connection.prepareStatement("Select USERNAME from USER where USERNAME = ?;");
			statement.setString(1, userName);
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				correct = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return correct;
	}
		public static boolean validateUser(String userName, String password) throws InterruptedException {
			Thread.sleep(1000);
			Boolean correct = false;
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select USERNAME, PASSWORD from USER where USERNAME = ? and PASSWORD = ?;");
				statement.setString(1, userName);
				statement.setString(2, password);
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					correct = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return correct;
		}
}
