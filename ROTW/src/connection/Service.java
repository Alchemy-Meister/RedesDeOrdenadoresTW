package connection;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.naming.NoPermissionException;

import com.github.sarxos.webcam.Webcam;

import util.NotFoundException;
import util.Utilities;
import dataBase.DatabaseController;
import dataBase.Record;
import dataBase.Sensor;

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
						correct = DatabaseController.validateUserName(parameter.toString());
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
								correct = DatabaseController.validateUser(userName, password);
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
						ArrayList<Sensor> sensorList = DatabaseController.getSensorList(userName);
						for(int i = 0; i < sensorList.size(); i++) {
							clientSocket.Escribir(sensorList.get(i).toString()  + '\n');
						}
						clientSocket.Escribir("322 OK Lista finalizada.\n");
					} else if(command.equals("HISTORICO")) {
						if(parameter.toString().equals("")) {
							clientSocket.Escribir("525 ERR Falta parámetro id_sensor.\n");
						} else {
							ArrayList<Record> recordList = new ArrayList<Record>();
							try {
								recordList = DatabaseController.getSensorRecord(parameter.toString());
							} catch (Exception e) {
								clientSocket.Escribir("524 ERR Sensor desconocido.\n");
							}
							for(int i = 0; i < recordList.size(); i++) {
								clientSocket.Escribir(new String(recordList.get(i).toString().getBytes(), "UTF-8") + "\n");
							}
							clientSocket.Escribir("322 OK Lista finalizada.\n");
							
							
						}
					} else if(command.equals("ON")) {
						try {
							DatabaseController.enableSensor(parameter.toString());
							clientSocket.Escribir("313 OK Sensor activo.\n");
						} catch (NoPermissionException e) {
							clientSocket.Escribir("528 ERR Sensor en estado ON.\n");							
						} catch (NotFoundException e) {
							clientSocket.Escribir("527 ERR Sensor no existe.\n");
						}
					} else if(command.equals("OFF")) {
						try {
							DatabaseController.disableSensor(parameter.toString());
							clientSocket.Escribir("314 OK Sensor desactivado.\n");
						} catch (NoPermissionException e) {
							clientSocket.Escribir("529 ERR Sensor en estado OFF.\n");							
						} catch (NotFoundException e) {
							clientSocket.Escribir("527 ERR Sensor no existe.\n");
						}
					} else if(command.equals("ONGPS")) {
						try {
							DatabaseController.enableGPS(userName);
							clientSocket.Escribir("315 OK GPS activado.\n");
						} catch (NoPermissionException e) {
							clientSocket.Escribir("529 ERR GPS en estado ON.\n");
						}
					} else if(command.equals("OFFGPS")) {
						try {
							DatabaseController.disableGPS(userName);
							clientSocket.Escribir("316 OK GPS desactivado.\n");
						} catch (NoPermissionException e) {
							clientSocket.Escribir("530 ERR GPS en estado OFF.\n");
						}
					} else if(command.equals("GET_VALACT")) {
						if(parameter.toString().equals("")) {
							clientSocket.Escribir("525 ERR Falta parámetro id_sensor.\n");
						} else {
							try {
								Sensor sensor = DatabaseController.getSensor(parameter.toString());
								if(sensor.isEnabled()) {
									Record record = new Record(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), Utilities.randomFloat(0, 999),  Utilities.randomFloat(0, 59.99f), 
											 Utilities.randomFloat(0, 59.99f),  Utilities.randomFloat(0, 999),  Utilities.randomFloat(0, 59.99f),
											 Utilities.randomFloat(0, 59.99f),  Utilities.randomFloat(0, 999), Integer.valueOf(parameter.toString()));
									DatabaseController.updateRecord(record);
									clientSocket.Escribir("224 OK "+ record.toString() +"\n");
								} else {
									clientSocket.Escribir("￼526 ERR Sensor en OFF.\n");
								}
							} catch (NotFoundException e) {
								clientSocket.Escribir("524 ERR Sensor desconocido.\n");
							}
						}
					} else if(command.equals("GET_FOTO")) {
						//TODO check if the gps is on
						Webcam webcam = Webcam.getDefault();
						webcam.open();
						BufferedImage image = webcam.getImage();
						webcam.close();
						ImageIO.write(image, "PNG", new File("photo.png"));
						FileInputStream imageFile = new FileInputStream("photo.png");
						clientSocket.Escribir("316 OK " + imageFile.available() +  " Bytes transmitiendo.\n");
						clientSocket.EscribirBytes(imageFile);
						imageFile.close();
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
