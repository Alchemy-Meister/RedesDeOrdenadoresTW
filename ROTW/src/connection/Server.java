package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import dataBase.User;

public class Server {
	
	private int userLimit = 3;
	
	private ServerSocket serverS;
	
	private volatile boolean shutdown = false;
	
	protected static ArrayList<Service> serviceList = new ArrayList<Service>();
	SocketManager clientSocket;
	
	public Server(int port) { 
		try {
			serverS = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startServer() {
		while(!shutdown) {
			try {
				clientSocket = new SocketManager(serverS.accept());
				if(serviceList.size() < userLimit) {
					Service service = new Service(clientSocket);
					serviceList.add(service);
					new Thread(service).start();
				} else {
					clientSocket.Escribir("599 ERR Server full.\n");
				}
			} catch (IOException e) {
			
			}
		}
		for(int i = 0; i < serviceList.size(); i++) {
			serviceList.get(i).terminateSesion();
		}
	}
	
	public ArrayList<User> getUsers() {
		ArrayList<User> userList = new ArrayList<User>();
		for(int i = 0; i < serviceList.size(); i++) {
			userList.add(serviceList.get(i).getUser());
		}
		return userList;
	}
	
	public void shutdownServer() {
		try {
			shutdown = true;
			serverS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getMaximumUserLimit() {
		return userLimit;
	}
	
	public void setMaximumUserLimit(int userLimit) {
		this.userLimit = userLimit;
	}
	
	public static void main(String[] args) {
		new Server(1234);
	}
}
