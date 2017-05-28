package poo.calendar.view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * Widget class that displays an unordered list of TaskView.
 */
class TaskListView extends VBox {
	public TaskListView(){
		this.setAlignment(Pos.TOP_CENTER);
	}
	
	private static int count = 0;
	public void add(){
		count++;
		if(count % 5 == 0)
			this.getChildren().add(new TaskView("Task"));
		else
			this.getChildren().add(new TaskView("Task", "" + count));
	}
}