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
		//TODO: Check if ControlledWidget constructor is automatically called
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
		
		AnchorPane.setTopAnchor(mHourRange, 0.0);
		AnchorPane.setRightAnchor(mHourRange, 0.0);
		AnchorPane.setLeftAnchor(mHourRange, 0.0);
		mHourRange.setAlignment(Pos.TOP_RIGHT);
		
		AnchorPane.setBottomAnchor(mIDLabel, 0.0);
		AnchorPane.setRightAnchor(mIDLabel, 0.0);
		AnchorPane.setLeftAnchor(mIDLabel, 0.0);
		mIDLabel.setAlignment(Pos.BOTTOM_LEFT);
		
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
	 * @param title the appointment's title
	 * @param color the appointment's background color
	 * @param id the source appointment's ID
	 * @throws NullPointerException if any argument is null
	 */
	public void initializeModel(String title, Color bg, UUID id) throws NullPointerException {	
		if(title == null || bg == null || id == null)
			throw new NullPointerException();
		
		mID = id;
		mTitle.setText(title);
		mIDLabel.setText(id.toString());
		mIDLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
		setBackground(bg);
	}
	
	private void setBackground(Color bg){
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
