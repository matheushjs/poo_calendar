package poo.calendar.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Widget class that displays the tasks window.
 * It's a vertical box containing a list of tasks and 2 buttons.
 */
public class TaskWindow extends VBox {
	private Button mAddButton, mDeleteButton;
	private TaskListView mTLV;
	
	public TaskWindow(){
		this.setAlignment(Pos.BASELINE_CENTER);
		this.setSpacing(20);
		this.setPrefHeight(800);
		this.setPrefWidth(500);
		
		mAddButton = new Button("Add");
		mDeleteButton = new Button("Delete");

		HBox box = new HBox();
		box.getChildren().addAll(mAddButton, mDeleteButton);
		box.setAlignment(Pos.BASELINE_CENTER);
		box.setSpacing(10);

		mTLV = new TaskListView();
		VBox.setVgrow(mTLV, Priority.ALWAYS);
		
		this.getChildren().addAll(mTLV, box);
	}
	
	/**
	 * @return The TaskListView within this widget
	 */
	public TaskListView getTaskListView(){
		return mTLV;
	}
	/**
	 * @return The "Add" Button within this widget
	 */
	public Button getAddButton(){
		return mAddButton;
	}
	
	/**
	 * @return The "Delete" button within this widget
	 */
	public Button getDeleteButton(){
		return mDeleteButton;
	}
}
