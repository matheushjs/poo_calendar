package poo.calendar.dialogscenes;

import java.util.Calendar;
import java.util.UUID;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
	
	private Label mTitleWarningLabel;
	private Label mDateWarningLabel;
	private Border mWarningBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null));
	
	// Model data
	private CalendarDataModel mModel;
	private Task mTask;
	
	/**
	 * Default constructor
	 */
	public TaskDialogController(){
		mDeadlineWasClicked = false;
		
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
		mButtonBox.getChildren().remove(mDeleteButton);
		mDeleteButton.setOnAction(action -> onDeleteClick());
		
		mDateField.setOnMouseClicked(action -> onDeadlineFieldClick());
		mHourField.setOnMouseClicked(action -> onDeadlineFieldClick());
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
		
		clearDateWarning();
		clearTitleWarning();
		
		String title = mTitleField.getText().trim();
		if(title.length() == 0){
			addTitleWarning("Title cannot be blank");
			allFine = false;
		}
		
		String date = mDateField.getText();
		String hour = mHourField.getText();
		if(date.length() != 0 && hour.length() != 0){
			try {
				DateUtil.parseFields(date, hour);
			} catch(IllegalArgumentException e){
				addDateWarning("Deadline Date is malformed");
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
		
		//TODO: Make fields start greyish, then make them white upon clicking
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 30);
		mDateField.setText(DateUtil.dateString(calendar));
		mHourField.setText(DateUtil.hourString(calendar));
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
	 * Removes the task being edited from the list of tasks.
	 */
	private void onDeleteClick(){
		//TODO: Request confirmation
		if(mTask != null)
			mModel.removeTask(mTask.getID());
		mMainApp.displayMainRoot();
	}
}
