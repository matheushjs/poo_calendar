package poo.calendar.mainscene.appointments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.Recurrence;


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
	private AnchorPane mInnerPane;
	
	@FXML
	private HBox mInnerBox;
	
	@FXML
	private Button mAddButton;
	
	@FXML
	private Button mLeftButton;
	
	@FXML
	private Button mRightButton;
	
	private MainApplication mMainApp;
	private ArrayList<AppointmentDayPortController> mDayPorts;
	private Calendar mAssignedWeek;
	
	//Model data
	private CalendarDataModel mModel;
	
	/**
	 * Default constructor
	 */
	public AppointmentWindowController(){
		mDayPorts = new ArrayList<>();
		
		mAssignedWeek = Calendar.getInstance();
		DateUtil.resetToWeek(mAssignedWeek);
		
		Calendar auxCalendar = (Calendar) mAssignedWeek.clone();
		for(int i = 0; i < 7; i++){
			AppointmentDayPortController adpc = new AppointmentDayPortController(auxCalendar);
			mDayPorts.add(adpc);
			
			auxCalendar.add(Calendar.DAY_OF_WEEK, 1);
		}
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded
	 */
	@FXML
	private void initialize(){
		//TODO: Register due listeners
		for(AppointmentDayPortController adpc: mDayPorts){
			AnchorPane widget = adpc.getWidget();
			widget.prefWidthProperty().bind(mInnerBox.widthProperty().divide(7.0));
			widget.maxWidthProperty().bind(mInnerBox.widthProperty().divide(7.0));
			mInnerBox.getChildren().add(widget);
		}
		mInnerBox.prefWidthProperty().bind(mInnerScrollPane.widthProperty());
		mInnerBox.maxWidthProperty().bind(mInnerScrollPane.widthProperty());
	}
	
	/**
	 * Receives the structures needed for working.
	 */
	public void initializeStructures(MainApplication app, CalendarDataModel model){
		if(mModel != null){
			//TODO: Verify logging / exception
			System.err.println(this.getClass().getName() + ": Can only initialize model once.");
			System.exit(1);
		}
		
		mModel = model;
		
		// Initialize model for each DayPort
		for(AppointmentDayPortController adpc: mDayPorts)
			adpc.initializeModel(mModel);
		
		mModel.getAppointments().forEach((uuid, appt) -> {
			this.addAppointmentView(appt);
			//TODO: Register listeners to the appointment's calendar properties
		});
		
		mModel.getAppointments().addListener((MapChangeListener.Change<? extends UUID, ? extends Appointment> change) -> {
			if(change.wasRemoved()){
				removeAppointmentView(change.getKey());
			}
			
			if(change.wasAdded()){
				this.addAppointmentView(change.getValueAdded());
				//TODO: Register listeners to the appointment's calendar properties
			}
		});
		
		mMainApp = app;	
		mAddButton.setOnAction(action -> {
			mMainApp.displayAppointmentDialog();
		});
		for(AppointmentDayPortController adpc: mDayPorts)
			adpc.setMainApp(mMainApp);
	}
	
	/**
	 * Adds an appointment to the UI. The appointment can be any appointment,
	 * with any initDate or endDate. This method only those that belong to the
	 * current week being displayed.
	 * @param appointment Appointment to be added.
	 */
	private void addAppointmentView(Appointment appointment){
		Calendar init, end;
		Recurrence rec = appointment.getRecurrence();
		
		init = appointment.getInitDate();
		end = appointment.getEndDate();
		
		//Gets calendar for last day of the week being displayed.
		Calendar endOfWeek = (Calendar) mAssignedWeek.clone();
		endOfWeek.add(Calendar.DAY_OF_WEEK, 6);
		
		int base1, base2;
		int low, high;
		
		/*
		 * Verifies if given appointment should be displayed anywhere in the current week
		 * being displayed.
		 */
		if(rec != Recurrence.DAILY && rec != Recurrence.WEEKLY){
			// Verify week intersection
			base1 = mAssignedWeek.get(Calendar.DATE);
			base2 = endOfWeek.get(Calendar.DATE);
			low = init.get(Calendar.DATE);
			high = end.get(Calendar.DATE);
			if(low > base2 || high < base1) return;
			
			// If recurrence is not daily nor weekly, we need to check month intersection
			if(rec != Recurrence.MONTHLY){
				// Verify month intersection
				base1 = mAssignedWeek.get(Calendar.MONTH);
				base2 = endOfWeek.get(Calendar.MONTH);
				low = init.get(Calendar.MONTH);
				high = end.get(Calendar.MONTH);
				if(low > base2 || high < base1) return;
				
				// If recurrence is not daily/weekly/monthly, we need to check year intersection
				if(rec != Recurrence.YEARLY){
					// Verify year intersection
					base1 = mAssignedWeek.get(Calendar.YEAR);
					base2 = endOfWeek.get(Calendar.YEAR);
					low = init.get(Calendar.YEAR);
					high = end.get(Calendar.YEAR);
					if(low > base2 || high < base1) return;
				}
			}
		}
		
		// Get comparison keys for each edge date of the appointment
		int day1 = init.get(Calendar.DATE);
		int day2 = end.get(Calendar.DATE);
		
		// If recurrence is weekly, shift the comparison keys to the corresponding weekday of the week being displayed
		if(rec == Recurrence.WEEKLY){
			int delta = day2 - day1; //TODO: Solve problem when appointment spans for more than 1 month.
			int weekday = init.get(Calendar.DAY_OF_WEEK);
			
			Calendar newInit = (Calendar) mAssignedWeek.clone();
			newInit.set(Calendar.DAY_OF_WEEK, weekday);
			
			Calendar newEnd = (Calendar) newInit.clone();
			newEnd.add(Calendar.DAY_OF_MONTH, delta);

			day1 = newInit.get(Calendar.DAY_OF_MONTH);
			day2 = newEnd.get(Calendar.DAY_OF_MONTH);
		}
		
		boolean control = false;
		for(AppointmentDayPortController adpc: mDayPorts){
			if(day1 == adpc.getAssignedDay())
				control = true;
			
			if(control || rec == Recurrence.DAILY)
				adpc.addAppointment(appointment, mModel.getRefGroup(appointment));
			
			if(day2 == adpc.getAssignedDay())
				control = false;
		}
	}
	
	/**
	 * Removes an appointment from the list of appointments
	 * @param id the ID of the appointment to be removed
	 */
	private void removeAppointmentView(UUID id){
		for(AppointmentDayPortController adpc: mDayPorts){
			adpc.removeAppointmentView(id);
		}
	}
}