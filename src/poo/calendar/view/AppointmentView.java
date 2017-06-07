package poo.calendar.view;

import java.util.Calendar;
import java.util.UUID;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Widget class that displays an Appointment horizontally.
 */
public class AppointmentView extends HBox implements Comparable<AppointmentView> {
	private Background mainBG =
			new Background(new BackgroundFill(Color.WHITESMOKE, null, null));
	
	//TODO: Implement as an ObjectProperty bound to the original appointment's initDate.
	private Calendar mInitDate;
	
	private UUID mID;
	
	/**
	 * Default constructor
	 * @param title Appointment title
	 * @param initDate Appointment initial date
	 * @param id Source appointment's ID
	 */
	public AppointmentView(String title, Calendar initDate, UUID id){
		mID = id;
		mInitDate = initDate;
		
		this.setAlignment(Pos.CENTER);
		this.setBackground(mainBG);
		
		Label l1 = new Label(title);
		l1.setAlignment(Pos.CENTER_LEFT);
		l1.setPrefWidth(125);
		
		String format = String.format("%02d/%02d %02d:%02d",
				mInitDate.get(Calendar.MONTH),
				mInitDate.get(Calendar.DAY_OF_MONTH),
				mInitDate.get(Calendar.HOUR),
				mInitDate.get(Calendar.MINUTE));
		
		Label l2 = new Label(format);
		l2.setAlignment(Pos.CENTER_RIGHT);
		l2.setPrefWidth(125);

		this.getChildren().addAll(l1, l2);
	}
	
	/**
	 * @return The ID of the appointment view, which should be equal its source Appointment
	 */
	public UUID getID(){
		return mID;
	}
	
	/**
	 * Comparison function.
	 */
	public int compareTo(AppointmentView other){
		return this.mInitDate.compareTo(other.mInitDate);
	}
}