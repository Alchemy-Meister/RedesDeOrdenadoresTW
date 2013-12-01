package util;

import graphicInterface.Animate;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Utilities {
	
	/** Loads and returns image from the resources.
     * @param path : the path to the resource.
     * @return : The image the image loaded.
     */
    public static Image loadImage(String path) {
            URL url = Utilities.class.getClassLoader().getResource(path);
            Image image = null;
            try {
                    image = ImageIO.read(url);
            } catch (IOException e) {
                    System.out.println("Error occurred while loading image");
            }
            return image;
    }
	
	/** Resizes and image to the specified horizontal and vertical proportions.
     * @param width : 
     * @param height : 
     * @param path : The path to the resource.
     * @return the image rescaled.
     */
    public static Image resizeImage(int width, int height, String path) {
            Image image = null;
            try {
                    image = ImageIO.read(Utilities.class.getClassLoader().getResource(path));
                    image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            } catch(IOException e) {
            	System.out.println("Error occurred while resizing image");
            }
            return image;
    }
    
    public static String charArrayToString(char[] charArray) {
    	StringBuilder string = new StringBuilder();
    	for(int i = 0; i < charArray.length; i++) {
    		string.append(charArray[i]);
    	}
    	return string.toString();
    }
    
    public static boolean intToBoolean(int bool) {
    	if(bool == 0) 
    		return false;
    	else
    		return true;
    }
    
    public static String booleanToString(boolean bool) {
    	if(bool)
    		return "ON";
    	else
    		return "OFF";
    }
    
    public static void transitionEffect(final JPanel from, JPanel to, final JFrame parent, boolean rightTransition) {
    	final Animate toPanel = new Animate(to, to.getBounds(), from.getBounds(), 350);
    	Animate fromPanel;
    	if(rightTransition) {
    	fromPanel = new Animate(from, from.getBounds(), new Rectangle(from.getX() + from.getWidth(),
    			from.getY(), from.getWidth(), from.getHeight()), 350);
    	} else {
    		fromPanel = new Animate(from, from.getBounds(), new Rectangle(from.getX() - from.getWidth(),
        			from.getY(), from.getWidth(), from.getHeight()), 350);
    	}
    	if(rightTransition) {
    		toPanel.start();
    		fromPanel.start();
    	} else {
    		fromPanel.start();
    		toPanel.start();
    	}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!toPanel.hasFinished()) {
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				parent.remove(from);
			}
		}).start();
    }
    
    public static float randomFloat(float min, float max) {
    	return new Random().nextFloat() * (max - min) + min;
    }
}
