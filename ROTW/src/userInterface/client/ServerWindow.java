package userInterface.client;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import util.AllUserTableModel;
import util.UserTableModel;
import connection.Server;
import connection.Service;
import dataBase.DatabaseController;
import dataBase.User;

@SuppressWarnings("serial")
public class ServerWindow extends JFrame implements ActionListener {

	private int width = 800;
	private int height = 600;
	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int deviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	private JButton updateConnectedUsers = new JButton("Update Connected User List");
	private JButton updateMaximum = new JButton("Update Maximum");
	private JButton disconnectSelectedUsers = new JButton("Disconnect selected users");
	
	private JTextField maximumUser = new JTextField();
	
	private Thread serverActivation;
	private Server server;
	
	private JTable connectedUsersTable;
	private UserTableModel model;
	
	private JTable allUsers;
	private AllUserTableModel allmodel;
	
	private JButton addUser = new JButton("Add User");
	private JButton removeUser = new JButton("Remove selected users");
	private JButton updateUser = new JButton("Update selected users");
	
	JScrollPane sp;
	
	public ServerWindow() {
		super("Server Administration");
		this.setBounds(deviceWidth / 2 - width / 2,  deviceHeight / 2 - height / 2, width, height);
		this.setResizable(false);
		
		this.setLayout(null);
		
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		
		JLabel maximumUsers = new JLabel("Maximum connected users");
		maximumUsers.setBounds(230, 0, 220, 25);
		
		maximumUser.setBounds(400, 0, 50, 25);
		
		tablePanel.setBounds(0, 25, 800, 200);
		
		connectedUsersTable = new JTable();
		sp = new JScrollPane(connectedUsersTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		model = new UserTableModel(0, 3);
		model.setColumnIdentifiers(new String[] {"Username", "Password", "Disconnect"});
		connectedUsersTable.setModel(model);
		connectedUsersTable.getTableHeader().setReorderingAllowed(false);
		tablePanel.add(sp, BorderLayout.CENTER);
		
		updateConnectedUsers.setBounds(0, 0, 200, 25);
		updateMaximum.setBounds(maximumUser.getX() + maximumUser.getWidth() + 5, maximumUser.getY(), 150, 25);
		
		disconnectSelectedUsers.setBounds(0, 225, 200, 25);
		
		JPanel allUsersPanel = new JPanel();
		allUsersPanel.setBounds(0, 250, 800, 200);
		allUsersPanel.setLayout(new BorderLayout());
		
		allUsers = new JTable();
		
		allmodel = new AllUserTableModel(0, 4);
		allmodel.setColumnIdentifiers(new String[] {"Username", "Password", "Update", "Remove"});
		allUsers.setModel(allmodel);
		allUsers.getTableHeader().setReorderingAllowed(false);
		
		ArrayList<User> userList = DatabaseController.getUsers();
		for(int i = 0; i < userList.size(); i++) {
			allmodel.addRow(new Object[] {userList.get(i).getUserName(), userList.get(i).getPassword(), false, false});
		}
		model.fireTableDataChanged();
		allUsersPanel.add(new JScrollPane(allUsers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		
		addUser.setBounds(0, allUsersPanel.getY() + allUsersPanel.getHeight(), 100, 25);
		updateUser.setBounds(addUser.getX() + addUser.getWidth() + 3, addUser.getY(), 175, 25);
		removeUser.setBounds(updateUser.getX() + updateUser.getWidth() + 3, updateUser.getY(), 175, 25);
		
		this.add(updateConnectedUsers);
		this.add(tablePanel);
		this.add(maximumUser);
		this.add(maximumUsers);
		this.add(updateMaximum);
		this.add(disconnectSelectedUsers);
		this.add(allUsersPanel);
		this.add(addUser);
		this.add(updateUser);
		this.add(removeUser);
		
		updateConnectedUsers.addActionListener(this);
		updateMaximum.addActionListener(this);
		disconnectSelectedUsers.addActionListener(this);
		addUser.addActionListener(this);
		updateUser.addActionListener(this);
		removeUser.addActionListener(this);
		
		server = new Server(1234);
		
		serverActivation = new Thread( new Runnable() {
			
			@Override
			public void run() {
				server.startServer();
			}
		});
		serverActivation.start();
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				server.shutdownServer();
				System.exit(0);
			}
		});
		
		maximumUser.setText(server.getMaximumUserLimit() + "");
		
		this.setVisible(true);
	}
	
