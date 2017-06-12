package poo.calendar.mainscene.appointments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import poo.calendar.ControlledWidget;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Recurrence;

/**
 * Class for controlling the appointment day port, where all appointments for a given day are displayed.
 */
public class AppointmentDayPortController extends ControlledWidget<AnchorPane> {
	private AnchorPane mMainPane;
	
	private static int PORT_HEIGHT = 2016; //Divisible by 96
	
	//96 intervals of 15 minutes each
	private List<List<UUID>> mIntervalInfo;
	
	private MainApplication mMainApp;
	private ArrayList<AppointmentViewController> mAppointmentControllers;
	
	// Calendar storing the day this DayPort represents
	private Calendar mAssignedDay;
	
	// Model data
	private ObservableMap<UUID, CalendarGroup> mGroupMap;
	
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
	
	public void initializeModel(ObservableMap<UUID, CalendarGroup> map){
		mGroupMap = map;
	}
	
	/**
	 * Adds an appointment to the Day port widget.
	 * @param appt An appointment whose day correspond to that of this DayPort
	 */
	public void addAppointment(Appointment appt, CalendarGroup cg){
		if(mGroupMap == null){
			System.err.println("Must initialize model for AppointmentDayPortController");
			System.exit(1);
		}
		
		System.out.println("Control3");
		AppointmentViewController AVC = new AppointmentViewController();
		AnchorPane widget = AVC.getWidget();
		
		mAppointmentControllers.add(AVC);
		
		String range = DateUtil.hourString(appt.getInitDate()) + " - " + DateUtil.hourString(appt.getEndDate());
		AVC.initializeModel(appt, cg, range);
		
		// If appointment changes Calendar Group, repaint it.
		appt.groupIDProperty().addListener(change -> {
			AVC.setColor(mGroupMap.get(appt.getGroupID()).getColor());
		});
		
		if(mMainApp != null)
			widget.setOnMouseClicked(action -> mMainApp.displayAppointmentDialog(AVC.getID()));
		
		decideWidgetSize(widget, appt);
		
		mMainPane.getChildren().add(widget);
	}
	
	/**
	 * Checks the appointment InitDate and EndDate to decide the widget's size 
	 * within this DayPort. Resizes other widgets if needed.
	 * @param widget
	 * @param appt
	 */
	private void decideWidgetSize(Node widget, Appointment appt){
		Calendar init, end;
		Recurrence rec = appt.getRecurrence();
		int minute1, hour1, day1;
		int minute2, hour2, day2;
		
		init = (Calendar) appt.getInitDate();
		end = (Calendar) appt.getEndDate();
		
		// Get all comparison keys needed
		minute1 = init.get(Calendar.MINUTE);
		hour1 = init.get(Calendar.HOUR_OF_DAY);
		day1 = init.get(Calendar.DAY_OF_MONTH);
		minute2 = end.get(Calendar.MINUTE);
		hour2 = end.get(Calendar.HOUR_OF_DAY);
		day2 = end.get(Calendar.DAY_OF_MONTH);
		
		if(rec == Recurrence.DAILY){
			//If recurrence is daily, change the day comparison keys to match the current day.
			int delta = day2 - day1; //TODO: Solve problem when appointment spans for more than 1 day
			
			Calendar newInit = (Calendar) mAssignedDay.clone();
			Calendar newEnd = (Calendar) mAssignedDay.clone();
			newEnd.add(Calendar.DAY_OF_MONTH, delta);
			
			init = newInit;
			end = newEnd;
			day1 = init.get(Calendar.DAY_OF_MONTH);
			day2 = end.get(Calendar.DAY_OF_MONTH);
		
		} else if(rec == Recurrence.WEEKLY){
			//If recurrence is weekly, change the day comparison keys to match the current week's corresponding weekday
			
			int delta = day2 - day1; //TODO: Solve problem when appointment spans for more than 1 month.
			int weekday = init.get(Calendar.DAY_OF_WEEK);
			
			Calendar newInit = (Calendar) mAssignedDay.clone();
			newInit.set(Calendar.DAY_OF_WEEK, weekday);
			
			Calendar newEnd = (Calendar) newInit.clone();
			newEnd.add(Calendar.DAY_OF_MONTH, delta);

			init = newInit;
			end = newEnd;
			day1 = newInit.get(Calendar.DAY_OF_MONTH);
			day2 = newEnd.get(Calendar.DAY_OF_MONTH);
			
			//TODO: This loops is also present in AppointmentWindowController. Find a way to abstract this out.
		}
		
		int offset1 = 60 * hour1 + minute1;
		int offset2 = 60 * hour2 + minute2;
		int whole = 60 * 24;
		
		offset1 -= offset1%15; //Rounds to lower 15 minutes
		offset2 += offset2%15 == 0 ? 0 : 15 - offset2%15; //Rounds to upper 15 minutes
		
		// If beginning day of the appointment is before the current day, the new
		// appointment will span from 00:00
		//TODO: If recurrence is daily, the above behavior is ignore for an appointment that
		// spans, for example, from 23:45 - 00:15. Fix this.
		if(day1 < mAssignedDay.get(Calendar.DAY_OF_MONTH)){
			offset1 = 0;
		}
		
		// If the end day of the appointment is after current day, the new
		// appointment will span up to 23:59
		if(day2 > mAssignedDay.get(Calendar.DAY_OF_MONTH)){
			offset2 = whole;
		}
		
		double topAnchor = PORT_HEIGHT * offset1 / (double) whole ;
		double bottomAnchor = PORT_HEIGHT * (whole - offset2) / (double) whole;
		
		// Fills the intervals that the new appointment uses
		for(int i = (int) offset1/15, j = (int) offset2/15; i < j; i++){
			mIntervalInfo.get(i).add(appt.getID());
		}
		
		//TODO: Resize other appointments whenever needed
		
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
		for(AppointmentViewController adpc: mAppointmentControllers){
			if(adpc.getID().compareTo(id) == 0){
				mAppointmentControllers.remove(adpc);
				
				AnchorPane widget = adpc.getWidget();
				mMainPane.getChildren().remove(widget);
				
				return;
			}
		}
		
		//TODO: reallocate all affected views
	}
	
	/**
	 * Sets the main application from which this widget will later request a scene change;
	 * @param app
	 */
	public void setMainApp(MainApplication app){
		mMainApp = app;

		for(AppointmentViewController AVC: mAppointmentControllers){
			AVC.getWidget().setOnMouseClicked(action -> mMainApp.displayAppointmentDialog(AVC.getID()));
		}
	}
	
	/**
	 * @return DAY_OF_MONTH this DayPort represents
	 */
	public int getAssignedDay(){
		return mAssignedDay.get(Calendar.DATE);
	}
}
