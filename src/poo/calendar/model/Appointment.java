package poo.calendar.model;

import java.io.Serializable;
import java.util.Calendar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import poo.calendar.IDGenerator;

/**
 * Model class that represents an appointment.
 * An appointment has a title, an initial date (and time), and an end date.
 */
public class Appointment {
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
	 * @throws NullPointerException if any argument is null
	 * @throws IllegalArgumentException if end date is earlier than init date
	 */
	public Appointment(String title, Calendar initDate, Calendar endDate) throws NullPointerException, IllegalArgumentException {
		mTitle = new SimpleStringProperty(title);
		mInitDate = new SimpleObjectProperty<Calendar>(initDate);
		mEndDate = new SimpleObjectProperty<Calendar>(endDate);

		this.setTitle(title);
		this.setDates(initDate, endDate);
		
		mID = IDGenerator.getID();
	}

	/**
	 * @return The Appointment's title value
	 */
	public final String getTitle() {
		return mTitle.get();
	}

	/**
	 * @param title the new task title.
	 * @throws NullPointerException if given title is null
	 */
	public final void setTitle(String title) throws NullPointerException {
		if(title == null)
			throw new NullPointerException("Appointment title cannot be null");
		mTitle.set(title);
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
	 * @param init Initial date
	 * @param end End date
	 * @throws IllegalArgumentException if end date is before init date
	 * @throws NullPointerException if any of the arguments are null
	 */
	public final void setDates(Calendar init, Calendar end) throws IllegalArgumentException, NullPointerException {
		if(init == null || end == null)
			throw new NullPointerException("Dates cannot be null");
		if(init.compareTo(end) > 0)
			throw new IllegalArgumentException("End date cannot be earlier than init date");
	}
	
	/**
	 * @return The Appointment's ID
	 */
	public final long getID() {
		return mID;
	}
}
