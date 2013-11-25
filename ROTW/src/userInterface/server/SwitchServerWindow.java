package userInterface.server;

import graphicInterface.GradientPanel;
import graphicInterface.HintTextField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class SwitchServerWindow extends JDialog implements ActionListener 
{

	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int DeviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private int width = 300;
	private int height = 100;
	
	private JButton connectb = new JButton("Connect");
	protected static HintTextField iptf = new HintTextField("192.168.1.155");
	private JLabel ipLabel = new JLabel("IP ADDRESS:");
	
	public SwitchServerWindow()
	{
		
		//Window's components.
		GradientPanel gp = new GradientPanel(new Color(173, 173, 173), new Color(78, 78, 78));
		gp.setPreferredSize(new Dimension(width, height));
		gp.setLayout(null);
		ipLabel.setBounds(width / 2 - (83 + 145) / 2, height / 2 - (25 + 5 + 25) / 2, 83, 25);
		//ipLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		iptf.setBounds(ipLabel.getX() + ipLabel.getWidth(), ipLabel.getY() + 1, 145, ipLabel.getHeight() - 3);
		iptf.setBorder(BorderFactory.createLineBorder(new Color(123, 0, 1), 2));
		connectb.setBounds(width / 2 - 100 / 2, ipLabel.getY() + ipLabel.getHeight() + 10, 100, 25);
		
		
		gp.add(ipLabel);
		gp.add(iptf);
		gp.add(connectb);
		
		//Window's settings.
		this.setLocation(deviceWidth / 2 - width / 2, DeviceHeight / 2 - height / 2);
		this.setResizable(false);
		this.setContentPane(gp);
		this.setModal(true);
		this.setAlwaysOnTop(true);
		this.pack();
		//Window's Listeners.
		iptf.addActionListener(this);
		connectb.addActionListener(this);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
            	Authentication.bSignIn.setEnabled(true);
                Authentication.pfPassword.setEnabled(true);
                Authentication.tfUserName.setEnabled(true);
                Authentication.tfUserName.requestFocusInWindow();
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
			if(iptf.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Ip field can't be blank!", "Error", JOptionPane.ERROR_MESSAGE);
				System.out.println("Blank ip");
			}else
			{
				System.out.println(iptf.getText());
			}
		}
		
	}
}