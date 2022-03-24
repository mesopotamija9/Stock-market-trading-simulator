package stock_market;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class Portfolio extends JFrame {
	private User user;
	private App app;
	private JPanel centerPanel = new JPanel();
	private JLabel balanceLabel;
	private JTable transactionsTable;
	private JTable stocksTable;
	private JTextField actionId = new JTextField(8);
	private JTextField actionAmount = new JTextField(8);
	private JTextField buyActionName = new JTextField(8);
	private JTextField buyActionAmount = new JTextField(8);
	private JLabel errorPortfolio = new JLabel("");
	private JLabel buyErrorPortfolio = new JLabel("");
	private JButton buyButton = new JButton("Buy");
	private JButton sellButton = new JButton("Sell");
	
	public Portfolio(User user, App app) {
		this.user = user;
		this.app = app;
		
		setTitle("Portfolio - " + user.getUsername());
		setBounds(300, 150, 500, 500);
		setLayout(new BorderLayout());
		
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		
		populateNorth();
		populateStocksTable();
		populateTransactionsTable();
		populateEast();
		
		add(centerPanel, BorderLayout.CENTER);
		pack();
		
		addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e) {
				 app.portfolio = null;
			 }
		});
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private void populateNorth() {
		JPanel northPanel = new JPanel(new GridLayout(2, 1));
		
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.add(new JLabel("Username: " + user.getUsername()));
		
		northPanel.add(p1);
		
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		balanceLabel = new JLabel("Balance: " + new Formatter().format("%.2f", user.getBalance()) + "$");
		p2.add(balanceLabel);
		
		northPanel.add(p2);
		
		add(northPanel, BorderLayout.NORTH);
	}
	
	private void updateStocksTable() {
		String[] columnNames = {"Name", "Amount"};
		
		HashMap<String, Integer> actionVolume = user.getActionVolume();
		
		Object[][] data = new Object[actionVolume.size()][2];
		int i = 0;
		for (Map.Entry<String, Integer> entry : user.getActionVolume().entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		    Object[] arr = {key, value};
		    data[i++] = arr;
		}
		
		stocksTable.setModel(new DefaultTableModel(data, columnNames));
		stocksTable.repaint();
	}
	
	private void populateStocksTable() {
		String[] columnNames = {"Name", "Amount"};
	
		HashMap<String, Integer> actionVolume = user.getActionVolume();
		
		Object[][] data = new Object[actionVolume.size()][2];
		int i = 0;
		for (Map.Entry<String, Integer> entry : user.getActionVolume().entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		    Object[] arr = {key, value};
		    data[i++] = arr;
		}
		
		stocksTable = new JTable(data, columnNames) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
	            return false;
	         }
		};
		
		stocksTable.setRowSelectionAllowed(false);
		stocksTable.getTableHeader().setReorderingAllowed(false);
	
		centerPanel.add(stocksTable.getTableHeader());
		centerPanel.add(stocksTable);
	}
	
	private void updateTransactionsTable() {
		String[] columnNames = {"ID", "Name", "Amount", "Buy Price", "Live Price", "Price Diff", "Price Diff(%)"};

		ArrayList<Transaction> transactions = user.getTransactions();
		
		Object[][] data = new Object[transactions.size()][7];
		int i = 0;
		for (Transaction t : transactions) {
			int id = t.getId();
			String name = t.getSymbol();
			int amount = t.getVolume();
			double buyPrice = t.getBuyPrice();
			
			
			double livePrice = t.getLivePrice();
			double priceDiff = livePrice - buyPrice;
			double priceDiffPercentage =  (priceDiff / buyPrice) * 100;
			
		    Object[] arr = {id, name, amount, buyPrice, livePrice, priceDiff, priceDiffPercentage};
		    data[i++] = arr;
		}
		
		transactionsTable.setModel(new DefaultTableModel(data, columnNames));
		transactionsTable.repaint();
	}
	
	private void populateTransactionsTable() {
		String[] columnNames = {"ID", "Name", "Amount", "Buy Price", "Live Price", "Price Diff", "Price Diff(%)"};

		ArrayList<Transaction> transactions = user.getTransactions();
		
		Object[][] data = new Object[transactions.size()][7];
		int i = 0;
		for (Transaction t : transactions) {
			int id = t.getId();
			String name = t.getSymbol();
			int amount = t.getVolume();
			double buyPrice = t.getBuyPrice();
			
			
			double livePrice = t.getLivePrice();
			double priceDiff = livePrice - buyPrice;
			double priceDiffPercentage =  (priceDiff / buyPrice) * 100;
			
		    Object[] arr = {id, name, amount, buyPrice, livePrice, priceDiff, priceDiffPercentage};
		    data[i++] = arr;
		}
		
		transactionsTable = new JTable(data, columnNames) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
	            return false;
	         }
		};
		
		transactionsTable.setRowSelectionAllowed(false);
		transactionsTable.getTableHeader().setReorderingAllowed(false);
		
		centerPanel.add(transactionsTable.getTableHeader());
		centerPanel.add(transactionsTable);
		
		
		
		 for (int x = 0; x < 4; x++) {
			//Object val = transactionsTable.getModel().get;
//			val = (Double)val;
//			System.out.println(val);
		}
		
	}
	
	private void populateEast() {
		JPanel eastPanel = new JPanel(new GridLayout(9, 1));
		
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.add(new JLabel("ID:"));
		p1.add(actionId);
		
		eastPanel.add(p1);
		
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p2.add(new JLabel("Amount:"));
		p2.add(actionAmount);
		
		eastPanel.add(p2);
		
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p3.add(errorPortfolio);
		
		eastPanel.add(p3);
		
		JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		sellButton.addActionListener((ae) ->{
			String idStr = actionId.getText();
			String amountStr = actionAmount.getText();
			
			if (idStr.length() == 0) {
				errorPortfolio.setText("Enter id");
				errorPortfolio.revalidate();
				return;
			}
			if (amountStr.length() == 0) {
				errorPortfolio.setText("Enter amount");
				errorPortfolio.revalidate();
				return;
			}
			
			int id;
			try {
				id = Integer.valueOf(idStr);
			} catch (Exception e) {
				errorPortfolio.setText("Invalid id");
				errorPortfolio.revalidate();
				return;
			}
			
			int amount;
			try {
				amount = Integer.valueOf(amountStr);
			} catch (Exception e) {
				errorPortfolio.setText("Invalid amount");
				errorPortfolio.revalidate();
				return;
			}
			
			if (amount <= 0) {
				errorPortfolio.setText("You can sell 1 or more actions");
				errorPortfolio.revalidate();
				return;
			}
			
			ArrayList<Transaction> transactions = user.getTransactions();
			
			Boolean idExists = false;
			Transaction transaction = null;
			for (Transaction t: transactions) {
				if (t.getId() == id) {
					idExists = true;
					transaction = t;
					break;
				}
			}
			
			if (!idExists) {
				errorPortfolio.setText("Id doesn't exist");
				errorPortfolio.revalidate();
				return;
			}
			
			if (transaction.getVolume() < amount) {
				errorPortfolio.setText("There is no enough actions");
				errorPortfolio.revalidate();
				return;
			}
			
			// There is a transaction with given id and there is enough volume
			// Update volume
			transaction.updateVolume(transaction.getVolume() - amount);

			// Update balance
			double price = transaction.getLivePrice();
			user.updateBalance(user.getBalance() + amount * price);

			// Update info about all stocks user has
			user.getActionVolume().put(transaction.getSymbol(), user.getActionVolume().get(transaction.getSymbol()) - amount);	
			
			// Remove stock from all stocks list if there is no more stocks
			if (user.getActionVolume().get(transaction.getSymbol()) == 0)
				user.getActionVolume().remove(transaction.getSymbol());
			
			// Remove transaction from transactions vector if there is no more actions
			if (transaction.getVolume() == 0)
				user.getTransactions().remove(transaction);
			
			user.saveToFile();
			
			balanceLabel.setText("Balance: " + new Formatter().format("%.2f", user.getBalance()) + "$");
			balanceLabel.revalidate();	
			
			app.balanceLabel.setText("   " + new Formatter().format("%.2f", user.getBalance()) + "$");
			updateTransactionsTable();
			updateStocksTable();
			
			actionId.setText("");
			actionId.revalidate();
			actionAmount.setText("");
			actionAmount.revalidate();
			errorPortfolio.setText("");
			errorPortfolio.revalidate();
		});
		
		p4.add(sellButton);
		
		eastPanel.add(p4);
		
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p5.add(new JLabel("Action name:"));
		p5.add(buyActionName);
		
		eastPanel.add(p5);
		
		JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p6.add(new JLabel("Amount:"));
		p6.add(buyActionAmount);
		
		eastPanel.add(p6);
		
		JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		p7.add(buyErrorPortfolio);
		
		eastPanel.add(p7);
		
		JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		buyButton.addActionListener((ae) -> {
			String symbol = buyActionName.getText();
			String amountStr = buyActionAmount.getText();
			
			if (symbol.length() == 0) {
				buyErrorPortfolio.setText("Enter action name");
				buyErrorPortfolio.revalidate();
				return;
			}
			if (amountStr.length() == 0) {
				buyErrorPortfolio.setText("Enter amount");
				buyErrorPortfolio.revalidate();
				return;
			}
			
			for (int i = 0; i < symbol.length(); i++) {
			   if (!Character.isLetter(symbol.charAt(i))) {
				   buyErrorPortfolio.setText("Invalid action name");
				   buyErrorPortfolio.revalidate();
				   return;
			   }
			}
			
			int amount;
			try {
				amount = Integer.valueOf(amountStr);
			} catch(Exception e) {
				buyErrorPortfolio.setText("Invalid amount");
			    buyErrorPortfolio.revalidate();
			    return;
			}
			
			if (amount <= 0) {
				buyErrorPortfolio.setText("You can buy 1 or more actions");
			    buyErrorPortfolio.revalidate();
			    return;
			}
			
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			int timeNow = Integer.valueOf(String.valueOf(timestamp.getTime()).substring(0, 10));


			// Last close price is current price. Set starting date 3d ago to prevent
			// getting empty JSON if current time is at weekend
			int time3dAgo = timeNow - 3600 * 72;

			System.loadLibrary("Crawler");
			Crawler crawler = new Crawler();
			JSONParser jsonParser = new JSONParser();
			crawler.getJSON(symbol, time3dAgo, timeNow, "data.JSON");
			
			String jsonString = null;
			
			try {
				FileReader fr = new FileReader(new File("data.JSON"));
				BufferedReader br = new BufferedReader(fr);
				
				jsonString = br.readLine();
				ArrayList<Integer> timestamps = new ArrayList<>();
				if (!jsonParser.getTimestamps(jsonString, timestamps)) {
					buyErrorPortfolio.setText("Symbol name: " + symbol + " doesn't exist");
				    buyErrorPortfolio.revalidate();
				    return;
				}
				
				br.close();
			} catch (IOException e) {}
			
			// Create transaction
			// Get the next ID form a file
			int id = 0;
			int nextId = 0;
			String line;
			
			try {
				FileReader fr = new FileReader(new File("id.txt"));
				BufferedReader br = new BufferedReader(fr);

				line = br.readLine();
				id = Integer.valueOf(line);
				
				br.close();
				
				nextId = id + 1;
			} catch (IOException e) {
				System.out.println("Unable to read from a file");
			}

			ArrayList<Double> closeArr = jsonParser.getIdentifier(jsonString, 'c');
			
			double livePrice = closeArr.get(closeArr.size() - 1);
			if (amount * livePrice > user.getBalance()) {
				// No enough money
				buyErrorPortfolio.setText("You don't have enough money");
			    buyErrorPortfolio.revalidate();
			    return;
			}
			
			// Enough money --> make transaction and add it to users transaction list
			Transaction transaction = new Transaction(id, symbol, amount, livePrice);
			user.getTransactions().add(transaction);
			
			// Remove money from balance
			user.updateBalance(user.getBalance() - amount * livePrice);

			// Add actions to all stocks list
			if (user.getActionVolume().get(symbol) == null) {
				// Not found
				user.getActionVolume().put(symbol, amount);
			} else {
				// Found
				user.getActionVolume().put(symbol, user.getActionVolume().get(symbol) + amount);
			}
			
			try {
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("id.txt", false));
				bufferedWriter.write(String.valueOf(nextId));
				bufferedWriter.close();
			} catch(Exception e) {}
			
			user.saveToFile();
			
			updateStocksTable();
			updateTransactionsTable();
			
			balanceLabel.setText("Balance: " + new Formatter().format("%.2f", user.getBalance()) + "$");
			balanceLabel.revalidate();
			app.balanceLabel.setText("   " + new Formatter().format("%.2f", user.getBalance()) + "$");
			app.balanceLabel.revalidate();
			
			buyActionName.setText("");
			buyActionName.revalidate();
			buyActionAmount.setText("");
			buyActionAmount.revalidate();
			buyErrorPortfolio.setText("");
			buyErrorPortfolio.revalidate();
		});
		
		p8.add(buyButton);
		
		eastPanel.add(p8);
		
		add(eastPanel, BorderLayout.EAST);
	}
}
