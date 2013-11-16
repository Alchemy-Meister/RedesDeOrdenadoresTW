package userInterface.server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import connection.Client;

@SuppressWarnings("serial")
public class AdminClient extends JFrame {
	
	private int width = 800;
	private int height = 600;
	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int deviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public Client client;
	
	public AdminClient() {
		
		this.getContentPane().setPreferredSize(new Dimension(width, height));
		this.getContentPane().setLayout(null);
		this.getContentPane().setBackground(Color.BLACK);
		
		//Windows components.
		Authentication a = new Authentication(this);
		//a.setLocation(800, 0);
		this.add(a);
		
		//Window's Properties.
		this.pack();
		this.setLocation(deviceWidth/2 - width/2, deviceHeight/2 - height/2);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new AdminClient();
	}

}
