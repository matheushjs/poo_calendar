package poo.calendar.mainscene.tasks;

import java.util.Calendar;
import java.util.UUID;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import poo.calendar.ColorUtil;
import poo.calendar.ControlledWidget;
import poo.calendar.DateUtil;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Task;

/**
 * Widget for displaying a task in the application.
 * If the task has a deadline, the title is placed to the left, and the date to the right of the HBox.
 * If the task does not have a deadline, the title is centered.
 * 
 * TaskView will observe the source Task's:
 *   - Title
 *   - Deadline
 */
public class TaskViewController extends ControlledWidget<HBox> implements Comparable<TaskViewController> {
	private HBox mWidget;
	private Label mTitle;
	private Label mDeadlineLabel;
	private Calendar mDeadline;
	private boolean hasDeadline;
	private UUID mID;
	
	/**
	 * Default constructor
	 */
	public TaskViewController(Task task, CalendarGroup cg){
		super();
		
		mID = task.getID();
		
		mTitle.setText(task.getTitle());
		mTitle.textProperty().bind(task.titleProperty());
		
		setDeadline(task.getDeadlineDate());
		task.deadlineDateProperty().addListener((obs, oldval, newval) -> {
			setDeadline(task.getDeadlineDate());
		});
		
		setColor(cg.getColor());
	}
	
	protected void initializeWidget(){
		mWidget = new HBox();
		mTitle = new Label();
		mDeadlineLabel = new Label();
		
		mWidget.setCursor(Cursor.HAND);
		
		mDeadlineLabel.setAlignment(Pos.CENTER);
		mTitle.setAlignment(Pos.CENTER);
		mWidget.setAlignment(Pos.CENTER);
		
		HBox.setHgrow(mTitle, Priority.ALWAYS);
		HBox.setHgrow(mDeadlineLabel, Priority.ALWAYS);
		
		mTitle.setMaxWidth(Double.MAX_VALUE);
		mDeadlineLabel.setMaxWidth(Double.MAX_VALUE);
		mWidget.setPrefWidth(Double.MAX_VALUE);
		
		mWidget.getChildren().add(mTitle);
	}
	
	public HBox getWidget(){
		return mWidget;
	}
	
	private void setDeadline(Calendar deadline){
		mDeadline = deadline;
		
		if(deadline != null){
			mDeadlineLabel.setText(
					DateUtil.dateString(deadline) +
					" " + 
					DateUtil.hourString(deadline));
			
			// Check if widget isn't already inside mWidget
			if(!hasDeadline)
				mWidget.getChildren().add(mDeadlineLabel);
			
			hasDeadline = true;
			
		} else {
			mDeadlineLabel.setText("");
			mWidget.getChildren().remove(mDeadlineLabel);
			hasDeadline = false;
		}
	}
	
	public void setColor(Color col){
		Color contrast = ColorUtil.contrastColor(col);
		
		mTitle.setTextFill(Paint.valueOf(contrast.toString()));
		mDeadlineLabel.setTextFill(Paint.valueOf(contrast.toString()));
		
		mWidget.setBackground(
				new Background(
						new BackgroundFill(
								Paint.valueOf(col.toString()), null, null)));
	}
	
	public boolean hasDeadline(){
		return hasDeadline;
	}
	
	public UUID getID(){
		return mID;
	}
	
	public int compareTo(TaskViewController other){
		int cmp;
		
		if(this.hasDeadline && !other.hasDeadline) return -1;
		else if(!this.hasDeadline && other.hasDeadline) return 1;
		else {
			cmp = this.mDeadline.compareTo(other.mDeadline);
			if(cmp != 0) return cmp;
			else return this.mID.compareTo(other.mID);
		}
	}
}
