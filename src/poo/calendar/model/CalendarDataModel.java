package poo.calendar.model;

import java.io.File;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import poo.calendar.model.xmlwrappers.ModelXMLHandler;

/**
 * This class should wrap every interaction with the data model.
 * It should provide a way to undo up to a certain number of previous actions.
 * Should provide a thread-safe save-to-xml, and a function to save the model to the file
 * Should also add listeners to calendar groups, so when they are deleted, all related tasks
 *   and appointments are set to default group.
 */
public class CalendarDataModel {
	private ObservableMap<UUID, Appointment> mAppointments;
	private ObservableMap<UUID, Task> mTasks;
	private ObservableMap<UUID, CalendarGroup> mGroups;
	
	public CalendarDataModel(){
		mAppointments = FXCollections.observableHashMap();
		mTasks = FXCollections.observableHashMap();
		mGroups = FXCollections.observableHashMap();
		mGroups.put(CalendarGroup.DEFAULT_ID, CalendarGroup.DEFAULT_GROUP);
	}
	
	public void save(){
		mGroups.remove(CalendarGroup.DEFAULT_ID);
		File file = new File("calendar_data_model.xml");
		ModelXMLHandler.saveModel(this, file);
		mGroups.put(CalendarGroup.DEFAULT_ID, CalendarGroup.DEFAULT_GROUP);
	}
	
	public void load(){
		File file = new File("calendar_data_model.xml");
		if(file.exists())
			ModelXMLHandler.loadModel(this, file);
	}
	
	public ObservableMap<UUID, Appointment> getAppointments(){
		return mAppointments;
	}
	
	public ObservableMap<UUID, Task> getTasks(){
		return mTasks;
	}
	
	public ObservableMap<UUID, CalendarGroup> getGroups(){
		return mGroups;
	}
	
	/**
	 * Adds given appointment to the map.
	 * Adding the same appointment twice won't trigger invalidation of the ObservableMap..
	 * @param appt
	 */
	public void addAppointment(Appointment appt){
		Appointment retval;
		retval = mAppointments.get(appt.getID());
		if(retval == null)
			mAppointments.put(appt.getID(), appt);
	}
	
	/**
	 * Adds given task to the map.
	 * Adding the same task twice won't trigger invalidation of the ObservableMap.
	 * @param task
	 */
	public void addTask(Task task){
		Task retval;
		retval = mTasks.get(task.getID());
		if(retval == null)
			mTasks.put(task.getID(), task);
	}
	
	/**
	 * Adds given CalendarGroup to the map.
	 * Adding the same group twice won't trigger invalidation of the ObservableMap.
	 * @param cg
	 */
	public void addGroup(CalendarGroup cg){
		CalendarGroup retval;
		retval = mGroups.get(cg.getID());
		if(retval == null)
			mGroups.put(cg.getID(), cg);
	}
	
	public Appointment removeAppointment(UUID id){
		return mAppointments.remove(id);
	}
	
	public Task removeTask(UUID id){
		return mTasks.remove(id);
	}
	
	public CalendarGroup removeGroup(UUID id){
		CalendarGroup retval = mGroups.remove(id);
		if(retval == null) return null;
		
		// Whenever a group is removed, all references to it are erased.
		// Tasks and Appointments that previously referenced to that group
		//   will then reference to the Default group.
		mAppointments.values().forEach(appointment ->{
			if(appointment.getGroupID().compareTo(id) == 0){
				appointment.setGroupID(CalendarGroup.DEFAULT_ID);
			}
		});
		
		mTasks.values().forEach(task -> {
			if(task.getGroupID().compareTo(id) == 0)
				task.setGroupID(CalendarGroup.DEFAULT_ID);
		});
		
		return retval;
	}
	
	/**
	 * 
	 * @param id
	 * @return The appointment with UUID 'id'. Null if appointment doesn't exist.
	 */
	public Appointment getAppointment(UUID id){
		return mAppointments.get(id);
	}
	
	/**
	 * 
	 * @param id
	 * @return The task with UUID 'id'. Null if task doesn't exist.
	 */
	public Task getTask(UUID id){
		return mTasks.get(id);
	}
	
	/**
	 * @param id
	 * @return The task with UUID 'id'. Null if task doesn't exist.
	 */
	public CalendarGroup getGroup(UUID id){
		return mGroups.get(id);
	}
	
	/**
	 * 
	 * @param base any Task or Appointment.
	 * @return the CalendarGroup referenced by the given parameter.
	 */
	public CalendarGroup getRefGroup(CalendarNodeBase base){
		return getGroup(base.getGroupID());
	}

}
