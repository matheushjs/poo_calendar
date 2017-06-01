package poo.calendar.view;

import javafx.event.ActionEvent;
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
		mAddButton.setOnAction(a -> onAddClick(a));
		mDeleteButton.setOnAction(a -> onDeleteClick(a));

		HBox box = new HBox();
		box.getChildren().addAll(mAddButton, mDeleteButton);
		box.setAlignment(Pos.BASELINE_CENTER);
		box.setSpacing(10);

		mTLV = new TaskListView();
		VBox.setVgrow(mTLV, Priority.ALWAYS);
		
		this.getChildren().addAll(mTLV, box);
	}
	
	private void onAddClick(ActionEvent a){
		// Open auxiliary window (or change scene) for adding appointments
		// Send User input to the controller
		// Repeat steps above
		mTLV.add();
	}
	
	private void onDeleteClick(ActionEvent a){
		// Open auxiliary window (or change scene) for removing appointments
		// Send User input to the controller
		// Repeat steps above
	}
}
