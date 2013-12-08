package userInterface.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dataBase.DatabaseController;

@SuppressWarnings("serial")
public class NewUser extends JDialog implements ActionListener {
	
	private ServerWindow parent;
	
	private JTextField userName = new JTextField();
	private JTextField password = new JTextField();
	private JButton add = new JButton("Add User");
	
	public NewUser(ServerWindow parent) {
		super(parent);
		this.parent = parent;
		
		this.setModal(true);
		
		this.setTitle("New User");
		this.setBounds((parent.getX() + parent.getWidth()) / 2, (parent.getY() + parent.getHeight()) / 2, 300, 150);
		this.setResizable(false);
		this.setLayout(null);
		
		JLabel user = new JLabel("Username");
		user.setBounds(10, 10, 80, 25);
		JLabel pass = new JLabel("Password");
		pass.setBounds(10, 40, 80, 25);
		
		userName.setBounds(100, 10, 160, 25);
		password.setBounds(100, 40, 160, 25);
		
		add.setBounds(this.getWidth() / 2 - 100 / 2, 80, 100, 25);
		
		this.add(user);
		this.add(userName);
		this.add(pass);
		this.add(password);
		this.add(add);
		
		add.addActionListener(this);
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(add)) {
			try {
				DatabaseController.addUser(userName.getText(), password.getText());
				JOptionPane.showMessageDialog(NewUser.this, "The new username has been added successfully");
				parent.updateAllUSerTable();
				this.dispose();
			} catch(IllegalAccessError e1) {
	    		JOptionPane.showMessageDialog(NewUser.this, "Username " + userName.getText() + " is already in use." , "Error", JOptionPane.ERROR_MESSAGE);
	    		userName.setText("");
				password.setText("");
			}
		}
	}
}
