package poo.calendar.model;

import java.util.Calendar;

public class Appointment {
	// The Appointment's title
	private String mTitle;
	
	// The time when the Appointment begins
	private Calendar mInitDate;
	
	// The time when the Appointment ends
	private Calendar mEndDate;
	
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
		mTitle = title;
		mInitDate = initDate;
		mEndDate = endDate;

		mID = IDGenerator.getID();
		
		//TODO: Validade dates
	}

	/**
	 * @return The Appointment's title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * @return The Appointment's initial date
	 */
	public Calendar getInitDate() {
		return mInitDate;
	}

	/**
	 * @return The Appointment's end date
	 */
	public Calendar getEndDate() {
		return mEndDate;
	}

	/**
	 * @return The Appointment's ID
	 */
	public long getID() {
		return mID;
	}
}