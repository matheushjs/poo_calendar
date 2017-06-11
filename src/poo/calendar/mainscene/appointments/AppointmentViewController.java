package poo.calendar.mainscene.appointments;

import java.util.UUID;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import poo.calendar.ControlledWidget;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarGroup;

/**
 * Class for controlling a single appointment view.
 */
public class AppointmentViewController extends ControlledWidget<AnchorPane> {
	private AnchorPane mMainPane;
	private Label mTitle;
	private Label mHourRange;
	private Label mIDLabel;
	
	private UUID mID;
	
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
		mIDLabel = new Label();
		
		AnchorPane.setBottomAnchor(mTitle, 0.0);
		AnchorPane.setTopAnchor(mTitle, 0.0);
		AnchorPane.setRightAnchor(mTitle, 0.0);
		AnchorPane.setLeftAnchor(mTitle, 0.0);
		mTitle.setTextOverrun(OverrunStyle.ELLIPSIS);
		mTitle.setAlignment(Pos.TOP_LEFT);
		//TODO: Clip the title to not override the hour range.
		//TODO: Either hide the hour range based on this view's height
		//TODO: Or make 2 labels, one to the top(smaller), other to the center.
		
		AnchorPane.setTopAnchor(mHourRange, 0.0);
		AnchorPane.setRightAnchor(mHourRange, 0.0);
		AnchorPane.setLeftAnchor(mHourRange, 0.0);
		mHourRange.setAlignment(Pos.TOP_RIGHT);
		
		AnchorPane.setBottomAnchor(mIDLabel, 0.0);
		AnchorPane.setRightAnchor(mIDLabel, 0.0);
		AnchorPane.setLeftAnchor(mIDLabel, 0.0);
		mIDLabel.setAlignment(Pos.BOTTOM_LEFT);
		mIDLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
		
		mMainPane.getChildren().addAll(mTitle, mHourRange, mIDLabel);
	}
	
	/**
	 * @return The controller widget
	 */
	public AnchorPane getWidget(){
		return mMainPane;
	}
	
	/**
	 * Sets up the AppointmentView to display the information given as parameter.
	 * This widget obverve's the source Appointment's title, and the CalendarGroup's color.
	 * The dates are not observed.
	 * 
	 * @param appt Appointment that is displayed
	 * @param cg Group of the appointment displayed
	 * @throws NullPointerException if any argument is null
	 */
	public void initializeModel(Appointment appt, CalendarGroup cg, String hourRange) throws NullPointerException {	
		if(appt == null || cg == null)
			throw new NullPointerException();
		
		mID = appt.getID();
		mIDLabel.setText(mID.toString());

		appt.titleProperty().addListener(change -> setTitle(appt.getTitle()));
		cg.colorProperty().addListener(change -> setColor(cg.getColor()));
		
		setTitle(appt.getTitle());
		setColor(cg.getColor());
		setHourRange(hourRange);
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
	private void setColor(Color bg){
		//TODO: Decide the label's font color
		mMainPane.setBackground(new Background(new BackgroundFill(Paint.valueOf(bg.toString()), null, null)));
	}
	
	/**
	 * @return this class's ID
	 */
	public UUID getID(){
		return mID;
	}
}
