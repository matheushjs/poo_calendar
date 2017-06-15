package poo.calendar.dialogscenes;

import java.util.UUID;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
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
import poo.calendar.controller.MainApplication;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.CalendarGroup;

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
	private HBox mNameBox;
	
	@FXML
	private TextField mNameField;
	
	@FXML
	private ColorPicker mColorPicker;
	
	private MainApplication mMainApp;
	
	private Label mNameWarningLabel;
	private Border mWarningBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null));
	
	// Model Data
	private CalendarDataModel mModel;
	private CalendarGroup mGroup;
	
	/**
	 * Default constructor
	 */
	public GroupDialogController() {
		mNameWarningLabel = new Label("");
		mNameWarningLabel.setFont(Font.font(10));
		mNameWarningLabel.setTextFill(Paint.valueOf("red"));
		mNameWarningLabel.setWrapText(true);
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
	 * Receives the structures needed for working.
	 */
	public void initializeStructures(MainApplication app, CalendarDataModel model){
		mModel = model;
		
		mMainApp = app;
		mApplyButton.setOnAction(action -> this.onApplyClick(action));
		mCancelButton.setOnAction(action -> mMainApp.displayMainRoot());
	}
	
	/**
	 * Sets the dialog to EDIT mode, to edit the group identified by 'id'.
	 * @param id
	 */
	public void setGroupID(UUID id) throws IllegalArgumentException {
		mGroup = mModel.getGroup(id);
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
			mModel.addGroup(cg);
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
		clearNameWarning();
		
		String title = mNameField.getText().trim();
		if(title.length() == 0){
			addNameWarning("Name cannot be blank");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Adds the given string to the list of warnings that appears beside 
	 * the Name form Box.
	 * @param warning
	 */
	private void addNameWarning(String warning){
		String old = mNameWarningLabel.getText();
		mNameWarningLabel.setText(old + "\n * " + warning);
		
		ObservableList<Node> list = mNameBox.getChildren();
		if(!list.contains(mNameWarningLabel)){
			list.add(mNameWarningLabel);
			mNameBox.setBorder(mWarningBorder);
		}
	}
	
	private void clearNameWarning(){
		mNameWarningLabel.setText("");
		mNameBox.getChildren().remove(mNameWarningLabel);
		mNameBox.setBorder(Border.EMPTY);
	}
	
	/**
	 * Remove the group being edited from the map of groups.
	 */
	private void onDeleteClick(){
		//TODO: Request confirmation
		if(mGroup != null)
			mModel.removeGroup(mGroup.getID());
		mMainApp.displayMainRoot();
	}
}