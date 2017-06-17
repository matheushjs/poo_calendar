package poo.calendar.mainscene.tasks;

import java.util.Calendar;
import java.util.UUID;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import poo.calendar.ColorUtil;
import poo.calendar.ControlledWidget;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Task;

/**
 * Widget for displaying a task in the application.
 * If the task has a deadline, the title is placed to the left, and the date to the right of the HBox.
 * If the task does not have a deadline, the title is centered.
 * 
 * TaskView will observe:
 * 	- source task's Title
 *  - source task's group ID
 *  - source task's deadline data
 *  - source group's color
 */
public class TaskViewController extends ControlledWidget<HBox> implements Comparable<TaskViewController> {
	// Attributes
	private Calendar mDeadline;
	private boolean hasDeadline;
	private UUID mID;
	private ChangeListener<Color> mGroupListener;
	
	// View Structures
	private HBox mWidget;
	private Label mTitle;
	private Label mDeadlineLabel;
	
	// Foreign structures
	private MainApplication mMainApp;
	private CalendarDataModel mModel;
	
	/**
	 * Default constructor
	 * Initializes the widget to a valid state
	 */
	public TaskViewController(){
	}
	
	protected void initializeWidget(){
		mWidget = new HBox();
		mTitle = new Label();
		mDeadlineLabel = new Label();
		
		mTitle.setAlignment(Pos.CENTER);
		mTitle.setWrapText(true);
		mTitle.setMinWidth(100);
		
		mDeadlineLabel.setAlignment(Pos.CENTER);
		mDeadlineLabel.setWrapText(true);
		mDeadlineLabel.setMinWidth(100);
		mDeadlineLabel.setMaxWidth(100);
		
		mWidget.setAlignment(Pos.CENTER);
		mWidget.setPrefWidth(Double.MAX_VALUE);
		mWidget.setSpacing(5);
		mWidget.setCursor(Cursor.HAND);
		mWidget.getChildren().add(mTitle);
	}
	
	public HBox getWidget(){
		return mWidget;
	}
	
	public void initializeStructures(MainApplication app, CalendarDataModel model, UUID taskID){
		mMainApp = app;
		mModel = model;
		mID = taskID;
		
		Task task = mModel.getTask(taskID);
		CalendarGroup cg = mModel.getRefGroup(task);
		
		mWidget.setOnMouseClicked(action -> mMainApp.displayTaskDialog(mID));
		
		// Observe source group's color
		mGroupListener = (obs, oldval, newval) -> setColor(newval);
		cg.colorProperty().addListener(new WeakChangeListener<Color>(mGroupListener));
		
		// Observe source task's group ID
		task.groupIDProperty().addListener((obs, oldval, newval) -> {
			// Must observe new group, and drop the reference to the old lambda function.
			CalendarGroup cg2 = mModel.getGroup(newval);
			mGroupListener = (obs2, oldval2, newval2) -> setColor(newval2);
			cg2.colorProperty().addListener(new WeakChangeListener<Color>(mGroupListener));
			setColor(cg2.getColor());
		});
		
		mTitle.setText(task.getTitle());
		
		//Observe source task's title
		mTitle.textProperty().bind(task.titleProperty());
		
		//Observe source task's deadline date
		setDeadline(task.getDeadlineDate());
		task.deadlineDateProperty().addListener((obs, oldval, newval) -> {
			setDeadline(task.getDeadlineDate());
		});
		
		setColor(cg.getColor());
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
