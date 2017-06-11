package poo.calendar.mainscene.appointments;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import poo.calendar.ControlledWidget;
import poo.calendar.model.Appointment;

/**
 * Class for controlling the appointment day port, where all appointments for a given day are displayed.
 */
public class AppointmentDayPortController extends ControlledWidget<AnchorPane> {
	AnchorPane mMainPane;
	
	//96 intervals of 15 minutes each
	private List<List<UUID>> mIntervalInfo = new ArrayList<List<UUID>>(96);
	
	/**
	 * Default constructor
	 */
	public AppointmentDayPortController(){
		for(int i = 0; i < 96; i++)
			mIntervalInfo.add(new ArrayList<UUID>());
	}
	
	protected void initializeWidget(){
		mMainPane = new AnchorPane();
		mMainPane.setPrefHeight(2000);
		mMainPane.setMinHeight(2000);
		mMainPane.setMaxHeight(2000);
	}

	public AnchorPane getWidget(){
		return mMainPane;
	}
	
	/**
	 * Adds an appointment to the Day port widget.
	 * @param appt
	 */
	private static int counter = 0; //TODO: Remove this
	public void addAppointment(Appointment appt){
		Double top, bottom;
		top = 60.0*counter;
		bottom = 2000 - (top+60);
		counter++;
		
		AppointmentViewController AVC = new AppointmentViewController();
		AnchorPane widget = AVC.getWidget();
		AVC.initializeModel(appt.getTitle(), Color.AQUA, appt.getID()); //TODO: Get group color
		
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
}
