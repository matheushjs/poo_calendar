package poo.calendar.controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.Scene;
import javafx.stage.Stage;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Task;
import poo.calendar.view.MainScene;

public class MainApplication extends Application {
	private ObservableList<Appointment> mAppointments;
	private ObservableList<Task> mTasks;
	private ObservableSet<CalendarGroup> mGroups;
	
	@Override
	public void start(Stage stage) {
		// TODO: read application data from a persistent storage.
		mAppointments = FXCollections.observableArrayList();
		mTasks = FXCollections.observableArrayList();
		mGroups = FXCollections.observableSet();
		
		AppointmentController.getInstance().initializeModel(mAppointments);
		TaskController.getInstance().initializeModel(mTasks);

		Scene scene = new Scene(new MainScene());

		stage.setTitle("Calendar");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
