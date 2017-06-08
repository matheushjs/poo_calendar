package poo.calendar.view;


import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import poo.calendar.controller.AppointmentController;
import poo.calendar.controller.TaskController;

/**
 * Widget class that represents the main window of the application.
 */
public class MainScene extends VBox {
	private AppointmentWindow mAppointmentWindow;
	private TaskWindow mTaskWindow;
	private Background mainBG =
			new Background(new BackgroundFill(Color.DARKGRAY, null, null));
	
	/**
	 * Default constructor.
	 */
    public MainScene() { 
    	this.setBackground(mainBG);
    	this.setAlignment(Pos.CENTER);
    	
    	HBox hbox = new HBox();
    	hbox.setAlignment(Pos.CENTER);
    	hbox.setSpacing(20);
    	
    	mAppointmentWindow = AppointmentController.getInstance().getAppointmentWindow();
    	mTaskWindow = TaskController.getInstance().getTaskWindow();
    	hbox.getChildren().addAll(mAppointmentWindow, mTaskWindow);
    	hbox.setPadding(new Insets(10, 10, 10, 10));
    	
    	
    	Text title = new Text("Calendar");
    	
    	DropShadow ds = new DropShadow();
    	ds.setOffsetY(3.0f);
    	ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
    	
    	title.setCache(true);
    	title.setEffect(ds);
    	title.setTextAlignment(TextAlignment.CENTER);
    	title.setFont(Font.font("Liberation Serif", FontPosture.ITALIC, 40));
    	title.setFill(Paint.valueOf("linear-gradient(from 0% 0% to 100% 200%, repeat, lightgrey 0%, white 60%)"));
    	VBox.setMargin(title, new Insets(10, 0, 10, 0));
    	
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(this.getClass().getResource("/poo/calendar/view/CalendarGroupListView.fxml"));
    	
    	VBox groupsWidget = null;
    	try {
    		groupsWidget = (VBox) loader.load();
    	} catch(IOException e){
    		System.out.println(e.getMessage());
    		System.exit(1);
    	}
    	
    	VBox.setVgrow(groupsWidget, Priority.ALWAYS);
    	this.getChildren().addAll(title, groupsWidget, hbox);
    }
}