	public static void main(String[] argv) {
		new ServerWindow();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(updateConnectedUsers)) {
			updateUserTable();
		} else if(e.getSource().equals(updateMaximum)) {
			try {
				server.setMaximumUserLimit(Integer.valueOf(maximumUser.getText()));
			}catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(ServerWindow.this, "Maximum connected user field must be a decimal number", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(e.getSource().equals(disconnectSelectedUsers)) {
			ArrayList<Service> serviceList = new ArrayList<Service>();
			for (int i = 0; i < model.getRowCount(); i++) {
			     boolean isChecked = (Boolean) model.getValueAt(i, 2);
			     if (isChecked) {
			        server.getServices().get(i).terminateSesion();
			        serviceList.add(server.getServices().get(i));
			    }
			}
			for(int i = 0; i < serviceList.size(); i++) {
				server.getServices().remove(serviceList.get(i));
			}
			updateUserTable();
		} else if(e.getSource().equals(addUser)) {
			NewUser n = new NewUser(this);
			n.requestFocus();
		} else if(e.getSource().equals(removeUser)) {
			if(allUsers.getCellEditor() != null) {
				allUsers.getCellEditor().stopCellEditing();
			}
			ArrayList<User> removeList = new ArrayList<User>();
			for(int i = 0; i < allmodel.getRowCount(); i++) {
				boolean isChecked = (Boolean) allmodel.getValueAt(i, 3);
			    if (isChecked) {
			    	removeList.add(new User((String) allmodel.getValueAt(i, 0), (String) allmodel.getValueAt(i, 1)));
			    }
			}
			for(int i = 0; i < removeList.size(); i++) {
				try {
					DatabaseController.removeUser(removeList.get(i).getUserName(), removeList.get(i).getPassword());
					ArrayList<Service> removeServices = new ArrayList<Service>();
					for(int j = 0; j < server.getServices().size(); j++) {
						if(server.getServices().get(j).getUser().getUserName().equals(removeList.get(i).getUserName())) {
							server.getServices().get(i).terminateSesion();
							removeServices.add(server.getServices().get(j));
						}
					}
					for(int j = 0; j < removeServices.size(); j++) {
						if(server.getServices().get(j).getUser().getUserName().equals(removeList.get(i).getUserName())) {
							server.getServices().remove(j);
						}
					}
				} catch(IllegalAccessError e1) {
					JOptionPane.showMessageDialog(ServerWindow.this, "Username " + removeList.get(i).getUserName() + " does not exit." , "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			updateAllUSerTable();
			updateUserTable();
		} else if(e.getSource().equals(updateUser)) {
			if(allUsers.getCellEditor() != null) {
				allUsers.getCellEditor().stopCellEditing();
			}
			ArrayList<User> userList = DatabaseController.getUsers();
			for (int i = 0; i < allmodel.getRowCount(); i++) {
			     boolean isChecked = (Boolean) allmodel.getValueAt(i, 2);
			     if (isChecked) {
			    	 try {
				    	 if(!userList.get(i).getUserName().equals((String) allmodel.getValueAt(i, 0))) {
				    		 DatabaseController.updateUserName(userList.get(i).getUserName(), (String) allmodel.getValueAt(i, 0));
				    	 }
				    	 DatabaseController.updatePassword((String) allmodel.getValueAt(i, 0), userList.get(i).getPassword(), (String) allmodel.getValueAt(i, 1));
				    	 allmodel.setValueAt(false, i, 2);
				    	 for(int j = 0; j < server.getServices().size(); j++) {
				    		 if(server.getServices().get(j).getUser().getUserName().equals(userList.get(i).getUserName())) {
				    			 server.getServices().get(j).getUser().setUserName((String) allmodel.getValueAt(i, 0));
				    			 if(!server.getServices().get(j).getUser().getPassword().equals("")) {
				    				 server.getServices().get(j).getUser().setPassword((String) allmodel.getValueAt(i, 1));
				    			 }
				    		 }
				    	 }
			    	 } catch(IllegalAccessError e1) {
			    		 allmodel.setValueAt(userList.get(i).getUserName(), i, 0);
			    		 allmodel.setValueAt(userList.get(i).getPassword(), i, 1);
			    		 JOptionPane.showMessageDialog(ServerWindow.this, "Username " + (String) allmodel.getValueAt(i, 0) + " is already in use." , "Error", JOptionPane.ERROR_MESSAGE);
			    	 }
			    }
			}
			updateUserTable();
		}
	}
	
	public void updateUserTable() {
		ArrayList<User> userList = server.getUsers();
		for(int i = model.getRowCount() - 1; i >= 0 ; i--) {
			model.removeRow(i);
		}
		for(int i = 0; i < userList.size(); i++) {
			model.addRow(new Object[] {userList.get(i).getUserName(), userList.get(i).getPassword(), false});
		}
		model.fireTableDataChanged();
	}
	
	public void updateAllUSerTable() {
		ArrayList<User> userList = DatabaseController.getUsers();
		for(int i = allmodel.getRowCount() - 1; i >= 0 ; i--) {
			allmodel.removeRow(i);
		}
		for(int i = 0; i < userList.size(); i++) {
			allmodel.addRow(new Object[] {userList.get(i).getUserName(), userList.get(i).getPassword(), false, false});
		}
		allmodel.fireTableDataChanged();
	}
}
