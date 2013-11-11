package util;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

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
}
