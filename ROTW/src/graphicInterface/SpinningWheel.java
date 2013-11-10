package graphicInterface;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

import util.Utilities;

@SuppressWarnings("serial")
public class SpinningWheel extends JComponent {
	
	private Image[] images = new Image[13];
	private Image currentImage;
	private Timer timer;
	int j = 1;
	
	public SpinningWheel(int x, int y, int size, int delay) {
		for(int i = 0; i < images.length; i++) {
			images[i] = Utilities.resizeImage(size, size, "resources/InfiniteProgress_" + i + ".png");
			currentImage = images[0];
		}
		this.setBounds(x, y, images[0].getWidth(null), images[0].getHeight(null));
		timer = new Timer(delay, new ActionListener() {
	        @Override
	        public void actionPerformed( ActionEvent e ) {
	        	currentImage = images[j++];
	        	if(j == 12) {
	        		j = 0;
	          }
	          SpinningWheel.this.repaint();
	        }
	      } );
		timer.setRepeats(false);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		timer.stop();
		super.paintComponent(g);
		g.drawImage(currentImage, 0, 0, null);
		timer.start();
	}
}
