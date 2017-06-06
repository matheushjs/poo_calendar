package poo.calendar.view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * Widget class that displays an ordered list of AppointmentView.
 */
public class AppointmentListView extends VBox {
	
	/**
	 * Default constructor
	 */
	public AppointmentListView(){
		this.setAlignment(Pos.TOP_CENTER);
		this.setStyle("-fx-border-style: solid; -fx-border-width: 5;");
	}
}