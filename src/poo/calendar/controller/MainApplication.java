package poo.calendar.controller;

import java.io.IOException;
import java.util.UUID;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import poo.calendar.dialogscenes.AboutDialogController;
import poo.calendar.dialogscenes.AppointmentDialogController;
import poo.calendar.dialogscenes.GroupDialogController;
import poo.calendar.dialogscenes.TaskDialogController;
import poo.calendar.mainscene.MainSceneController;
import poo.calendar.mainscene.appointments.AppointmentWindowController;
import poo.calendar.mainscene.groups.GroupListWindowController;
import poo.calendar.mainscene.tasks.TaskWindowController;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.widgets.ElfCalendarText;

public class MainApplication extends Application {
	private Stage mStage;
	private Scene mMainScene;
	private Parent mMainParent;
	
	private CalendarDataModel mModel;
	
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
		appointmentsController.initializeStructures(this, mModel);
		
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
		tasksController.initializeStructures(this, mModel);
		
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
		groupsController.initializeStructures(this, mModel);
		
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
		mainSceneController.initializeStructures(this);
		mainSceneController.addAppointmentWidget(appointmentsWidget);
		mainSceneController.addTaskWidget(tasksWidget);
		mainSceneController.addGroupsWidget(groupsWidget);
		
		mMainParent = mainScene;
	}
	
	/**
	 * Changes stage to the already created main scene
	 */
	public void displayMainRoot(){
		switchScenes(mMainParent);
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
		controller.initializeStructures(this, mModel);
		
		if(id != null)
			controller.setGroupID(id);
		
		switchScenes(dialog);
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
		controller.initializeStructures(this, mModel);
		
		if(id != null)
			controller.setAppointmentID(id);

		switchScenes(dialog);
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
		controller.initializeStructures(this, mModel);
		
		if(id != null)
			controller.setTaskID(id);
		
		//mMainScene.setRoot(dialog);
		switchScenes(dialog);
	}
	
	/**
	 * Creates a dialog on CREATE mode
	 */
	public void displayTaskDialog(){
		displayTaskDialog(null);
	}
	
	/**
	 * Displays the About Dialog.
	 */
	public void displayAboutDialog(){
		AboutDialogController ADC = new AboutDialogController();
		VBox widget = ADC.getWidget();
		
		Scene dialogScene = new Scene(widget, 500, 500);
		
		Stage dialog = new Stage();
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(this.mStage);
		dialog.setScene(dialogScene);
		dialog.show();
	}
	
	/**
	 * Switch scenes using a Fade transition.
	 * The old scene is transitioned as a snapshot image, so there is no danger in the user
	 * attempting to click the old scene while it's transitioning.
	 */
	public void switchScenes(Parent newroot){
		double oldOpacity = newroot.getOpacity();
		newroot.setOpacity(0.0);

		// Take a photo of the current scene
		WritableImage oldi = new WritableImage((int) mMainScene.getWidth(), (int) mMainScene.getHeight());
		Image oldimg = mMainScene.snapshot(oldi);
		ImageView oldview = new ImageView(oldimg);
		
		// Change the Stage to a "transitionable" Node. A stack pane in this case.
		StackPane pane = new StackPane(oldview, newroot);
		mMainScene.setRoot(pane);
		
		// Transition proper
		FadeTransition fadeUp = new FadeTransition(Duration.millis(500), newroot);
		FadeTransition fadeDown = new FadeTransition(Duration.millis(500), oldview);
		
		fadeUp.setFromValue(0.0);
		fadeUp.setToValue(oldOpacity);
		fadeUp.setOnFinished(action -> {
			pane.getChildren().remove(newroot);
			mMainScene.setRoot(newroot);
		});
		
		fadeDown.setFromValue(1.0);
		fadeDown.setToValue(0.0);

		fadeUp.playFromStart();
		fadeDown.playFromStart();
	}
	
	/**
	 * Performs the initial animation, and then creates the mainRoot.
	 */
	public void initializeView(){
		ElfCalendarText ECT = new ElfCalendarText(80);
		HBox box = new HBox(ECT);
		box.setAlignment(Pos.CENTER);
		box.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
		ECT.setOpacity(0.0);
		
		mMainScene.setRoot(box);
		
		FadeTransition fadeUp = new FadeTransition(Duration.millis(2000), ECT);
		FadeTransition fadeNeutral = new FadeTransition(Duration.millis(0), ECT);
		fadeUp.setFromValue(0.0);
		fadeUp.setToValue(1.0);
		fadeUp.setOnFinished(action -> fadeNeutral.playFromStart());
		
		// I seriously don't know how any better class for having this 3 second delay,
		// so for now I'll stick to using FadeTransition.
		fadeNeutral.setFromValue(1.0);
		fadeNeutral.setToValue(1.0);
		fadeNeutral.setDelay(Duration.millis(5000));
		fadeNeutral.setOnFinished(action -> {
			createMainParent();
			displayMainRoot();
		});
		
		fadeUp.playFromStart();
	}
	
	@Override
	public void start(Stage stage) {
		this.mStage = stage;
		
		mModel = new CalendarDataModel();

		createMainParent();
		mMainScene = new Scene(new HBox(new Rectangle(800, 1400))); //Dummy root
		mStage.setTitle("Elf Calendar");
		mStage.setScene(mMainScene);
		
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		mStage.setX(primaryScreenBounds.getMinX());
		mStage.setY(primaryScreenBounds.getMinY());
		mStage.setWidth(primaryScreenBounds.getWidth());
		mStage.setHeight(primaryScreenBounds.getHeight());
		
		mStage.show();
		mStage.setOnCloseRequest(event -> terminateApplication());
		
		initializeView();
		
		mModel.load();
	}
	
	/**
	 * Saves what needs to be saved, then closes window.
	 * @param event
	 */
	public void terminateApplication(){
		Platform.exit();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
