package poo.calendar.mainscene.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Task;

/**
 * Widget class that displays the tasks window.
 * It's a vertical box containing 2 list of tasks, one for tasks with deadline,
 * and other for tasks without deadline.
 * 
 * This class observes:
 *  - The model map of Tasks
 *  - The model map of Groups
 */
public class TaskWindowController {
	@FXML
	private AnchorPane mMainPane;

	@FXML
	private VBox mUpperBox;

	@FXML
	private VBox mLowerBox;
	
	@FXML
	private Button mAddButton;

	private MainApplication mMainApp;
	
	//Model data
	private CalendarDataModel mModel;
	
	private Map<UUID, TaskViewController> mControllers;
	
	/**
	 * Default constructor.
	 */
	public TaskWindowController(){
		mControllers = new HashMap<>();
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded
	 */
	@FXML
	private void initialize(){
	}
	
	/**
	 * Receives the structures needed for working.
	 * @param list List of tasks that should be controlled by this Class
	 */
	public void initializeStructures(MainApplication app, CalendarDataModel model){
		if(mModel != null){
			//TODO: Verify logging / exception
			System.err.println(this.getClass().getName() + ": Can only initialize model once.");
			System.exit(1);
		}
			 
		mModel = model;

		mModel.getTasks().forEach((uuid, task) -> {
			this.addTaskView(task);
		});
		
		mModel.getTasks().addListener((MapChangeListener.Change<? extends UUID, ? extends Task> change) -> {
			if(change.wasRemoved()){
				this.removeTaskView(change.getKey());
			}
			if(change.wasAdded()){
				this.addTaskView(change.getValueAdded());
			}
		});
		
		mModel.getGroups().addListener((MapChangeListener.Change<? extends UUID, ? extends CalendarGroup> change) -> {
			if(change.wasRemoved()){
				for(TaskViewController TVC: mControllers.values()){
					TVC.setColor(CalendarGroup.DEFAULT_GROUP.getColor());
				}
			}
		});
		
		mMainApp = app;
		mAddButton.setOnAction(action -> {
			mMainApp.displayTaskDialog();
		});
	}
	
	/**
	 * Adds a task to the UI.
	 * @param task Task to be added.
	 */
	private void addTaskView(Task task){
		CalendarGroup cg = mModel.getRefGroup(task);
		TaskViewController TVC = new TaskViewController(task, cg);
		
		TVC.getWidget().setOnMouseClicked(action -> mMainApp.displayTaskDialog(task.getID()));
		mControllers.put(TVC.getID(), TVC);
		
		addWidgetToView(task, TVC);
		
		task.groupIDProperty().addListener(change -> {
			CalendarGroup cg2 = mModel.getRefGroup(task);
			TVC.setColor(cg2.getColor());
		});
		
		task.deadlineDateProperty().addListener(change -> {
			removeWidgetFromView(TVC);
			addWidgetToView(task, TVC);
		});
	}
	
	private void addWidgetToView(Task task, TaskViewController TVC){
		if(task.getDeadlineDate() != null){
			regenerateUpperBox();
		} else {
			mLowerBox.getChildren().add(TVC.getWidget());
		}
	}
	
	private void removeWidgetFromView(TaskViewController TVC){
		mUpperBox.getChildren().remove(TVC.getWidget());
		mLowerBox.getChildren().remove(TVC.getWidget());
	}
	
	private void regenerateUpperBox(){
		mUpperBox.getChildren().clear();
		
		PriorityQueue<TaskViewController> queue = new PriorityQueue<>();
		
		mControllers.forEach((uuid, TVC) -> {
			if(TVC.hasDeadline())
				queue.add(TVC);
		});
		
		while(!queue.isEmpty()){
			mUpperBox.getChildren().add(queue.poll().getWidget());
		}
	}
	
	/**
	 * Removes a task from the UI
	 * @param id ID of the task to remove
	 */
	private void removeTaskView(UUID id){
		TaskViewController TVC = mControllers.get(id);
		removeWidgetFromView(TVC);
		mControllers.remove(id);
	}
}
