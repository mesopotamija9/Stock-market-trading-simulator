package stock_market;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
	private String username;
	private String password;
	private HashMap<String, Integer> actionVolume = new HashMap<>();
	private ArrayList<Transaction> transactions = new ArrayList<>();
	double balance;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		
		// Read user info from user_data\<username>.txt
		String filePath = "user_data\\" + username + ".txt";
		try {
			FileReader fr = new FileReader(new File(filePath));
			BufferedReader br = new BufferedReader(fr);
			
			balance = Double.valueOf(br.readLine());
			br.readLine(); // Read first $
			
			Pattern pattern = Pattern.compile("(.*):(.*)");
			Matcher matcher;
			
			while(true) {
				String line = br.readLine();
				if (line.equals("$")) break;
				matcher = pattern.matcher(line);
				
				if (matcher.matches())
					actionVolume.put(matcher.group(1), Integer.valueOf(matcher.group(2)));
			}
			
			pattern = Pattern.compile("(.*):(.*):(.*):(.*)");
			
			br.readLine(); // Read first #
			
			while(true) {
				String line = br.readLine();
				if (line.equals("#")) break;
				
				matcher = pattern.matcher(line);
				
				if (matcher.matches()) {
					Transaction transaction = new Transaction(Integer.valueOf(matcher.group(1)), matcher.group(2), Integer.valueOf(matcher.group(3)), Double.valueOf(matcher.group(4)));
					transactions.add(transaction);
				}
			}
			
			br.close();
		} catch (IOException e) {
			System.out.println("Unable to read form a file " + filePath);
		}
	}
	
	public void saveToFile() {
		String filePath = "user_data\\" + username + ".txt";
		
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, false));

			bufferedWriter.write(balance + "\n$\n");
			for (Map.Entry<String, Integer> entry : actionVolume.entrySet()) {
			    String key = entry.getKey();
			    Integer value = entry.getValue();
			    
			    bufferedWriter.write(key + ":" + value + "\n");
			}

			bufferedWriter.write("$\n#\n");
			 
			for (Transaction t : transactions) {
				bufferedWriter.write(t.getId() + ":" + t.getSymbol() + ":" + t.getVolume() + ":" + t.getBuyPrice() + "\n");
			}

			bufferedWriter.write("#");
			
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("Error writing to a file " + filePath);
		}
		
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void updateBalance(double newBalance) {
		balance = newBalance;
	}
	
	public HashMap<String, Integer> getActionVolume(){
		return actionVolume;
	}
	
	public ArrayList<Transaction> getTransactions(){
		return transactions;
	}
}
