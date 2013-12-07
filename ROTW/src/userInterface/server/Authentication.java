package userInterface.server;

import graphicInterface.Animate;
import graphicInterface.GradientPanel;
import graphicInterface.JBackgroundedPanel;
import graphicInterface.RotatingComponent;
import graphicInterface.SpinningWheel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import connection.Client;
import util.Shaker;
import util.Utilities;

@SuppressWarnings("serial")
public class Authentication extends JBackgroundedPanel implements FocusListener, ActionListener, KeyListener, DocumentListener {
	
	private GradientPanel authPanel;
	
	private JLabel lUserName = new JLabel("USER");
	private JLabel lPassword = new JLabel("ACCESS CODE");
	protected JTextField tfUserName = new JTextField(12);
	protected JPasswordField pfPassword = new JPasswordField(12);
	private SpinningWheel progress;
	private JBackgroundedPanel correct;
	private JBackgroundedPanel error;
	protected JButton bSignIn = new JButton();
	
	private Shaker shacker;
	
	private Thread userChecker;
	private Thread passChecker;
	
	private AdminClient parent;
	
	protected Animate panelSigninA;
	protected Animate panelMenuA;
	
	private Object lastFocused = null;
	
	public Authentication(AdminClient parent) {
		super("resources/UmbrellaSignIn.png");
		this.parent = parent;
		this.parent.setTitle("Authentication");
		if(AdminClient.client == null) {
			try {
				AdminClient.client = new Client("127.0.0.1", 1234);
			}catch (IOException e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					
					public void run() {
						new SwitchServerWindow();
					}
				});
			}
		}
		//Window's components.
		this.setSize(new Dimension(800, 600));
		this.setLayout(null);
		
		RotatingComponent rc = new RotatingComponent("resources/UmbrellaLogo.png", 95, 97, 255, 255, 0.5);
		authPanel = new GradientPanel(new Color(173, 173, 173), new Color(78, 78, 78));
		authPanel.setBounds(425, 200, 300, 58);
		authPanel.setLayout(null);
		
		authPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 10, 11), 1));
		
		AdminClient.serverLocator.setEnabled(true);
		
		shacker = new Shaker(authPanel);
		
		tfUserName.setBorder(BorderFactory.createLineBorder(new Color(123, 0, 1), 2));
		pfPassword.setBorder(BorderFactory.createLineBorder(new Color(123, 0, 1), 2));
		
		lUserName.setBounds(70, 8, 40, 23);
		//lUserName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		lPassword.setBounds(15, lUserName.getY() + lUserName.getHeight() - 4, 100, 25);
		//lPassword.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		authPanel.add(lUserName);
		authPanel.add(lPassword);
		
		tfUserName.setBounds(lUserName.getX() + lUserName.getWidth(), 8, 145, lUserName.getHeight() - 3);
		pfPassword.setBounds(lUserName.getX() + lUserName.getWidth(), lPassword.getY() + 4, tfUserName.getWidth(), tfUserName.getHeight() + 2);
		
		authPanel.add(tfUserName);
		authPanel.add(pfPassword);
		
		progress = new SpinningWheel(5, 0, 20, 55);
		progress.setVisible(false);
		
		correct = new JBackgroundedPanel("resources/Correct.png", 5, 0, 20, 20);
		correct.setVisible(false);
		
		error = new JBackgroundedPanel("resources/Error.png", 5, 0, 20, 20);
		error.setVisible(false);
		
		bSignIn.setIcon(new ImageIcon(Utilities.resizeImage(20, 20, "resources/NextButton.png")));
		bSignIn.setBounds(5, 23, bSignIn.getIcon().getIconWidth(), bSignIn.getIcon().getIconHeight());
		bSignIn.setBorderPainted(false);
		bSignIn.setContentAreaFilled(false);
		bSignIn.setFocusable(false);
		
		JPanel panelAux = new JPanel();
		panelAux.setLayout(null);
		panelAux.setBounds(tfUserName.getX() + tfUserName.getWidth() + 5, lUserName.getY(), 50, 50);
		panelAux.setOpaque(false);
		
		panelAux.add(correct);
		panelAux.add(error);
		panelAux.add(progress);
		panelAux.add(bSignIn);
		
		authPanel.add(panelAux);
		
		this.add(rc);
		this.add(authPanel);
		
		//Component's listeners
		tfUserName.addFocusListener(this);
		pfPassword.addFocusListener(this);
		tfUserName.getDocument().addDocumentListener(this);
		bSignIn.addActionListener(this);
		pfPassword.addKeyListener(this);
		
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		lastFocused = e.getSource();
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
		} else if(e.getSource().equals(pfPassword)) {
			if(lastFocused == tfUserName) {
				if(tfUserName.getText().length() >= 0) {
					progress.setVisible(true);
					if(userChecker == null || !userChecker.isAlive()) {
						userChecker = new Thread(new Runnable() {	
					
							@Override
							public void run() {
								boolean granted = false;
								try {
								granted = AdminClient.client.validateUserName(tfUserName.getText());
								} catch(IllegalAccessError e) {
									JOptionPane.showMessageDialog(Authentication.this, "The server is full.", "Error", JOptionPane.ERROR_MESSAGE);
									SwitchServerWindow s = new SwitchServerWindow();
									s.requestFocus();
								}
								if(AdminClient.client != null && granted) {
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
								progress.setVisible(false);
								progress.repaint();
							}
						});
					}
					userChecker.start();
				}
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
						boolean granted = false;
						try {
							granted = AdminClient.client.validatePassword(Utilities.charArrayToString(pfPassword.getPassword()));
						} catch(IllegalAccessError e) {
							JOptionPane.showMessageDialog(Authentication.this, "The server is full.", "Error", JOptionPane.ERROR_MESSAGE);
							SwitchServerWindow s = new SwitchServerWindow();
							s.requestFocus();
						}
						if(AdminClient.client != null && granted) {
							panelSigninA = new Animate(Authentication.this, Authentication.this.getBounds(), 
									new Rectangle(Authentication.this.getX() - Authentication.this.getWidth(),
											Authentication.this.getY(), Authentication.this.getWidth(), Authentication.this.getHeight()), 350);
							Menu menu = new Menu(parent, "resources/Menu.png");
							menu.setLocation(Authentication.this.getX() + Authentication.this.getWidth(), Authentication.this.getY());
							parent.getContentPane().add(menu, 0);
							panelMenuA = new Animate(menu, menu.getBounds(), Authentication.this.getBounds(), 350);
							panelMenuA.start();
							panelSigninA.start();
							while(!panelSigninA.hasFinished()) {
								//Waiting bitch
								Thread.sleep(0);
							}
							parent.remove(Authentication.this);
						} else {
							tfUserName.setText(null);
							pfPassword.setText(null);
							tfUserName.requestFocus();
							if(userChecker != null && userChecker.isAlive()) {
								userChecker.interrupt();
							}
							shacker.shakeComponent(authPanel);
						}
					} catch (InterruptedException e) {
					
					}
				}
			});
			passChecker.start();
		}
	}
}