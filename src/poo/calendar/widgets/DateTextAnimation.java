package poo.calendar.widgets;

import java.util.Calendar;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

/**
 * Class for animating the DateText widget.
 */
public class DateTextAnimation extends AnimationTimer{
	
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
