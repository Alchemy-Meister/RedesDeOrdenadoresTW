package userInterface.server;

import graphicInterface.JBackgroundedPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketTimeoutException;
import java.nio.channels.NonReadableChannelException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.NotFoundException;
import util.Utilities;

@SuppressWarnings("serial")
public class SetupSensor extends JDialog implements ActionListener{
	
	JButton button;
	JButton enableApply;
	JButton disableApply;
	JButton record;
	JButton current;
	
	JTextField enabletf;
	JTextField disabletf;
	JTextField recordtf;
	JTextField currenttf;
	
	JTextArea area;
	
	private int width = 800;
	private int height = 600;
	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int deviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	AdminClient parent;
	JPanel menu;
	
	public SetupSensor(AdminClient parent, JPanel menu) {
		super();
		
		this.parent = parent;
		this.menu = menu;
		
		JBackgroundedPanel bp = new JBackgroundedPanel("resources/Menu.png");
		
		bp.setLayout(null);
		this.setModal(true);
		this.setBounds(deviceWidth/2 - width/2, deviceHeight/2 - height/2, width, height);
		
		button = new JButton("Back");
		button.setBounds(800 / 2 + 125, 400, 125, 25);
		
		JLabel enable = new JLabel("ENABLE SENSOR");
		enable.setBounds(10, 100, 110, 25);
		enable.setForeground(new Color(2,214,247));
		JLabel disable = new JLabel("DISABLE SENSOR");
		disable.setBounds(10, 130, 110, 25);
		disable.setForeground(new Color(2,214,247));
		
		enabletf = new JTextField();
		enabletf.setBounds(enable.getX() + enable.getWidth() + 2, enable.getY(), 50, 25);
		disabletf = new JTextField();
		disabletf.setBounds(disable.getX() + disable.getWidth() + 2, disable.getY(), 50, 25);
		
		enableApply = new JButton("Apply");
		enableApply.setBounds(enabletf.getX() + enabletf.getWidth() + 3, enabletf.getY(), 100, 25);
		disableApply = new JButton("Apply");
		disableApply.setBounds(disabletf.getX() + disabletf.getWidth() + 3, disabletf.getY(), 100, 25);
		
		JLabel recordl = new JLabel("RETRIEVE RECORD DATA");
		recordl.setBounds(enableApply.getX() + enableApply.getWidth() + 3, enableApply.getY(), 170, 25);
		recordl.setForeground(new Color(2,214,247));
		JLabel currentl = new JLabel("RETRIEVE CURRENT DATA");
		currentl.setBounds(disableApply.getX() + disableApply.getWidth() + 3, disableApply.getY(), 170, 25);
		currentl.setForeground(new Color(2,214,247));
		
		recordtf = new JTextField();
		recordtf.setBounds(recordl.getX() + recordl.getWidth(), recordl.getY(), 50, 25);
		currenttf = new JTextField();
		currenttf.setBounds(currentl.getX() + currentl.getWidth(), currentl.getY(), 50, 25);
		
		record = new JButton("Apply");
		record.setBounds(recordtf.getX() + recordtf.getWidth() + 3, recordtf.getY(), 100, 25);
		current = new JButton("Apply");
		current.setBounds(currenttf.getX() + currenttf.getWidth() + 3, currenttf.getY(), 100, 25);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBounds(800 / 2 - 250 / 2, 160, 250, 200);
		
		area = new JTextArea();
		area.setForeground(new Color(2,214,247));
		area.setEditable(false);
		area.setOpaque(false);
		
		JScrollPane sp = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		panel.add(sp);
		
		bp.add(panel);
		bp.add(button);
		bp.add(enable);
		bp.add(disable);
		bp.add(enabletf);
		bp.add(disabletf);
		bp.add(enableApply);
		bp.add(disableApply);
		bp.add(currentl);
		bp.add(recordl);
		bp.add(recordtf);
		bp.add(currenttf);
		bp.add(record);
		bp.add(current);
		
		this.add(bp);
		
		button.addActionListener(this);
		enableApply.addActionListener(this);
		disableApply.addActionListener(this);
		record.addActionListener(this);
		current.addActionListener(this);
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		enableApply.setEnabled(false);
		disableApply.setEnabled(false);
		record.setEnabled(false);
		current.setEnabled(false);
		button.setEnabled(false);
		if(e.getSource().equals(enableApply)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						AdminClient.client.enableSensor(enabletf.getText());
						JOptionPane.showMessageDialog(SetupSensor.this, "The sensor has been successfully enabled.");
					} catch (IllegalAccessError e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "Selected sensor is already enabled.", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (NotFoundException e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "Sensor not found.", "Error", JOptionPane.ERROR_MESSAGE);
					}catch (SocketTimeoutException e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "The connection is down.", "Error", JOptionPane.ERROR_MESSAGE);
						SetupSensor.this.dispose();
						Authentication authentication = new Authentication(parent);
						authentication.setLocation(menu.getX() - menu.getWidth(), menu.getY());
						parent.getContentPane().add(authentication, 0);
						Utilities.transitionEffect(menu, authentication, parent, true);
						SwitchServerWindow s = new SwitchServerWindow();
						s.requestFocus();
					}
					enabletf.setText("");
					enableApply.setEnabled(true);
					disableApply.setEnabled(true);
					record.setEnabled(true);
					current.setEnabled(true);
					button.setEnabled(true);
				}
			}).start();
		}else if(e.getSource().equals(disableApply)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						AdminClient.client.disableSensor(disabletf.getText());
						JOptionPane.showMessageDialog(SetupSensor.this, "The sensor has been successfully disabled.");
					} catch (IllegalAccessError e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "Selected sensor is already disabled.", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (NotFoundException e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "Sensor not found.", "Error", JOptionPane.ERROR_MESSAGE);
					}catch (SocketTimeoutException e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "The connection is down.", "Error", JOptionPane.ERROR_MESSAGE);
						SetupSensor.this.dispose();
						Authentication authentication = new Authentication(parent);
						authentication.setLocation(menu.getX() - menu.getWidth(), menu.getY());
						parent.getContentPane().add(authentication, 0);
						Utilities.transitionEffect(menu, authentication, parent, true);
						SwitchServerWindow s = new SwitchServerWindow();
						s.requestFocus();
					}
					
					disabletf.setText("");
					enableApply.setEnabled(true);
					disableApply.setEnabled(true);
					record.setEnabled(true);
					current.setEnabled(true);
					button.setEnabled(true);
				}
			}).start();
		} else if(e.getSource().equals(record)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						ArrayList<String> list = AdminClient.client.getSensorRecord(recordtf.getText());
						area.setSize(250, 26 * list.size());
						area.setText("");
						for(int i = 0; i < list.size(); i++) {
							if(i < list.size() - 1)
								area.setText(area.getText() + list.get(i) + "\n");
							else
								area.setText(area.getText() + list.get(i));
						}
					} catch (IllegalAccessError e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "Sensor's identifier is required.", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (NotFoundException e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "Sensor not found.", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (SocketTimeoutException e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "The connection is down.", "Error", JOptionPane.ERROR_MESSAGE);
						SetupSensor.this.dispose();
						Authentication authentication = new Authentication(parent);
						authentication.setLocation(menu.getX() - menu.getWidth(), menu.getY());
						parent.getContentPane().add(authentication, 0);
						Utilities.transitionEffect(menu, authentication, parent, true);
						SwitchServerWindow s = new SwitchServerWindow();
						s.requestFocus();
					}
					recordtf.setText("");
					enableApply.setEnabled(true);
					disableApply.setEnabled(true);
					record.setEnabled(true);
					current.setEnabled(true);
					button.setEnabled(true);
				}
			}).start();
		}else if(e.getSource().equals(current)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						String value = AdminClient.client.currentValue(currenttf.getText());
						area.setText(value);
					} catch (IllegalAccessError e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "Sensor's identifier is required.", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (NonReadableChannelException e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "The sensor is off, turn it on before getting it's current value.", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (NotFoundException e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "Sensor not found.", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (SocketTimeoutException e) {
						JOptionPane.showMessageDialog(SetupSensor.this, "The connection is down.", "Error", JOptionPane.ERROR_MESSAGE);
						SetupSensor.this.dispose();
						Authentication authentication = new Authentication(parent);
						authentication.setLocation(menu.getX() - menu.getWidth(), menu.getY());
						parent.getContentPane().add(authentication, 0);
						Utilities.transitionEffect(menu, authentication, parent, true);
						SwitchServerWindow s = new SwitchServerWindow();
						s.requestFocus();
					}
					currenttf.setText("");
					enableApply.setEnabled(true);
					disableApply.setEnabled(true);
					record.setEnabled(true);
					current.setEnabled(true);
					button.setEnabled(true);
				}
			}).start();
		}else if(e.getSource().equals(button)) {
			SetupSensor.this.dispose();
		}
	}
}
