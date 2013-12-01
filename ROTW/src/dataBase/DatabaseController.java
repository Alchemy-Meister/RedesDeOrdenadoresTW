package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		
		public static synchronized void enableGPS(String userName) throws NoPermissionException {
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select g.* from GPS g, USER u where g.USER_ID = u.ID and u.USERNAME = ?;");
				statement.setString(1, userName);
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					if(!Utilities.intToBoolean(result.getInt("STATE"))) {
						statement = connection.prepareStatement("Update GPS set STATE = 1 where USER_ID = (Select ID from USER where USERNAME = ?);");
						statement.setString(1, userName);
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
		
		public static synchronized void disableGPS(String userName) throws NoPermissionException {
			Connection connection = connectToDatabase();
			try {
				PreparedStatement statement = connection.prepareStatement("Select g.* from GPS g, USER u where g.USER_ID = u.ID and u.USERNAME = ?;");
				statement.setString(1, userName);
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					if(Utilities.intToBoolean(result.getInt("STATE"))) {
						statement = connection.prepareStatement("Update GPS set STATE = 0 where USER_ID = (Select ID from USER where USERNAME = ?);");
						statement.setString(1, userName);
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
}
