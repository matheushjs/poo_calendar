package poo.calendar.controller;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import poo.calendar.model.Appointment;
import poo.calendar.model.Task;
import poo.calendar.view.MainScene;

public class MainApplication extends Application {
	private ArrayList<Appointment> mAppointments;
	private ArrayList<Task> mTasks;
	
	@Override 
    public void start(Stage stage) {
    	Scene scene = new Scene(new MainScene());
		
		// Read from disk
		
		// onAddTask(title, calendar, *taskid*) -> mTasks.add(new Task(title, calendar, taskid))
		// onAddTask(title, calendar, calendar, *appointmentId*) -> mAppointments.add(new Task(title, calendar, appointmentId))
		
		// onDeleteTask(title, taskID) -> mTasks.remove(taskid)
		// onDeleteTask(title, appointmentID) -> mAppointments.remove(appointmentId)
		
		// Write to disk
    	
		stage.setTitle("Calendar");
    	stage.setScene(scene);
    	stage.show();
	}
	
	public static void main(String[] args){
		Application.launch(args);
	}
	
	/**
	 * @param title Title of Task being added
	 * @param date Date of Task being added
	 */
	private void addTask(String title, Calendar date){
		//TODO: Validate arguments
		mTasks.add(new Task(title, date));
	}
	
	/**
	 * @param title Title of Appointment being added
	 * @param init Initial date of the Appointment
	 * @param end End date of the Appointment
	 */
	private void addAppointment(String title, Calendar init, Calendar end){
		//TODO: Validate arguments
		mAppointments.add(new Appointment(title, init, end));
	}
	
	/**
	 * @param id The ID of the Task to remove
	 */
	private void removeTask(long id){
		for(Task t: mTasks){
			if(t.getID() == id){
				mTasks.remove(t);
				
				// There can be only 1 Task with ID 'id', so we break.
				// Since the loop is broken, it's fine to delete an element from the
				//   ArrayList being iterated over.
				break;
			}
		}
	}
	
	/**
	 * @param id The ID of the Appointment to remove
	 */
	private void removeAppointment(long id){
		for(Appointment a: mAppointments){
			if(a.getID() == id){
				mAppointments.remove(a);
				
				// The ID is unique, so we break;
				// Since the loop is broken, it's fine to delete an element from the
				//   ArrayList being iterated over.
				break;
			}
		}
	}
}
