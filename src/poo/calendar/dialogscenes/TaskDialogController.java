package poo.calendar.dialogscenes;

import java.util.Calendar;
import java.util.UUID;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.dialogscenes.utils.GroupComboBoxUtil;
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

	@FXML
	private TextField mTitleField;
	
	@FXML
	private TextField mDateField;
	
	@FXML
	private TextField mHourField;
	
	// Stores whether the + button has been clicked.
	private boolean mDeadlineWasClicked;

	@FXML
	private TextField mDescriptionField;
	
	@FXML
	private ComboBox<CalendarGroup> mGroupCombo;
	
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
		mDeadlineWasClicked = false;
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded.
	 */
	@FXML
	private void initialize(){
		//ŦODO: Connect due signals
		
		mDateField.setOnMouseClicked(action -> onDeadlineFieldClick());
		mHourField.setOnMouseClicked(action -> onDeadlineFieldClick());
	}
	
	/**
	 * Receives the map of groups to which the new group will be added.
	 */
	public void initializeModel(ObservableMap<UUID, CalendarGroup> map, ObservableList<Task> tasks, ObservableList<Appointment> appts){
		mGroupMap = map;
		mTasks = tasks;
		mAppointments = appts;
		
		mGroupCombo.getItems().addAll(mGroupMap.values());
		mGroupCombo.setCellFactory(GroupComboBoxUtil.getAddCallback());
		mGroupCombo.setValue(CalendarGroup.DEFAULT_GROUP);
	}
	
	/**
	 * Sets the main application from which this dialog will later request a scene change;
	 * @param app
	 */
	public void setMainApp(MainApplication app){
		mMainApp = app;
		
		Button bt = (Button) mMainPane.lookupButton(ButtonType.CANCEL);
		bt.setOnAction(action -> mMainApp.displayMainRoot());
		
		bt = (Button) mMainPane.lookupButton(ButtonType.APPLY);
		bt.setOnAction(action -> onApplyClick(action));
	}
	
	/**
	 * Sets the dialog to EDIT mode, to edit the task identified by 'id'.
	 * @param id
	 */
	public void setTaskID(UUID id){
		mTaskID = id;
		//TODO: Place task info in the UI
		//TODO: configure UI to edit mode
	}
	
	private void onApplyClick(ActionEvent a){
		if(!validateInput()) return;
		
		String title = mTitleField.getText();
		String description = mDescriptionField.getText();
		
		String date = mDateField.getText().trim();
		String hour = mHourField.getText().trim();
		Calendar calendar = null;
		if(date.length() != 0 && hour.length() != 0){
			calendar = DateUtil.parseFields(date, hour);
		}
		
		CalendarGroup cg = mGroupCombo.getValue();
		
		Task task = new Task(title, description, calendar, cg.getID());
		mTasks.add(task);
		
		mMainApp.displayMainRoot();
	}
	
	/**
	 * Returns true if user input is valid
	 */
	private boolean validateInput(){
		boolean allFine = true;
		
		String title = mTitleField.getText();
		if(title.length() == 0){
			//TODO: Add alert
			allFine = false;
		}
		
		String date = mDateField.getText().trim();
		String hour = mHourField.getText().trim();
		if(date.length() != 0 && hour.length() != 0){
			try {
				DateUtil.parseFields(date, hour);
			} catch(IllegalArgumentException e){
				//TODO: Add alert
				allFine = false;
			}
		}

		return allFine;
	}
	
	/**
	 * Callback for when the user clicks any Deadline text field.
	 * On first click, will fill the deadline fields with some valid values.
	 */
	private void onDeadlineFieldClick(){
		if(mDeadlineWasClicked) return;
		mDeadlineWasClicked = true;
		
		//TODO: Make fields start greyish, then make them white upon clicking
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 30);
		mDateField.setText(DateUtil.dateString(calendar));
		mHourField.setText(DateUtil.hourString(calendar));

	}
}
