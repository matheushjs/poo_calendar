package poo.calendar.view;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class TaskWindow extends VBox {
	private Button mAddButton, mDeleteButton;
	
	public TaskWindow(){
		this.setAlignment(Pos.BASELINE_CENTER);
		this.setSpacing(20);
		
		Rectangle r1 = new Rectangle(0, 0, 200, 600);
		Rectangle r2 = new Rectangle(0, 0, 200, 100);
		r1.setFill(Color.ALICEBLUE);
		r2.setFill(Color.BEIGE);
		
		mAddButton = new Button("Add");
		mDeleteButton = new Button("Delete");
		mAddButton.setOnAction(a -> onAddClick(a));
		mDeleteButton.setOnAction(a -> onDeleteClick(a));
		
		HBox box = new HBox();
		box.getChildren().addAll(mAddButton, mDeleteButton);
		box.setAlignment(Pos.BASELINE_CENTER);
		box.setSpacing(10);
		
		this.getChildren().addAll(r1, box);
	}
	
	private void onAddClick(ActionEvent a){
		// Open auxiliary window (or change scene) for adding appointments
		// Send User input to the controller
		// Repeat steps above
	}
	
	private void onDeleteClick(ActionEvent a){
		// Open auxiliary window (or change scene) for removing appointments
		// Send User input to the controller
		// Repeat steps above
	}
}
