package poo.calendar.mainscene;


import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import poo.calendar.controller.MainApplication;
import poo.calendar.widgets.DateText;
import poo.calendar.widgets.ElfCalendarText;

/**
 * Widget class that represents the main window of the application.
 */
public class MainSceneController {
	@FXML
	private VBox mMainBox;

	@FXML
	private MenuBar mMenuBar;

	@FXML
	private HBox mTopBox;

	@FXML
	private Text mDateText;

	@FXML
	private HBox mBottomBox;

	private MainApplication mMainApp;

	/**
	 * Default constructor.
	 */
    public MainSceneController() {
    }

    /**
     * Called by FXML -after- the .fxml is loaded.
     */
    @FXML
    private void initialize(){
    	ElfCalendarText ECT = new ElfCalendarText(40);
    	HBox box = new HBox(ECT);
		box.setAlignment(Pos.CENTER);
		box.setStyle("-fx-padding: 30px; -fx-background-color: white;");
    	try {
    		mTopBox.getChildren().add(0, box);
    	} catch(IndexOutOfBoundsException e) {
    		mTopBox.getChildren().add(box);
    	}
	    
    	DateText DT = new DateText(35);
    	box = new HBox(DT);
		box.setAlignment(Pos.CENTER);
		box.setStyle("-fx-padding: 30px; -fx-background-color: white;");
    	try {
    		mTopBox.getChildren().add(1, box);
    	} catch(IndexOutOfBoundsException e) {
    		mTopBox.getChildren().add(box);
    	}
    }

    /**
     * Receives the structures needed for working.
     * @param app
     */
    public void initializeStructures(MainApplication app){
    	mMainApp = app;
    }

    /**
     * Receives the AppointmentWindow widget and put it in the correct place.
     * @param widget
     */
    public void addAppointmentWidget(Node widget){
    	try {
    		mBottomBox.getChildren().add(0, widget);
    	} catch(IndexOutOfBoundsException e){
    		mBottomBox.getChildren().add(widget);
    	}
    }

    /**
     * Receives the TaskWindow widget and place it in the correct place
     * @param widget
     */
    public void addTaskWidget(Node widget){
    	try {
    		mBottomBox.getChildren().add(1, widget);
    	} catch(IndexOutOfBoundsException e){
    		mBottomBox.getChildren().add(widget);
    	}
    }

    /**
     * Receives the widget that displays calendar groups
     * @param widget
     */
    public void addGroupsWidget(Node widget){
    	HBox.setHgrow(widget, Priority.ALWAYS);
    	mTopBox.getChildren().add(widget);
    }

    @FXML
    private void onCloseItemAction(){
    	mMainApp.terminateApplication();
    }

    @FXML
    private void onAddAppointmentItemAction(){
    	mMainApp.displayAppointmentDialog();
    }

    @FXML
    private void onAddTaskItemAction(){
    	mMainApp.displayTaskDialog();
    }

    @FXML
    private void onAddGroupItemAction(){
    	mMainApp.displayGroupDialog();
    }

    @FXML
    private void onAboutItemAction(){
    	mMainApp.displayAboutDialog();
    }
}
