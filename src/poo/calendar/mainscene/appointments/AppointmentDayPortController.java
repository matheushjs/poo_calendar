package poo.calendar.mainscene.appointments;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javafx.scene.layout.AnchorPane;
import poo.calendar.ControlledWidget;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;

/**
 * Class for controlling the appointment day port, where all appointments for a given day are displayed.
 */
public class AppointmentDayPortController extends ControlledWidget<AnchorPane> {
	private AnchorPane mMainPane;
	
	//96 intervals of 15 minutes each
	private List<List<UUID>> mIntervalInfo = new ArrayList<List<UUID>>(96);
	
	private MainApplication mMainApp;
	private ArrayList<AppointmentViewController> mAppointmentControllers;
	
	/**
	 * Default constructor
	 */
	public AppointmentDayPortController(){
		for(int i = 0; i < 96; i++)
			mIntervalInfo.add(new ArrayList<UUID>());
		mAppointmentControllers = new ArrayList<>();
	}
	
	/**
	 * Sets the controlled widget to a valid state, independent of any model data.
	 */
	protected void initializeWidget(){
		mMainPane = new AnchorPane();
		mMainPane.setPrefHeight(2016);
		mMainPane.setMinHeight(2016);
		mMainPane.setMaxHeight(2016);
	}
	
	/**
	 * returns the controlled widget.
	 */
	public AnchorPane getWidget(){
		return mMainPane;
	}
	
	/**
	 * Adds an appointment to the Day port widget.
	 * @param appt
	 */
	private static int counter = 0; //TODO: Remove this
	public void addAppointment(Appointment appt, CalendarGroup cg){
		Double top, bottom;
		top = 60.0*counter;
		bottom = 2000 - (top+60);
		counter++;
		
		AppointmentViewController AVC = new AppointmentViewController();
		AnchorPane widget = AVC.getWidget();
		AVC.initializeModel(appt.getTitle(), cg.getColor(), appt.getID());
		
		if(mMainApp != null)
			widget.setOnMouseClicked(action -> mMainApp.displayAppointmentDialog(AVC.getID()));
		
		AnchorPane.setTopAnchor(widget, top);
		AnchorPane.setBottomAnchor(widget, bottom);
		AnchorPane.setLeftAnchor(widget, 0.0);
		AnchorPane.setRightAnchor(widget, 0.0);
		mMainPane.getChildren().add(widget);
		
		//Create appointmentView
		//Calculate view size and space
		//bind view properties to the Appointment properties
		//add listeners to Appointment calendar properties
		//register onAppointmentClick function
	}
	
	public void removeAppointment(UUID id){
		//Find and remove view
		//reallocate all affected views
	}
	
	public void registerOnAppointmentClick(Consumer<UUID> handler){
		//Unregister old consumer (or forbid double-registering)
		//Register new consumer into all nodes
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
