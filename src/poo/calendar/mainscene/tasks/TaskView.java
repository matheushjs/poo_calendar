package poo.calendar.mainscene.tasks;

import java.util.Calendar;
import java.util.UUID;

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
	
	private UUID mID;
	
	/**
	 * Default constructor.
	 * @param title The source Task's title
	 * @param deadline The source Task's deadline date
	 * @param id The source Task's ID
	 */
	public TaskView(String title, Calendar deadline, UUID id){
		mID = id;
		mDeadlineDate = deadline;
		
		this.setAlignment(Pos.CENTER);
		this.setBackground(mainBG);
		
		Label l1 = new Label(title);
		
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
			
			this.getChildren().addAll(l1, l2);
		}
	}
	
	/**
	 * Constructs a task without a deadline date.
	 * @param title The Task's title
	 * @param id The source Tasks's ID
	 */
	public TaskView(String title, UUID id){
		this(title, null, id);
	}
	
	/**
	 * @return The TaskView's ID, which must be equal its source Task's ID.
	 */
	public UUID getID(){
		return mID;
	}
}