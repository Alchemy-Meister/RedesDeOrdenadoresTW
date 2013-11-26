package userInterface.server;

import graphicInterface.Animate;
import graphicInterface.RotatingComponent;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.Utilities;

@SuppressWarnings("serial")
public class Menu extends JPanel implements ActionListener {
	
	JButton signout = new JButton("Sign Out");
	
	AdminClient parent;
	Animate menuPanelA;
	Animate authPanelA;
	
	public Menu(AdminClient parent) {
		
		this.parent = parent;
		
		this.setSize(800, 600);
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		
		RotatingComponent rc = new RotatingComponent("resources/UmbrellaLogo.png",23, 23, 28, 28, 3);
		JTextField label = new JTextField("Under Construction");
		label.setBounds(58, 25, 150, 23);
		signout.setBounds(0, 100, 100, 25);
		
		AdminClient.serverLocator.setEnabled(false);
		AdminClient.signout.setEnabled(true);
		
		this.add(rc);
		this.add(label);
		this.add(signout);
		
		//Adding Listeners
		signout.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(signout)) {
			AdminClient.client.signOut();
			AdminClient.client = null;
			Authentication authentication = new Authentication(parent);
			authentication.setLocation(Menu.this.getX() - Menu.this.getWidth(), Menu.this.getY());
			parent.getContentPane().add(authentication, 0);
			Utilities.transitionEffect(Menu.this, authentication, parent, true);
			parent.validate();
		}
	}
}