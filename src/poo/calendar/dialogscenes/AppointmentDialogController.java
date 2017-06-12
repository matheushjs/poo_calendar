package poo.calendar.dialogscenes;

import java.util.Calendar;
import java.util.UUID;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.dialogscenes.utils.GroupComboBoxUtil;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Recurrence;
import poo.calendar.model.Task;


/**
 * Class for controlling the dialog for managing appointments.
 * If not provided an appointment ID, the dialog is run on CREATE mode.
 * If provided an ID, run on EDIT mode.
 */
public class AppointmentDialogController {
	@FXML
	private DialogPane mMainPane;
	
	@FXML
	private Text mHeaderText;
	
	@FXML
	private TextField mTitleField;
	
	@FXML
	private TextField mDateField1;
	
	@FXML
	private TextField mHourField1;
	
	@FXML
	private TextField mDateField2;
	
	@FXML
	private TextField mHourField2;
	
	@FXML
	private TextField mDescriptionField;
	
	@FXML
	private ComboBox<CalendarGroup> mGroupCombo;
	
	@FXML
	private ChoiceBox<String> mRecurrenceChoice;
	
	private MainApplication mMainApp;
	
	// Model data
	public ObservableMap<UUID, CalendarGroup> mGroupMap;
	public ObservableList<Task> mTasks;
	public ObservableList<Appointment> mAppointments;
	public UUID mAppointmentID;
	
	/**
	 * Default constructor
	 */
	public AppointmentDialogController(){
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded.
	 */
	@FXML
	private void initialize(){
		mRecurrenceChoice.getItems().addAll("None", "Daily", "Weekly", "Monthly", "Yearly");
		mRecurrenceChoice.setValue("None");
		
		Calendar calendar = Calendar.getInstance();
		mDateField1.setText(DateUtil.dateString(calendar));
		mHourField1.setText(DateUtil.hourString(calendar));
		
		calendar.add(Calendar.MINUTE, 30);
		mDateField2.setText(DateUtil.dateString(calendar));
		mHourField2.setText(DateUtil.hourString(calendar));
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
	 * Sets the dialog to EDIT mode, to edit the appointment identified by 'id'.
	 * @param id
	 */
	public void setAppointmentID(UUID id){
		mAppointmentID = id;
		//TODO: Place appointment info in the UI
		//TODO: configure UI to edit mode
	}
	
	/**
	 * Callback for when the apply button is clicked
	 */
	private void onApplyClick(ActionEvent a){
		if(!validateInput()) return;
		
		String title = mTitleField.getText();
		String description = mDescriptionField.getText();
		
		String date = mDateField1.getText().trim();
		String hour = mHourField1.getText().trim();
		Calendar initDate = DateUtil.parseFields(date, hour);
		
		date = mDateField2.getText().trim();
		hour = mHourField2.getText().trim();
		Calendar endDate = DateUtil.parseFields(date, hour);
		
		CalendarGroup cg = mGroupCombo.getValue();
		Recurrence rc = Recurrence.valueOf(mRecurrenceChoice.getValue().toUpperCase());
		
		Appointment appointment = new Appointment(title, description, initDate, endDate, cg.getID());
		appointment.setRecurrence(rc);
		mAppointments.add(appointment);
		
		mMainApp.displayMainRoot();
	}
	
	/**
	 * Returns true if user input is valid.
	 */
	private boolean validateInput(){
		boolean allFine = true;
		
		String title = mTitleField.getText();
		if(title.length() == 0){
			//TODO: Add alert
			allFine = false;
		}
		
		String date = mDateField1.getText().trim();
		String hour = mHourField1.getText().trim();
		Calendar initDate = null;
		try {
			initDate = DateUtil.parseFields(date, hour);
		} catch(IllegalArgumentException e){
			//TODO: Add alert
			allFine = false;
		}
		
		date = mDateField2.getText().trim();
		hour = mHourField2.getText().trim();
		Calendar endDate = null;
		try {
			endDate = DateUtil.parseFields(date, hour);
		} catch(IllegalArgumentException e){
			//TODO: Add alert
			allFine = false;
		}
		
		try {
			new Appointment("", "", initDate, endDate);
		} catch (IllegalArgumentException e){
			//TODO: Add alert
			allFine = false;
		}

		return allFine;
	}
}
