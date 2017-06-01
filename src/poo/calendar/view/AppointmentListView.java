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
	
	public void add(String str1, String str2){
		this.getChildren().add(new AppointmentView(str1, str2));
	}
}