package connection;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import dataBase.Sensor;

public class Client {
	
	private SocketManager clientSocket;
	private String serverAnswer = null;
	
	public Client(String address, int port) throws ConnectException, IOException {
		try {
			clientSocket = new SocketManager(address, port);
		} catch (ConnectException e) {
			throw new ConnectException();
		}catch (IOException e) {
			throw new IOException();
		}
	}
	
	public boolean validateUserName(String userName) {
		boolean answer = false;
		try {
			clientSocket.Escribir("USUARIO " + userName + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(serverAnswer != null) {
			if(serverAnswer.contains("OK")) {
				answer = true;
			}
			System.out.println(serverAnswer);
		}
		return answer;
		
	}
	
	public boolean validatePassword(String password) {
		boolean answer = false;
		try {
			clientSocket.Escribir("CLAVE " + password + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(serverAnswer != null) {
			if(serverAnswer.contains("OK")) {
				answer = true;
			}
			System.out.println(serverAnswer);
		}
		return answer;
	}
	
	public void signOut() {
		try {
			clientSocket.Escribir("SALIR\n");
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
	}
	
	public ArrayList<Sensor> getSensorList() {
		ArrayList<String> sensorListString = new ArrayList<String>();
		ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
		try {
			clientSocket.Escribir("LISTSENSOR\n");
			serverAnswer = clientSocket.Leer();
			sensorListString.add(serverAnswer);
			while(!serverAnswer.contains("322 OK")) {
				serverAnswer = clientSocket.Leer();
				System.out.println(serverAnswer);
				sensorListString.add(serverAnswer);
			}
			for(int i = 1; i < sensorListString.size() - 1; i++) {
				sensorList.add(Sensor.stringToSensor(sensorListString.get(i)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sensorList;
	}
	
	public ArrayList<String> getSensorRecord(String sensor_id) {
		ArrayList<String> recordList = new ArrayList<String>();
		try {
			clientSocket.Escribir("HISTORICO " + sensor_id + '\n');
			serverAnswer = clientSocket.Leer();
			recordList.add(new String(serverAnswer.getBytes(), "UTF-8"));
			while(!serverAnswer.contains("524 ERR") && !serverAnswer.contains("322 OK")) {
				serverAnswer = clientSocket.Leer();
				recordList.add(new String(serverAnswer.getBytes(), "UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < recordList.size(); i++) {
			System.out.println(recordList.get(i));
		}
		return recordList;
	}
	
	public void enableSensor(String sensor_id) {
		try {
			clientSocket.Escribir("ON " + sensor_id + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
	}
	
	public void disableSensor(String sensor_id) {
		try {
			clientSocket.Escribir("OFF " + sensor_id + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
	}
	
	public void enableGPS() {
		try {
			clientSocket.Escribir("ONGPS\n");
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
	}
	
	public void disableGPS() {
		try {
			clientSocket.Escribir("OFFGPS\n");
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
	}
	
	public void currentValue(String sensor_id) {
		try {
			clientSocket.Escribir("GET_VALACT " + sensor_id + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
	}
	
	public ImageIcon getPhoto() {
		ImageIcon image = null;
		try {
			clientSocket.Escribir("GET_FOTO \n");
			serverAnswer = clientSocket.Leer();
			
		} catch(IOException e) {
			
		}
		return image;
	}
	
	public void getLocation() {
		try {
			clientSocket.Escribir("GET_LOC\n");
			serverAnswer = clientSocket.Leer();
		} catch(IOException e) {
			
		}
		System.out.println(serverAnswer);
	}
	
	public static void main(String[] argv) {
		Client c = null;
		try {
			c = new Client("127.0.0.1", 1234);
		} catch (Exception e) {
			
		}
		if(c != null) {
			c.validateUserName("Albert Wesker");
			c.getSensorList();
			c.getSensorRecord("1");
			c.disableSensor("1");
			c.enableSensor("1");
			c.disableGPS();
			c.enableGPS();
			c.currentValue("1");
			c.getPhoto();
			c.signOut();
		}
	}
	
}
