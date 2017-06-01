package poo.calendar.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Class for opening a Dialog through which the user can provide data for creating a Task or an Appointment.
 * 
 * The Dialog always returns a map with keys: title, year1, month1, day1, hour1, minute1, year2, month2, day2, hour2, minute2.
 * 
 * Each value in the map is validated based solely on what it represents, not on what its future purpose is.
 * For example, the "year1" value is guaranteed to be a number; "duration" is guaranteed to be a positive number.
 * 
 * No values will be null. Even if the Dialog is created with the parameter TASK_DIALOG, the second date values will not be null.
 * 
 */
public class DateChooserDialog extends Dialog<Map<String,String>> {
	private TextField titleField;
	private Label dateLabel, dateLabel2;
	private List<TextField> dateFields, dateFields2;
	
	/** Pass this static value to this class' constructor if the Dialog should create a Task */
	public static int TASK_DIALOG = 0;
	
	/** Pass this static value to this class' constructor if the Dialog should create an Appointment */
	public static int APPOINTMENT_DIALOG = 1;
	
	/**
	 * Function required for extending the Dialog class.
	 * Converts the values in the TextFields to a returnable structure.
	 * 
	 * @param bt Type of button pressed by the user
	 * @return A Map of (string,string) pairs, each of which represents a user input.
	 */
	private Map<String,String> convertResult(ButtonType bt){
		if(bt.getButtonData() == ButtonData.CANCEL_CLOSE){
			return null;
		
		} else if(bt.getButtonData() == ButtonData.OK_DONE){
			Map<String,String> hash = new HashMap<>();
			
			//TODO: Loosely validate input
			
			hash.put("title", titleField.getText());
			
			hash.put("year1", dateFields.get(0).getText());
			hash.put("month1", dateFields.get(1).getText());
			hash.put("day1", dateFields.get(2).getText());
			hash.put("hour1", dateFields.get(3).getText());
			hash.put("minute1", dateFields.get(4).getText());
			
			hash.put("year2", dateFields2.get(0).getText());
			hash.put("month2", dateFields2.get(1).getText());
			hash.put("day2", dateFields2.get(2).getText());
			hash.put("hour2", dateFields2.get(3).getText());
			hash.put("minute2", dateFields2.get(4).getText());
			
			//TODO: Handle duration
			hash.put("duration", "0");
			
			return hash;
		
		} else {
			//TODO: Think about Logging errors
			System.err.println(this.getClass().getName() + ": Function convertResult() received invalid button.");
			return null;
		}
	}
	
	/**
	 * This Dialog class can have at most 2 horizontal boxes with fields for providing a date.
	 * This function initializes both of these horizontal boxes.
	 */
	private void initializeDateFields(){
		dateFields = new ArrayList<>(5); //YMDHM
		dateFields2 = new ArrayList<>(5);
		
		for(int i = 0; i < 5; i++){
			TextField tf = new TextField();
			TextField tf2 = new TextField();
			
			tf.setPrefWidth(50);
			tf2.setPrefWidth(50);
			
			dateFields.add(tf);
			dateFields2.add(tf2);
		}
		
		Calendar calendar = Calendar.getInstance();
		dateFields.get(0).setText("" + calendar.get(Calendar.YEAR));
		dateFields.get(1).setText("" + calendar.get(Calendar.MONTH));
		dateFields.get(2).setText("" + calendar.get(Calendar.DAY_OF_MONTH));
		dateFields.get(3).setText("" + calendar.get(Calendar.HOUR_OF_DAY));
		dateFields.get(4).setText("" + calendar.get(Calendar.MINUTE));
		
		calendar.add(Calendar.MINUTE, 30);
		dateFields2.get(0).setText("" + calendar.get(Calendar.YEAR));
		dateFields2.get(1).setText("" + calendar.get(Calendar.MONTH));
		dateFields2.get(2).setText("" + calendar.get(Calendar.DAY_OF_MONTH));
		dateFields2.get(3).setText("" + calendar.get(Calendar.HOUR_OF_DAY));
		dateFields2.get(4).setText("" + calendar.get(Calendar.MINUTE));
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param windowTitle Title for the Dialog window.
	 * @param dialogType Use the static variables TASK_DIALOG and APPOINTMENT_DIALOG to indicate
	 * the purpose of the instantiated Dialog.
	 */
	public DateChooserDialog(String windowTitle, String headerString, int dialogType){
		setTitle(windowTitle);
		setHeaderText(headerString);
		this.setResultConverter(bt -> convertResult(bt));
		
		titleField = new TextField();
		
		if(dialogType == TASK_DIALOG){
			titleField.setPromptText("Untitled Task");
			dateLabel = new Label("Deadline (HM DMY):");
			dateLabel2 = new Label("");
		} else if(dialogType == APPOINTMENT_DIALOG){
			titleField.setPromptText("Untitled Appointment");
			dateLabel = new Label("Initial Date:");
			dateLabel2 = new Label("End Date:");
		} else {
			throw new IllegalArgumentException("Invalid chooserType parameter passed.");
		}
		
		initializeDateFields();
		
		HBox hbox = new HBox(5);
		hbox.getChildren().addAll(dateFields);
		
		HBox hbox2 = new HBox(5);
		hbox2.getChildren().addAll(dateFields2);
		
		GridPane grid = new GridPane();
		grid.setVgap(5);
		
		GridPane.setConstraints(titleField, 0, 0, 2, 1);
		GridPane.setConstraints(dateLabel, 0, 1);
		GridPane.setConstraints(hbox, 1, 1);
		
		GridPane.setConstraints(dateLabel2, 0, 2);
		GridPane.setConstraints(hbox2, 1, 2);
		
		grid.getChildren().addAll(titleField, dateLabel, hbox);
		
		if(dialogType == APPOINTMENT_DIALOG)
			grid.getChildren().addAll(dateLabel2, hbox2);
		
		getDialogPane().setContent(grid);
		getDialogPane().getButtonTypes().add(new ButtonType("Close", ButtonData.CANCEL_CLOSE));
		getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonData.OK_DONE));
	}
}