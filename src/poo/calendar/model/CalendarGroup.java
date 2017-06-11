package poo.calendar.model;

import java.util.UUID;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

/**
 * Class that represents a group in the calendar.
 * A group contains information that are common among many tasks and appointments,
 * such as their colors in the UI.
 */
public class CalendarGroup {
	// ID for the default group
	public static UUID DEFAULT_ID = UUID.fromString("d26a05c8-60c7-4a15-86ce-12605bb198a8");
	public static CalendarGroup DEFAULT_GROUP = new CalendarGroup("Default", Color.ORANGE, DEFAULT_ID);
	
	//The group's name
	private StringProperty mName;
	
	//The group's color
	private ObjectProperty<Color> mColor;
	
	//The group's ID
	private UUID mID;
	
	private CalendarGroup(String name, Color color, UUID id) throws NullPointerException {
		this(name, color);
		mID = id;
	}
	
	/**
	 * Default constructor.
	 * @param name The group's name
	 * @param mColor The group's color
	 * @throws NullPointerException if any argument is null
	 */
	public CalendarGroup(String name, Color color) throws NullPointerException {
		mName = new SimpleStringProperty();
		mColor = new SimpleObjectProperty<Color>();
		
		this.setName(name);
		this.setColor(color);
		
		mID = UUID.randomUUID();
	}
	
	/**
	 * @return the group's name
	 */
	public final String getName(){
		return mName.get();
	}
	
	/**
	 * @param name new name for the group
	 * @throws NullPointerException if parameter 'name' is null
	 */
	public final void setName(String name) throws NullPointerException {
		if(name == null)
			throw new NullPointerException("CalendarGroup's name cannot be null");
		mName.set(name);
	}
	
	/**
	 * @return the group's name property.
	 */
	public StringProperty nameProperty(){
		return mName;
	}
	
	/**
	 * @return the group's color.
	 */
	public final Color getColor(){
		return mColor.get();
	}
	
	/**
	 * @param col new color for the group
	 * @throws NullPointerException if given parameter is null
	 */
	public final void setColor(Color col) throws NullPointerException {
		if(col == null)
			throw new NullPointerException("CalendarGroup's color cannot be null");
		mColor.set(col);
	}
	
	/**
	 * @return the color property of the group
	 */
	public ObjectProperty<Color> colorProperty(){
		return mColor;
	}
	
	/**
	 * @return the group's UUID
	 */
	public UUID getID(){
		return mID;
	}
	
	@Override
	public String toString(){
		return mName.get();
	}
}
