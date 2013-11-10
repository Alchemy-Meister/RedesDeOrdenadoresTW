package graphicInterface;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import util.Utilities;

@SuppressWarnings("serial")
public class JBackgroundedPanel extends JPanel {
	
	private Image image;
	
	public JBackgroundedPanel(String imageName, int x, int y, int width, int height) {
		image = Utilities.resizeImage(width, height, imageName);
		this.setBounds(x, y, width, height);
	}
	
	public JBackgroundedPanel(String imageName) {
		image = Utilities.loadImage(imageName);
		this.setSize(image.getWidth(null), image.getHeight(null));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
