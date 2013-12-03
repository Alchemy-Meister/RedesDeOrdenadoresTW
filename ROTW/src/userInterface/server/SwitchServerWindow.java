package userInterface.server;

import graphicInterface.GradientPanel;
import graphicInterface.HintTextField;
import graphicInterface.SpinningWheel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import connection.Client;

@SuppressWarnings("serial")
public class SwitchServerWindow extends JDialog implements ActionListener, KeyListener 
{

	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int DeviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private int width = 300;
	private int height = 100;
	
	private JButton connectb = new JButton("Connect");
	protected HintTextField iptf = new HintTextField("192.168.1.155");
	private JLabel ipLabel = new JLabel("IP ADDRESS:");
	private SpinningWheel sp;
	
	private Thread connect;
	
	public SwitchServerWindow()
	{
		//Window's components.
		GradientPanel gp = new GradientPanel(new Color(173, 173, 173), new Color(78, 78, 78));
		gp.setPreferredSize(new Dimension(width, height));
		gp.setLayout(null);
		ipLabel.setBounds(width / 2 - (83 + 145) / 2, height / 2 - (25 + 5 + 25) / 2, 83, 25);
		iptf.setBounds(ipLabel.getX() + ipLabel.getWidth(), ipLabel.getY() + 1, 145, ipLabel.getHeight() - 3);
		iptf.setBorder(BorderFactory.createLineBorder(new Color(123, 0, 1), 2));
		connectb.setBounds(width / 2 - 100 / 2, ipLabel.getY() + ipLabel.getHeight() + 10, 100, 25);
		sp = new SpinningWheel(width / 2 - 25/2, connectb.getY(), 25, 55);
		sp.setVisible(false);
		
		gp.add(ipLabel);
		gp.add(iptf);
		gp.add(connectb);
		gp.add(sp);
		
		//Window's settings.
		this.setLocation(deviceWidth / 2 - width / 2, DeviceHeight / 2 - height / 2);
		this.setResizable(false);
		this.setContentPane(gp);
		this.setModal(true);
		this.setAlwaysOnTop(true);
		this.pack();
		//Window's Listeners.
		iptf.addActionListener(this);
		iptf.addKeyListener(this);
		connectb.addActionListener(this);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                if(connect != null && connect.isAlive()) {
                	connect.interrupt();
                	connect = null;
                }
                AdminClient.client = null;
                SwitchServerWindow.this.dispose();
            }
        });
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(connectb))
		{
			connectToServer();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getSource().equals(iptf)) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				connectToServer();
			}
		}
	}
	
	private void connectToServer() {
		if(iptf.getText().equals(""))
		{
			JOptionPane.showMessageDialog(SwitchServerWindow.this, "The IP can't be blank!", "Error", JOptionPane.ERROR_MESSAGE);
		}else
		{
			if(connect == null || !connect.isAlive()) {
				connect = new Thread( new Runnable() {
					
					@Override
					public void run() {
						boolean correct = false;
						connectb.setVisible(false);
						connectb.repaint();
						sp.setVisible(true);
						try {
							iptf.setEnabled(false);
							AdminClient.client = new Client(iptf.getText(), 1234);
							correct = true;
						} catch (IOException e1) {
							iptf.setEnabled(true);
							iptf.setText("");
							sp.setVisible(false);
							AdminClient.client = null;
							connectb.setVisible(true);
							JOptionPane.showMessageDialog(SwitchServerWindow.this, "Couldn't connect with the server.", "Error", JOptionPane.ERROR_MESSAGE);
						}
						if(correct) {
							SwitchServerWindow.this.dispose();
						}
					}
				});
				connect.start();
			}
		}
	}
}