package util;

import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Shaker implements Runnable {
	
	JComponent component;
	
	Thread t;
	
	public Shaker(JComponent component) {
		this.component = component;
	}
	
	public void shakeComponent(final JComponent component) {
        if(t == null || !t.isAlive()) {
        	t = new Thread(this);
        	t.start();
        }
    }

	@Override
	public void run() {
		
		final Point point = component.getLocation();
        
        final int delay = 10;
		
		for (int i = 0; i < 8; i++) {
			try{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						component.setLocation( (int)(point.getX() + 10), (int) point.getY());
		          }
		        });
				Thread.sleep(delay);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						component.setLocation( (int)(point.getX()), (int) point.getY());
		          }
		        });
				Thread.sleep(delay);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						component.setLocation( (int)(point.getX() - 10), (int) point.getY());
		          }
		        });
				Thread.sleep(delay);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						component.setLocation( (int)(point.getX()), (int) point.getY());
		          }
		        });
				Thread.sleep(delay);
			} catch(InterruptedException e) {
		
			}
		}
	}
}
