package poo.calendar.widgets;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Text widget that displays a date.
 * The widget is animated, so it updates when the date changes.
 */
public class DateText extends Text {
	DateTextAnimation mAnimation;
	
	/**
	 * Default constructor.
	 * @param size font size
	 */
	public DateText(int size) {
		setText(makeString());
		setFont(Font.font("Liberation Sans", FontWeight.BOLD, size));
		setCache(true);
		this.setFontSmoothingType(FontSmoothingType.LCD);
		
		mAnimation = new DateTextAnimation(this); //Animate this widget
	}
	
	private String makeString(){
		String[] weekDays = DateFormatSymbols.getInstance(Locale.US).getShortWeekdays();
		String[] months = DateFormatSymbols.getInstance(Locale.US).getMonths();
		
		Calendar cal = Calendar.getInstance();
		String date = weekDays[cal.get(Calendar.DAY_OF_WEEK)] + ", " + months[cal.get(Calendar.MONTH)] + " " + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

		return date;
	}
	
	/**
	 * Updates the widget with current date.
	 */
	public void update(){
		setText(makeString());
	}
}
