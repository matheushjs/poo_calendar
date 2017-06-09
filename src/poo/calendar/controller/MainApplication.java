package poo.calendar.controller;

import java.io.IOException;
import java.util.UUID;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import poo.calendar.dialogscenes.GroupCreationDialogController;
import poo.calendar.mainscene.MainSceneController;
import poo.calendar.mainscene.appointments.AppointmentWindowController;
import poo.calendar.mainscene.groups.GroupListWindowController;
import poo.calendar.mainscene.tasks.TaskWindowController;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Task;

public class MainApplication extends Application {
	private Stage mStage;
	private Scene mMainScene;
	
	private ObservableList<Appointment> mAppointments;
	private ObservableList<Task> mTasks;
	private ObservableMap<UUID, CalendarGroup> mGroups;
	
	/**
	 * Creates the main scene, which should be live during the whole program.
	 */
	private void createMainScene(){
		// Load AppointmentsWindow
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/mainscene/appointments/AppointmentWindow.fxml"));
		Node appointmentsWidget = null;
		try {
			appointmentsWidget = loader.load();
		} catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		AppointmentWindowController appointmentsController = loader.getController();
		appointmentsController.initializeModel(mAppointments);

		// Load TaskWindow
		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/mainscene/tasks/TaskWindow.fxml"));
		Node tasksWidget = null;
		try {
			tasksWidget = loader.load();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		TaskWindowController tasksController = loader.getController();
		tasksController.initializeModel(mTasks);
		
		// Load GroupsWindow
		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/mainscene/groups/GroupListWindow.fxml"));
		Node groupsWidget = null;
		try {
			groupsWidget = loader.load();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		GroupListWindowController groupsController = loader.getController();
		groupsController.initializeModel(mGroups);
		groupsController.setMainApp(this);
		
		// Load MainScene
		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/mainscene/MainScene.fxml"));
		Parent mainScene = null;
		try {
			mainScene = (Parent) loader.load();
		} catch(IOException e){ 
			e.printStackTrace();
			System.exit(1);
		}
		MainSceneController mainSceneController = loader.getController();
		mainSceneController.addAppointmentWidget(appointmentsWidget);
		mainSceneController.addTaskWidget(tasksWidget);
		mainSceneController.addGroupsWidget(groupsWidget);
		
		mMainScene = new Scene(mainScene);
	}
	
	/**
	 * Changes stage to the already created main scene
	 */
	public void displayMainScene(){
		mStage.setTitle("Calendar");
		mStage.setScene(mMainScene);
		mStage.show();
	}
	
	@Override
	public void start(Stage stage) {
		this.mStage = stage;
		
		// TODO: read application data from a persistent storage.
		mAppointments = FXCollections.observableArrayList();
		mTasks = FXCollections.observableArrayList();
		mGroups = FXCollections.observableHashMap();
		
		createMainScene();
		displayMainScene();
	}
	
	/**
	 * Changes current scene to the group creation dialog.
	 */
	public void displayGroupCreationDialog(){
		//TODO: Add switching animation using snapshots
		
		// Load dialog
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/dialogscenes/GroupCreationDialog.fxml"));
		Parent dialog = null;
		try {
			dialog = loader.load();
		} catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		GroupCreationDialogController controller = loader.getController();
		controller.initializeModel(mGroups);
		controller.setMainApp(this);
		
		Scene scene = new Scene(dialog);
		this.mStage.setScene(scene);
		mStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
