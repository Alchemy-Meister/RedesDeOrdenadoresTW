package userInterface.client;

import graphicInterface.RotatingComponent;
import graphicInterface.SpinningWheel;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Menu extends JFrame {
	
	public Menu() {
		SpinningWheel s = new SpinningWheel(0, 0, 100, 55);
		RotatingComponent r = new RotatingComponent("resources/InfiniteProgress_0.png", 100, 0, 100, 100, 10);
		
		this.setLayout(null);
		this.add(s);
		this.add(r);
		
		this.setSize(200, 125);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Menu();
	}
}