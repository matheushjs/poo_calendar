package poo.calendar.widgets;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Text Widget that represents the application's stylized name.
 */
public class ElfCalendarText extends Text {
	public ElfCalendarText(int size) {
		setText("Elf Calendar");
		setFont(Font.font("Liberation Serif", FontWeight.BOLD, FontPosture.ITALIC, size));
		setStyle("-fx-fill: linear-gradient(from 0% 0% to 100% 500%, repeat, black 0%, darkcyan 50%);");
    	setCache(true);
    	this.setFontSmoothingType(FontSmoothingType.LCD);
    	
    	DropShadow ds = new DropShadow();
    	ds.setOffsetY(3.0f);
    	ds.setColor(Color.color(0.6f, 0.6f, 0.6f));
    	setEffect(ds);
	}
}
