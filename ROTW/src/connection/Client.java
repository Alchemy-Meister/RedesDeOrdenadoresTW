package connection;

import java.io.IOException;
import java.net.UnknownHostException;

public class Client {
	
	private SocketManager clientSocket;
	
	public Client(String address, int port) {
		try {
			clientSocket = new SocketManager(address, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean validateUserName(String userName) {
		boolean answer = false;
		String serverAnswer = null;
		try {
			clientSocket.Escribir("USER " + userName + '\n');
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
		String serverAnswer = null;
		try {
			clientSocket.Escribir("SALIR\n");
			serverAnswer = clientSocket.Leer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverAnswer);
	}
	
	public static void main(String[] argv) {
		Client c = new Client("127.0.0.1", 1234);
		c.validateUserName("Albert Wesker");
	}
}
