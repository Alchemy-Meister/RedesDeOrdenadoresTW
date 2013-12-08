package connection;

import java.io.IOException;
import java.net.ServerSocket;

public class LocationServer {
	
	ServerSocket socketS;
	SocketManager clientSocket;
	
	public LocationServer() {
		
		try {
			socketS = new ServerSocket(6666);
			
			while(true) {
				clientSocket = new SocketManager(socketS.accept());
				LocationService service = new LocationService(clientSocket);
				new Thread(service).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
