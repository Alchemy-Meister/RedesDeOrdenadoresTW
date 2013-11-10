package graphicInterface;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;
import javax.swing.Timer;

import util.Utilities;

@SuppressWarnings("serial")
public class RotatingComponent extends JComponent {
	
	private Image image;
	private Timer timer;
	private double angleInDegrees = 90;
	
	public RotatingComponent(String imageName, int x, int y, int width, int height, final double angleSpeed) {
		this.setBounds(x, y, width, height);
		image = Utilities.resizeImage(width, height, imageName);
		//this.setSize(image.getWidth(null), image.getHeight(null));
		timer = new Timer(20, new ActionListener() {
	        @Override
	        public void actionPerformed( ActionEvent e ) {
	          angleInDegrees = angleInDegrees + angleSpeed;
	          if ( angleInDegrees == 360 ){
	            angleInDegrees = 0;
	          }
	          RotatingComponent.this.repaint();
	        }
	      } );
		timer.setRepeats(false);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		timer.stop();
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform original = g2.getTransform();
	    AffineTransform at = new AffineTransform();
	    at.concatenate(original);
		at.rotate(Math.toRadians( angleInDegrees ), this.getWidth()/2, this.getHeight()/2);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setTransform(at);
        g2.drawImage(image, 0, 0, null);
        timer.start();
	}
	
	@Override
	public Dimension getPreferredSize() {   
		return new Dimension(image.getWidth(null), image.getHeight(null));
		}
}
