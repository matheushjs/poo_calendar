package poo.calendar.mainscene.appointments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import poo.calendar.ControlledWidget;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.CalendarGroup;

/**
 * Class for controlling the appointment day port, where all appointments for a given day are displayed.
 */
public class AppointmentDayPortController extends ControlledWidget<AnchorPane> {
	// Height of the widget. The widget will have this size as a strong limit,
	// meaning it won't expand nor shrink.
	private static int PORT_HEIGHT = 2016; //Divisible by 96
	
	// Main Node
	private AnchorPane mMainPane;
	
	// List containing 96 other lists.
	// Each of the 96 lists represent an interval of 15 minutes within the DayPort.
	// Each interval list contains UUIDs of the Appointments being displayed there.
	private List<List<UUID>> mIntervalInfo;
	
	// Link to the main application
	private MainApplication mMainApp;
	
	// Array of controllers of AppointmentViews summoned by this DayPort.
	private ArrayList<AppointmentViewController> mAppointmentControllers;
	
	// Calendar storing the date this DayPort represents
	private Calendar mAssignedDay;
	
	// Model data
	private CalendarDataModel mModel;
	
	/**
	 * Default constructor
	 * @param day the day this DayPort represents
	 */
	public AppointmentDayPortController(Calendar day) throws NullPointerException {
		mAssignedDay = (Calendar) day.clone();
		DateUtil.resetToDate(mAssignedDay);
		
		mIntervalInfo = new ArrayList<List<UUID>>(96);
		for(int i = 0; i < 96; i++)
			mIntervalInfo.add(new LinkedList<UUID>());
		
		mAppointmentControllers = new ArrayList<>();
	}
	
	/**
	 * Sets the controlled widget to a valid state, independent of any model data.
	 * Automatically called upon construction.
	 */
	protected void initializeWidget(){
		mMainPane = new AnchorPane();
		mMainPane.setPrefHeight(PORT_HEIGHT);
		mMainPane.setMinHeight(PORT_HEIGHT);
		mMainPane.setMaxHeight(PORT_HEIGHT);
	}
	
	/**
	 * returns the controlled widget.
	 */
	public AnchorPane getWidget(){
		return mMainPane;
	}
	
	/**
	 * Receives the structures needed for working.
	 * @param app
	 * @param model
	 */
	public void initializeStructures(MainApplication app, CalendarDataModel model){
		mModel = model;
		mMainApp = app;
	}
	
	public void addAppointment(int initOffset, int endOffset, Appointment appt, CalendarGroup cg){
		if(mModel == null){
			System.err.println("Must initialize model for AppointmentDayPortController");
			System.exit(1);
		}
		
		AppointmentViewController AVC = new AppointmentViewController();
		AnchorPane widget = AVC.getWidget();
		mAppointmentControllers.add(AVC);
		
		String range = DateUtil.hourString(initOffset) + " - " + DateUtil.hourString(endOffset);
		AVC.initializeStructures(mMainApp, mModel, appt);
		AVC.setHourRange(range);
		
		decideWidgetAnchors(initOffset, endOffset, widget);
		mMainPane.getChildren().add(widget);
	}

	/**
	 * Checks the appointment InitDate and EndDate to decide the widget's size 
	 * within this DayPort. Resizes other widgets if needed.
	 * @param widget
	 * @param appt
	 */
	private void decideWidgetAnchors(int initOffset, int endOffset, Node widget){
		double topAnchor = PORT_HEIGHT * initOffset / (double) DateUtil.MINUTES_IN_DAY ;
		double bottomAnchor = PORT_HEIGHT * (1 - (endOffset / (double) DateUtil.MINUTES_IN_DAY));
		
		AnchorPane.setTopAnchor(widget, topAnchor);
		AnchorPane.setBottomAnchor(widget, bottomAnchor);
		AnchorPane.setLeftAnchor(widget, 0.0);
		AnchorPane.setRightAnchor(widget, 0.0);
	}
	
	/**
	 * Removes an appointment view from this DayPort
	 * @param id
	 */
	public void removeAppointmentView(UUID id){
		List<AppointmentViewController> list = new LinkedList<>();
		
		mAppointmentControllers.forEach(AC -> {
			if(AC.getID().compareTo(id) == 0)
				list.add(AC);
		});
		
		list.forEach(AC -> {
			mAppointmentControllers.remove(AC);
			mMainPane.getChildren().remove(AC.getWidget());
		});
		
		//TODO: reallocate all affected views
	}
	
	/**
	 * @return DAY_OF_MONTH this DayPort represents
	 */
	public int getAssignedDay(){
		return mAssignedDay.get(Calendar.DATE);
	}
}
