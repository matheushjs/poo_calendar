package poo.calendar.dialogscenes;

import java.util.UUID;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Task;

/**
 * Class for controlling the dialog for managing a calendar group.
 * If the class is provided with a group UUID, it presents to the user an EDIT interface.
 * Else, it presents a CREATE interface.
 */
public class GroupDialogController {
	@FXML
	private AnchorPane mMainPane;
	
	@FXML
	private HBox mButtonBox;
	
	@FXML
	private Button mApplyButton;
	
	@FXML
	private Button mCancelButton;
	
	@FXML
	private Button mDeleteButton;
	
	@FXML
	private Text mHeaderText;
	
	@FXML
	private TextField mNameField;
	
	@FXML
	private ColorPicker mColorPicker;
	
	private MainApplication mMainApp;
	
	// Model Data
	public ObservableMap<UUID, CalendarGroup> mGroupMap;
	public ObservableList<Task> mTasks;
	public ObservableList<Appointment> mAppointments;
	public CalendarGroup mGroup;
	
	/**
	 * Default constructor
	 */
	public GroupDialogController() {
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded.
	 */
	@FXML
	private void initialize(){
		//TODO: Connect due signals
		//TODO: Wisely choose a default color
		mButtonBox.getChildren().remove(mDeleteButton);
		mDeleteButton.setOnAction(action -> onDeleteClick());
	}
	
	/**
	 * Receives the map of groups to which the new group will be added.
	 */
	public void initializeModel(ObservableMap<UUID, CalendarGroup> map, ObservableList<Task> tasks, ObservableList<Appointment> appts){
		mGroupMap = map;
		
		// To manipulate when a group is deleted. If it's deleted, tasks/appointments of that group will be set to default group.
		mTasks = tasks;
		mAppointments = appts;
	}
	
	/**
	 * Sets the main application from which this dialog will later request a scene change;
	 * @param app
	 */
	public void setMainApp(MainApplication app){
		mMainApp = app;

		mApplyButton.setOnAction(action -> this.onApplyClick(action));
		mCancelButton.setOnAction(action -> mMainApp.displayMainRoot());
	}
	
	/**
	 * Sets the dialog to EDIT mode, to edit the group identified by 'id'.
	 * @param id
	 */
	public void setGroupID(UUID id) throws IllegalArgumentException {
		mGroup = mGroupMap.get(id);
		if(mGroup == null)
			throw new IllegalArgumentException("Received UUID for an inexistent group.");
		
		//Add the delete button.
		mButtonBox.getChildren().add(mDeleteButton);
		
		mNameField.setText(mGroup.getName());
		mColorPicker.setValue(mGroup.getColor());
	}
	
	/**
	 * Callback for when the apply button is clicked.
	 */
	private void onApplyClick(ActionEvent a){
		if(!validateInput()) return;
		
		String title = mNameField.getText();
		Color color = mColorPicker.getValue();
		
		if(mGroup == null){
			CalendarGroup cg = new CalendarGroup(title, color);
			mGroupMap.put(cg.getID(), cg);
		} else {
			mGroup.nameProperty().set(title);
			mGroup.colorProperty().set(color);
		}
		
		mMainApp.displayMainRoot();
	}
	
	/**
	 * Returns true if user input is valid.
	 */
	private boolean validateInput(){
		String title = mNameField.getText().trim();
		if(title.length() == 0){
			//TODO: Add alert
			return false;
		}
		
		return true;
	}
	
	/**
	 * Remove the group being edited from the map of groups.
	 */
	private void onDeleteClick(){
		//TODO: Request confirmation
		mGroupMap.remove(mGroup.getID());
		mMainApp.displayMainRoot();
	}
}