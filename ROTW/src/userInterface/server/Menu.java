package userInterface.server;

import graphicInterface.RotatingComponent;

import java.awt.FlowLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Menu extends JFrame {
	
	public Menu() {
		this.setSize(800, 600);
		this.getContentPane().setLayout(new FlowLayout());
		RotatingComponent rc = new RotatingComponent("resources/UmbrellaLogo.png",23, 23, 23, 23, 3);
		this.getContentPane().add(rc);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Menu();
	}
}