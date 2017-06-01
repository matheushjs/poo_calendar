package poo.calendar.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Widget class that represents the main window of the application.
 */
public class MainScene extends HBox {
	private AppointmentsWindow mCalendarWindow;
	private TaskWindow mTaskWindow;
	private Background mainBG =
			new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, null, null));
	
    public MainScene() { 
    	this.setAlignment(Pos.CENTER);
    	this.setSpacing(20);
    	this.setBackground(mainBG);
    	
    	mCalendarWindow = new AppointmentsWindow();
    	mTaskWindow = new TaskWindow();
    	this.getChildren().addAll(mCalendarWindow, mTaskWindow);
    	this.setPadding(new Insets(10, 10, 10, 10));
    }
}
