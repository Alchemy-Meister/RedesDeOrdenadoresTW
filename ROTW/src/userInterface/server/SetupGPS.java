package userInterface.server;

import graphicInterface.JBackgroundedPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketTimeoutException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import util.Utilities;

@SuppressWarnings("serial")
public class SetupGPS extends JBackgroundedPanel implements ActionListener {
	
	AdminClient parent;
	
	JButton enableApply;
	JButton disableApply;
	JButton button;
	
	public SetupGPS(AdminClient parent) {
		
		super("resources/Menu.png");
		
		this.parent = parent;
		parent.setTitle("Setup GPS");
		this.setLayout(null);
		
		button = new JButton("Back");
		button.setBounds(800 / 2 + 125, 400, 125, 25);
		
		JLabel enable = new JLabel("ENABLE GPS");
		enable.setBounds(10, 100, 110, 25);
		enable.setForeground(new Color(2,214,247));
		JLabel disable = new JLabel("DISABLE GPS");
		disable.setBounds(10, 130, 110, 25);
		disable.setForeground(new Color(2,214,247));
		
		enableApply = new JButton("Apply");
		enableApply.setBounds(enable.getX() + enable.getWidth() + 3, enable.getY(), 100, 25);
		disableApply = new JButton("Apply");
		disableApply.setBounds(disable.getX() + disable.getWidth() + 3, disable.getY(), 100, 25);
		
		this.add(enable);
		this.add(disable);
		this.add(enableApply);
		this.add(disableApply);
		this.add(button);
		
		enableApply.addActionListener(this);
		disableApply.addActionListener(this);
		button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		enableApply.setEnabled(false);
		disableApply.setEnabled(false);
		button.setEnabled(false);
		if(e.getSource().equals(enableApply)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						AdminClient.client.enableGPS();
						JOptionPane.showMessageDialog(SetupGPS.this, "The GPS has been successfully enabled.");
					} catch(IllegalAccessError e) {
						JOptionPane.showMessageDialog(SetupGPS.this, "Selected GPS is already enabled.", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (SocketTimeoutException e) {
						JOptionPane.showMessageDialog(SetupGPS.this, "The connection is down.", "Error", JOptionPane.ERROR_MESSAGE);
						
						Authentication authentication = new Authentication(parent);
						authentication.setLocation(SetupGPS.this.getX() - SetupGPS.this.getWidth(), SetupGPS.this.getY());
						parent.getContentPane().add(authentication, 0);
						Utilities.transitionEffect(SetupGPS.this, authentication, parent, true);
						SwitchServerWindow s = new SwitchServerWindow();
						s.requestFocus();
					}
					enableApply.setEnabled(true);
					disableApply.setEnabled(true);
					button.setEnabled(true);
				}
			}).start();
		}else if(e.getSource().equals(disableApply)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						AdminClient.client.disableGPS();
						JOptionPane.showMessageDialog(SetupGPS.this, "The GPS has been successfully disabled.");
					} catch(IllegalAccessError e) {
						JOptionPane.showMessageDialog(SetupGPS.this, "Selected GPS is already disabled.", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (SocketTimeoutException e) {
						JOptionPane.showMessageDialog(SetupGPS.this, "The connection is down.", "Error", JOptionPane.ERROR_MESSAGE);
						
						Authentication authentication = new Authentication(parent);
						authentication.setLocation(SetupGPS.this.getX() - SetupGPS.this.getWidth(), SetupGPS.this.getY());
						parent.getContentPane().add(authentication, 0);
						Utilities.transitionEffect(SetupGPS.this, authentication, parent, true);
						SwitchServerWindow s = new SwitchServerWindow();
						s.requestFocus();
					}
					enableApply.setEnabled(true);
					disableApply.setEnabled(true);
					button.setEnabled(true);
				}
			}).start();
		} else if(e.getSource().equals(button)) {
			Menu m = new Menu(parent, "resources/Menu.png");
			m.setLocation(SetupGPS.this.getX() - SetupGPS.this.getWidth(), SetupGPS.this.getY());
			parent.getContentPane().add(m, 0);
			Utilities.transitionEffect(SetupGPS.this, m, parent, true);
		}
	}
}
