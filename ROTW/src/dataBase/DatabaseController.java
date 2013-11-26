package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.Utilities;

public class DatabaseController {
	
	private static synchronized Connection connectToDatabase() {
		 try {
             //Loads the driver.
             Class.forName("org.sqlite.JDBC").newInstance();
             //returns the connection to the resource.
             return DriverManager.getConnection("jdbc:sqlite::resource:" + DatabaseController.class.getResource("database.sqlite"));
     } catch (ClassNotFoundException e) {
     
     } catch (SQLException e) {
     
     } catch (InstantiationException e) {
             e.printStackTrace();
     } catch (IllegalAccessException e) {
             e.printStackTrace();
     }
     return null;
	}
	
	public static synchronized boolean validateUserName(String userName) throws InterruptedException {
		Boolean correct = false;
		Connection connection = connectToDatabase();
		try {
			PreparedStatement statement = connection.prepareStatement("Select USERNAME from USER where USERNAME = ?;");
			statement.setString(1, userName);
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				correct = true;
			}
			result.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return correct;
	}
		public static synchronized boolean validateUser(String userName, String password) throws InterruptedException {
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
				result.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return correct;
		}
		
		public static synchronized ArrayList<Sensor> getSensorList(String userName) {
			ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
			Connection connection = connectToDatabase();
			PreparedStatement statement;
			try {
				statement = connection.prepareStatement("Select s.ID, NAME, STATE from SENSOR s, USER u where u.ID = USER_ID AND USERNAME = ?;");
				statement.setString(1, userName);
				ResultSet result = statement.executeQuery();
				while(result.next()) {
					sensorList.add(new Sensor(result.getInt("ID"), result.getString("NAME"), Utilities.intToBoolean(result.getInt("STATE"))));
				}
				for(int i = 0; i < sensorList.size(); i++) {
					System.out.println(sensorList.get(i).toString());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return sensorList;
		}
}
