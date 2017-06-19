package poo.calendar.widgets;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DateText extends Text {
	public DateText(int size) {
		setText(makeString());
		setFont(Font.font("Liberation Sans", FontWeight.BOLD, FontPosture.ITALIC, size));
		setStyle("-fx-fill: linear-gradient(from 0% 0% to 100% 500%, repeat, black 0%, darkcyan 50%);");
		setCache(true);
		this.setFontSmoothingType(FontSmoothingType.LCD);

		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.6f, 0.6f, 0.6f));
		setEffect(ds);
	}
	
	private String makeString(){
		String[] weekDays = DateFormatSymbols.getInstance(Locale.US).getShortWeekdays();
		String[] months = DateFormatSymbols.getInstance(Locale.US).getMonths();
		
		Calendar cal = Calendar.getInstance();
		String date = weekDays[cal.get(Calendar.DAY_OF_WEEK)] + ", " + months[cal.get(Calendar.MONTH)] + " " + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

		return date;
	}
	
	public void update(){
		setText(makeString());
	}
}
