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

import util.UserTableModel;
import connection.Server;
import dataBase.User;

@SuppressWarnings("serial")
public class ServerWindow extends JFrame implements ActionListener {

	private int width = 800;
	private int height = 600;
	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int deviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	private JButton updateConnectedUsers = new JButton("Update Connected User List");
	private JButton updateMaximum = new JButton("Update Maximum");
	
	private JTextField maximumUser = new JTextField();
	
	private Thread serverActivation;
	private Server server;
	
	private JTable connectedUsersTable;
	private UserTableModel model;
	
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
		tablePanel.add(sp, BorderLayout.CENTER);
		
		updateConnectedUsers.setBounds(0, 0, 200, 25);
		updateMaximum.setBounds(maximumUser.getX() + maximumUser.getWidth() + 5, maximumUser.getY(), 150, 25);
		
		this.add(updateConnectedUsers);
		this.add(tablePanel);
		this.add(maximumUser);
		this.add(maximumUsers);
		this.add(updateMaximum);
		
		updateConnectedUsers.addActionListener(this);
		updateMaximum.addActionListener(this);
		
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
}
