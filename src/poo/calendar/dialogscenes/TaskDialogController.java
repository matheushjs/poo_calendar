package poo.calendar.dialogscenes;

import java.util.UUID;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Task;

/**
 * Class for controlling the dialog for managing tasks.
 * If not provided a task ID, dialog is run on CREATE mode.
 * Otherwise, it runs on EDIT mode, editing the task with the given ID.
 */
public class TaskDialogController {
	@FXML
	private DialogPane mMainPane;
	
	@FXML
	private Text mHeaderText;
	
	private MainApplication mMainApp;
	
	// Model data
	public ObservableMap<UUID, CalendarGroup> mGroupMap;
	public ObservableList<Task> mTasks;
	public ObservableList<Appointment> mAppointments;
	public UUID mTaskID;
	
	/**
	 * Default constructor
	 */
	public TaskDialogController(){
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
	public void initializeModel(ObservableMap<UUID, CalendarGroup> map, ObservableList<Task> tasks, ObservableList<Appointment> appts){
		mGroupMap = map;
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
		bt.setOnAction(action -> mMainApp.displayMainRoot());
	}
	
	/**
	 * Sets the dialog to EDIT mode, to edit the task identified by 'id'.
	 * @param id
	 */
	public void setTaskID(UUID id){
		mTaskID = id;
		//TODO: Place appointment info in the UI
		//TODO: configure UI to edit mode
	}
}
