package connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class LocalizationClient {
	
	private SocketManager clientSocket;
	private String serverAnswer = null;
	
	public LocalizationClient(String ip, int port) throws IOException, ConnectException {
		try {
			clientSocket = new SocketManager(ip, port);
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
	
	public String getCellVal() throws SocketTimeoutException {
		try {
			clientSocket.Escribir("GETCOOR 1\n");
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			throw new SocketTimeoutException();
		}
		System.out.println(serverAnswer);
		return serverAnswer;
	}
}
