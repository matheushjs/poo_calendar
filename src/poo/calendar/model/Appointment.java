package poo.calendar.model;

import java.io.Serializable;
import java.util.Calendar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment implements Serializable {
	// The Appointment's title
	private StringProperty mTitle;
	
	// The time when the Appointment begins
	private ObjectProperty<Calendar> mInitDate;
	
	// The time when the Appointment ends
	private ObjectProperty<Calendar> mEndDate;
	
	// The Appointment's Identification Number
	private long mID;
	
	/**
	 * Appointment's default constructor.
	 * 
	 * @param title A Title for the Appointment
	 * @param initDate Initial Date
	 * @param endDate End Date
	 */
	public Appointment(String title, Calendar initDate, Calendar endDate){
		mTitle = new SimpleStringProperty(title);
		mInitDate = new SimpleObjectProperty<Calendar>(initDate);
		mEndDate = new SimpleObjectProperty<Calendar>(endDate);

		mID = IDGenerator.getID();
		
		//TODO: Validade dates
	}

	/**
	 * @return The Appointment's title value
	 */
	public final String getTitle() {
		return mTitle.get();
	}

	/**
	 * @return The Appointment's initial date
	 */
	public final Calendar getInitDate() {
		return mInitDate.get();
	}

	/**
	 * @return The Appointment's initial date property
	 */
	public ObjectProperty<Calendar> InitDateProperty(){
		return mInitDate;
	}
	
	/**
	 * @return The Appointment's end date value
	 */
	public final Calendar getEndDate() {
		return mEndDate.get();
	}
	
	/**
	 * @return The Appointment's end date property
	 */
	public ObjectProperty<Calendar> EndDateProperty(){
		return mEndDate;
	}

	/**
	 * @return The Appointment's ID
	 */
	public final long getID() {
		return mID;
	}
}