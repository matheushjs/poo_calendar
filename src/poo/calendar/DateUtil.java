package poo.calendar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
	public static int MINUTES_IN_DAY = 24 * 60;

	// Maps each integer such as Calendar.JUNE into a representative String.
	private static Map<Integer, String> sMonthMapping = null;
	
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
				calendar.set(Calendar.MONTH, Integer.parseInt(dateMatcher.group(2))-1 ); //January is 0, so we sum 1.
				
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
	
	/**
	 * Retrieves a hour:minute string from the given amount of minutes elapsed in the day.
	 * @param minutes
	 * @return
	 */
	public static String hourString(int minutes){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, minutes / 60);
		c.set(Calendar.MINUTE, minutes % 60);
		return hourString(c);
	}
	
	/**
	 * Sets fields lower than DATE of 'calendar' to 0. 
	 * @param calendar
	 */
	public static void resetToDate(Calendar calendar){
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	
	
	/**
	 * Sets fields lower than DAY_OF_WEEK to 0.
	 * @param calendar
	 */
	public static void resetToWeek(Calendar calendar){
		DateUtil.resetToDate(calendar);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
	}
	
	/**
	 * Given an interval between 2 dates, translate the interval so that the
	 * 'field' of the initial date  is the same as the 'field' of the given 'target' date.
	 * All fields higher than 'field' are translated also.
	 * 
	 * Supported values for 'field' are Calendar's YEAR, MONTH, DATE and DAY_OF_WEEK.
	 * 
	 * The given 'init' and 'end' calendars are effectively altered, so cloning might be necessary.
	 * 
	 * @param target Base date
	 * @param init Initial date of the interval to be translated
	 * @param end End date of the interval to be translated
	 * @throws IllegalArgumentException if translation related to the given 'field' is not supported.
	 */
	public static void translateInterval(int field, Calendar target, Calendar init, Calendar end) throws IllegalArgumentException {
		if(field != Calendar.YEAR && field != Calendar.MONTH && field != Calendar.DATE && field != Calendar.DAY_OF_WEEK)
			throw new IllegalArgumentException("Unsupported calendar translation field");
		
		boolean translateWeekday;
		int savedWeekDay = 0;

		if(field == Calendar.DAY_OF_WEEK){	
			translateWeekday = true;
			savedWeekDay = init.get(Calendar.DAY_OF_WEEK);
			field = Calendar.DATE;
		} else {
			translateWeekday = false;
		}
		
		// Save states
		boolean initLeniency, endLeniency;
		initLeniency = init.isLenient();
		endLeniency = end.isLenient();
		
		// Change states
		init.setLenient(true);
		end.setLenient(true);
		
		int[] fields = new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DATE };
		int[] deltas = new int[3];
		
		// Calculate needed offsets
		for(int i = 0; i < 3; i++)
			deltas[i] = end.get(fields[i]) - init.get(fields[i]);
		
		// Translate initial date
		for(int i = 0; i < 3; i++){
			init.set(fields[i], target.get(fields[i]));
			
			if(fields[i] == field) break;
		}
		
		// Translate end date by applying offsets in the new init date.
		// Inverted order to prevent wrong rounding in the day number.
		for(int i = 2; i >= 0; i--){
			end.set(fields[i], init.get(fields[i]));
			end.add(fields[i], deltas[i]);
		}
		
		// Translate to DAY_OF_WEEK if needed
		int previous, delta;
		if(translateWeekday){
			// Set initial date to the correct DAY_OF_WEEK
			previous = init.get(Calendar.DATE);
			init.set(Calendar.DAY_OF_WEEK, savedWeekDay);
			
			// Calculate the variation
			delta = init.get(Calendar.DATE) - previous;
			
			// Apply variation to the end date.
			end.add(Calendar.DATE, delta);
		}
		
		// Restore states
		init.setLenient(initLeniency);
		end.setLenient(endLeniency);
	}
	
	/**
	 * Verifies if the interval [init, end] has intersection with the interval [lowerBound, upperBound].
	 * Intersection is checked to all fields higher than or equal to DATE. This means HOUR and MINUTE for
	 * example are ignored.
	 * 
	 * @param lowerBound
	 * @param upperBound
	 * @param init
	 * @param end
	 * @return TRUE if intersection exists.
	 */
	public static boolean hasDayIntersection(Calendar lowerBound, Calendar upperBound, Calendar init, Calendar end){
		int[] fields = new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DATE };
		
		for(int i = 0; i < 3; i++){
			if( init.get(fields[i]) > upperBound.get(fields[i]) )
				return false;
			if( lowerBound.get(fields[i]) > end.get(fields[i]) )
				return false;
		}
		
		return true;
	}
	
	/**
	 * Verifies if the interval [init, end] contains 'subjectDay', in the same fashion as the other
	 * hasDayIntersection() method.
	 * 
	 * Remember that HOUR, MINUTE and any lower fields are ignored.
	 * 
	 * @param subjectDay
	 * @param init
	 * @param end
	 * @return if 'subjectDay' is within the interval [init, end]
	 */
	public static boolean hasDayIntersection(Calendar subjectDay, Calendar init, Calendar end){
		return hasDayIntersection(subjectDay, subjectDay, init, end);
	}
	
	/**
	 * @return 60 * HOUR + MINUTES   of the given calendar.
	 */
	public static int minuteCount(Calendar calendar){
		return 60*calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE);
	}
	
	/**
	 * Verifies if 'maybeOffset' has the same date as today.add(DATE, offset)
	 * 
	 * @param today
	 * @param maybeOffset
	 * @param offset
	 * @return
	 */
	public static boolean isDayOffset(Calendar today, Calendar maybeOffset, int offset){
		int[] fields = new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DATE };
		
		// Clone so as to not change the user's calendar.
		Calendar key = (Calendar) today.clone();
		key.add(Calendar.DATE, offset);
		
		for(int i = 0; i < 2; i++)
			if(key.get(fields[i]) != maybeOffset.get(fields[i]))
				return false;
		
		return true;
	}
	
	/**
	 * Given an integer such as Calendar.JUNE, returns a string representative of the month.
	 * @param month
	 * @return
	 */
	public static String monthString(int month){
		if(sMonthMapping == null){
			sMonthMapping = new HashMap<>(12);
			sMonthMapping.put(Calendar.JANUARY, "January");
			sMonthMapping.put(Calendar.FEBRUARY, "February");
			sMonthMapping.put(Calendar.MARCH, "March");
			sMonthMapping.put(Calendar.APRIL, "April");
			sMonthMapping.put(Calendar.MAY, "May");
			sMonthMapping.put(Calendar.JUNE, "June");
			sMonthMapping.put(Calendar.JULY, "July");
			sMonthMapping.put(Calendar.AUGUST, "August");
			sMonthMapping.put(Calendar.SEPTEMBER, "September");
			sMonthMapping.put(Calendar.OCTOBER, "October");
			sMonthMapping.put(Calendar.NOVEMBER, "November");
			sMonthMapping.put(Calendar.DECEMBER, "December");
		}
		
		return sMonthMapping.get(month);
	}
	
	/*
	public static void main(String[] args){
		System.out.println(monthString(Calendar.JANUARY));
	}*/
}
