package poo.calendar.model;

import java.io.Serializable;
import java.util.Calendar;

import javafx.beans.property.StringProperty;
import poo.calendar.IDGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A model class that represents a Task. A task has a title(required) and a
 * deadline date(optional).
 */
public class Task {

	/** The task's title. */
	private StringProperty mTitle;

	/** The task's deadline date. */
	private ObjectProperty<Calendar> mDeadlineDate;

	/** The task's identification number */
	private long mID;

	/**
	 * The task's default constructor.
	 *
	 * @param title required
	 * @param deadlineDate optional
	 * @throws NullArgumentException if 'title' argument is null.
	 */
	public Task(String title, Calendar deadlineDate) throws NullPointerException {
		mID = IDGenerator.getID();
		mTitle = new SimpleStringProperty();
		mDeadlineDate = new SimpleObjectProperty<Calendar>();
		
		this.setTitle(title);
		this.setDeadlineDate(deadlineDate);
	}

	/**
	 * @return The Task's Title value
	 */
	public final String getTitle() {
		return mTitle.get();
	}

	/**
	 * @param mTitle the task's title to set.
	 * @throws NullPointerException if title is null
	 */
	public final void setTitle(String title) throws NullPointerException {
		if(title == null)
			throw new NullPointerException("Task title cannot be null");
		mTitle.set(title);
	}

	/**
	 * @return The Task's Title property
	 */
	public StringProperty TitleProperty(){
		return mTitle;
	}

	/**
	 * @return the task's deadline date
	 */
	public final Calendar getDeadlineDate() {
		return mDeadlineDate.get();
	}

	/**
	 * @param mDeadlineDate the task's deadline date to set. Must be null if Task
	 * doesn't have a deadline.
	 */
	public final void setDeadlineDate(Calendar deadlineDate) {
		mDeadlineDate.set(deadlineDate);
	}

	/**
	 * @return The Task's Deadline Date property
	 */
	public ObjectProperty<Calendar> DeadlineDateProperty(){
		return mDeadlineDate;
	}

	/**
	 * @return The task's ID
	 */
	public final long getID(){
		return mID;
	}
}
