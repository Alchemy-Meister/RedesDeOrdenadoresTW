package connection;

import java.io.IOException;

import dataBase.DatabaseController;

public class Service implements Runnable {
	
	private SocketManager clientSocket;
	private boolean exit = false;
	private String userName = "";
	private String password = "";
	
	public Service(SocketManager clientSocket) {
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
							clientSocket.Escribir("503 ERR Falta la clave.\n");
						}
					} else if(command.equals("SALIR")) {
						clientSocket.Escribir("318 OK Adios.\n");
						exit = true;
					} else if(command.equals("LISTSENSOR")) {
						clientSocket.Escribir("222 OK Lista de sensores.\n");
						//TODO Coger de la base de datos la lista de todos los sensores.
					} else if(command.equals("HISTORICO")) {
						if(parameter.toString().equals("")) {
							clientSocket.Escribir("525 ERR Falta parámetro id_sensor.\n");
						} else {
							//TODO Coger de la base de datos todos los valores del sensor. Y procesar que el id sensor sea valido.
							clientSocket.Escribir("524 ERR Sensor desconocido.\n");
							clientSocket.Escribir("322 OK Lista finalizada.\n");
							
							
						}
					} else if(command.equals("ON")) {
						//TODO activar el sensor desde la base de datos si es que existe y no esta ya activado.
						clientSocket.Escribir("527 ERR Sensor no existe.\n");
						clientSocket.Escribir("313 OK Sensor activo.\n");
						clientSocket.Escribir("￼528 ERR Sensor en estado ON.\n");
					} else if(command.equals("OFF")) {
						//TODO desactivar el sensor desde la base de datos si es que existe y no esta ya desactivado.
						clientSocket.Escribir("314 OK Sensor desactivado.\n");
						clientSocket.Escribir("527 ERR Sensor no existe.\n");
						clientSocket.Escribir("529 ERR Sensor en estado OFF.\n");
					} else if(command.equals("ONGPS")) {
						//TODO check if the gps is already enabled.
						clientSocket.Escribir("315 OK GPS activado.\n");
						clientSocket.Escribir("529 ERR GPS en estado ON.\n");
					} else if(command.equals("OFFGPS")) {
						//TODO check if the gps is already disabled.
						clientSocket.Escribir("316 OK GPS desactivado.\n");
						clientSocket.Escribir("530 ERR GPS en estado OFF.\n");
					} else if(command.equals("GET_VALACT")) {
						if(parameter.toString().equals("")) {
							clientSocket.Escribir("525 ERR Falta parámetro id_sensor.\n");
						}
						//TODO check if the sensor is enabled or if it exists.
						clientSocket.Escribir("224 OK\n");
						clientSocket.Escribir("524 ERR Sensor desconocido.\n");
						clientSocket.Escribir("￼526 ERR Sensor en OFF.\n");
					} else if(command.equals("GET_FOTO")) {
						//TODO check if the gps is on
						String bytes = "lolololololol";
						clientSocket.Escribir("316 OK " + bytes +  "bytes transmitiendo.\n");
						clientSocket.Escribir("530 ERR GPS en estado OFF.\n");
					} else if(command.equals("GET_LOC")) {
						//TODO only after sending a photo.
						clientSocket.Escribir("124 OK\n");
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
