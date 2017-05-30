package poo.calendar.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Widget class that represents the main window of the application.
 */
public class MainWindow extends Application {
	private CalendarWindow mCalendarWindow;
	private TaskWindow mTaskWindow;
	private Background mainBG =
			new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, null, null));
	
	public static void main(String args[]){
    	launch(args);
    }
	
    @Override 
    public void start(Stage stage) { 
    	HBox box = new HBox();
    	Scene scene = new Scene(box);
    	
    	box.setAlignment(Pos.CENTER);
    	box.setSpacing(20);
    	box.setBackground(mainBG);
    	
    	mCalendarWindow = new CalendarWindow();
    	mTaskWindow = new TaskWindow();
    	box.getChildren().addAll(mCalendarWindow, mTaskWindow);
    	box.setPadding(new Insets(10, 10, 10, 10));
    	
    	stage.setTitle("Calendar");
    	stage.setScene(scene);
    	stage.show();
    } 
}
