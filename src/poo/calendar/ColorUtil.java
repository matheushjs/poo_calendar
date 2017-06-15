package poo.calendar;

import javafx.scene.paint.Color;

public class ColorUtil {
	
	//Algorithm source (adapted): https://stackoverflow.com/questions/1855884/determine-font-color-based-on-background-color
	//User: 'Gacek'
	public static Color contrastColor(Color color) {
		double r = color.getRed();
		double g = color.getGreen();
		double b = color.getBlue();
		int d = 0;
	    
	    double a = 1 - (0.299 * r + 0.587 * g + 0.114 * b);

	    if (a < 0.5)
	       d = 0; // bright colors - black font
	    else
	       d = 255; // dark colors - white font

	    return Color.rgb(d, d, d);
	}
}
