package poo.calendar.controller;

import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import poo.calendar.model.Task;
import poo.calendar.view.DateChooserDialog;
import poo.calendar.view.TaskView;
import poo.calendar.view.TaskWindow;

/**
 * Singleton Class for controlling the Tasks Window.
 * Should not call any method whose purpose is stylization of the GUI (e.g. setAlignment etc).
 * 
 * See AppointmentController for discussion on this decisions.
 */
public class TaskController {
	private static TaskController mInstance = null;
	
	//Widgets
	private TaskWindow mTW = null;
	
	//Model Data
	private ObservableList<Task> mTaskList = null;
	
	// Prevent construction
	private TaskController(){}
	
	/**
	 * @return The class's singleton instance
	 */
	public static TaskController getInstance(){
		if(mInstance == null){
			mInstance = new TaskController();
		}
		return mInstance;
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

		mTaskList.addListener((ListChangeListener.Change<? extends Task> change) -> {
			while(change.next()){
				//TODO: Handle updated/permuted appointments
				
				for(Object a: change.getRemoved()){
					//TODO: Handle removal
					System.out.println( ((Task) a).getTitle());
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
		ObservableList<Node> nodes = mTW.getTaskListView().getChildren();
		Calendar calendar = task.getDeadlineDate();
		String format = String.format("%02d/%02d %02d:%02d",
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR),
				calendar.get(Calendar.MINUTE));

		nodes.add(new TaskView(task.getTitle(), format));
	}
	
	/**
	 * @return the TaskWindow controlled by this class
	 */
	public TaskWindow getTaskWindow(){
		if(mTaskList == null){
			System.err.println(this.getClass().getName()
					+ ": Cannot instantiate a view class if model"
					+ " has not been initialized");
		}
		
		if(mTW == null){
			mTW = new TaskWindow();
		}
		
		mTW.getAddButton().setOnAction(a -> onAddClick(a));
		mTW.getDeleteButton().setOnAction(a -> onDeleteClick(a));
		
		return mTW;
	}
	
	/**
	 * Function to run whenever the 'Add' button in the tasks window is clicked.
	 * @param a
	 */
	private void onAddClick(ActionEvent a){
		Optional<Map<String,String>> result =
				new DateChooserDialog(
						"New Task", 
						"Set up your new task",
						DateChooserDialog.TASK_DIALOG
				).showAndWait();

		result.ifPresent(name -> {
			Calendar c1 = Calendar.getInstance();
			
			//TODO: Validate input. Check 'duration' key in the map
			
			c1.set( Integer.parseInt(name.get("year1")),
					Integer.parseInt(name.get("month1")),
					Integer.parseInt(name.get("day1")),
					Integer.parseInt(name.get("hour1")),
					Integer.parseInt(name.get("minute1")) );
			
			Task task = new Task(name.get("title"), c1);
			mTaskList.add(task);
		});
	}

	/**
	 * Function to run whenever the 'Delete' button in the tasks window is clicked
	 * @param a
	 */
	private void onDeleteClick(ActionEvent a){
		// Open auxiliary window (or change scene) for removing tasks
		// Send User input to the controller
		// Repeat steps above
	}
}