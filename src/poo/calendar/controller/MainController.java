package poo.calendar.controller;

import java.util.ArrayList;

import poo.calendar.model.Appointment;
import poo.calendar.model.Task;
import poo.calendar.view.MainWindow;

public class MainController {
	private ArrayList<Appointment> mAppointments;
	private ArrayList<Task> mTasks;
	
	public MainController(){
		// Read from disk
		
		// onAddTask(title, calendar, *taskid*) -> mTasks.add(new Task(title, calendar, taskid))
		// onAddTask(title, calendar, calendar, *appointmentId*) -> mAppointments.add(new Task(title, calendar, appointmentId))
		
		// onDeleteTask(title, taskID) -> mTasks.remove(taskid)
		// onDeleteTask(title, appointmentID) -> mAppointments.remove(appointmentId)
		
		// Write to disk
	}
}
