package poo.calendar.dialogscenes;

import java.util.Calendar;
import java.util.UUID;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
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
	private HBox mTitleBox;
	
	@FXML
	private HBox mDateBox;
	
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
	
	private Label mTitleWarningLabel;
	private Label mDateWarningLabel;
	private Border mWarningBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null));
	
	private MainApplication mMainApp;
	
	// Model data
	private CalendarDataModel mModel;
	private Appointment mAppointment;
	
	/**
	 * Default constructor
	 */
	public AppointmentDialogController(){
		mTitleWarningLabel = new Label("");
		mTitleWarningLabel.setFont(Font.font(10));
		mTitleWarningLabel.setTextFill(Paint.valueOf("red"));
		mTitleWarningLabel.setWrapText(true);
		
		mDateWarningLabel = new Label("");
		mDateWarningLabel.setFont(Font.font(10));
		mDateWarningLabel.setTextFill(Paint.valueOf("red"));
		mDateWarningLabel.setWrapText(true);
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
	 * Receives the structures needed for working.
	 */
	public void initializeStructures(MainApplication app, CalendarDataModel model){
		mModel = model;
		mGroupCombo.getItems().addAll(mModel.getGroups().values());
		mGroupCombo.setCellFactory(GroupComboBoxUtil.getAddCallback());
		mGroupCombo.setValue(CalendarGroup.DEFAULT_GROUP);
	
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
		
		if(mAppointment == null){
			Appointment appointment = new Appointment(title, description, initDate, endDate, cg.getID());
			appointment.setRecurrence(rc);
			mModel.addAppointment(appointment);
		} else {
			mAppointment.setTitle(title);
			mAppointment.setDescription(description);
			mAppointment.setDates(initDate, endDate);
			mAppointment.setGroupID(cg.getID());
			mAppointment.setRecurrence(rc);
		}
		
		mMainApp.displayMainRoot();
	}
	
	/**
	 * Returns true if user input is valid.
	 */
	private boolean validateInput(){
		boolean allFine = true;
		
		clearTitleWarning();
		clearDateWarning();
		
		String title = mTitleField.getText().trim();
		if(title.length() == 0){
			addTitleWarning("Title cannot be blank");
			allFine = false;
		}
		
		String date = mDateField1.getText();
		String hour = mHourField1.getText();
		Calendar initDate = null;
		try {
			initDate = DateUtil.parseFields(date, hour);
		} catch(IllegalArgumentException e){
			addDateWarning("Initial Date is malformed");
			allFine = false;
		}
		
		date = mDateField2.getText();
		hour = mHourField2.getText();
		Calendar endDate = null;
		try {
			endDate = DateUtil.parseFields(date, hour);
		} catch(IllegalArgumentException e){
			addDateWarning("End Date is malformed");
			allFine = false;
		}
		
		if(endDate != null && initDate != null){
			try {
				new Appointment("", "", initDate, endDate);
			} catch (IllegalArgumentException e){
				addDateWarning("End Date cannot be earlier than Initial Date");
				allFine = false;
			}
		}

		return allFine;
	}
	
	/**
	 * Adds the given string to the list of warnings that appears beside 
	 * the Title form Box.
	 * @param warning
	 */
	private void addTitleWarning(String warning){
		String old = mTitleWarningLabel.getText();
		mTitleWarningLabel.setText(old + "\n * " + warning);
		
		ObservableList<Node> list = mTitleBox.getChildren();
		if(!list.contains(mTitleWarningLabel)){
			list.add(mTitleWarningLabel);
			mTitleBox.setBorder(mWarningBorder);
		}
	}
	
	private void clearTitleWarning(){
		mTitleWarningLabel.setText("");
		mTitleBox.getChildren().remove(mTitleWarningLabel);
		mTitleBox.setBorder(Border.EMPTY);
	}
	
	/**
	 * Adds the given string to the list of warnings that appears beside 
	 * the date form Box.
	 * @param warning
	 */
	private void addDateWarning(String warning){
		String old = mDateWarningLabel.getText();
		mDateWarningLabel.setText(old + "\n * " + warning);
		
		ObservableList<Node> list = mDateBox.getChildren();
		if(!list.contains(mDateWarningLabel)){
			list.add(mDateWarningLabel);
			mDateBox.setBorder(mWarningBorder);
		}
	}
	
	private void clearDateWarning(){
		mDateWarningLabel.setText("");
		mDateBox.getChildren().remove(mDateWarningLabel);
		mDateBox.setBorder(Border.EMPTY);
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
