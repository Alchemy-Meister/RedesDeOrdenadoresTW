package connection;

import java.io.IOException;

import dataBase.DatabaseController;

public class Service implements Runnable {
	
	private SocketManager clientSocket;
	private boolean exit = false;
	private String userName;
	private String password;
	
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
					if(command.equals("USUARIO")) {
						boolean correct = false;
						try {
							correct = DatabaseController.validateUserName(parameter.toString());
						} catch (InterruptedException e) {
							
						}
						if(correct) {
							userName = parameter.toString();
							clientSocket.Escribir("301 OK Bienvenido " + parameter.toString() + ".\n");
						} else {
							clientSocket.Escribir("501 ERR Falta el nombre de usuario.\n");
						}
					} else if(command.equals("CLAVE")) {
						boolean correct = false;
						password = parameter.toString();
						if(!password.equals("")) {	
							try {
								correct = DatabaseController.validateUser(userName, password);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if(correct) {
								clientSocket.Escribir("302 OK Bienvenido al sistema.\n");
							} else {
								clientSocket.Escribir("502 ERR La clave es incorrecta.\n");
							}
						} else {
							clientSocket.Escribir("￼503 ERR Falta la clave.\n");
						}
					} else if(command.equals("SALIR")) {
						clientSocket.Escribir("318 OK Adios.\n");
						exit = true;
					}
				} else {
					exit = true;
					System.out.println("Client disconnected.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
