package connection;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	
	int i = 0;
	
	public Server(int port) {
		try {
			ServerSocket serverS = new ServerSocket(port);
			while(true) {
				SocketManager clientSocket = new SocketManager(serverS.accept());
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
