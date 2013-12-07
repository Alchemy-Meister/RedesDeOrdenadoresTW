package userInterface.server;

import graphicInterface.JBackgroundedPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dataBase.Sensor;

@SuppressWarnings("serial")
public class SensorList extends JDialog implements ActionListener{

	
	JButton button;
	private int width = 800;
	private int height = 600;
	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int deviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public SensorList(ArrayList<Sensor> sensor) {
		super();
		
		JBackgroundedPanel bp = new JBackgroundedPanel("resources/Menu.png");
		
		bp.setLayout(null);
		this.setModal(true);
		this.setBounds(deviceWidth/2 - width/2, deviceHeight/2 - height/2, width, height);
		
		button = new JButton("Back");
		button.setBounds(800 / 2 + 125, 400, 125, 25);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBounds(800 / 2 - 250 / 2, 160, 250, 200);
		
		JTextArea area = new JTextArea();
		area.setForeground(new Color(2,214,247));
		area.setBackground(Color.BLACK);
		area.setEditable(false);
		area.setOpaque(false);
		
		for(int i = 0; i < sensor.size(); i++) {
			if(i < sensor.size() - 1)
				area.setText(area.getText() + sensor.get(i) + "\n");
			else
				area.setText(area.getText() + sensor.get(i));
		}
		area.setBounds(0, 0, 250, sensor.size() * 26);
		
		JScrollPane sp = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		panel.add(sp);
		
		bp.add(panel);
		bp.add(button);
		
		this.add(bp);
		
		button.addActionListener(this);
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(button)) {
			SensorList.this.dispose();
		}
	}
}
