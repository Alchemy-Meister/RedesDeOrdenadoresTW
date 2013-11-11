package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public Server(int port) {
		try {
			ServerSocket serverS = new ServerSocket(port);
			while(true) {
				Socket clientSocket = serverS.accept();
				new Thread(new Service(clientSocket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Server(1234);
	}
}
