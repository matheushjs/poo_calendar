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
import poo.calendar.view.MainSceneController;

public class MainApplication extends Application {
	private Stage primaryStage;
	
	private ObservableList<Appointment> mAppointments;
	private ObservableList<Task> mTasks;
	private ObservableMap<UUID, CalendarGroup> mGroups;
	
	@Override
	public void start(Stage stage) {
		this.primaryStage = stage;
		
		// TODO: read application data from a persistent storage.
		mAppointments = FXCollections.observableArrayList();
		mTasks = FXCollections.observableArrayList();
		mGroups = FXCollections.observableHashMap();
		
		AppointmentController.getInstance().initializeModel(mAppointments);
		TaskController.getInstance().initializeModel(mTasks);

		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/view/MainScene.fxml"));
		
		VBox mainScene = null;
		try {
			mainScene = (VBox) loader.load();
		} catch(IOException e){ 
			System.out.println(e.getMessage());
			System.exit(1);
		}
		MainSceneController mainSceneController = loader.getController();
		
		Scene scene = new Scene(mainScene);
		
		stage.setTitle("Calendar");
		stage.setScene(scene);
		stage.show();
	}

	public void presentMainScene(){
		
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
