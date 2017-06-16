package poo.calendar.mainscene;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import poo.calendar.controller.MainApplication;

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
	private Text mAppTitleText;
	
	@FXML
	private Text mDateText;
	
	@FXML
	private HBox mBottomBox;
	
	private MainApplication mMainApp;
	
	private Background mainBG =
			new Background(new BackgroundFill(Color.DARKGRAY, null, null));
	
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
    	mMainBox.setBackground(mainBG);
    	
    	DropShadow ds = new DropShadow();
    	ds.setOffsetY(3.0f);
    	ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
    	mAppTitleText.setEffect(ds);
    	mAppTitleText.setCache(true);
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
    	
    }
}
