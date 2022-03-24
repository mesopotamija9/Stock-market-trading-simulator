package stock_market;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class App extends JFrame {
	User user;
	Graph graph = new Graph(this);
	Stock stock;
	Portfolio portfolio = null;
	JLabel balanceLabel;
	private JButton logoutButton = new JButton("Logout");
	private JButton portfolioButton = new JButton("Portfolio");
	private JTextField actionName = new JTextField(10);
	
	private JTextField startTimestamp = new JTextField(10);
	
	private JTextField endTimestamp = new JTextField(10);
	
	private JLabel errorLabel = new JLabel("");
	private JButton submitButton = new JButton("Submit");
	
	private JTextField nTextField = new JTextField(4);
	private JCheckBox showMA = new JCheckBox();
	private JCheckBox showEMA = new JCheckBox();
	private JLabel indicatorsErrorLabel = new JLabel("");
	
	JLabel openLabel = new JLabel("");
	JLabel closeLabel = new JLabel("");
	JLabel highLabel = new JLabel("");
	JLabel lowLabel = new JLabel("");
	JLabel timestampLabel = new JLabel("");
	
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
				App.this.dispose();
				if (portfolio != null) {
					portfolio.dispose();
				}
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
			
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setVisible(true);
		}
	}
	
	public App(User user) {
		this.user = user;
		
		// Tmp for testing
		actionName.setText("aapl");
		startTimestamp.setText("1621252081");
		endTimestamp.setText("1624197101");
		
		// End tmp for testing
		
		setTitle("Stock Market");
		setLocation(300, 150);
		setSize(new Dimension(700, 500));
		setLayout(new BorderLayout());
		
		populateEast();
		
		
		add(graph, BorderLayout.CENTER);
		
		addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e) {
				 new ExitDialog(App.this);
			 }
		});
		
		processUserInput();
		
		graph.stockLoaded = false;
		graph.scale = 1;
		graph.repaint();
		
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new App(new User("mesopotamija9", "Mesopotamija9"));
	}
	
	private void populateEast() {
		JPanel eastPanel = new JPanel(new GridLayout(0, 1));
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.add(new JLabel(user.getUsername()));
		balanceLabel = new JLabel("   " + new Formatter().format("%.2f", user.getBalance())   + "$");
		p1.add(balanceLabel);
		
		eastPanel.add(p1);
		
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		portfolioButton.addActionListener((ae) -> {
			if (portfolio == null)
				portfolio = new Portfolio(user, App.this);
			else 
				portfolio.toFront();
		});
		
		p2.add(portfolioButton);
		
		logoutButton.addActionListener((ae)->{
			dispose();
			if (portfolio != null)
				portfolio.dispose();
			new AuthenticationMenu();
		});
		
		p2.add(logoutButton);
		
		eastPanel.add(p2);
		
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p3.add(new JLabel("Action name:"));
		p3.add(actionName);
		
		
		eastPanel.add(p3);
		
		JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p4.add(new JLabel("Start timestamp:"));
		p4.add(startTimestamp);
		
		
		eastPanel.add(p4);
		
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p5.add(new JLabel("End timestamp:"));
		p5.add(endTimestamp);
		
		
		eastPanel.add(p5);
		
		JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p6.add(errorLabel);
		
		eastPanel.add(p6);
		
		JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		submitButton.addActionListener((ae) -> {
			processUserInput();
			
			graph.stockLoaded = false;
			graph.scale = 1;
			graph.repaint();
		});
		
		p7.add(submitButton);
		
		eastPanel.add(p7);
		
		JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p8.add(new JLabel("n:"));
		
		nTextField.getDocument().addDocumentListener(new DocumentListener() {
				int n;
				public void changedUpdate(DocumentEvent e) {
					if (nTextField.getText().length() == 0) {
						indicatorsErrorLabel.setText("");
						indicatorsErrorLabel.revalidate();
						return;
					  }
				try {
					n = Integer.valueOf(nTextField.getText());
					indicatorsErrorLabel.setText("");
					indicatorsErrorLabel.revalidate();
					
					graph.repaint();
				} catch(Exception e2) {
					indicatorsErrorLabel.setText("Invalid n");
					indicatorsErrorLabel.revalidate();
				}
			  }
			  public void removeUpdate(DocumentEvent e) {
				  if (nTextField.getText().length() == 0) {
						indicatorsErrorLabel.setText("");
						indicatorsErrorLabel.revalidate();
						return;
					  }
				  try {
						n = Integer.valueOf(nTextField.getText());
						indicatorsErrorLabel.setText("");
						indicatorsErrorLabel.revalidate();
						
						graph.repaint();
					} catch(Exception e0) {
						indicatorsErrorLabel.setText("Invalid n");
						indicatorsErrorLabel.revalidate();
					}
			  }
			  public void insertUpdate(DocumentEvent e) {
				  if (nTextField.getText().length() == 0) {
						indicatorsErrorLabel.setText("");
						indicatorsErrorLabel.revalidate();
						return;
					  }
				  try {
						n = Integer.valueOf(nTextField.getText());
						indicatorsErrorLabel.setText("");
						indicatorsErrorLabel.revalidate();
						
						graph.repaint();
					} catch(Exception e1) {
						indicatorsErrorLabel.setText("Invalid n");
						indicatorsErrorLabel.revalidate();
					}
			  }
		});
		p8.add(nTextField);
		p8.add(new JLabel("MA:"));
		
		
		showMA.addActionListener((ae) -> {
			graph.repaint();
		});
		
		p8.add(showMA);
		p8.add(new JLabel("EMA:"));
		
		showEMA.addActionListener((ae) -> {
			graph.repaint();
		});
		
		p8.add(showEMA);
		
		
		eastPanel.add(p8);
		
		JPanel p9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p9.add(indicatorsErrorLabel);
		
		eastPanel.add(p9);
		
		JPanel p10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JLabel oLabel = new JLabel("Open:");
		oLabel.setForeground(Color.decode("#8e9198"));
		oLabel.setFont(new Font(null, Font.BOLD, 14));
		p10.add(oLabel);
		openLabel.setFont(new Font(null, Font.BOLD, 14));
		p10.add(openLabel);
		
		eastPanel.add(p10);
		
		JPanel p11 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel hLabel = new JLabel("High:");
		hLabel.setForeground(Color.decode("#8e9198"));
		hLabel.setFont(new Font(null, Font.BOLD, 14));
		p11.add(hLabel);
		highLabel.setFont(new Font(null, Font.BOLD, 14));
		p11.add(highLabel);
		
		eastPanel.add(p11);
		
		JPanel p12 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lLabel = new JLabel("Low:");
		lLabel.setForeground(Color.decode("#8e9198"));
		lLabel.setFont(new Font(null, Font.BOLD, 14));
		p12.add(lLabel);
		lowLabel.setFont(new Font(null, Font.BOLD, 14));
		p12.add(lowLabel);
		
		eastPanel.add(p12);
		
		JPanel p13 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel cLabel = new JLabel("Close:");
		cLabel.setForeground(Color.decode("#8e9198"));
		cLabel.setFont(new Font(null, Font.BOLD, 14));
		p13.add(cLabel);
		closeLabel.setFont(new Font(null, Font.BOLD, 14));
		p13.add(closeLabel);
		
		eastPanel.add(p13);
		
		JPanel p14 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel tsLabel = new JLabel("Timestamp:");
		tsLabel.setForeground(Color.decode("#8e9198"));
		tsLabel.setFont(new Font(null, Font.BOLD, 14));
		p14.add(tsLabel);
		timestampLabel.setFont(new Font(null, Font.BOLD, 14));
		p14.add(timestampLabel);
		
		eastPanel.add(p14);
		
		add(eastPanel, BorderLayout.EAST);
		
	}
	
	private void processUserInput() {
		String actionN = actionName.getText();
		String startT = startTimestamp.getText();
		String endT = endTimestamp.getText();
		
		if (actionN.length() == 0) {
			errorLabel.setText("Enter action name");
			errorLabel.revalidate();
			return;
		}
		if (startT.length() == 0) {
			errorLabel.setText("Enter start timestamp");
			errorLabel.revalidate();
			return;
		}
		if (endT.length() == 0) {
			errorLabel.setText("Enter end timestamp");
			errorLabel.revalidate();
			return;
		}
		
		for (int i = 0; i < actionN.length(); i++) {
		   if (!Character.isLetter(actionN.charAt(i))) {
			   errorLabel.setText("Invalid action name");
			   errorLabel.revalidate();
			   return;
		   }
		}
		
	    int startTS;
		try {
			startTS = Integer.valueOf(startT);
			if (startTS < 0) throw new Exception();
		} catch (Exception e) {
			errorLabel.setText("Invalid start timestamp");
			errorLabel.revalidate();
			return;
		}
		
		int endTS;
		try {
			endTS = Integer.valueOf(endT);
			if (endTS < 0) throw new Exception();
		} catch (Exception e) {
			errorLabel.setText("Invalid end timestamp");
			errorLabel.revalidate();
			return;
		}
		
		System.loadLibrary("Crawler");
		Crawler crawler = new Crawler();
		crawler.getJSON(actionN, startTS, endTS, "data.JSON");
		
		ArrayList<Integer> timestamps = new ArrayList<>();
		JSONParser jsonParser = new JSONParser();

		try {
			FileReader fr = new FileReader(new File("data.JSON"));
			BufferedReader br = new BufferedReader(fr);
			
			String l = br.readLine();
			
			if(!jsonParser.getTimestamps(l, timestamps)) {
				errorLabel.setText("Unable to get data");
				errorLabel.revalidate();
				return;
			}
			ArrayList<Double> high = jsonParser.getIdentifier(l, 'h');
			ArrayList<Double> low = jsonParser.getIdentifier(l, 'l');
			ArrayList<Double> open = jsonParser.getIdentifier(l, 'o');
			ArrayList<Double> close = jsonParser.getIdentifier(l, 'c');
			
			// Create Sock 
			stock = new Stock(actionN);

			// Create Candles and add them to Stock
			Iterator<Integer> itT = timestamps.iterator();
			Iterator<Double> itH = high.iterator();
			Iterator<Double> itL = low.iterator();
			Iterator<Double> itO = open.iterator();
			Iterator<Double> itC = close.iterator();

			while (itT.hasNext() && itH.hasNext() && itL.hasNext() && itO.hasNext() && itC.hasNext()) {
				Candle candle = new Candle(itT.next(), itH.next(), itL.next(), itO.next(), itC.next());
				stock.addCandle(candle);
			}
			
			br.close();
			
		} catch (IOException e) {}		
		
		errorLabel.setText("");
		errorLabel.revalidate();
	}
	
	public void updateIndicators() {
		
	}
	
	public Stock getStock() {
		return stock;
	}
	
	public JCheckBox getShowMA() {
		return showMA;
	}
	
	public JCheckBox getShowEMA() {
		return showEMA;
	}
	
	public JTextField getNTextField() {
		return nTextField;
	}
	
	public JLabel getIndicatorsErrorLabel() {
		return indicatorsErrorLabel;
	}
}
