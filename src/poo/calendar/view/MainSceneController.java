package poo.calendar.view;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import poo.calendar.controller.TaskController;

/**
 * Widget class that represents the main window of the application.
 */
public class MainSceneController {
	@FXML
	private VBox mMainBox;
	
	@FXML
	private Text mTitleText;
	
	@FXML
	private HBox mBottomBox;

	//TODO: Use CSS here
	private Background mainBG =
			new Background(new BackgroundFill(Color.DARKGRAY, null, null));
	
	/**
	 * Default constructor.
	 */
    public MainSceneController() {
    }
    
    /**
     * Called by FXML -after- the .fxml is loaded.
     */
    @FXML
    private void initialize(){
    	mMainBox.setBackground(mainBG);
    	
    	DropShadow ds = new DropShadow();
    	ds.setOffsetY(3.0f);
    	ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
    	mTitleText.setEffect(ds);
    	mTitleText.setCache(true);
    	
    	addTaskWidget(TaskController.getInstance().getTaskWindow());
    	
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(this.getClass().getResource("/poo/calendar/view/CalendarGroupListView.fxml"));
    	
    	VBox groupsWidget = null;
    	try {
    		groupsWidget = (VBox) loader.load();
    	} catch(IOException e){
    		System.out.println(e.getMessage());
    		System.exit(1);
    	}
    	
    	addGroupsWidget(groupsWidget);
    }
    
    /**
     * Receives the AppointmentWindow widget and put it in the correct place.
     * @param widget
     */
    public void addAppointmentWidget(Node widget){
    	try {
    		mBottomBox.getChildren().add(0, widget);
    	} catch(IndexOutOfBoundsException e){
    		mBottomBox.getChildren().add(widget);
    	}
    }
    
    /**
     * Receives the TaskWindow widget and place it in the correct place
     * @param widget
     */
    public void addTaskWidget(Node widget){
    	try {
    		mBottomBox.getChildren().add(1, widget);
    	} catch(IndexOutOfBoundsException e){
    		mBottomBox.getChildren().add(widget);
    	}
    }
    
    /**
     * Receives the widget that displays calendar groups
     * @param widget
     */
    public void addGroupsWidget(Node widget){
    	VBox.setVgrow(widget, Priority.ALWAYS);
    	try {
    		mMainBox.getChildren().add(1, widget);
    	} catch(IndexOutOfBoundsException e){
    		mMainBox.getChildren().add(widget);
    	}
    }
}
