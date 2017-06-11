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
import poo.calendar.dialogscenes.AppointmentDialogController;
import poo.calendar.dialogscenes.GroupDialogController;
import poo.calendar.dialogscenes.TaskDialogController;
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
	private Parent mMainParent;
	
	private ObservableList<Appointment> mAppointments;
	private ObservableList<Task> mTasks;
	private ObservableMap<UUID, CalendarGroup> mGroups;
	
	/**
	 * Creates the main root for the scene graph.
	 * This root should be live during the whole program.
	 */
	private void createMainParent(){
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
		appointmentsController.initializeModel(mAppointments, mGroups);
		appointmentsController.setMainApp(this);
		
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
		tasksController.setMainApp(this);
		
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
		
		mMainParent = mainScene;
	}
	
	/**
	 * Changes stage to the already created main scene
	 */
	public void displayMainRoot(){
		mMainScene.setRoot(mMainParent);
	}
	
	@Override
	public void start(Stage stage) {
		this.mStage = stage;
		
		// TODO: read application data from a persistent storage.
		mAppointments = FXCollections.observableArrayList();
		mTasks = FXCollections.observableArrayList();
		mGroups = FXCollections.observableHashMap();
		mGroups.put(CalendarGroup.DEFAULT_ID, CalendarGroup.DEFAULT_GROUP);
		
		createMainParent();
		mMainScene = new Scene(mMainParent);
		mStage.setTitle("Calendar");
		mStage.setScene(mMainScene);
		mStage.show();
	}
	
	/**
	 * Changes current scene to the group creation dialog.
	 * Creates a dialog on EDIT mode if ID is provided.
	 * @param id the ID of the group to edit
	 */
	public void displayGroupDialog(UUID id){
		//TODO: Add switching animation using snapshots
		
		// Load dialog
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/dialogscenes/GroupDialog.fxml"));
		Parent dialog = null;
		try {
			dialog = loader.load();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		GroupDialogController controller = loader.getController();
		controller.initializeModel(mGroups, mTasks, mAppointments);
		controller.setMainApp(this);
		
		if(id != null)
			controller.setGroupID(id);
		
		mMainScene.setRoot(dialog);
	}
	
	/**
	 * Creates a dialog on CREATE mode.
	 */
	public void displayGroupDialog(){
		displayGroupDialog(null);
	}
	
	/**
	 * Changes current scene to the appointment creation dialog.
	 * Creates a dialog on EDIT mode, if ID is provided.
	 * @param id the ID of the appointment to edit
	 */
	public void displayAppointmentDialog(UUID id){
		//TODO: Add switching animation using snapshots
		
		//Load dialog
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/dialogscenes/AppointmentDialog.fxml"));
		Parent dialog = null;
		try {
			dialog = loader.load();
		} catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		AppointmentDialogController controller = loader.getController();
		controller.initializeModel(mGroups, mTasks, mAppointments);
		controller.setMainApp(this);
		
		if(id != null)
			controller.setAppointmentID(id);
		
		mMainScene.setRoot(dialog);
	}
	
	/**
	 * Creates a dialog on CREATE mode
	 */
	public void displayAppointmentDialog(){
		displayAppointmentDialog(null);
	}
	
	/**
	 * Changes current scene to the task creation dialog.
	 * Creates a dialog on EDIT mode, if ID is provided.
	 * @param id the ID of the task to edit
	 */
	public void displayTaskDialog(UUID id){
		//TODO: Add switching animation using snapshots
		
		//Load dialog
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/poo/calendar/dialogscenes/TaskDialog.fxml"));
		Parent dialog = null;
		try {
			dialog = loader.load();
		} catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		TaskDialogController controller = loader.getController();
		controller.initializeModel(mGroups, mTasks, mAppointments);
		controller.setMainApp(this);
		
		if(id != null)
			controller.setTaskID(id);
		
		mMainScene.setRoot(dialog);
	}
	
	/**
	 * Creates a dialog on CREATE mode
	 */
	public void displayTaskDialog(){
		displayTaskDialog(null);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
