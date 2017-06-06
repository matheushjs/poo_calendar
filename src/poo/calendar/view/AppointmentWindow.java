package poo.calendar.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


/**
 * Widget class that displays the calendar window.
 * It's a vertical box containing a list of appointments and 2 buttons.
 */
public class AppointmentWindow extends VBox {
	Button mAddButton;
	ToggleButton mDeleteButton;
	AppointmentListView mALV;
	
	/**
	 * Default constructor
	 */
	public AppointmentWindow(){
		this.setAlignment(Pos.BASELINE_CENTER);
		this.setSpacing(20);
		this.setPrefHeight(800);
		this.setPrefWidth(500);
		
		mAddButton = new Button("Add");
		mDeleteButton = new ToggleButton("Delete");
		
		HBox box = new HBox();
		box.getChildren().addAll(mAddButton, mDeleteButton);
		box.setAlignment(Pos.BASELINE_CENTER);
		box.setSpacing(10);
		
		mALV = new AppointmentListView();
		VBox.setVgrow(mALV, Priority.ALWAYS);
		
		this.getChildren().addAll(mALV, box);
	}
	
	/**
	 * @return The "Add" button widget within this window.
	 */
	public Button getAddButton(){
		return mAddButton;
	}
	
	/**
	 * @return The "Delete" button widget within this window.
	 */
	public ToggleButton getDeleteButton(){
		return mDeleteButton;
	}
	
	/**
	 * @return The AppointmentListView widget within this window.
	 */
	public AppointmentListView getAppointmentListView(){
		return mALV;
	}
}