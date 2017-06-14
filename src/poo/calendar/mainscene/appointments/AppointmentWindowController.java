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
		
		mModel.getAppointments().forEach((uuid, appt) -> {
			this.addAppointmentView(appt);
			this.prepareAppointment(appt);
		});
		
		mModel.getAppointments().addListener((MapChangeListener.Change<? extends UUID, ? extends Appointment> change) -> {
			if(change.wasRemoved()){
				removeAppointmentView(change.getKey());
			}
			
			if(change.wasAdded()){
				this.addAppointmentView(change.getValueAdded());
				this.prepareAppointment(change.getValueAdded());
			}
		});
		
		mMainApp = app;	
		mAddButton.setOnAction(action -> {
			mMainApp.displayAppointmentDialog();
		});
		
		for(AppointmentDayPortController adpc: mDayPorts){
			adpc.initializeStructures(mMainApp, mModel);
		}
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
		
		init = (Calendar) appointment.getInitDate().clone();
		end = (Calendar) appointment.getEndDate().clone();
		
		// Gets the interval being displayed.
		Calendar begOfWeek = (Calendar) mAssignedWeek.clone();
		Calendar endOfWeek = (Calendar) mAssignedWeek.clone();
		endOfWeek.add(Calendar.DAY_OF_WEEK, 6);
		
		// Assumes Appointment does not belong to the current week.
		boolean shouldDisplay = false;
		
		/*
		 * Decide if should display or not.
		 */
		if(rec == Recurrence.DAILY || rec == Recurrence.WEEKLY || rec == Recurrence.NONE) {
			shouldDisplay = true;
			
		} else if(rec == Recurrence.MONTHLY) {
			DateUtil.translateInterval(Calendar.MONTH, begOfWeek, init, end);
			shouldDisplay = DateUtil.hasDayIntersection(begOfWeek, endOfWeek, init, end);
			
		} else if(rec == Recurrence.YEARLY) {
			DateUtil.translateInterval(Calendar.YEAR, begOfWeek, init, endOfWeek);
			shouldDisplay = DateUtil.hasDayIntersection(begOfWeek, endOfWeek, init, end);
		}
		
		if(!shouldDisplay) return; // Nothing else to do.
		
		/*
		 * If should display, add the appointment to all due ports.
		 */
		if(rec == Recurrence.DAILY) {
			 // Generate all 7 intervals and display each of them
			Calendar subjectDay = (Calendar) begOfWeek.clone();
			for(int i = 0; i < 7; i++){
				DateUtil.translateInterval(Calendar.DATE, subjectDay, init, end);
				
				addIntervalToPorts(appointment, init, end);
				
				subjectDay.add(Calendar.DATE, 1);
			}
			
		} else if(rec == Recurrence.WEEKLY) {
			DateUtil.translateInterval(Calendar.DAY_OF_WEEK, begOfWeek, init, end);
			addIntervalToPorts(appointment, init, end);
			
		} else {
			addIntervalToPorts(appointment, init, end);
		}
	}
	
	private void addIntervalToPorts(Appointment appointment, Calendar init, Calendar end){
		/*
		 * If should display, add the appointment to all due ports.
		 */
		Calendar subjectDay = (Calendar) mAssignedWeek.clone();
		for(int i = 0; i < 7; i++){
			/* FOR EACH DAY IN CURRENT WEEK */
			
			/* CALCULATE THE OFFSETS OF THE APPOINTMENT IN THE DAY BEING ANALYZED */
			if(DateUtil.hasDayIntersection(subjectDay, init, end)){
				int offset1, offset2;
				boolean containsInit, containsEnd;
				
				containsInit = DateUtil.isDayOffset(subjectDay, init, 0);
				containsEnd = DateUtil.isDayOffset(subjectDay, end, 0);
				
				/* CASE 1: subjectDay is entirely contained in [init, end] */
				if( !containsInit && !containsEnd ){
					offset1 = 0;
					offset2 = DateUtil.MINUTES_IN_DAY;
				}
				/* CASE 2: subjectDay contains 'init', but not 'end'. */
				else if( containsInit && !containsEnd ){
					offset1 = DateUtil.minuteCount(init);
					offset2 = DateUtil.MINUTES_IN_DAY;
				}
				/* CASE 3: subjectDay contains 'end', but not 'init'. */
				else if( !containsInit && containsEnd ){
					offset1 = 0;
					offset2 = DateUtil.minuteCount(end);
				}
				/* CASE 4: subjectDay contains the whole interval [init, end] */
				else {
					offset1 = DateUtil.minuteCount(init);
					offset2 = DateUtil.minuteCount(end);
				}
				
				mDayPorts.get(i).addAppointment(offset1, offset2, appointment, mModel.getRefGroup(appointment));
			}
		
			subjectDay.add(Calendar.DATE, 1);
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
	
	private void prepareAppointment(Appointment appt){
		appt.initDateProperty().addListener(action -> {
			removeAppointmentView(appt.getID());
			addAppointmentView(appt);
		});
		appt.endDateProperty().addListener(action -> {
			removeAppointmentView(appt.getID());
			addAppointmentView(appt);
		});
		appt.recurrenceProperty().addListener(action -> {
			removeAppointmentView(appt.getID());
			addAppointmentView(appt);
		});
	}
}