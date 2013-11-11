package userInterface.server;

import graphicInterface.RotatingComponent;

import java.awt.FlowLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Menu extends JPanel {
	
	public Menu() {
		this.setSize(800, 600);
		this.setLayout(new FlowLayout());
		RotatingComponent rc = new RotatingComponent("resources/UmbrellaLogo.png",23, 23, 23, 23, 3);
		this.add(rc);
		
	}
}