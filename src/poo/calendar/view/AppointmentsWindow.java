package poo.calendar.view;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


/**
 * Widget class that displays the calendar window.
 * It's a vertical box containing a list of appointments and 2 buttons.
 */
public class AppointmentsWindow extends VBox {
	Button mAddButton, mDeleteButton;
	AppointmentListView mALV;
	
	public AppointmentsWindow(){
		this.setAlignment(Pos.TOP_CENTER);
		this.setSpacing(20);
		this.setPrefHeight(800);
		this.setPrefWidth(250);
		
		mAddButton = new Button("Add");
		mDeleteButton = new Button("Delete");
		
		mAddButton.setOnAction(a -> onAddClick(a));
		mDeleteButton.setOnAction(a -> onDeleteClick(a));
		
		HBox box = new HBox();
		box.getChildren().addAll(mAddButton, mDeleteButton);
		box.setAlignment(Pos.BASELINE_CENTER);
		box.setSpacing(10);
		
		mALV = new AppointmentListView();
		VBox.setVgrow(mALV, Priority.ALWAYS);
		
		this.getChildren().addAll(mALV, box);
	}
	
	private void onAddClick(ActionEvent a){
		// Open auxiliary window (or change scene) for adding appointments
		// Send User input to the controller
		// Repeat steps above
		mALV.add();
	}
	
	private void onDeleteClick(ActionEvent a){
		// Open auxiliary window (or change scene) for removing appointments
		// Send User input to the controller
		// Repeat steps above
	}
}