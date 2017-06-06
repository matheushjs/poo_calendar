package poo.calendar.view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * Widget class that displays an unordered list of TaskView.
 */
public class TaskListView extends VBox {
	/**
	 * Default constructor
	 */
	public TaskListView(){
		this.setAlignment(Pos.TOP_CENTER);
		this.setStyle("-fx-border-style: solid; -fx-border-width: 5;");
	}
}
