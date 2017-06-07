package poo.calendar.model;

import javafx.scene.paint.Color;

/**
 * Class that represents a group in the calendar.
 * A group contains information that are common among many tasks and appointments,
 * such as their colors in the UI.
 */
public class CalendarGroup {
	// ID for the default group
	public static Integer DEFAULT_ID = 0;
	
	// Array of colors that CalendarGroups can assume
	private static Color[] mColorArray = new Color[] {
		Color.AQUA, Color.CORAL, Color.CORNFLOWERBLUE,
		Color.GOLD, Color.GREENYELLOW, Color.HOTPINK,
		Color.MOCCASIN, Color.WHITESMOKE
	};
	
	//The group's name
	private String mName;
	
	//The group's color
	private Color mColor;
	
	//The group's ID
	private Integer mID;
	
	public CalendarGroup(String name, Color mColor, Integer id){
		
	}
	
	@Override
	public int hashCode(){
		return mID.hashCode();
	}
}
