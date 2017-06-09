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
	}
	
	public void initializeModel(ObservableMap<UUID, CalendarGroup> map){
		mGroupMap = map;
	}
}