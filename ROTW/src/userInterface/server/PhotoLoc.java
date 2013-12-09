package userInterface.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.Utilities;
import graphicInterface.JBackgroundedPanel;

@SuppressWarnings("serial")
public class PhotoLoc extends JBackgroundedPanel implements ActionListener {
	
	JButton back = new JButton("Back");
	JButton getLoc = new JButton("Get Location");
	
	JTextArea location = new JTextArea();
	
	AdminClient parent;
	
	public PhotoLoc(AdminClient parent) {
		super("resources/Menu.png");
		
		this.parent = parent;
		parent.setTitle("Photo Localization");
		
		this.setLayout(null);
		
		back.setBounds(800 / 2 + 125, 400, 125, 25);
		
		getLoc.setBounds(800/2 - 150/2, 600 / 2 - 25 / 2, 150, 25);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		location.setEditable(false);
		
		panel.setBounds(800 / 2 - 300 / 2, 200, 300, 27);
		
		location.setBounds(0, 0, 300, 27);
		
		panel.add(location);
		
		this.add(panel);
		this.add(back);
		this.add(getLoc);
		
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
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					location.setText(AdminClient.client.getLocation());
				}
			}).start();
		}
	}
}
