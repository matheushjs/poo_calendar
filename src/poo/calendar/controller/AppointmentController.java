package poo.calendar.controller;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import poo.calendar.model.Appointment;
import poo.calendar.view.AppointmentView;
import poo.calendar.view.AppointmentWindow;
import poo.calendar.view.DateChooserDialog;

/**
 * Singleton Class for controlling the Appointments Window.
 * Should not call any method whose purpose is stylization of the GUI (e.g. setAlignment etc).
 *
 * Regarding the decision on how to handle views and controllers.
 * Controlled widgets - ones that have function associated with interacting with them - will have a
 * controller class associated with them. These widgets should be manufactured by the controller class,
 * where their events are connected to due listeners, and then they are stylized within the view class that
 * instantiates this controlled widget (AppointmentWindow instantiates AppointmentListView, so AppointmentWindow
 * should instantiate an AppointmentListView through a call to whatever class is the controller for
 * AppointmentListView,for example).
 * 
 * The controller should have its model data initialized before any widget is instantiated through it.
 * 
 * If a widget is expected to have only 1 live instance, the controller may hold a 'lock' boolean variable to prevent
 * multiple instantiations. The controller may hold references to the widgets it controls.
 */
public final class AppointmentController {
	private static AppointmentController mInstance = null;
	
	// Widgets
	private AppointmentWindow mAW = null;
	private ToggleButton mDeleteButton;
	
	// Model data
	private ObservableList<Appointment> mAppointmentList = null;
	
	// Prevent construction
	private AppointmentController(){}
	
	/**
	 * @return The class's singleton instance
	 */
	public static AppointmentController getInstance(){
		if(mInstance == null){
			mInstance = new AppointmentController();
		}
		return mInstance;
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
		ObservableList<Node> nodes = mAW.getAppointmentListView().getChildren();
		
		AppointmentView view = new AppointmentView(
				appointment.getTitle(), appointment.getInitDate(), appointment.getID()
			);
		view.setOnMouseClicked(click -> {
			AppointmentView source = (AppointmentView) click.getSource();
			if(mDeleteButton.isSelected() && click.getButton() == MouseButton.PRIMARY){
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
		ObservableList<Node> nodes = mAW.getAppointmentListView().getChildren();
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
	 * @return the AppointmentWindow controlled by this class
	 */
	public AppointmentWindow getAppointmentWindow(){
		if(mAppointmentList == null){
			System.err.println(this.getClass().getName()
					+ ": Cannot instantiate a view class if model"
					+ " has not been initialized");
		}
		
		if(mAW == null){
			mAW = new AppointmentWindow();
			
			//First time initializing the window, so the model list might have
			//appointments that haven't been added to the UI (appointments that were
			//save to a file)
			for(Appointment a: mAppointmentList){
				this.addAppointmentView(a);
			}
		}
		
		mAW.getAddButton().setOnAction(a -> onAddClick(a));
		mDeleteButton = mAW.getDeleteButton();
		
		return mAW;
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
