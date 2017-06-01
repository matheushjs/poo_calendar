package poo.calendar.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * A model class that represents a Task. A task has a title(required) and a
 * deadline date(optional).
 *
 */
public class Task implements Serializable {

	/** The task's title. */
	private String mTitle;

	/** The task's deadline date. */
	private Calendar mDeadlineDate;
	
	/** The task's identification number */
	private long mID;
	
	/**
	 * The task's default constructor.
	 *
	 * @param title required
	 * @param deadlineDate optional
	 * @throws IllegalArgumentException
	 */
	public Task(String title, Calendar deadlineDate) throws IllegalArgumentException {
		if (title == null || title.equals("")) {
			throw new IllegalArgumentException("A task must provide a non-null non-empty title string.");
		}

		mID = IDGenerator.getID();
		mTitle = title;
		mDeadlineDate = deadlineDate;
	}

	/**
	 * @return the task's title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * @param mTitle the task's title to set
	 */
	public void setTitle(String title) {
		mTitle = title;
	}

	/**
	 * @return the task's deadline date
	 */
	public Calendar getDeadlineDate() {
		return mDeadlineDate;
	}

	/**
	 * @param mDeadlineDate the task's deadline date to set
	 */
	public void setDeadlineDate(Calendar deadlineDate) {
		mDeadlineDate = deadlineDate;
	}
	
	/**
	 * @return The task's ID
	 */
	public long getID(){
		return mID;
	}
}
