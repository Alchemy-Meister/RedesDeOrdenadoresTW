package connection;

import java.net.Socket;

public class Service implements Runnable {
	
	private Socket clientSocket;
	
	public Service(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		
	}
}
