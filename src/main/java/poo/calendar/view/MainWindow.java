package poo.calendar.view;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

/**
 * Widget class that represents the main window of the application.
 */
public class MainWindow extends Application {
	private CalendarWindow mCalendarWindow;
	private TaskWindow mTaskWindow;
	
	public static void main(String args[]){
    	launch(args);
    }
	
    @Override 
    public void start(Stage stage) { 
    	HBox box = new HBox();
    	Scene scene = new Scene(box);
    	
    	box.setAlignment(Pos.BASELINE_CENTER);
    	box.setSpacing(6);
    	
    	mCalendarWindow = new CalendarWindow();
    	mTaskWindow = new TaskWindow();
    	box.getChildren().addAll(mCalendarWindow, mTaskWindow);
    	
    	stage.setTitle("Calendar");
    	stage.setScene(scene);
    	stage.show();
    } 
}
