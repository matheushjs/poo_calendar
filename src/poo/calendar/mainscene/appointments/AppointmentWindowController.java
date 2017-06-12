package poo.calendar.mainscene.appointments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;


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
	private HBox mInnerBox;
	
	@FXML
	private Button mAddButton;
	
	@FXML
	private Button mLeftButton;
	
	@FXML
	private Button mRightButton;
	
	private MainApplication mMainApp;
	private ArrayList<AppointmentDayPortController> mDayPort;
	private Calendar mAssignedWeek;
	
	//Model data
	private ObservableList<Appointment> mAppointmentList;
	private ObservableMap<UUID, CalendarGroup> mGroupMap;
	
	/**
	 * Default constructor
	 */
	public AppointmentWindowController(){
		mDayPort = new ArrayList<>();
		
		mAssignedWeek = Calendar.getInstance();
		DateUtil.resetToWeek(mAssignedWeek);
		
		Calendar auxCalendar = (Calendar) mAssignedWeek.clone();
		for(int i = 0; i < 7; i++){
			AppointmentDayPortController adpc = new AppointmentDayPortController(auxCalendar);
			adpc.initializeModel(mGroupMap);
			//TODO: All initializeModel functions, remove them and do their task in constructor.
			mDayPort.add(adpc);
			
			auxCalendar.add(Calendar.DAY_OF_WEEK, 1);
		}
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded
	 */
	@FXML
	private void initialize(){
		//TODO: Register due listeners
		for(AppointmentDayPortController adpc: mDayPort){
			AnchorPane widget = adpc.getWidget();
			widget.prefWidthProperty().bind(mInnerBox.widthProperty().divide(7.0));
			widget.maxWidthProperty().bind(mInnerBox.widthProperty().divide(7.0));
			mInnerBox.getChildren().add(widget);
		}
		mInnerBox.prefWidthProperty().bind(mInnerScrollPane.widthProperty());
		mInnerBox.maxWidthProperty().bind(mInnerScrollPane.widthProperty());
	}
	
	/**
	 * Connects listeners for modifications on the the data model, so that the UI is updated
	 * accordingly.
	 * 
	 * @param list List of appointments that should be controlled by this Class
	 */
	public void initializeModel(ObservableList<Appointment> list, ObservableMap<UUID, CalendarGroup> map){
		if(mAppointmentList != null){
			//TODO: Verify logging / exception
			System.err.println(this.getClass().getName() + ": Can only initialize model once.");
			System.exit(1);
		}
		
		mAppointmentList = list;
		mGroupMap = map;

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
		Calendar init, end;
		init = appointment.getInitDate();
		end = appointment.getEndDate();
		
		int base = mAssignedWeek.get(Calendar.YEAR);
		int low = init.get(Calendar.YEAR);
		int high = end.get(Calendar.YEAR);
		if(low > base || high < base) return;
		
		base = mAssignedWeek.get(Calendar.MONTH);
		low = init.get(Calendar.MONTH);
		if(low > base) return;

		//Gets calendar for last day of the week being displayed.
		Calendar endOfWeek = (Calendar) mAssignedWeek.clone();
		endOfWeek.add(Calendar.DAY_OF_WEEK, 6);
		
		base = endOfWeek.get(Calendar.MONTH);
		high = end.get(Calendar.MONTH);
		if(high < base) return;

		base = mAssignedWeek.get(Calendar.DATE);
		high = end.get(Calendar.DATE);
		if(high < base) return;

		base = endOfWeek.get(Calendar.DATE);
		low = init.get(Calendar.DATE);
		if(low > base) return;

		int day1 = low;
		int day2 = high;
		boolean control = false;
		for(AppointmentDayPortController adpc: mDayPort){
			if(day1 == adpc.getAssignedDay())
				control = true;
			
			if(control)
				adpc.addAppointment(appointment, mGroupMap.get(appointment.getGroupID()));
			
			if(day2 == adpc.getAssignedDay())
				control = false;
		}
		
		//TODO: add listeners to Appointment calendar properties
	}
	
	/**
	 * Removes an appointment from the UI.
	 * @param id ID of the appointment to remove.
	 */
	private void removeAppointmentView(UUID id){
		/*
		nodes.removeIf(view -> {
			return ((AppointmentView)view).getID().compareTo(id) == 0;
		});
		*/
	}
	
	/**
	 * Removes an appointment from the list of appointments
	 * @param id the ID of the appointment to be removed
	 */
	private void removeAppointment(UUID id){
		/*
		//Maybe make a model class that should handle remove/add operations
		for(Appointment a: mAppointmentList){
			if(a.getID().compareTo(id) == 0){
				mAppointmentList.remove(a);
				break;
			}
		}
		*/
	}
	
	/**
	 * Sets the main application from which this widget will later request a scene change;
	 * @param app
	 */
	public void setMainApp(MainApplication app){
		mMainApp = app;
		
		mAddButton.setOnAction(action -> {
			mMainApp.displayAppointmentDialog();
		});
		
		for(AppointmentDayPortController adpc: mDayPort)
			adpc.setMainApp(mMainApp);
	}
}