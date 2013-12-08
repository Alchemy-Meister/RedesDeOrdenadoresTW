package connection;

import java.io.IOException;

import dataBase.DatabaseController;
import dataBase.User;

public class LocationService implements Runnable {
	
	SocketManager clientSocket;
	
	boolean exit = false;
	
	User user = new User();
	
	public LocationService(SocketManager clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		while(!exit) {
			try {
				String message = clientSocket.Leer();
				if(message != null) {
					System.out.println(message);
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
					if(clientSocket != null) {
						if(command.equals("USUARIO")) {
							boolean correct = false;
							correct = DatabaseController.validateUserName(parameter.toString());
							if(correct) {
								user.setUserName(parameter.toString());
								clientSocket.Escribir("301 OK Bienvenido " + parameter.toString() + ".\n");
							} else {
								clientSocket.Escribir("501 ERR Falta el nombre de usuario.\n");
							}
						} else if(command.equals("CLAVE")) {
							boolean correct = false;
							user.setPassword(parameter.toString());
							if(!user.getPassword().equals("")) {	
									correct = DatabaseController.validateUser(user.getUserName(), user.getPassword());
								if(correct) {
									clientSocket.Escribir("302 OK Bienvenido al sistema.\n");
								} else {
									clientSocket.Escribir("502 ERR La clave es incorrecta.\n");
								}
							} else {
								clientSocket.Escribir("503 ERR Falta la clave.\n");
							}
						} else if(command.equals("SALIR")) {
							clientSocket.Escribir("318 OK Adios.\n");
							exit = true;
						} else if(command.equals("GETCOOR")) {
							if(parameter.toString().equals("")) {
								clientSocket.Escribir("528 ERR Falta parametro cell_id.\n");
							} else {
								try {
									DatabaseController.getCellValue(parameter.toString());
									clientSocket.Escribir("224 OK "+ "" + "\n");
								} catch(IllegalAccessError e) {
									
								}
								clientSocket.Escribir("527 ERR Celda desconocida.\n");
							}
						}
					}
				}
			}catch(IOException e) {
				
			}
		}
	}
}
