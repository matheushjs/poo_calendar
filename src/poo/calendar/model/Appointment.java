package poo.calendar.model;

import java.util.Calendar;
import java.util.UUID;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Model class that represents an appointment.
 * An appointment has a title, an initial date (and time), and an end date.
 */
public class Appointment extends CalendarNodeBase {
	// The time when the Appointment begins
	private ObjectProperty<Calendar> mInitDate;

	// The time when the Appointment ends
	private ObjectProperty<Calendar> mEndDate;

	//TODO: ADD RECURRENCY ENUM HERE
	
	/**
	 * Appointment's default constructor.
	 *
	 * @param title A Title for the Appointment
	 * @param description A description for the Appointment
	 * @param initDate Initial Date
	 * @param endDate End Date
	 * @param groupID the group ID of the group of which this Appointment is part
	 * @throws NullPointerException if any argument, but groupID, is null
	 * @throws IllegalArgumentException if endDate is earlier than initDate
	 */
	public Appointment(String title, String description, Calendar initDate, Calendar endDate, UUID groupID) throws NullPointerException, IllegalArgumentException {
		super(title, description, groupID);
		mInitDate = new SimpleObjectProperty<Calendar>();
		mEndDate = new SimpleObjectProperty<Calendar>();

		this.setDates(initDate, endDate);
	}

	/**
	 * Wrapper of the default constructor. groupID is set to default.
	 */
	public Appointment(String title, String description, Calendar initDate, Calendar endDate) throws NullPointerException, IllegalArgumentException {
		this(title, description, initDate, endDate, null);
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
	public ObjectProperty<Calendar> initDateProperty(){
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
	public ObjectProperty<Calendar> endDateProperty(){
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
		mInitDate.set(init);
		mEndDate.set(end);
	}
}
