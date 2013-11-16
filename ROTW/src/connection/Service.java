package connection;

import java.io.IOException;

import dataBase.DatabaseController;

public class Service implements Runnable {
	
	private SocketManager clientSocket;
	private boolean exit = false;
	
	public Service(SocketManager clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		while(!exit) {
			try {
				String message = clientSocket.Leer();
				if(message != null) {
					String[] splitter = message.split(" ");
					String command = splitter[0];
					StringBuilder parameter = new StringBuilder();
					if(splitter.length > 1) {
						parameter.append(splitter[1]);
						for(int i = 2; i < splitter.length; i++) {
							parameter.append(" " + splitter[i]);
						} 
					} else {
						parameter.append("");
					}
					if(command.equals("USER")) {
						boolean correct = false;
						try {
							correct = DatabaseController.validateUserName(parameter.toString());
						} catch (InterruptedException e) {
							
						}
						if(correct) {
							clientSocket.Escribir("301 OK Bienvenido " + parameter.toString() + ".\n");
						} else {
							clientSocket.Escribir("501 ERR Falta el nombre de usuario.\n");
						}
					} else if(command.equals("SALIR")) {
						clientSocket.Escribir("318 OK Adios.\n");
						exit = true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
