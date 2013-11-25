package userInterface.server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import connection.Client;

@SuppressWarnings("serial")
public class AdminClient extends JFrame implements ActionListener {
	
	private int width = 800;
	private int height = 600;
	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int deviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public Client client;
	
	private int acceleratorKey;
	
	private JMenuItem serverLocator;
	
	public AdminClient() {
		
		this.getContentPane().setPreferredSize(new Dimension(width, height));
		this.getContentPane().setLayout(null);
		this.getContentPane().setBackground(Color.BLACK);
		
		if(System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			acceleratorKey = ActionEvent.META_MASK;
		} else {
			acceleratorKey = ActionEvent.CTRL_MASK;
		}
		
		//Window's menu bar.
		JMenuBar menubar = new JMenuBar();
		
		JMenu server = new JMenu("Server");
		
		serverLocator = new JMenuItem("Switch Server");
		server.add(serverLocator);
		
		menubar.add(server);
		
		this.setJMenuBar(menubar);
		
		serverLocator.setMnemonic(KeyEvent.VK_S);
		serverLocator.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_S, acceleratorKey));
		serverLocator.addActionListener(this);
		//Window's components.
		Authentication a = new Authentication(this);
		this.add(a);
		
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
			System.out.println("hola");
		}
	}
}
