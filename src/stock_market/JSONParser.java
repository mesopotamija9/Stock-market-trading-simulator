package stock_market;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONParser {
	public Boolean getTimestamps(String jsonString, ArrayList<Integer> timestamps) {
		Pattern pattern = Pattern.compile("(\"timestamp\":\\[)(.*?)(\\])");
		Matcher matcher = pattern.matcher(jsonString);
		
		String timestampsString;
		String numStr = "";
		int num;
		
		if(matcher.find()) {
			timestampsString = matcher.group(2) + ","; // Added "," in order to cast last num to int in for loop
			for (int i = 0; i < timestampsString.length();  i++) {
				if (timestampsString.charAt(i) != ',') numStr += timestampsString.charAt(i);
				else {
					num = Integer.valueOf(numStr);
					numStr = "";
					timestamps.add(num);
				}
			}
			
			return true;
		} 
		
		return false;
	}

	public ArrayList<Double> getIdentifier(String jsonString, char identifier) {
		identifier = Character.toLowerCase(identifier);
		String identifierChoice;
		switch (identifier) {
		case 'o':
			identifierChoice = "open";
			break;
		case 'c':
			identifierChoice = "close";
			break;
		case 'h':
			identifierChoice = "high";
			break;
		case 'l':
		default:
			identifierChoice = "low";
			break;
		}

		Pattern pattern = Pattern.compile("(\"" + identifierChoice + "\":\\[)(.*?)(\\])");
		Matcher matcher = pattern.matcher(jsonString);

		String identifiersString;
		String numStr = "";
		double num;
		ArrayList<Double> identifiers = new ArrayList<>();
		
		if (matcher.find()) {
			identifiersString = matcher.group(2) + ","; // Added "," in order to cast last num to double in for loop
			for(int i = 0; i < identifiersString.length(); i++) {
				if (identifiersString.charAt(i) != ',') numStr += identifiersString.charAt(i);
				else {
					num = Double.valueOf(numStr);
					identifiers.add(num);
					numStr = "";
				}
			};
			
			return identifiers;
		}
		
		return null;
	}
}
