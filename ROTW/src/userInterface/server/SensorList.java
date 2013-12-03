package userInterface.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dataBase.Sensor;
import util.Utilities;
import graphicInterface.JBackgroundedPanel;

@SuppressWarnings("serial")
public class SensorList extends JBackgroundedPanel implements ActionListener{

	AdminClient parent;
	
	JButton button;
	
	public SensorList(String imageName, AdminClient parent, ArrayList<Sensor> sensor) {
		super(imageName);
		
		this.parent = parent;
		parent.setTitle("Sensor List");
		
		this.setLayout(null);
		
		button = new JButton("Back");
		button.setBounds(800 / 2 - 125 / 2, 400, 125, 25);
		
		String[][] data = new String[sensor.size()][3];
		for(int i = 0; i < sensor.size(); i++) {
			data[i][0] = String.valueOf(sensor.get(i).getId());
			data[i][1] = sensor.get(i).getName();
			data[i][2] = Utilities.booleanToString(sensor.get(i).isEnabled());
		}
		
		DefaultTableModel model = new DefaultTableModel(data, new String[] {"ID", "DESCRIPTION", "VALUE"});
		
		JTable table = new JTable();
		table.setModel(model);
		
		table.setBounds(0, 0, 800, 600);
		
		JScrollPane scroll = new JScrollPane(table);
		
		scroll.setBounds(0, 0, 800, 10);
		scroll.add(table);
		
		this.add(button);
		this.add(scroll);
		
		button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(button)) {
			Menu menu = new Menu(parent, "resources/Menu.png");
			menu.setLocation(SensorList.this.getX() - SensorList.this.getWidth(), SensorList.this.getY());
			parent.getContentPane().add(menu, 0);
			Utilities.transitionEffect(SensorList.this, menu, parent, true);
		}
	}
}
