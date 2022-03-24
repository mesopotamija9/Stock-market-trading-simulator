package stock_market;

public class Candle {
	private int timestamp;
	private double high;
	private double low;
	private double open;
	private double close;
	
	public Candle(int timestamp, double high, double low, double open, double close) {
		this.timestamp = timestamp;
		this.high = high;
		this.low = low;
		this.open = open;
		this.close = close;
	}
	
	public int getTimestamp() {
		return timestamp;
	}
	
	public double getHigh() {
		return high;
	}
	
	public double getLow() {
		return low;
	}
	
	public double getOpen() {
		return open;
	}
	
	public double getClose() {
		return close;
	}
	
	@Override
	public String toString() {
		return timestamp + "- H: " + high + " L: " + low + " O: " + open + " C: " + close;
	}
}
