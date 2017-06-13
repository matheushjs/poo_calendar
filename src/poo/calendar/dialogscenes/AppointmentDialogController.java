package poo.calendar.dialogscenes;

import java.util.Calendar;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.dialogscenes.utils.GroupComboBoxUtil;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Recurrence;


/**
 * Class for controlling the dialog for managing appointments.
 * If not provided an appointment ID, the dialog is run on CREATE mode.
 * If provided an ID, run on EDIT mode.
 */
public class AppointmentDialogController {
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
	private CalendarDataModel mModel;
	private Appointment mAppointment;
	
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
		
		mButtonBox.getChildren().remove(mDeleteButton);
		mDeleteButton.setOnAction(action -> onDeleteClick());
		
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
	public void initializeModel(CalendarDataModel model){
		mModel = model;
		
		mGroupCombo.getItems().addAll(mModel.getGroups().values());
		mGroupCombo.setCellFactory(GroupComboBoxUtil.getAddCallback());
		mGroupCombo.setValue(CalendarGroup.DEFAULT_GROUP);
	}
	
	/**
	 * Sets the main application from which this dialog will later request a scene change;
	 * @param app
	 */
	public void setMainApp(MainApplication app){
		mMainApp = app;

		mCancelButton.setOnAction(action -> mMainApp.displayMainRoot());
		mApplyButton.setOnAction(action -> onApplyClick(action));
	}
	
	/**
	 * Sets the dialog to EDIT mode, to edit the appointment identified by 'id'.
	 * @param id
	 */
	public void setAppointmentID(UUID id){
		mAppointment = mModel.getAppointment(id);

		mButtonBox.getChildren().add(mDeleteButton);
		
		mTitleField.setText(mAppointment.getTitle());
		mDescriptionField.setText(mAppointment.getDescription());
		mDateField1.setText(DateUtil.dateString(mAppointment.getInitDate()));
		mDateField2.setText(DateUtil.dateString(mAppointment.getEndDate()));
		mHourField1.setText(DateUtil.hourString(mAppointment.getInitDate()));
		mHourField2.setText(DateUtil.hourString(mAppointment.getEndDate()));
		
		mGroupCombo.setValue(mModel.getRefGroup(mAppointment));
		
		String recString = mAppointment.getRecurrence().toString().toLowerCase();
		recString = recString.substring(0, 1).toUpperCase() + recString.substring(1);
		mRecurrenceChoice.setValue(recString);
	}
	
	/**
	 * Callback for when the apply button is clicked
	 */
	private void onApplyClick(ActionEvent a){
		if(!validateInput()) return;
		
		String title = mTitleField.getText().trim();
		String description = mDescriptionField.getText();
		
		String date = mDateField1.getText();
		String hour = mHourField1.getText();
		Calendar initDate = DateUtil.parseFields(date, hour);
		
		date = mDateField2.getText();
		hour = mHourField2.getText();
		Calendar endDate = DateUtil.parseFields(date, hour);
		
		CalendarGroup cg = mGroupCombo.getValue();
		Recurrence rc = Recurrence.valueOf(mRecurrenceChoice.getValue().toUpperCase());
		
		Appointment appointment = new Appointment(title, description, initDate, endDate, cg.getID());
		appointment.setRecurrence(rc);
		
		mModel.addAppointment(appointment);
		
		mMainApp.displayMainRoot();
	}
	
	/**
	 * Returns true if user input is valid.
	 */
	private boolean validateInput(){
		boolean allFine = true;
		
		String title = mTitleField.getText().trim();
		if(title.length() == 0){
			//TODO: Add alert
			allFine = false;
		}
		
		String date = mDateField1.getText();
		String hour = mHourField1.getText();
		Calendar initDate = null;
		try {
			initDate = DateUtil.parseFields(date, hour);
		} catch(IllegalArgumentException e){
			//TODO: Add alert
			allFine = false;
		}
		
		date = mDateField2.getText();
		hour = mHourField2.getText();
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
	
	/**
	 * Removes appointment being edited from the appointments list.
	 */
	private void onDeleteClick(){
		//TODO: Request confirmation
		if(mAppointment != null){
			mModel.removeAppointment(mAppointment.getID());
		}
		mMainApp.displayMainRoot();
	}
}
