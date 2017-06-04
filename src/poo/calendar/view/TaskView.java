package poo.calendar.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Widget class that displays a Task horizontally.
 */

//TODO: Might make TaskView and AppointmentView a single class.

public class TaskView extends HBox {
	private Background mainBG =
			new Background(new BackgroundFill(Color.BURLYWOOD, null, null));
	
	private long mID;
	
	public TaskView(String left, String right, long id){
		mID = id;
		
		this.setAlignment(Pos.CENTER);
		
		Label l1 = new Label(left);
		l1.setBackground(mainBG);
		
		if(right == null){
			l1.setAlignment(Pos.CENTER);
			this.getChildren().add(l1);
			l1.setPrefWidth(250);
		} else {
			l1.setAlignment(Pos.CENTER_LEFT);
			l1.setPrefWidth(125);
			
			Label l2 = new Label(right);
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