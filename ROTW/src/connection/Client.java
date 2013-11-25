package connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class Client {
	
	private SocketManager clientSocket;
	private String serverAnswer = null;
	
	public Client(String address, int port) throws ConnectException {
		try {
			clientSocket = new SocketManager(address, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch(ConnectException e) {
			throw new ConnectException();
		}catch (IOException e) {
			e.printStackTrace();
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
	
	public void getSensorList() {
		try {
			clientSocket.Escribir("LISTSENSOR\n");
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
	}
	
	public void getSensorRecord(String sensor_id) {
		try {
			clientSocket.Escribir("HISTORICO " + sensor_id + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
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
			clientSocket.Escribir("GET_VALACT " + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
	}
	
	public void getPhoto() {
		try {
			clientSocket.Escribir("GET_FOTO \n");
			serverAnswer = clientSocket.Leer();
		} catch(IOException e) {
			
		}
		System.out.println(serverAnswer);
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
		} catch (ConnectException e) {
			
		}
		//c.getSensorList();
		//c.getPhoto();
	}
	
}
