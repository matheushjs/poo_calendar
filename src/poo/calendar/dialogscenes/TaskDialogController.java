package poo.calendar.dialogscenes;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.dialogscenes.utils.GroupComboBoxUtil;
import poo.calendar.dialogscenes.utils.WarningHandler;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Task;

/**
 * Class for controlling the dialog for managing tasks.
 * If not provided a task ID, dialog is run on CREATE mode.
 * Otherwise, it runs on EDIT mode, editing the task with the given ID.
 */
public class TaskDialogController {
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
	
	private WarningHandler mTitleWarning;
	private WarningHandler mDateWarning;
	
	// Model data
	private CalendarDataModel mModel;
	private Task mTask;
	
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
		mButtonBox.getChildren().remove(mDeleteButton);
		mDeleteButton.setOnAction(action -> onDeleteClick());
		
		mDateField.setOnMouseClicked(action -> onDeadlineFieldClick());
		mHourField.setOnMouseClicked(action -> onDeadlineFieldClick());
		
		mTitleWarning = new WarningHandler(mTitleBox);
		mDateWarning = new WarningHandler(mDateBox);
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
	 * Sets the dialog to EDIT mode, to edit the task identified by 'id'.
	 * @param id
	 */
	public void setTaskID(UUID id){
		mTask = mModel.getTask(id);
		
		mButtonBox.getChildren().add(mDeleteButton);
		
		mTitleField.setText(mTask.getTitle());
		mDescriptionField.setText(mTask.getDescription());
		
		if(mTask.getDeadlineDate() != null){
			mDateField.setText(DateUtil.dateString(mTask.getDeadlineDate()));
			mHourField.setText(DateUtil.hourString(mTask.getDeadlineDate()));
		}
		
		mGroupCombo.setValue(mModel.getRefGroup(mTask));
	}
	
	/**
	 * Callback function to call upon a click on the 'apply' button.
	 * @param a
	 */
	private void onApplyClick(ActionEvent a){
		if(!validateInput()) return;
		
		String title = mTitleField.getText().trim();
		String description = mDescriptionField.getText();
		
		String date = mDateField.getText().trim();
		String hour = mHourField.getText().trim();
		Calendar calendar = null;
		if(date.length() != 0 && hour.length() != 0){
			calendar = DateUtil.parseFields(date, hour);
		}
		
		CalendarGroup cg = mGroupCombo.getValue();
		if(mTask == null){
			Task task = new Task(title, description, calendar, cg.getID());
			mModel.addTask(task);
		} else {
			mTask.setTitle(title);
			mTask.setDescription(description);
			mTask.setDeadlineDate(calendar);
			mTask.setGroupID(cg.getID());
		}
		
		mMainApp.displayMainRoot();
	}
	
	/**
	 * Returns true if user input is valid
	 */
	private boolean validateInput(){
		boolean allFine = true;
		
		mTitleWarning.clear();
		mDateWarning.clear();
		
		String title = mTitleField.getText().trim();
		if(title.length() == 0){
			mTitleWarning.addWarning("Title cannot be blank");
			allFine = false;
		}
		
		String date = mDateField.getText();
		String hour = mHourField.getText();
		if(date.length() != 0 && hour.length() != 0){
			try {
				DateUtil.parseFields(date, hour);
			} catch(IllegalArgumentException e){
				mDateWarning.addWarning("Deadline Date is malformed");
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
		
		//If we are on edit mode, and Task has a deadline date, we want to disable the auto-completion too.
		if(mTask != null && mTask.getDeadlineDate() != null) return;
		
		mDeadlineWasClicked = true;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 30);
		mDateField.setText(DateUtil.dateString(calendar));
		mHourField.setText(DateUtil.hourString(calendar));
	}
	
	/**
	 * Removes the task being edited from the list of tasks.
	 */
	private void onDeleteClick(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Request");
		alert.setHeaderText("Confirm Deletion Request");
		alert.setContentText("Do you really wish to delete this Task?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			if(mTask != null)
				mModel.removeTask(mTask.getID());
			mMainApp.displayMainRoot();
		} else {
		    return;
		}
	}
}
