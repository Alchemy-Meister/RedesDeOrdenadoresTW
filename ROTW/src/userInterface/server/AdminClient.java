package userInterface.server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.SocketTimeoutException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import util.Utilities;
import connection.Client;

@SuppressWarnings("serial")
public class AdminClient extends JFrame implements ActionListener {
	
	private int width = 800;
	private int height = 600;
	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int deviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static Client client;
	
	private int acceleratorKey;
	
	protected static JMenuItem serverLocator;
	private JMenuItem closeWindow;
	protected static JMenuItem signout;
	
	public AdminClient() {
		
		this.getContentPane().setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.BLACK);
		
		if(System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			acceleratorKey = ActionEvent.META_MASK;
		} else {
			acceleratorKey = ActionEvent.CTRL_MASK;
		}
		
		//Window's menu bar.
		JMenuBar menubar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu server = new JMenu("Server");
		
		serverLocator = new JMenuItem("Switch Server");
		closeWindow = new JMenuItem("Close Window");
		signout = new JMenuItem("Sign Out");
		signout.setEnabled(false);
		
		file.add(closeWindow);
		
		server.add(serverLocator);
		server.add(signout);
		
		menubar.add(file);
		menubar.add(server);
		
		this.setJMenuBar(menubar);
		
		signout.setMnemonic(KeyEvent.VK_E);
		signout.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_E, acceleratorKey));
		signout.addActionListener(this);
		serverLocator.setMnemonic(KeyEvent.VK_S);
		serverLocator.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_S, acceleratorKey));
		closeWindow.setMnemonic(KeyEvent.VK_W);
		closeWindow.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_W, acceleratorKey));
		serverLocator.addActionListener(this);
		closeWindow.addActionListener(this);
		//Window's components.
		this.add(new Authentication(this), 0);
		
		//Window's Properties.
		this.pack();
		this.setLocation(deviceWidth/2 - width/2, deviceHeight/2 - height/2);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(serverLocator)) {
			if(AdminClient.client != null) {
				try {
					AdminClient.client.signOut();
				} catch (SocketTimeoutException e1) {
				}
				AdminClient.client = null;
			}
			SwitchServerWindow ssw = new SwitchServerWindow();
			ssw.requestFocus();
		} else if(e.getSource().equals(closeWindow)) {
			AdminClient.this.dispose();
		} else if(e.getSource().equals(signout)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						AdminClient.client.signOut();
					} catch (SocketTimeoutException e) {
					
					}
					AdminClient.client = null;
					AdminClient.signout.setEnabled(false);
					JPanel from = (JPanel) AdminClient.this.getContentPane().getComponent(0);
					JPanel to = new Authentication(AdminClient.this);
					to.setLocation(from.getX() - from.getWidth(), from.getY());
					AdminClient.this.getContentPane().add(to, 0);
					Utilities.transitionEffect(from, to, AdminClient.this, true);
					AdminClient.this.getContentPane().validate();
				}
			}).start();
		}
	}
}
