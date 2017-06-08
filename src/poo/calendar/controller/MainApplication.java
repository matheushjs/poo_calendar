package poo.calendar.controller;

import java.io.IOException;
import java.util.UUID;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Task;
import poo.calendar.view.AppointmentWindowController;
import poo.calendar.view.MainSceneController;
import poo.calendar.view.TaskWindowController;

public class MainApplication extends Application {
	private Stage mStage;
	
	private ObservableList<Appointment> mAppointments;
	private ObservableList<Task> mTasks;
	private ObservableMap<UUID, CalendarGroup> mGroups;
	
	@Override
	public void start(Stage stage) {
		this.mStage = stage;
		
		// TODO: read application data from a persistent storage.
		mAppointments = FXCollections.observableArrayList();
		mTasks = FXCollections.observableArrayList();
		mGroups = FXCollections.observableHashMap();
		
		// Load AppointmentsWindow
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/view/AppointmentWindow.fxml"));
		VBox appointmentsWidget = null;
		try {
			appointmentsWidget = (VBox) loader.load();
		} catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(1);
		}
		AppointmentWindowController appointmentsController = loader.getController();
		appointmentsController.initializeModel(mAppointments);

		// Load TaskWindow
		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/view/TaskWindow.fxml"));
		VBox tasksWidget = null;
		try {
			tasksWidget = (VBox) loader.load();
		} catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		TaskWindowController tasksController = loader.getController();
		tasksController.initializeModel(mTasks);
		
		// Load MainScene
		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/view/MainScene.fxml"));
		VBox mainScene = null;
		try {
			mainScene = (VBox) loader.load();
		} catch(IOException e){ 
			System.out.println(e.getMessage());
			System.exit(1);
		}
		MainSceneController mainSceneController = loader.getController();
		mainSceneController.addAppointmentWidget(appointmentsWidget);
		mainSceneController.addTaskWidget(tasksWidget);
		
		Scene scene = new Scene(mainScene);
		mStage.setTitle("Calendar");
		mStage.setScene(scene);
		mStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
