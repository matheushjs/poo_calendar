package poo.calendar.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.Stage;
import poo.calendar.model.Appointment;
import poo.calendar.model.Task;
import poo.calendar.view.MainScene;

public class MainApplication extends Application {
	private static final String APPOINTMENTS_SER = "appointments.ser";
	private static final String TASKS_SER = "tasks.ser";

	private ObservableList<Appointment> mAppointments;
	private ObservableList<Task> mTasks;

	@Override
	public void start(Stage stage) {
		mAppointments = deserializeAppointments();
		mTasks = deserializeTasks();

		AppointmentController.getInstance().initializeModel(mAppointments);
		TaskController.getInstance().initializeModel(mTasks);
		
		Scene scene = new Scene(new MainScene());
		
		stage.setTitle("Calendar");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args){
		Application.launch(args);
	}

	private void serializeAppointments() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(APPOINTMENTS_SER));
			out.writeObject(mAppointments);
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private ObservableList<Appointment> deserializeAppointments() {
		ObservableList<Appointment> appointments = FXCollections.observableArrayList();
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(APPOINTMENTS_SER));
			appointments = (ObservableList<Appointment>) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
		} catch(IOException | ClassNotFoundException  e) {
			e.printStackTrace();
		}

		return appointments;
	}

	private void serializeTasks() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TASKS_SER));
			out.writeObject(mTasks);
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private ObservableList<Task> deserializeTasks() {
		ObservableList<Task> tasks = FXCollections.observableArrayList();
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(TASKS_SER));
			tasks = (ObservableList<Task>) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
		} catch(IOException | ClassNotFoundException  e) {
			e.printStackTrace();
		}

		return tasks;
	}

	/**
	 * @param title Title of Task being added
	 * @param date Date of Task being added
	 */
	private void addTask(String title, Calendar date){
		//TODO: Validate arguments
		//TODO: Serialize using Listeners
		mTasks.add(new Task(title, date));
		serializeTasks();
	}

	/**
	 * @param title Title of Appointment being added
	 * @param init Initial date of the Appointment
	 * @param end End date of the Appointment
	 */
	private void addAppointment(String title, Calendar init, Calendar end){
		//TODO: Validate arguments
		//TODO: Serialize using Listeners
		mAppointments.add(new Appointment(title, init, end));
		serializeAppointments();
	}

	/**
	 * @param id The ID of the Task to remove
	 */
	private void removeTask(long id){
		//TODO: Serialize using Listeners
		for(Task t: mTasks){
			if(t.getID() == id){
				mTasks.remove(t);

				// There can be only 1 Task with ID 'id', so we break.
				// Since the loop is broken, it's fine to delete an element from the
				//   ArrayList being iterated over.
				break;
			}
		}
		serializeTasks();
	}

	/**
	 * @param id The ID of the Appointment to remove
	 */
	private void removeAppointment(long id){
		//TODO: Serialize using Listeners
		for(Appointment a: mAppointments){
			if(a.getID() == id){
				mAppointments.remove(a);

				// The ID is unique, so we break;
				// Since the loop is broken, it's fine to delete an element from the
				//   ArrayList being iterated over.
				break;
			}
		}
		serializeAppointments();
	}
}
