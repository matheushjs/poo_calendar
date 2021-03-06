package poo.calendar.model;

import java.util.Calendar;
import java.util.UUID;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import poo.calendar.DateUtil;

/**
 * Model class that represents an appointment.
 * An appointment has a title, an initial date (and time), and an end date.
 */
public class Appointment extends CalendarNodeBase {
	// The time when the Appointment begins
	private ObjectProperty<Calendar> mInitDate;

	// The time when the Appointment ends
	private ObjectProperty<Calendar> mEndDate;

	// Recurrence type
	private ObjectProperty<Recurrence> mRecurrence;
	
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
		mRecurrence = new SimpleObjectProperty<Recurrence>(Recurrence.NONE);
		
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
	
	/**
	 * @param rec new recurrence type
	 */
	public final void setRecurrence(Recurrence rec) throws IllegalArgumentException {
		long dayDiff = DateUtil.dayDiff(mInitDate.get(), mEndDate.get());
		
		if(rec == Recurrence.DAILY){
			if(dayDiff > 3) throw new IllegalArgumentException("If recurrence is daily, the appointment cannot span more than 3 days.");
		} else if(rec == Recurrence.WEEKLY) {
			if(dayDiff > 21) throw new IllegalArgumentException("If recurrence is weekly, the appointment cannot span more than 21 days.");
		} else if(rec == Recurrence.MONTHLY) {
			if(dayDiff > 63) throw new IllegalArgumentException("If recurrence is monthly, the appointment cannot span more than 63 days.");
		} else if(rec == Recurrence.YEARLY) {
			if(dayDiff > 366*3) throw new IllegalArgumentException("If recurrence is yearly, the appointment cannot span more than " + 366*3 + " days.");
		}
		
		mRecurrence.set(rec);
	}
	
	/**
	 * @return the appointment's recurrence type
	 */
	public final Recurrence getRecurrence(){
		return mRecurrence.get();
	}
	
	/**
	 * @return the appointment's recurrence property
	 */
	public ObjectProperty<Recurrence> recurrenceProperty(){
		return mRecurrence;
	}
}
