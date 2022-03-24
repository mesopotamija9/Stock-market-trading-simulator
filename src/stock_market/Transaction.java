package stock_market;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

public class Transaction {
	private int id;
	private String symbol;
	private int volume;
	private double buyPrice;
	
	public Transaction(int id, String symbol, int volume, double buyPrice) {
		this.id = id;
		this.symbol = symbol;
		this.volume = volume;
		this.buyPrice = buyPrice;
	}
	
	public int getId() {
		return id;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public double getBuyPrice() {
		return buyPrice;
	}
	
	public double getLivePrice() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int timeNow = Integer.valueOf(String.valueOf(timestamp.getTime()).substring(0, 10));


		// Last close price is current price. Set starting date 3d ago to prevent
		// getting empty JSON if current time is at weekend
		int time3dAgo = timeNow - 3600 * 72;

		System.loadLibrary("Crawler");
		Crawler crawler = new Crawler();
		JSONParser jsonParser = new JSONParser();
		crawler.getJSON(symbol, time3dAgo, timeNow, "data.JSON");
		
		try {
			FileReader fr = new FileReader(new File("data.JSON"));
			BufferedReader br = new BufferedReader(fr);
			ArrayList<Double> closeArr = jsonParser.getIdentifier(br.readLine(), 'c');
			double livePrice = closeArr.get(closeArr.size()- 1);
			br.close();
			return livePrice;
		} catch (IOException e) {}

		return 0.0;
	}
	
	public void updateVolume(int newVolume) {
		volume = newVolume;
	}
}
