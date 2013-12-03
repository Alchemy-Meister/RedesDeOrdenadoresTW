package userInterface.server;

import graphicInterface.Animate;
import graphicInterface.JBackgroundedPanel;
import graphicInterface.RotatingComponent;
import graphicInterface.SpinningWheel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataBase.Sensor;
import util.Utilities;

@SuppressWarnings("serial")
public class Menu extends JBackgroundedPanel implements ActionListener {
	
	JButton sensorList = new JButton("Sensor List");
	JButton setupSensor = new JButton("Setup Sensor");
	JButton setupGPS = new JButton("Setup GPS");
	JButton photo = new JButton("Photo");
	JButton signout = new JButton("Sign Out");
	
	SpinningWheel sp;
	RotatingComponent rc;
	
	AdminClient parent;
	Animate menuPanelA;
	Animate authPanelA;
	
	public Menu(AdminClient parent, String path) {
		
		super(path);
		
		this.parent = parent;
		parent.setTitle("Menu");
		
		this.setSize(800, 600);
		this.setLayout(null);
		
		rc = new RotatingComponent("resources/UmbrellaLogo.png", 3, 18, 25, 25, 3);
		sp = new SpinningWheel(3, 18, 25, 55);
		sp.setVisible(false);
		
		JLabel umbrellaCopyright = new JLabel();
		umbrellaCopyright.setBounds(140, 565, 25, 25);
		umbrellaCopyright.setIcon(new ImageIcon(Utilities.resizeImage(25, 25, "resources/UmbrellaLogo.png")));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		
		sensorList.setBounds(0, 0, 125, 25);
		setupSensor.setBounds(0, 30, 125, 25);
		setupGPS.setBounds(0, 60, 125, 25);
		photo.setBounds(0, 90, 125, 25);
		signout.setBounds(0, 120, 125, 25);
		
		buttonPanel.setBounds(800 / 2 - 125 / 2, 600 / 2 - 145 / 2, 125, 145);
		
		AdminClient.serverLocator.setEnabled(false);
		AdminClient.signout.setEnabled(true);
		
		this.add(sp);
		this.add(rc);
		this.add(umbrellaCopyright);
		
		buttonPanel.add(sensorList);
		buttonPanel.add(setupSensor);
		buttonPanel.add(setupGPS);
		buttonPanel.add(photo);
		buttonPanel.add(signout);
		
		this.add(buttonPanel);
		
		//Adding Listeners
		sensorList.addActionListener(this);
		setupSensor.addActionListener(this);
		setupGPS.addActionListener(this);
		photo.addActionListener(this);
		signout.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		AdminClient.signout.setEnabled(false);
		sensorList.setEnabled(false);
		setupSensor.setEnabled(false);
		setupGPS.setEnabled(false);
		photo.setEnabled(false);
		signout.setEnabled(false);
		rc.setVisible(false);
		sp.setVisible(true);
		
		if(e.getSource().equals(sensorList)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					ArrayList<Sensor> sensorListArray = AdminClient.client.getSensorList();
					System.out.println(sensorListArray.get(0).toString());
					sensorList.setEnabled(true);
					setupSensor.setEnabled(true);
					setupGPS.setEnabled(true);
					photo.setEnabled(true);
					signout.setEnabled(true);
					sp.setVisible(false);
					rc.setVisible(true);
					AdminClient.signout.setEnabled(true);
					SensorList sensor = new SensorList("resources/Menu.png", parent, sensorListArray);
					sensor.setLocation(Menu.this.getX() + Menu.this.getWidth(), Menu.this.getY());
					parent.getContentPane().add(sensor, 0);
					Utilities.transitionEffect(Menu.this, sensor, parent, false);
				}
			}).start();
		} else if(e.getSource().equals(setupSensor)) {
			sensorList.setEnabled(true);
			setupSensor.setEnabled(true);
			setupGPS.setEnabled(true);
			photo.setEnabled(true);
			signout.setEnabled(true);
			sp.setVisible(false);
			rc.setVisible(true);
		} else if(e.getSource().equals(setupGPS)) {
			sensorList.setEnabled(true);
			setupSensor.setEnabled(true);
			setupGPS.setEnabled(true);
			photo.setEnabled(true);
			signout.setEnabled(true);
			sp.setVisible(false);
			rc.setVisible(true);
		} else if(e.getSource().equals(photo)) {
			sensorList.setEnabled(true);
			setupSensor.setEnabled(true);
			setupGPS.setEnabled(true);
			photo.setEnabled(true);
			signout.setEnabled(true);
			sp.setVisible(false);
			rc.setVisible(true);
		} else if(e.getSource().equals(signout)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					AdminClient.client.signOut();
					AdminClient.client = null;
					sensorList.setEnabled(true);
					setupSensor.setEnabled(true);
					setupGPS.setEnabled(true);
					photo.setEnabled(true);
					signout.setEnabled(true);
					sp.setVisible(false);
					rc.setVisible(true);
					Authentication authentication = new Authentication(parent);
					authentication.setLocation(Menu.this.getX() - Menu.this.getWidth(), Menu.this.getY());
					parent.getContentPane().add(authentication, 0);
					Utilities.transitionEffect(Menu.this, authentication, parent, true);
				}
			}).start();
		}
	}
}