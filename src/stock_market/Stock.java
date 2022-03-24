package stock_market;

import java.util.ArrayList;

public class Stock {
	private String name;
	private ArrayList<Candle> candles = new ArrayList<>();
	
	public Stock(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addCandle(Candle candle) {
		candles.add(candle);
	}
	
	public int getSize() {
		return candles.size();
	}
	
	public double getMA(int n, int i) {
		double sum = 0;
		
		int count = 0;
		while (n-- > 0) {
			sum += candles.get(i--).getClose();
			count++;
			if (i < 0) {
				break;
			}
		}
		
		return sum / count;
	}
	
	
	public double getEMA(int n, int i) {
		if (i < n)
			return getMA(n, i);
		return candles.get(i).getClose() * (2.0/(n+1)) + getEMA(n, i - 1) * (1 - (2.0/(n+1)));
	}
	
	@Override
	public String toString() {
		for (Candle c: candles)
			System.out.println(c);
		
		return "";
	}
	
	public ArrayList<Candle> getCandles(){
		return candles;
	}
	
	public double getMinLow() {
		if (candles.isEmpty()) return 0.0;
		double minimumLow = Double.MAX_VALUE;
		for (Candle c: candles) {
			if (c.getLow() < minimumLow)
				minimumLow = c.getLow();
		}
		
		return minimumLow;
	}
	
	public double getMaxHigh() {
		if (candles.isEmpty()) return 0.0;
		double maximumHigh = 0;
		for (Candle c: candles) {
			if (c.getHigh() > maximumHigh)
				maximumHigh = c.getHigh();
		}
		
		return maximumHigh;
	}
}
