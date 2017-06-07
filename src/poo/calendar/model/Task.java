package poo.calendar.model;

import java.util.Calendar;
import java.util.UUID;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A model class that represents a Task. A task has a title(required) and a
 * deadline date(optional).
 */
public class Task extends CalendarNodeBase {
	/** The task's deadline date. */
	private ObjectProperty<Calendar> mDeadlineDate;

	/**
	 * The task's default constructor.
	 *
	 * @param title title for the task
	 * @param description description for the task
	 * @param deadlineDate deadline date for the task. If null, the task
	 * @throws NullArgumentException if title or description are null.
	 */
	public Task(String title, String description, Calendar deadlineDate, UUID groupID) throws NullPointerException {
		super(title, description, groupID);
		mDeadlineDate = new SimpleObjectProperty<Calendar>();
		this.setDeadlineDate(deadlineDate);
	}

	/**
	 * Wrapper of default constructor. groupID is set to default group.
	 */
	public Task(String title, String description, Calendar deadlineDate) throws NullPointerException {
		this(title, description, deadlineDate, null);
	}
	
	/**
	 * Wrapper of default constructor. Creates a Task without a deadline date.
	 */
	public Task(String title, String description, UUID groupID){
		this(title, description, null, groupID);
	}
	
	/**
	 * Wrapper of default constructor. Creates a task without a deadline date, and groupID
	 * is set to default group.
	 */
	public Task(String title, String description){
		this(title, description, null, null);
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
	public ObjectProperty<Calendar> deadlineDateProperty(){
		return mDeadlineDate;
	}
}
