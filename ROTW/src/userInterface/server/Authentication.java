package userInterface.server;

import graphicInterface.GradientPanel;
import graphicInterface.JBackgroundedPanel;
import graphicInterface.RotatingComponent;
import graphicInterface.SpinningWheel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dataBase.DatabaseController;
import util.Shaker;
import util.Utilities;

@SuppressWarnings("serial")
public class Authentication extends JFrame implements FocusListener, ActionListener, KeyListener, DocumentListener {
	
	private int width = 800;
	private int height = 600;
	private int deviceWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int deviceHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	private JBackgroundedPanel panel = new JBackgroundedPanel("resources/UmbrellaSignIn.png");
	private GradientPanel authPanel;
	
	private JLabel lUserName = new JLabel("USER");
	private JLabel lPassword = new JLabel("ACCESS CODE");
	private JTextField tfUserName = new JTextField(12);
	private JPasswordField pfPassword = new JPasswordField(12);
	private SpinningWheel progress;
	private JBackgroundedPanel correct;
	private JBackgroundedPanel error;
	private JButton bSignIn = new JButton();
	
	private Shaker shacker;
	
	private Thread userChecker;
	private Thread passChecker;
	
	public Authentication() {
		super("Authentication");
		
		//Window's components.
		panel.setSize(new Dimension(800, 600));
		panel.setLayout(null);
		
		RotatingComponent rc = new RotatingComponent("resources/UmbrellaLogo.png", 95, 97, 255, 255, 0.5);
		authPanel = new GradientPanel(new Color(173, 173, 173), new Color(78, 78, 78));
		authPanel.setBounds(425, 200, 300, 58);
		
		authPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		authPanel.setLayout(new FlowLayout());
		authPanel.setBackground(Color.GRAY);
		authPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 10, 11), 1));
		
		shacker = new Shaker(authPanel);
		
		tfUserName.setBorder(BorderFactory.createLineBorder(new Color(123, 0, 1), 2));
		pfPassword.setBorder(BorderFactory.createLineBorder(new Color(123, 0, 1), 2));
		
		
		Box labelBox = Box.createVerticalBox();
		
		labelBox.add(lUserName);
		labelBox.add(Box.createRigidArea(new Dimension(0, 10)));
		labelBox.add(lPassword);
		
		lUserName.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		lPassword.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		
		authPanel.add(labelBox);
		
		authPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		Box fieldBox = Box.createVerticalBox();
		
		fieldBox.add(tfUserName);
		fieldBox.add(Box.createRigidArea(new Dimension(0, 6)));
		fieldBox.add(pfPassword);
		
		authPanel.add(fieldBox);
		
		JPanel validationPanel = new JPanel();
		validationPanel.setPreferredSize(new Dimension(25, 50));
		validationPanel.setOpaque(false);
		validationPanel.setLayout(null);
		
		progress = new SpinningWheel(0, 2, 20, 55);
		progress.setVisible(false);
		
		correct = new JBackgroundedPanel("resources/Correct.png", 0, 2, 20, 20);
		correct.setVisible(false);
		
		error = new JBackgroundedPanel("resources/Error.png", 0, 2, 20, 20);
		error.setVisible(false);
		
		bSignIn.setIcon(new ImageIcon(Utilities.resizeImage(20, 20, "resources/NextButton.png")));
		bSignIn.setBounds(0, 28, bSignIn.getIcon().getIconWidth(), bSignIn.getIcon().getIconHeight());
		bSignIn.setBorderPainted(false);
		bSignIn.setContentAreaFilled(false);
		
		validationPanel.add(correct);
		validationPanel.add(error);
		validationPanel.add(progress);
		validationPanel.add(bSignIn);
		
		authPanel.add(validationPanel);
		
		panel.add(rc);
		panel.add(authPanel);
		
		this.getContentPane().setPreferredSize(new Dimension(800, 600));
		this.getContentPane().setLayout(null);
		
		this.add(panel);
		
		//Window's properties. 
		this.setLocation(deviceWidth/2 - width/2, deviceHeight/2 - height/2);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		
		//Component's listeners
		tfUserName.addFocusListener(this);
		tfUserName.getDocument().addDocumentListener(this);
		bSignIn.addActionListener(this);
		pfPassword.addKeyListener(this);
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource().equals(tfUserName)) {
			
			if(tfUserName.getText().length() >= 0) {
				progress.setVisible(true);
				if(userChecker == null || !userChecker.isAlive()) {
					userChecker = new Thread(new Runnable() {	
				
						@Override
						public void run() {
							//TODO Thread to send to database the name validation.
							try {
								if(DatabaseController.validateUserName(tfUserName.getText())) {
									if(error.isVisible()) {
										error.setVisible(false);
									}
									correct.setVisible(true);
								} else {
									if(correct.isVisible()) {
										correct.setVisible(false);
									}
									error.setVisible(true);
								}
							} catch (InterruptedException e) {
								//Nothing to do there.
							}
							progress.setVisible(false);
						}
					});
				}
				userChecker.start();
			}
		}
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		if(e.getSource().equals(tfUserName)) {
			if(userChecker != null && userChecker.isAlive()) {
				userChecker.interrupt();
			}
			if(progress.isVisible()) {
				progress.setVisible(false);
			} else if(correct.isVisible()) {
				correct.setVisible(false);
			} else if(error.isVisible()) {
				error.setVisible(false);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(bSignIn)) {
			validatePassUser();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getSource().equals(pfPassword)) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				validatePassUser();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(progress.isVisible()) {
			progress.setVisible(false);
		} else if(correct.isVisible()) {
			correct.setVisible(false);
		} else if(error.isVisible()) {
			error.setVisible(false);
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if(progress.isVisible()) {
			progress.setVisible(false);
		} else if(correct.isVisible()) {
			correct.setVisible(false);
		} else if(error.isVisible()) {
			error.setVisible(false);
		}
	}
	
	private void validatePassUser() {
		if(passChecker == null || !passChecker.isAlive()) {
		passChecker = new Thread(new Runnable() {
			
				@Override
				public void run() {
					try {
						if(!DatabaseController.validateUser(tfUserName.getText(), Utilities.charArrayToString(pfPassword.getPassword()))) {
							tfUserName.setText(null);
							pfPassword.setText(null);
							tfUserName.requestFocus();
							if(userChecker.isAlive()) {
								userChecker.interrupt();
							}
							shacker.shakeComponent(authPanel);
						} else {
							System.out.println("Correct");
						}
					} catch (InterruptedException e) {
					
					}
				}
			});
			passChecker.start();
		}
	}
	
	public static void main(String[] args) {
		new Authentication();
	}
}