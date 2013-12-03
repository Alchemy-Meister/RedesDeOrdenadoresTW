package userInterface.client;

import graphicInterface.JBackgroundedPanel;
import graphicInterface.RotatingComponent;
import graphicInterface.SpinningWheel;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Menu extends JPanel {
	
	public Menu() {
		
		JBackgroundedPanel panel = new JBackgroundedPanel("resources/Menu.png");
		panel.setLayout(null);
		
		SpinningWheel s = new SpinningWheel(0, 0, 100, 55);
		RotatingComponent r = new RotatingComponent("resources/InfiniteProgress_0.png", 100, 0, 100, 100, 10);
		
		panel.add(s);
		panel.add(r);
		
		this.setSize(200, 125);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Menu();
	}
}