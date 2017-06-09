package poo.calendar.mainscene;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import poo.calendar.model.Appointment;


/**
 * Widget class that displays the calendar window.
 * It's a vertical box containing a list of appointments and 2 buttons.
 */
public class AppointmentWindowController {
	@FXML
	private AnchorPane mMainPane;
	
	@FXML
	private ScrollPane mInnerScrollPane;
	
	@FXML
	private AnchorPane mInnerAnchorPane;
	
	@FXML
	private HBox mInnerBox;
	
	@FXML
	private Button mAddButton;
	
	@FXML
	private Button mLeftButton;
	
	@FXML
	private Button mRightButton;
	
	//Model data
	private ObservableList<Appointment> mAppointmentList = null;
	
	/**
	 * Default constructor
	 */
	public AppointmentWindowController(){
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded
	 */
	@FXML
	private void initialize(){
		//TODO: Register due listeners

		mAddButton.setOnAction(a -> onAddClick(a));
	}
	
	/**
	 * Connects listeners for modifications on the the data model, so that the UI is updated
	 * accordingly.
	 * 
	 * @param list List of appointments that should be controlled by this Class
	 */
	public void initializeModel(ObservableList<Appointment> list){
		if(mAppointmentList != null){
			//TODO: Verify logging / exception
			System.err.println(this.getClass().getName() + ": Can only initialize model once.");
			System.exit(1);
		}
		
		mAppointmentList = list;

		for(Appointment a: mAppointmentList){
			this.addAppointmentView(a);
		}
		
		mAppointmentList.addListener((ListChangeListener.Change<? extends Appointment> change) -> {
			while(change.next()){
				//TODO: Handle updated/permuted appointments
				
				for(Object a: change.getRemoved()){
					System.out.println("Removed:");
					System.out.println( ((Appointment) a).getTitle());
					
					this.removeAppointmentView(((Appointment)a).getID());
				}
				for(Object a: change.getAddedSubList()){
					System.out.println("Added:");
					System.out.println( ((Appointment) a).getTitle());
					 
					this.addAppointmentView((Appointment) a);
				}
			}
		});
	}
	
	/**
	 * Adds an appointment to the UI.
	 * @param appointment Appointment to be added.
	 */
	private void addAppointmentView(Appointment appointment){
		ObservableList<Node> nodes = mInnerBox.getChildren();
		
		AppointmentView view = new AppointmentView(
				appointment.getTitle(), appointment.getInitDate(), appointment.getID()
			);
		view.setOnMouseClicked(click -> {
			AppointmentView source = (AppointmentView) click.getSource();
			if(click.getButton() == MouseButton.PRIMARY){
				this.removeAppointment(source.getID());
			}
		});
		nodes.add(view);
		FXCollections.sort(nodes, null);
	}
	
	/**
	 * Removes an appointment from the UI.
	 * @param id ID of the appointment to remove.
	 */
	private void removeAppointmentView(UUID id){
		ObservableList<Node> nodes = mInnerBox.getChildren();
		nodes.removeIf(view -> {
			return ((AppointmentView)view).getID().compareTo(id) == 0;
		});
	}
	
	/**
	 * Removes an appointment from the list of appointments
	 * @param id the ID of the appointment to be removed
	 */
	private void removeAppointment(UUID id){
		//Maybe make a model class that should handle remove/add operations
		for(Appointment a: mAppointmentList){
			if(a.getID().compareTo(id) == 0){
				mAppointmentList.remove(a);
				break;
			}
		}
	}
	
	/**
	 * Function to run whenever the 'Add' button in the appointments window is clicked.
	 * @param a
	 */
	private void onAddClick(ActionEvent a){
		DateChooserDialog dialog = new DateChooserDialog(
				"New Appointment", 
				"Set up your new appointment",
				DateChooserDialog.APPOINTMENT_DIALOG
		);

		//createButton has not been overridden in DateChooserDialog, so the return type is a Button.
		Button bt = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
				
		bt.addEventFilter(ActionEvent.ACTION, event -> {
			Map<String,String> map = dialog.getInputMap();
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			
			try {
				c1.set( Integer.parseInt(map.get("year1")),
						Integer.parseInt(map.get("month1")),
						Integer.parseInt(map.get("day1")),
						Integer.parseInt(map.get("hour1")),
						Integer.parseInt(map.get("minute1")) );
				
				c2.set( Integer.parseInt(map.get("year2")),
						Integer.parseInt(map.get("month2")),
						Integer.parseInt(map.get("day2")),
						Integer.parseInt(map.get("hour2")),
						Integer.parseInt(map.get("minute2")) );

				new Appointment("", "", c1, c2);
			} catch(NumberFormatException e){
				//Integer.parseInt failed
				dialog.alertUser("Dates must be given with integer numbers!");
				event.consume();
			} catch(IllegalArgumentException e){
				//Calendar 2 is earlier than the first one.
				dialog.alertUser("End date cannot be earlier than initial date!");
				event.consume();
			}
		});
		
		dialog.showAndWait().ifPresent(name -> {
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			
			//TODO: Validate input. Check 'duration' key in the map
			
			c1.set( Integer.parseInt(name.get("year1")),
					Integer.parseInt(name.get("month1")),
					Integer.parseInt(name.get("day1")),
					Integer.parseInt(name.get("hour1")),
					Integer.parseInt(name.get("minute1")) );
			
			c2.set( Integer.parseInt(name.get("year2")),
					Integer.parseInt(name.get("month2")),
					Integer.parseInt(name.get("day2")),
					Integer.parseInt(name.get("hour2")),
					Integer.parseInt(name.get("minute2")) );
			
			Appointment appointment = new Appointment(name.get("title"), "", c1, c2);	
			mAppointmentList.add(appointment);
		});
	}
}