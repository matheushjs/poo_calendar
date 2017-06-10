package poo.calendar.mainscene.tasks;

import java.util.UUID;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Task;

/**
 * Widget class that displays the tasks window.
 * It's a vertical box containing a list of tasks and 2 buttons.
 */
public class TaskWindowController {
	@FXML
	private AnchorPane mMainPane;
	
	@FXML
	private VBox mOuterBox;
	
	@FXML
	private ScrollPane mUpperScrollPane;
	
	@FXML
	private AnchorPane mUpperAnchorPane;
	
	@FXML
	private VBox mUpperBox;
	
	@FXML
	private ScrollPane mLowerScrollPane;
	
	@FXML
	private AnchorPane mLowerAnchorPane;
	
	@FXML
	private VBox mLowerBox;
	
	@FXML
	private Button mAddButton;

	private MainApplication mMainApp;
	
	//Model data
	private ObservableList<Task> mTaskList = null;
	
	/**
	 * Default constructor.
	 */
	public TaskWindowController(){
	}
	
	/**
	 * Called by FXML -after- the .fxml is loaded
	 */
	@FXML
	private void initialize(){
	}
	
	/**
	 * Connects listeners for modifications on the the data model, so that the UI is updated
	 * accordingly.
	 * 
	 * @param list List of tasks that should be controlled by this Class
	 */
	public void initializeModel(ObservableList<Task> list){
		if(mTaskList != null){
			//TODO: Verify logging / exception
			System.err.println(this.getClass().getName() + ": Can only initialize model once.");
			System.exit(1);
		}
			 
		mTaskList = list;

		for(Task t: mTaskList){
			this.addTaskView(t);
		}
		
		mTaskList.addListener((ListChangeListener.Change<? extends Task> change) -> {
			while(change.next()){
				//TODO: Handle updated/permuted appointments
				
				for(Object a: change.getRemoved()){
					this.removeTaskView(((Task)a).getID());
				}
				for(Object a: change.getAddedSubList()){
					System.out.println( ((Task) a).getTitle());
					
					this.addTaskView((Task) a);
				}
			}
		});
	}
	
	/**
	 * Adds a task to the UI.
	 * @param task Task to be added.
	 */
	private void addTaskView(Task task){
		ObservableList<Node> nodes = mLowerBox.getChildren();

		TaskView view = new TaskView(task.getTitle(), task.getDeadlineDate(), task.getID());
		view.setOnMouseClicked(click -> {
			TaskView source = (TaskView) click.getSource();
			if(click.getButton() == MouseButton.PRIMARY){
				this.removeTask(source.getID());
			}
		});
		nodes.add(view);
	}
	
	/**
	 * Removes a task from the UI
	 * @param id ID of the task to remove
	 */
	private void removeTaskView(UUID id){
		ObservableList<Node> nodes = mLowerBox.getChildren();
		nodes.removeIf(view -> {
			return 0 == ((TaskView)view).getID().compareTo(id);
		});
	}
	
	/**
	 * Removes a task from the task list
	 * @param id
	 */
	private void removeTask(UUID id){
		for(Task a: mTaskList){
			if(a.getID().compareTo(id) == 0){
				mTaskList.remove(a);
				break;
			}
		}
	}
	
	/**
	 * Sets the main application from which this widget will later request a scene change;
	 * @param app
	 */
	public void setMainApp(MainApplication app){
		mMainApp = app;
		
		mAddButton.setOnAction(action -> {
			mMainApp.displayTaskDialog();
		});
	}
}