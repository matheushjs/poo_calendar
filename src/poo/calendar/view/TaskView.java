package poo.calendar.view;

import java.util.Calendar;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Widget class that displays a Task horizontally.
 */
public class TaskView extends HBox {
	private Background mainBG =
			new Background(new BackgroundFill(Color.WHITESMOKE, null, null));
	
	//TODO: Implement as an ObjectProperty, bound to the original task's property
	private Calendar mDeadlineDate;
	
	private long mID;
	
	public TaskView(String title, Calendar deadline, long id){
		mID = id;
		mDeadlineDate = deadline;
		
		this.setAlignment(Pos.CENTER);
		
		Label l1 = new Label(title);
		l1.setBackground(mainBG);
		
		if(deadline == null){
			l1.setAlignment(Pos.CENTER);
			this.getChildren().add(l1);
			l1.setPrefWidth(250);
		} else {
			l1.setAlignment(Pos.CENTER_LEFT);
			l1.setPrefWidth(125);
			
			String format = String.format("%02d/%02d %02d:%02d",
					deadline.get(Calendar.MONTH),
					deadline.get(Calendar.DAY_OF_MONTH),
					deadline.get(Calendar.HOUR),
					deadline.get(Calendar.MINUTE));
			
			Label l2 = new Label(format);
			l2.setAlignment(Pos.CENTER_RIGHT);
			l2.setPrefWidth(125);
			l2.setBackground(mainBG);
			
			this.getChildren().addAll(l1, l2);
		}
	}
	
	public TaskView(String left, long id){
		this(left, null, id);
	}
	
	public long getID(){
		return mID;
	}
}