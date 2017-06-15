package poo.calendar.mainscene.appointments;

import java.util.UUID;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import poo.calendar.ColorUtil;
import poo.calendar.ControlledWidget;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.CalendarGroup;

/**
 * Class for controlling a single appointment view.
 * 
 * Each appointment view observes:
 * 	- source appointment's Title
 *  - source appointment's Group ID
 *  - source calendar group's Color
 */
public class AppointmentViewController extends ControlledWidget<AnchorPane> {
	private AnchorPane mMainPane;
	private Label mTitle;
	private Label mHourRange;
	
	private MainApplication mMainApp;
	private CalendarDataModel mModel;
	private UUID mID;
	
	// Keep a reference to the weak listener, to prevent garbage collection.
	private ChangeListener<Color> mGroupListener;
	
	/**
	 * Default constructor. Merely sets up the widget view.
	 */
	public AppointmentViewController(){
	}
	
	/**
	 * Initializes the widget to a valid state
	 */
	protected void initializeWidget(){
		mMainPane = new AnchorPane();
		mTitle = new Label();
		mHourRange = new Label();
		
		mMainPane.setCursor(Cursor.HAND);
		
		AnchorPane.setTopAnchor(mTitle, 0.0);
		AnchorPane.setRightAnchor(mTitle, 0.0);
		AnchorPane.setLeftAnchor(mTitle, 0.0);
		mTitle.setTextOverrun(OverrunStyle.ELLIPSIS);
		mTitle.setAlignment(Pos.TOP_LEFT);
		mTitle.setWrapText(true);
		mTitle.maxHeightProperty().bind(mMainPane.heightProperty());
		
		AnchorPane.setBottomAnchor(mHourRange, 0.0);
		AnchorPane.setRightAnchor(mHourRange, 0.0);
		AnchorPane.setLeftAnchor(mHourRange, 0.0);
		mTitle.heightProperty().addListener((obs, oldvalue, newvalue) -> {
			AnchorPane.setTopAnchor(mHourRange, newvalue.doubleValue());
		});
		mHourRange.setAlignment(Pos.TOP_CENTER);
		mHourRange.setTextOverrun(OverrunStyle.ELLIPSIS);
		
		mMainPane.getChildren().addAll(mTitle, mHourRange);
	}
	
	/**
	 * @return The controller widget
	 */
	public AnchorPane getWidget(){
		return mMainPane;
	}
	
	/**
	 * Receives structures needed for working.
	 * 
	 * @param app
	 * @param model
	 * @param appointmentID
	 * @throws NullPointerException if any argument is null
	 */
	public void initializeStructures(MainApplication app, CalendarDataModel model, UUID appointmentID) throws NullPointerException {	
		if(app == null || model == null || appointmentID == null)
			throw new NullPointerException();
		
		Appointment appt = model.getAppointment(appointmentID);
		
		mMainApp = app;
		mModel = model;
		
		mID = appt.getID();
		
		mMainPane.setOnMouseClicked(action -> mMainApp.displayAppointmentDialog(mID));
		
		appt.titleProperty().addListener((obs, oldval, newval) -> setTitle(newval));
		appt.groupIDProperty().addListener((obs, oldval, newval) -> {
			CalendarGroup group = mModel.getGroup(newval);
			mGroupListener = (obs2, oldval2, newval2) -> setColor(newval2);
			group.colorProperty().addListener(new WeakChangeListener<Color>(mGroupListener));
			
			setColor(mModel.getRefGroup(appt).getColor());
		});
		
		CalendarGroup cg = mModel.getRefGroup(appt);
		mGroupListener = (obs, oldval, newval) -> setColor(newval);
		cg.colorProperty().addListener(new WeakChangeListener<Color>(mGroupListener));
		
		setColor(cg.getColor());
		setTitle(appt.getTitle());
	}
	
	/**
	 * @param title The title to display
	 */
	private void setTitle(String title){
		mTitle.setText(title);
	}

	/**
	 * @param range The hour range to display
	 */
	public void setHourRange(String range){
		mHourRange.setText(range);
	}
	
	/**
	 * 
	 * @param bg
	 */
	public void setColor(Color bg){
		Color contrast = ColorUtil.contrastColor(bg);
		
		mTitle.setTextFill(Paint.valueOf(contrast.toString()));
		mHourRange.setTextFill(Paint.valueOf(contrast.toString()));
		
		mMainPane.setBackground(new Background(new BackgroundFill(Paint.valueOf(bg.toString()), null, null)));
	}
	
	/**
	 * @return this class's ID
	 */
	public UUID getID(){
		return mID;
	}
}
