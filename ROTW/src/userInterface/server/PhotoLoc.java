package userInterface.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import util.Utilities;
import graphicInterface.JBackgroundedPanel;

public class PhotoLoc extends JBackgroundedPanel implements ActionListener {
	
	JButton back = new JButton("Back");
	JButton getLoc = new JButton("Get Location");
	
	AdminClient parent;
	
	public PhotoLoc(AdminClient parent) {
		super("resources/Menu.png");
		
		this.parent = parent;
		parent.setTitle("Photo Localization");
		
		this.setLayout(null);
		
		back.setBounds(800 / 2 + 125, 400, 125, 25);
		
		this.add(back);
		
		back.addActionListener(this);
		getLoc.addActionListener(this);
		
		this.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(back)) {
			Menu m = new Menu(parent, "resources/Menu.png");
			m.setLocation(PhotoLoc.this.getX() - PhotoLoc.this.getWidth(), PhotoLoc.this.getY());
			parent.getContentPane().add(m, 0);
			Utilities.transitionEffect(PhotoLoc.this, m, parent, true);
		} else if(e.getSource().equals(getLoc)) {
			
		}
	}
}
