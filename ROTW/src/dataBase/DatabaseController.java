package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.naming.NoPermissionException;

import util.NotFoundException;
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
	
	public static synchronized boolean validateUserName(String userName) {
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
		public static synchronized boolean validateUser(String userName, String password) {
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
		
		public static synchronized ArrayList<Sensor> getSensorList() {
			ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
			Connection connection = connectToDatabase();
			PreparedStatement statement;
			try {
				statement = connection.prepareStatement("Select ID, NAME, STATE from SENSOR;");
				ResultSet result = statement.executeQuery();
				while(result.next()) {
					sensorList.add(new Sensor(result.getInt("ID"), result.getString("NAME"), Utilities.intToBoolean(result.getInt("STATE"))));
				}
				result.close();
				statement.close();
				connection.close();
				for(int i = 0; i < sensorList.size(); i++) {
					System.out.println(sensorList.get(i).toString());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return sensorList;
		}
		
		public static synchronized ArrayList<Record> getSensorRecord(String sensor_id) throws Exception {
			ArrayList<Record> recordList = new ArrayList<Record>();
			Connection connection = connectToDatabase();
			PreparedStatement statement;
			try {
				statement = connection.prepareStatement("Select * from SENSOR where ID = ?");
				statement.setString(1, sensor_id);
				ResultSet result = statement.executeQuery();
				if(!result.next()) {
					result.close();
					statement.close();
					connection.close();
					throw new Exception();
				}
				statement = connection.prepareStatement("Select DATE, XDEGREE, XMIN, XSEC, YDEGREE, YMIN, YSEC, VALUE, ID_SENSOR from RECORD where ID_SENSOR = ?;");
				statement.setString(1, sensor_id);
				result = statement.executeQuery();
				while(result.next()) {
					recordList.add(new Record(result.getString("DATE"), result.getFloat("XDEGREE"), result.getFloat("XMIN"),
							result.getFloat("XSEC"), result.getFloat("YDEGREE"), result.getFloat("YMIN"), result.getFloat("YSEC"),
							result.getFloat("VALUE"), result.getInt("ID_SENSOR")));
				}
				result.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return recordList;
		}
		
		public static synchronized void enableSensor(String sensor_id) throws NoPermissionException, NotFoundException {
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select s.ID, NAME, STATE from SENSOR s, USER u where s.ID = ? AND USER_ID = u.ID;");
				statement.setString(1, sensor_id);
				ResultSet result = statement.executeQuery();
				if(!result.next()) {
					result.close();
					statement.close();
					connection.close();
					throw new NotFoundException();
				} else {
					if(!Utilities.intToBoolean(result.getInt("STATE"))) {
						statement = connection.prepareStatement("Update SENSOR set state = 1 where ID = ?");
						statement.setString(1, sensor_id);
						statement.execute();
						result.close();
						statement.close();
						connection.close();
					} else {
						result.close();
						statement.close();
						connection.close();
						throw new NoPermissionException();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static synchronized void disableSensor(String sensor_id) throws NoPermissionException, NotFoundException {
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select s.ID, NAME, STATE from SENSOR s, USER u where s.ID = ? AND USER_ID = u.ID;");
				statement.setString(1, sensor_id);
				ResultSet result = statement.executeQuery();
				if(!result.next()) {
					result.close();
					statement.close();
					connection.close();
					throw new NotFoundException();
				} else {
					if(Utilities.intToBoolean(result.getInt("STATE"))) {
						statement = connection.prepareStatement("Update SENSOR set state = 0 where ID = ?");
						statement.setString(1, sensor_id);
						statement.execute();
						result.close();
						statement.close();
						connection.close();
					} else {
						result.close();
						statement.close();
						connection.close();
						throw new NoPermissionException();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static synchronized void enableGPS() throws NoPermissionException {
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select g.* from GPS;");
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					if(!Utilities.intToBoolean(result.getInt("STATE"))) {
						statement = connection.prepareStatement("Update GPS set STATE = 1;");
						statement.execute();
						result.close();
						statement.close();
						connection.close();
					} else {
						result.close();
						statement.close();
						connection.close();
						throw new NoPermissionException();
					}
				}
				result.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
			
			}
		}
		
		public static synchronized void disableGPS() throws NoPermissionException {
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select g.* from GPS;");
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					if(Utilities.intToBoolean(result.getInt("STATE"))) {
						statement = connection.prepareStatement("Update GPS set STATE = 0;");
						statement.execute();
						result.close();
						statement.close();
						connection.close();
					} else {
						result.close();
						statement.close();
						connection.close();
						throw new NoPermissionException();
					}
				}
				result.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
			
			}
		}
		
		public static synchronized Sensor getSensor(String sensor_id) throws NotFoundException {
			Sensor sensor = null;
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select * from SENSOR where ID = ?");
				statement.setString(1, sensor_id);
				ResultSet resultSet = statement.executeQuery();
				if(resultSet.next()) {
					sensor = new Sensor(resultSet.getInt("ID"), resultSet.getString("NAME"), Utilities.intToBoolean(resultSet.getInt("STATE")));
					resultSet.close();
					statement.close();
					connection.close();
				} else {
					resultSet.close();
					statement.close();
					connection.close();
					throw new NotFoundException();
				}
			} catch (SQLException e) {
			}
			return sensor;
		}
		
		public static synchronized void updateRecord(Record r) {
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Insert into RECORD(DATE, XDEGREE, XMIN, XSEC, YDEGREE, YMIN, YSEC, VALUE, ID_SENSOR)"+
						" values(?, ?, ?, ?, ?, ?, ?, ?, ?);");
				statement.setString(1, r.getDate() + " " + r.getTime());
				statement.setString(2, String.valueOf(r.getxDegree()));
				statement.setString(3, String.valueOf(r.getxMin()));
				statement.setString(4, String.valueOf(r.getxSec()));
				statement.setString(5, String.valueOf(r.getyDegree()));
				statement.setString(6, String.valueOf(r.getyMin()));
				statement.setString(7, String.valueOf(r.getySec()));
				statement.setString(8, String.valueOf(r.getValue()));
				statement.setString(9, String.valueOf(r.getSensor_id()));
				statement.execute();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static synchronized ArrayList<User> getUsers() {
			ArrayList<User> userlist = new ArrayList<User>();
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select * from USER;");
				ResultSet result = statement.executeQuery();
				while(result.next()) {
					userlist.add(new User(result.getString("USERNAME"), result.getString("PASSWORD")));
				}
				result.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return userlist;
		}
		
		public static synchronized void updateUserName(String oldName, String newName) throws IllegalAccessError {
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select * from USER where USERNAME = ?;");
				statement.setString(1, newName);
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					result.close();
					statement.close();
					connection.close();
					throw new IllegalAccessError();
				} else {
					statement = connection.prepareStatement("Update USER set USERNAME = ? where USERNAME = ?;");
					statement.setString(1, newName);
					statement.setString(2, oldName);
				}
				statement.execute();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static synchronized void updatePassword(String currentUserName, String oldPassword, String newPassword) {
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Update USER set PASSWORD = ? where USERNAME = ? and PASSWORD = ?;");
				statement.setString(1, newPassword);
				statement.setString(2, currentUserName);
				statement.setString(3, oldPassword);
				statement.execute();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static synchronized void addUser(String username, String password) throws IllegalAccessError {
			Connection connection = connectToDatabase();
			PreparedStatement statement;
			try {
				statement = connection.prepareStatement("Select * from USER where USERNAME = ?;");
				statement.setString(1, username);
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					result.close();
					statement.close();
					connection.close();
					throw new IllegalAccessError();
				} else {
					statement = connection.prepareStatement("Insert into USER(USERNAME, PASSWORD) values(?, ?);");
					statement.setString(1, username);
					statement.setString(2, password);
					statement.execute();
					statement.close();
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static synchronized void removeUser(String username, String password) {
			Connection connection = connectToDatabase();
			PreparedStatement statement;
			try {
				statement = connection.prepareStatement("Select * from USER where USERNAME = ?;");
				statement.setString(1, username);
				ResultSet result = statement.executeQuery();
				if(!result.next()) {
					result.close();
					statement.close();
					connection.close();
					throw new IllegalAccessError();
				} else {
					statement = connection.prepareStatement("Delete from USER where USERNAME = ? and PASSWORD = ?;");
					statement.setString(1, username);
					statement.setString(2, password);
					statement.execute();
					statement.close();
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static synchronized boolean isGPSActivated() {
			boolean activated = false;
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select * from GPS;");
				ResultSet resultSet = statement.executeQuery();
				activated = Utilities.intToBoolean(resultSet.getInt("STATE"));
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return activated;
		}
		
		public static synchronized GPSRecord getGPSRecord() {
			Connection connection = connectToDatabase();
			GPSRecord gpsrecord = null;
			try {
				PreparedStatement statement = connection.prepareStatement("Select * from GPSRECORD;");
				ResultSet resultSet = statement.executeQuery();
				gpsrecord = new GPSRecord(resultSet.getFloat("XDEGREE"), resultSet.getFloat("XMIN"), resultSet.getFloat("XSEC"),
						resultSet.getFloat("YDEGREE"), resultSet.getFloat("YMIN"), resultSet.getFloat("YSEC"), resultSet.getInt("GPS_SENSOR"));
				resultSet.close();
				statement.close();
				connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return gpsrecord;
		}
		
		public static synchronized String getCellValue(String cell_id) throws IllegalAccessError {
			Connection connection = connectToDatabase();
			String value = "";
			try {
				PreparedStatement statement = connection.prepareStatement("Select * from CELL where CELL_ID = ?");
				statement.setString(1, cell_id);
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					value = new DecimalFormat("0.##").format(result.getFloat("XDEGREE")) + "\u00b0" + 
						new DecimalFormat("0.##").format(result.getFloat("XMIN")) + "'" + new DecimalFormat("0.##").format(result.getFloat("XSEC")) +
						"\"" + "-" + new DecimalFormat("0.##").format(result.getFloat("YDEGREE")) + "\u00b0" + 
						new DecimalFormat("0.##").format(result.getFloat("YMIN")) + "'" +
						new DecimalFormat("0.##").format(result.getFloat("YSEC")) + "\"";
					result.close();
					statement.close();
					connection.close();
				} else {
					throw new IllegalAccessError();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return value;
		}
}
