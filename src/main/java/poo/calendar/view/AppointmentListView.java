package poo.calendar.view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * Widget class that displays an ordered list of AppointmentView.
 */
public class AppointmentListView extends VBox {
	public AppointmentListView(){
		this.setAlignment(Pos.TOP_CENTER);
	}
	
	private static int count = 0;
	public void add(){
		count++;
		this.getChildren().add(new AppointmentView("Appointment", "" + count));
	}
}