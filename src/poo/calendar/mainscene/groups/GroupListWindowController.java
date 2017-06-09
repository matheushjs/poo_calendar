package poo.calendar.mainscene.groups;

import java.util.UUID;

import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.CalendarGroup;

public class GroupListWindowController {
	@FXML
	private VBox mMainBox;
	
	@FXML
	private Label mHeaderLabel;
	
	@FXML
	private ScrollPane mScrollPane;
	
	@FXML
	private AnchorPane mAnchorPane;
	
	@FXML
	private FlowPane mMainPane;
	
	@FXML
	private Button mAddButton;
	
	private MainApplication mMainApp;
	
	// Mode data
	private ObservableMap<UUID, CalendarGroup> mGroupMap;
	
	/**
	 * Default constructor
	 */
	public GroupListWindowController(){
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded.
	 */
	@FXML
	private void initialize(){
		//TODO: Connect due signals
		for(int i = 0; i < 100; i++)
		mMainPane.getChildren().addAll(
				new GroupView("Oh", Color.RED, UUID.randomUUID()),
				new GroupView("Oh2", Color.RED, UUID.randomUUID())
						);
	}
	
	/**
	 * Receive the map of calendar groups that this widget should control
	 * @param map
	 */
	public void initializeModel(ObservableMap<UUID, CalendarGroup> map){
		mGroupMap = map;
	}
	
	/**
	 * Sets the main application from which this widget will later request a scene change;
	 * @param app
	 */
	public void setMainApp(MainApplication app){
		mMainApp = app;
		
		mAddButton.setOnAction(action -> {
			mMainApp.displayGroupCreationDialog();
		});
	}
}