package poo.calendar.view;

import java.util.Calendar;

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
			new Background(new BackgroundFill(Color.ALICEBLUE, null, null));
	
	//TODO: Implement as an ObjectProperty bound to the original appointment's initDate.
	private Calendar mInitDate;
	
	private long mID;
	
	public AppointmentView(String title, Calendar initDate, long id){
		mID = id;
		mInitDate = initDate;
		
		this.setAlignment(Pos.CENTER);
		
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

		l1.setBackground(mainBG);
		l2.setBackground(mainBG);
		this.getChildren().addAll(l1, l2);
	}
	
	public long getID(){
		return mID;
	}
	
	public int compareTo(AppointmentView other){
		return this.mInitDate.compareTo(other.mInitDate);
	}
}