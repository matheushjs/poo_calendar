package poo.calendar.mainscene.groups;

import java.util.UUID;

import javafx.animation.FadeTransition;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.CalendarGroup;

/**
 * Class for controlling the widget that displays Calendar Groups.
 * 
 * This class observes:
 *  - The model map of calendar groups;
 */
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
	
	private FadeTransition mFadeUp;
	private FadeTransition mFadeDown;
	
	private MainApplication mMainApp;
	
	// Model data
	private CalendarDataModel mModel;
	
	/**
	 * Default constructor
	 */
	public GroupListWindowController(){
		mFadeUp = new FadeTransition(Duration.millis(500));
		mFadeUp.setFromValue(0.3);
		mFadeUp.setToValue(1.0);
		
		mFadeDown = new FadeTransition(Duration.millis(500));
		mFadeDown.setFromValue(1.0);
		mFadeDown.setToValue(0.3);
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded.
	 */
	@FXML
	private void initialize(){
		mAddButton.setOpacity(0.3);
		
		mFadeUp.setNode(mAddButton);
		mFadeDown.setNode(mAddButton);
		mAddButton.setOnMouseEntered(action -> mFadeUp.playFromStart());
		mAddButton.setOnMouseExited(action -> mFadeDown.playFromStart());
	}
	
	/**
	 * Receives the structures needed for working.
	 * @param map
	 */
	public void initializeStructures(MainApplication app, CalendarDataModel model){
		mModel = model;
		
		mModel.getGroups().forEach((uuid, group) -> addGroupView(group));
		
		mModel.getGroups().addListener((MapChangeListener.Change<? extends UUID, ? extends CalendarGroup> change) -> {
			if(change.wasAdded())
				addGroupView(change.getValueAdded());
			
			if(change.wasRemoved()){
				UUID id = change.getKey();
				mMainPane.getChildren().removeIf(view -> {
					return ((GroupView) view).getID().compareTo(id) == 0;
				});
			}
		});
		
		mMainApp = app;
		mAddButton.setOnAction(action -> {
			mMainApp.displayGroupDialog();
		});
	}
	
	/**
	 * Adds a group view to the widget.
	 * @param cg
	 */
	public void addGroupView(CalendarGroup cg){
		GroupView view = new GroupView(cg);
		view.setOnMouseClicked(event -> {
			if(event.getButton().compareTo(MouseButton.PRIMARY) == 0){
				GroupView iview = (GroupView) event.getSource();
				
				// Do not allow user to edit the default group
				if(iview.getID() != CalendarGroup.DEFAULT_ID){
					mMainApp.displayGroupDialog(iview.getID());
				}
			}
		});
		
		mMainPane.getChildren().add(view);
	}
}