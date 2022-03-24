package stock_market;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AuthenticationMenu extends JFrame {
	private CardLayout cardLayout = new CardLayout();
	JPanel container = new JPanel(cardLayout);
	
	private JButton loginButton = new JButton("Login");
	private JButton registerButton = new JButton("Register");
	
	private JTextField loginUsername = new JTextField(10);
	private JPasswordField  loginPassword = new JPasswordField(10);
	private JLabel loginErrorLabel = new JLabel("");
	private JButton loginButtonSubmit = new JButton("Login");
	
	private JTextField registerUsername = new JTextField(10);
	private JPasswordField  registerPassword = new JPasswordField(10);
	private JTextField registerMoneyAmount = new JTextField(10);
	private JLabel registerErrorLabel = new JLabel("");
	private JButton registerButtonSubmit = new JButton("Register");
	
	private class ExitDialog extends Dialog {

		public ExitDialog(JFrame owner) {
			super(owner);
			
			setBounds(owner.getX() + owner.getWidth() / 2,
					owner.getY() + owner.getHeight() / 2, 200, 100);
			setTitle("Exit");
			setResizable(false);
			this.setModalityType(ModalityType.APPLICATION_MODAL);
			
			setLayout(new GridLayout(2, 0));
			
			JPanel p = new JPanel();
			p.add(new JLabel("Are you sure you want to exit?"));
			
			add(p);
			
			JPanel p2 = new JPanel();
			
			JButton exitButton = new JButton("Exit");
		
			exitButton.addActionListener((ae) -> {
				AuthenticationMenu.this.dispose();
			});
			
			p2.add(exitButton);
			
			JButton cancelButton = new JButton("Cancel");
			
			cancelButton.addActionListener((ae) -> {
				dispose();
			});
			
			p2.add(cancelButton);
			
			add(p2);
			
			pack();
			

			
			
			addWindowListener(new WindowAdapter() {
				 public void windowClosing(WindowEvent e) {
					 dispose();
				 }
			});
			
			setVisible(true);
		}
	}
	
	public AuthenticationMenu() {
		
		setTitle("Stock Market");
		setLocation(300, 150);
		setPreferredSize(new Dimension(280, 250));
		
		setLayout(new BorderLayout());
		
		populateStartMenu();
		populateLoginMenu();
		populateRegisterMenu();
		
		add(container, BorderLayout.NORTH);
		pack();
		addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e) {
				 new ExitDialog(AuthenticationMenu.this);
			 }
		});
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	private void populateStartMenu() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		JPanel p5 = new JPanel();
		p5.add(new JLabel(""));
		
		p.add(p5);
		
		JPanel p1 = new JPanel();
		
		loginButton.addActionListener((ae) -> {
			cardLayout.next(container);
		});
		
		p1.add(loginButton);
		
		p.add(p1);
		
		JPanel p2 = new JPanel();
		
		registerButton.addActionListener((ae) -> {
			cardLayout.last(container);
		});
		
		p2.add(registerButton);
		
		p.add(p2);
		
		JPanel p3 = new JPanel();
		p3.add(new JLabel(""));
		
		p.add(p3);
		
		JPanel p4 = new JPanel();
		p4.add(new JLabel(""));
		
		p.add(p4);
		
		JPanel p6 = new JPanel();
		p6.add(new JLabel(""));
		
		p.add(p6);
		
		container.add(p);
	}
	
	private void populateLoginMenu() {
		JPanel loginPanel = new JPanel(new GridLayout(5, 1));
		
		JPanel loginP1 = new JPanel(new FlowLayout());
		JLabel usernameLabel = new JLabel("Username:");
		loginP1.add(usernameLabel);
		loginP1.add(loginUsername);
		
		loginPanel.add(loginP1);
		
		JPanel loginP2 = new JPanel(new FlowLayout());
		JLabel passwordLabel = new JLabel("Password:");
		loginP2.add(passwordLabel);
		loginP2.add(loginPassword);
		
		loginPanel.add(loginP2);
		
		JPanel loginP3 = new JPanel(new FlowLayout());
		loginP3.add(loginErrorLabel);
		
		loginPanel.add(loginP3);
		
		
		JPanel loginP4 = new JPanel(new FlowLayout());
		
		loginButtonSubmit.addActionListener((ae) -> {
			login();
		});
		
		loginP4.add(loginButtonSubmit);
		
		loginPanel.add(loginP4);
		
		JPanel loginP5 = new JPanel(new FlowLayout());
		JButton backButton = new JButton("Back");
		
		backButton.addActionListener((ae) -> {
			cardLayout.first(container);
			loginUsername.setText("");
			loginUsername.revalidate();
			loginPassword.setText("");
			loginPassword.revalidate();
			loginErrorLabel.setText("");
			loginErrorLabel.revalidate();
		});
		
		loginP5.add(backButton);
		
		loginPanel.add(loginP5);
		
		loginUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
					login();
			}
		});
		
		loginPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
					login();
			}
		});
		
		container.add(loginPanel);
	}
	
	private void populateRegisterMenu() {

		JPanel registerPanel = new JPanel(new GridLayout(6, 1));
		
		JPanel registerP1 = new JPanel(new FlowLayout());
		JLabel usernameLabel = new JLabel("Username:");
		registerP1.add(usernameLabel);
		registerP1.add(registerUsername);
		
		registerPanel.add(registerP1);
		
		JPanel registerP2 = new JPanel(new FlowLayout());
		JLabel passwordLabel = new JLabel("Password:");
		registerP2.add(passwordLabel);
		registerP2.add(registerPassword);
		
		registerPanel.add(registerP2);
		
		JPanel registerP3 = new JPanel(new FlowLayout());
		registerP3.add(new JLabel("Money($):"));
		registerP3.add(registerMoneyAmount);
		
		registerPanel.add(registerP3);
		
		JPanel registerP4 = new JPanel(new FlowLayout());
		registerP4.add(registerErrorLabel);
		
		registerPanel.add(registerP4);
		
		JPanel registerP5 = new JPanel(new FlowLayout());
		
		registerButtonSubmit.addActionListener((ae) -> {
			register();
		});
		
		registerP5.add(registerButtonSubmit);
		
		registerPanel.add(registerP5);
		
		JPanel registerP6 = new JPanel(new FlowLayout());
		JButton backButton = new JButton("Back");
		
		backButton.addActionListener((ae) -> {
			cardLayout.first(container);
			registerUsername.setText("");
			registerUsername.revalidate();
			registerPassword.setText("");
			registerPassword.revalidate();
			registerErrorLabel.setText("");
			registerErrorLabel.revalidate();
			registerMoneyAmount.setText("");
			registerMoneyAmount.revalidate();
		});
		
		registerP6.add(backButton);
		
		registerPanel.add(registerP6);
		
		
		registerUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
					register();
			}
		});
		
		registerPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
					register();
			}
		});
		
		registerMoneyAmount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
					register();
			}
		});
		
		container.add(registerPanel);
	}

	private void login() {
		String username = loginUsername.getText();
		String password = String.valueOf(loginPassword.getPassword());
		
		if (username.length() == 0) {
			loginErrorLabel.setText("Enter username");
			loginErrorLabel.revalidate();
			return;
		} 
		if (password.length() == 0) {
			loginErrorLabel.setText("Enter password");
			loginErrorLabel.revalidate();
			return;
		}
		
		try {
			FileReader fileReader = new FileReader(new File("users.txt"));
			BufferedReader bufferReader = new BufferedReader(fileReader);
			bufferReader.lines().forEach(l -> {
				Pattern pattern = Pattern.compile("(.*):(.*)");
				Matcher matcher = pattern.matcher(l);
				
				if (matcher.matches()) {
					if (username.equals(matcher.group(1)) && password.equals(matcher.group(2))) {
						loginErrorLabel.setText("Login successful");
						loginErrorLabel.revalidate();
						dispose();
						new App(new User(username, password));
					} else if (username.equals(matcher.group(1)) && !password.equals(matcher.group(2))) {
						loginErrorLabel.setText("Wrong password");
						loginErrorLabel.revalidate();
					} else if (username.equals(bufferReader)) {
						loginErrorLabel.setText("Username doesn't exist");
						loginErrorLabel.revalidate();
					}
				}
			});
			bufferReader.close();
		} catch (IOException e) {
			System.out.println("Error reading file \"users.txt\"");
		}
	}
	
	private void register() {
		registerErrorLabel.setText("");
		registerErrorLabel.revalidate();
		
		String username = registerUsername.getText();
		String password = String.valueOf(registerPassword.getPassword());
		String money = registerMoneyAmount.getText();
		
		if (username.length() == 0) {
			registerErrorLabel.setText("Enter username");
			registerErrorLabel.revalidate();
			return;
		}
		if (password.length() == 0) {
			registerErrorLabel.setText("Enter password");
			registerErrorLabel.revalidate();
			return;
		}
		if (money.length() == 0) {
			registerErrorLabel.setText("Enter money amount");
			registerErrorLabel.revalidate();
			return;
		}

		// Check if username is valid
		Pattern usernamePattern = Pattern.compile("^(?=[a-zA-Z0-9._]{4,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
		Matcher usernameMatcher = usernamePattern.matcher(username);
		if (!usernameMatcher.matches()) {
			registerErrorLabel.setText("Invalid username");
			registerErrorLabel.revalidate();
			return;
		}
		
		// Check if password is valid
		Pattern passwordPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9@#$_-]{3,31}");
		Matcher passwordMatcher = passwordPattern.matcher(password);
		if (!passwordMatcher.matches()) {
			registerErrorLabel.setText("Invalid password");
			registerErrorLabel.revalidate();
			return;
		}

		try {
			FileReader fileReader = new FileReader(new File("users.txt"));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			bufferedReader.lines().forEach(l -> {
				Pattern pattern = Pattern.compile("(.*):(.*)");
				Matcher matcher = pattern.matcher(l);
				
				if (matcher.matches()) {
					if (username.equals(matcher.group(1))) {
						registerErrorLabel.setText("Username already taken");
						registerErrorLabel.revalidate();
						return;
					}
				}
			});
			
			bufferedReader.close();
		} catch (IOException e) {
			System.out.println("Error reading file \"users.txt\"");
		}
		
		// Check if money amount is valid
		Double moneyAmount;
		try {
			moneyAmount = Double.valueOf(money);
		} catch (Exception e) {
			registerErrorLabel.setText("Invalid money amount");
			registerErrorLabel.revalidate();
			return;
		}
		
		if (moneyAmount < 0) {
			registerErrorLabel.setText("Money amount must be positive number");
			registerErrorLabel.revalidate();
			return;
		}
		
		// Add new user to a file
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("users.txt", true));
			bufferedWriter.write(username + ":" + password + "\n");
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("Error writing to a file \"users.txt\"");
		}
		
		// Create new file for storing user data and fill in money amount
		String filePath = "user_data\\" + username + ".txt";
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, true));
			bufferedWriter.write(moneyAmount + "\n$\n$\n#\n#");
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("Error writing to a file " + filePath);
		}
		
		registerErrorLabel.setText("Register successful");
		registerErrorLabel.revalidate();
	}
}
