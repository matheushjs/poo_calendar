package poo.calendar.dialogscenes;

import java.util.UUID;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
	private DialogPane mMainPane;
	
	@FXML
	private Text mHeaderText;
	
	@FXML
	private VBox mFormBox;
	
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
		
		Button bt = (Button) mMainPane.lookupButton(ButtonType.APPLY);
		bt.setOnAction(action -> this.onApplyClick(action));
		
		Button bt2 = (Button) mMainPane.lookupButton(ButtonType.CANCEL);
		bt2.setOnAction(action -> mMainApp.displayMainRoot());
	}
	
	/**
	 * Sets the dialog to EDIT mode, to edit the group identified by 'id'.
	 * @param id
	 */
	public void setGroupID(UUID id) throws IllegalArgumentException {
		mGroup = mGroupMap.get(id);
		if(mGroup == null)
			throw new IllegalArgumentException("Received UUID for an inexistent group.");
		
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
		return true; //No validation to do yet
	}
}