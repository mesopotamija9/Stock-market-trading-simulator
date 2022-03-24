package stock_market;

public class Crawler {
	
	public native void getJSON(String symbol, int startDate, int endDate, String filename);

}
