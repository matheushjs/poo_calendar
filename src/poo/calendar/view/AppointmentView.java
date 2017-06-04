package poo.calendar.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Widget class that displays an Appointment horizontally.
 */
public class AppointmentView extends HBox {
	private Background mainBG =
			new Background(new BackgroundFill(Color.ALICEBLUE, null, null));
	
	private long mID;
	
	public AppointmentView(String left, String right, long id){
		mID = id;
		
		this.setAlignment(Pos.CENTER);
		
		Label l1 = new Label(left);
		l1.setAlignment(Pos.CENTER_LEFT);
		l1.setPrefWidth(125);
		
		Label l2 = new Label(right);
		l2.setAlignment(Pos.CENTER_RIGHT);
		l2.setPrefWidth(125);

		l1.setBackground(mainBG);
		l2.setBackground(mainBG);
		this.getChildren().addAll(l1, l2);
	}
	
	public long getID(){
		return mID;
	}
}