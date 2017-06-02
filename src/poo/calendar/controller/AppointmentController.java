package poo.calendar.controller;

import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import poo.calendar.model.Appointment;
import poo.calendar.view.AppointmentView;
import poo.calendar.view.AppointmentWindow;
import poo.calendar.view.DateChooserDialog;

/**
 * Singleton Class for controlling the Appointments Window.
 * Should not call any method whose purpose is stylization of the GUI (e.g. setAlignment etc).
 */
public final class AppointmentController {
	private static AppointmentController mInstance = null;
	
	private AppointmentWindow mAW = null;
	private ObservableList<Appointment> mAppointmentList = null;
	
	//Prevent construction
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
	 * @param list List of appointments that should be controlled by this Class
	 */
	public void initializeModel(ObservableList<Appointment> list){
		 if(mAppointmentList == null){
			 mAppointmentList = list;

			 mAppointmentList.addListener(new ListChangeListener<Appointment>() {
				 @Override
				 public void onChanged(ListChangeListener.Change change) {
					 while(change.next()){
						 //TODO: Handle updated appointments
						 
						 System.out.println("Removed:");
						 for(Object a: change.getRemoved()){
							 System.out.println( ((Appointment) a).getTitle());
						 }
						 for(Object a: change.getAddedSubList()){
							 System.out.println("Added:");
							 System.out.println( ((Appointment) a).getTitle());
							 
							 // Puta que pariu
							 AppointmentController.getInstance().addAppointmentView((Appointment) a);
						 }
					 }
				 }
			 });
				 
		 } else {
			 //TODO: Verify logging / exception
			 System.err.println(this.getClass().getName() + ": Can only initialize model once.");
			 System.exit(1);
		 }
	}
	
	private void addAppointmentView(Appointment appointment){
		ObservableList<Node> nodes = mAW.getAppointmentListView().getChildren();
		Calendar calendar = appointment.getInitDate();
		String format = String.format("%02d/%02d %02d:%02d",
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR),
				calendar.get(Calendar.MINUTE));
		
		nodes.add(new AppointmentView(appointment.getTitle(), format));
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
		}
		
		mAW.getAddButton().setOnAction(a -> onAddClick(a));
		mAW.getDeleteButton().setOnAction(a -> onDeleteClick(a));
		
		return mAW;
	}
	
	private void onAddClick(ActionEvent a){
		Optional<Map<String,String>> result =
				new DateChooserDialog("New Appointment", "Set up your new appointment", DateChooserDialog.APPOINTMENT_DIALOG).showAndWait();
		
		result.ifPresent(name -> {
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
			
			Appointment appointment = new Appointment(name.get("title"), c1, c2);
			mAppointmentList.add(appointment);
		});
	}
	
	private void onDeleteClick(ActionEvent a){
		// Open auxiliary window (or change scene) for removing appointments
		// Send User input to the controller
		// Repeat steps above
	}
}
