package poo.calendar.mainscene.appointments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import poo.calendar.ControlledWidget;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;

/**
 * Class for controlling the appointment day port, where all appointments for a given day are displayed.
 */
public class AppointmentDayPortController extends ControlledWidget<AnchorPane> {
	private AnchorPane mMainPane;
	
	private static int PORT_HEIGHT = 2016; //Divisible by 96
	
	//96 intervals of 15 minutes each
	private List<List<UUID>> mIntervalInfo = new ArrayList<List<UUID>>(96);
	
	private MainApplication mMainApp;
	private ArrayList<AppointmentViewController> mAppointmentControllers;
	
	// Calendar storing the day this DayPort represents
	private Calendar mAssignedDay;
	
	/**
	 * Default constructor
	 * @param day the day this DayPort represents
	 */
	public AppointmentDayPortController(Calendar day) throws NullPointerException {
		mAssignedDay = (Calendar) day.clone();
		DateUtil.resetToDate(mAssignedDay);
		
		for(int i = 0; i < 96; i++)
			mIntervalInfo.add(new ArrayList<UUID>());
		mAppointmentControllers = new ArrayList<>();
	}
	
	/**
	 * Sets the controlled widget to a valid state, independent of any model data.
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
	 * Adds an appointment to the Day port widget.
	 * @param appt An appointment whose day correspond to that of this DayPort
	 */
	public void addAppointment(Appointment appt, CalendarGroup cg){
		AppointmentViewController AVC = new AppointmentViewController();
		AnchorPane widget = AVC.getWidget();
		
		mAppointmentControllers.add(AVC);
		
		String range = DateUtil.hourString(appt.getInitDate()) + " - " + DateUtil.hourString(appt.getEndDate());
		AVC.initializeModel(appt, cg, range);
		
		if(mMainApp != null)
			widget.setOnMouseClicked(action -> mMainApp.displayAppointmentDialog(AVC.getID()));
		
		manageWidgetSize(widget, appt);
		
		mMainPane.getChildren().add(widget);
	}
	
	/**
	 * Checks the appointment InitDate and EndDate to decide the widget's size 
	 * within this DayPort. Resizes other widgets if needed.
	 * @param widget
	 * @param appt
	 */
	private void manageWidgetSize(Node widget, Appointment appt){
		Calendar init, end;
		int minute1, hour1;
		int minute2, hour2, day2;
		
		init = appt.getInitDate();
		end = appt.getEndDate();
		
		minute1 = init.get(Calendar.MINUTE);
		hour1 = init.get(Calendar.HOUR_OF_DAY);
		minute2 = end.get(Calendar.MINUTE);
		hour2 = end.get(Calendar.HOUR_OF_DAY);
		day2 = end.get(Calendar.DAY_OF_MONTH);
		
		double offset1 = 60 * hour1 + minute1;
		double offset2 = 60 * hour2 + minute2;
		double whole = 60 * 24;
		
		offset1 -= offset1%15; //Rounds to lower 15 minutes
		offset2 += offset2%15 == 0 ? 0 : 15 - offset2%15; //Rounds to upper 15 minutes
		
		if(day2 > mAssignedDay.get(Calendar.DAY_OF_MONTH)){
			offset2 = whole;
		}
		
		double topAnchor = PORT_HEIGHT * offset1 / whole ;
		double bottomAnchor = PORT_HEIGHT * (whole - offset2) / whole;
		
		//TODO: Resize other appointments whenever needed
		
		System.out.println(topAnchor);
		System.out.println(bottomAnchor);
		
		AnchorPane.setTopAnchor(widget, topAnchor);
		AnchorPane.setBottomAnchor(widget, bottomAnchor);
		AnchorPane.setLeftAnchor(widget, 0.0);
		AnchorPane.setRightAnchor(widget, 0.0);
	}
	
	public void removeAppointment(UUID id){
		//Find and remove view
		//reallocate all affected views
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
}
