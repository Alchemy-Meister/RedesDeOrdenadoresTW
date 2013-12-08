package connection;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.channels.NonReadableChannelException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import util.NotFoundException;
import dataBase.Sensor;

public class Client {
	
	private SocketManager clientSocket;
	private String serverAnswer = null;
	
	public Client(String address, int port) throws ConnectException, IOException {
		try {
			clientSocket = new SocketManager(address, port);
			clientSocket.setSoTimeout(5000);
		} catch (ConnectException e) {
			throw new ConnectException();
		}catch (IOException e) {
			throw new IOException();
		}
	}
	
	public boolean validateUserName(String userName) throws IllegalAccessError, SocketTimeoutException {
		boolean answer = false;
		try {
			clientSocket.Escribir("USUARIO " + userName + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			throw new SocketTimeoutException();
		}
		if(serverAnswer != null) {
			if(serverAnswer.contains("OK")) {
				answer = true;
			} else if(serverAnswer.contains("599 ERR")) {
				throw new IllegalAccessError();
			}
			System.out.println(serverAnswer);
		}
		return answer;
		
	}
	
	public boolean validatePassword(String password) throws IllegalAccessError, SocketTimeoutException {
		boolean answer = false;
		try {
			clientSocket.Escribir("CLAVE " + password + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			throw new SocketTimeoutException();
		}
		if(serverAnswer != null) {
			if(serverAnswer.contains("OK")) {
				answer = true;
			} else if(serverAnswer.contains("599 ERR")) {
				throw new IllegalAccessError();
			}
			System.out.println(serverAnswer);
		}
		return answer;
	}
	
	public void signOut() throws SocketTimeoutException {
		try {
			clientSocket.Escribir("SALIR\n");
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			throw new SocketTimeoutException();
		}
		System.out.println(serverAnswer);
	}
	
	public ArrayList<Sensor> getSensorList() throws SocketTimeoutException {
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
			throw new SocketTimeoutException();
		}
		return sensorList;
	}
	
	public ArrayList<String> getSensorRecord(String sensor_id) throws NotFoundException, IllegalAccessError, SocketTimeoutException {
		ArrayList<String> recordList = new ArrayList<String>();
		try {
			clientSocket.Escribir("HISTORICO " + sensor_id + '\n');
			serverAnswer = clientSocket.Leer();
			recordList.add(new String(serverAnswer.getBytes(), "UTF-8"));
			while(!serverAnswer.contains("525 ERR") &&!serverAnswer.contains("524 ERR") && !serverAnswer.contains("322 OK")) {
				serverAnswer = clientSocket.Leer();
				recordList.add(new String(serverAnswer.getBytes(), "UTF-8"));
			}
			recordList.remove(recordList.size() - 1);
			if(serverAnswer.contains("525 ERR")) {
				throw new IllegalAccessError();
			} else if(serverAnswer.contains("524 ERR")) {
				throw new NotFoundException();
			}
		} catch (IOException e) {
			throw new SocketTimeoutException();
		}
		for(int i = 0; i < recordList.size(); i++) {
			System.out.println(recordList.get(i));
		}
		return recordList;
	}
	
	public void enableSensor(String sensor_id) throws NotFoundException, IllegalAccessError, SocketTimeoutException {
		try {
			clientSocket.Escribir("ON " + sensor_id + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			throw new SocketTimeoutException();
		}
		if(serverAnswer.contains("527 ERR")) {
			throw new NotFoundException();
		} else if(serverAnswer.contains("528 ERR")) {
			throw new IllegalAccessError();
		}
		System.out.println(serverAnswer);
	}
	
	public void disableSensor(String sensor_id) throws NotFoundException, IllegalAccessError, SocketTimeoutException {
		try {
			clientSocket.Escribir("OFF " + sensor_id + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			throw new SocketTimeoutException();
		}
		if(serverAnswer.contains("527 ERR")) {
			throw new NotFoundException();
		} else if(serverAnswer.contains("529 ERR")) {
			throw new IllegalAccessError();
		}
		System.out.println(serverAnswer);
	}
	
	public void enableGPS() throws IllegalAccessError, SocketTimeoutException {
		try {
			clientSocket.Escribir("ONGPS\n");
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			throw new SocketTimeoutException();
		}
		if(serverAnswer.contains("529 ERR")) {
			throw new IllegalAccessError();
		}
		System.out.println(serverAnswer);
	}
	
	public void disableGPS() throws IllegalAccessError, SocketTimeoutException {
		try {
			clientSocket.Escribir("OFFGPS\n");
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			throw new SocketTimeoutException();
		}
		if(serverAnswer.contains("530 ERR")) {
			throw new IllegalAccessError();
		}
		System.out.println(serverAnswer);
	}
	
	public String currentValue(String sensor_id) throws NotFoundException, IllegalAccessError, NonReadableChannelException, SocketTimeoutException {
		try {
			clientSocket.Escribir("GET_VALACT " + sensor_id + '\n');
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			throw new SocketTimeoutException();
		}
		if(serverAnswer.contains("525 ERR")) {
			throw new IllegalAccessError();
		} else if(serverAnswer.contains("526 ERR")) {
			throw new NonReadableChannelException();
		} else if(serverAnswer.contains("524 ERR")) {
			throw new NotFoundException();
		}
		System.out.println(serverAnswer);
		serverAnswer = serverAnswer.replace("224 OK ", "");
		return serverAnswer;
	}
	
	public ImageIcon getPhoto() {
		ImageIcon image = null;
		try {
			clientSocket.setSoTimeout(15000);
			clientSocket.Escribir("GET_FOTO\n");
			serverAnswer = clientSocket.Leer();
			if(serverAnswer.contains("316 OK")) {
				int lenght = Integer.valueOf(serverAnswer.split(" ")[2]);
				System.out.println(lenght);
				InputStream fis = clientSocket.getSocket().getInputStream();
				ByteArrayOutputStream imageBytes = new ByteArrayOutputStream(lenght);
				int i = 0;
				byte[] buffer = new byte[1024];
				while(i < lenght - 1 && i != -1) {
					i += fis.read(buffer, 0, buffer.length);
					imageBytes.write(buffer, 0, buffer.length);
				}
				FileOutputStream fos = new FileOutputStream("photo.png");
                fos.write(imageBytes.toByteArray());
                fos.close();
                imageBytes.close();
                
			}
			clientSocket.setSoTimeout(5000);
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
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
		try {
			Client c = new Client("192.168.1.44", 1234);
			c.validateUserName("admin");
			c.validatePassword("admin");
			c.enableGPS();
			c.getLocation();
			c.signOut();
		} catch (ConnectException e) {
			
		} catch (IOException e) {
			
		}
	}
}
