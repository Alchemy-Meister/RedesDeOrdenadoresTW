package userInterface.server;

import graphicInterface.RotatingComponent;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Menu extends JPanel {
	
	public Menu() {
		this.setSize(800, 600);
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		RotatingComponent rc = new RotatingComponent("resources/UmbrellaLogo.png",23, 23, 28, 28, 3);
		JTextField label = new JTextField("Under Construction");
		label.setBounds(58, 25, 150, 23);
		label.setForeground(Color.WHITE);
		this.add(rc);
		this.add(label);
		
	}
}