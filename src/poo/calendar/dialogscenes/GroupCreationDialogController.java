package poo.calendar.dialogscenes;

import java.util.UUID;

import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.CalendarGroup;

/**
 * Class for handling the GroupCreationDialog scene
 */
public class GroupCreationDialogController {
	@FXML
	private DialogPane mMainPane;
	
	@FXML
	private Text mHeaderText;
	
	@FXML
	private VBox mFormBox;
	
	@FXML
	private TextField mTitleField;
	
	private MainApplication mMainApp;
	
	// Model Data
	public ObservableMap<UUID, CalendarGroup> mGroupMap;
	
	/**
	 * Default constructor
	 */
	public GroupCreationDialogController() {
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded.
	 */
	@FXML
	private void initialize(){
		//ŦODO: Connect due signals
	}
	
	/**
	 * Receives the map of groups to which the new group will be added.
	 */
	public void initializeModel(ObservableMap<UUID, CalendarGroup> map){
		mGroupMap = map;
	}
	
	/**
	 * Sets the main application from which this dialog will later request a scene change;
	 * @param app
	 */
	public void setMainApp(MainApplication app){
		mMainApp = app;
		
		Button bt = (Button) mMainPane.lookupButton(ButtonType.APPLY);
		bt.setOnAction(action -> mMainApp.displayMainRoot());
	}
}