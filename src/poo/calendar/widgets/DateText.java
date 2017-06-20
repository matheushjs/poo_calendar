package poo.calendar.widgets;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
	
	/**
	 * Class exclusively for animating the DateText widget.
	 * It's currently exclusive for the DateText class, hence why it's nested within it and declared private.
	 */
	private class DateTextAnimation extends AnimationTimer {
		
		private DateText date;
		
		/**
		 * Default constructor
		 * @param dateText the text to animate
		 */
		public DateTextAnimation(DateText dateText){
			date = dateText;
			this.start();
		}

		@Override
		public void handle(long now) {
			FadeTransition ft = new FadeTransition(Duration.millis(1000), date);		
			if(fadeOut()){
				ft.setFromValue(1.0);
				ft.setToValue(0.0);
				ft.setCycleCount(1);
				ft.playFromStart();
			}else if(fadeIn()){
				date.update();
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.setCycleCount(1);
				ft.playFromStart();
			}
			
		}
		
		private boolean fadeOut(){
			return checkMidnight();
		}
		
		private boolean fadeIn(){
			Calendar cal = Calendar.getInstance();
			if(cal.get(Calendar.HOUR) != 0) return false;
			if(cal.get(Calendar.MINUTE) != 0) return false;
			if(cal.get(Calendar.SECOND) != 2) return false;
			return true;
		}
		
		private boolean checkMidnight(){
			Calendar cal = Calendar.getInstance();
			if(cal.get(Calendar.HOUR) != 0) return false;
			if(cal.get(Calendar.MINUTE) != 0) return false;
			if(cal.get(Calendar.SECOND) != 0) return false;
			return true;
		}
	}
}
