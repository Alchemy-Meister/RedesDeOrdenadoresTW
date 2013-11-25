package userInterface.server;

import graphicInterface.GradientPanel;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SwitchServerWindow extends JFrame implements ActionListener 
{

	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int DeviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private int width = 400;
	private int height = 300;
	
	private JButton sendb;
	public JTextField iptf;
	
	public SwitchServerWindow()
	{
		this.setBounds(deviceWidth / 2 - width / 2, DeviceHeight / 2 - height / 2, width, height);
		this.setTitle("Switch server");
		this.setResizable(false);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		iptf = new JTextField(16);
		iptf.addActionListener(this);
		sendb = new JButton("Send");
		sendb.addActionListener(this);
		
		
		GradientPanel gp = new GradientPanel(new Color(173, 173, 173), new Color(78, 78, 78));
		gp.setLayout(new BoxLayout(gp, BoxLayout.Y_AXIS));
		
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.add(new JLabel("Ip address: "));
		p.add(iptf);
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel p2 = new JPanel();
		p2.setOpaque(false);
		p2.add(sendb);
		gp.add(p);
		gp.add(p2);
		
		
		this.setContentPane(gp);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		
		if(e.getSource().equals(sendb) || e.getSource().equals(iptf))
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
	
	public static void main(String args[])
	{
		SwitchServerWindow ssw = new SwitchServerWindow();
		ssw.setVisible(true);
	}
	
	
	
}
