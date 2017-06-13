package poo.calendar.mainscene.tasks;

import java.util.Calendar;
import java.util.UUID;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
public class TaskViewController extends ControlledWidget<HBox> {
	private HBox mWidget;
	private Label mTitle;
	private Label mDeadline;
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
		task.deadlineDateProperty().addListener(change -> {
			setDeadline(task.getDeadlineDate());
		});
		
		setColor(cg.getColor());
	}
	
	protected void initializeWidget(){
		mWidget = new HBox();
		mTitle = new Label();
		mDeadline = new Label();
		
		mDeadline.setAlignment(Pos.CENTER);
		mTitle.setAlignment(Pos.CENTER);
		mWidget.setAlignment(Pos.CENTER);
		
		HBox.setHgrow(mTitle, Priority.ALWAYS);
		HBox.setHgrow(mDeadline, Priority.ALWAYS);
		
		mTitle.setMaxWidth(Double.MAX_VALUE);
		mDeadline.setMaxWidth(Double.MAX_VALUE);
		mWidget.setPrefWidth(Double.MAX_VALUE);
		
		mWidget.getChildren().add(mTitle);
	}
	
	public HBox getWidget(){
		return mWidget;
	}
	
	private void setDeadline(Calendar deadline){
		if(deadline != null){
			mDeadline.setText(
					DateUtil.dateString(deadline) +
					" " + 
					DateUtil.hourString(deadline));
			
			mWidget.getChildren().add(mDeadline);
			hasDeadline = true;
			
		} else {
			mDeadline.setText("");
			mWidget.getChildren().remove(mDeadline);
			hasDeadline = false;
		}
	}
	
	public void setColor(Color col){
		mWidget.setBackground(
				new Background(
						new BackgroundFill(
								Paint.valueOf(col.toString()),
								null, 
								null )));
	}
	
	public boolean hasDeadline(){
		return hasDeadline;
	}
	
	public UUID getID(){
		return mID;
	}
}
