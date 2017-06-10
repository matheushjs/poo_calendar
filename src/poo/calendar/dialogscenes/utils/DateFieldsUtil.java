package poo.calendar.dialogscenes.utils;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateFieldsUtil {
	
	/**
	 * Creates a calendar from 2 strings 'date' and 'hour'.
	 * 'date' will be something like 12/4/2002
	 * 'hour' like 6:12
	 * 
	 * @param date string of the form "day/month/year"
	 * @param hour string of the form "hour:minute"
	 * @return Calendar created from information contained in the strings
	 * @throws IllegalArgumentException if anything goes wrong
	 */
	public static Calendar parseFields(String date, String hour) throws IllegalArgumentException {
		Calendar calendar = Calendar.getInstance();
		boolean leniency = calendar.isLenient();
		calendar.setLenient(false);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		try {
			Matcher dateMatcher = Pattern.compile("^\\s*(\\d{1,2})\\s*/\\s*(\\d{1,2})\\s*/\\s*(\\d{1,4})\\s*$").matcher(date);
			Matcher hourMatcher = Pattern.compile("^\\s*(\\d{1,2})\\s*:\\s*(\\d{1,2})\\s*$").matcher(hour);
			
			if(dateMatcher.matches() && dateMatcher.groupCount() == 3){
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateMatcher.group(1)));
				calendar.set(Calendar.MONTH, Integer.parseInt(dateMatcher.group(2)));
				
				String year = dateMatcher.group(3);
				int n = Integer.parseInt(year);
				if(year.length() <= 2)
					n+= n > 50 ? 1900 : 2000;
				calendar.set(Calendar.YEAR, n);
			} else throw new Exception();
			
			if(hourMatcher.matches() && hourMatcher.groupCount() == 2){
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourMatcher.group(1)));
				calendar.set(Calendar.MINUTE, Integer.parseInt(hourMatcher.group(2)));
			} else throw new Exception();
			
			calendar.setLenient(leniency);
		} catch(Exception e) {
			throw new IllegalArgumentException();
		}
		
		return calendar;
	}
	
	/**
	 * Retrieves a day/month/year string from the given calendar.
	 * @param calendar
	 * @return "day/month/year" string
	 */
	public static String dateString(Calendar calendar){
		return String.format("%02d/%02d/%04d",
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.MONTH)+1,
				calendar.get(Calendar.YEAR));
	}
	
	/**
	 * Retrieves a hour:minute string from the given calendar
	 * @param calendar
	 * @return "hour:minute" string
	 */
	public static String hourString(Calendar calendar){
		return String.format("%02d:%02d", 
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE));
	}
}
