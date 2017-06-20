package poo.calendar.mainscene.appointments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.Recurrence;


/**
 * Widget class that displays the calendar window.
 * It's a vertical box containing a list of appointments and 2 buttons.
 *
 * This class observes:
 * 	- The model map of appointments
 *  - Every appointment's date/title properties.
 */
public class AppointmentWindowController {
	@FXML
	private AnchorPane mMainPane;

	@FXML
	private HBox mWeekTextBox;

	@FXML
	private Text mWeekText;

	@FXML
	private ScrollPane mInnerScrollPane;

	@FXML
	private AnchorPane mInnerPane;

	@FXML
	private VBox mLabelsBox;

	@FXML
	private HBox mInnerBox;

	@FXML
	private HBox mWeekdaysBox;

	@FXML
	private Button mAddButton;

	@FXML
	private Button mLeftButton;

	@FXML
	private Button mRightButton;

	// Fade transitions for buttons
	private FadeTransition[] mButtonFadeUp, mButtonFadeDown;

	// Fade transitions for WeekdaysBox and WeekText
	private FadeTransition[] mWeekFade, mWeekFadeBack;

	private MainApplication mMainApp;
	private ArrayList<AppointmentDayPortController> mDayPorts;
	private Calendar mAssignedWeek;

	//Model data
	private CalendarDataModel mModel;
	
	//Timeline
	private Rectangle mTimeline;

	/**
	 * Default constructor
	 */
	public AppointmentWindowController(){
		mDayPorts = new ArrayList<>();

		mAssignedWeek = Calendar.getInstance();
		DateUtil.resetToWeek(mAssignedWeek);

		Calendar auxCalendar = (Calendar) mAssignedWeek.clone();
		for(int i = 0; i < 7; i++){
			AppointmentDayPortController adpc = new AppointmentDayPortController(auxCalendar);
			mDayPorts.add(adpc);

			auxCalendar.add(Calendar.DAY_OF_WEEK, 1);
		}

		mButtonFadeUp = new FadeTransition[3];
		mButtonFadeDown = new FadeTransition[3];

		for(int i = 0; i < 3; i++){
			mButtonFadeUp[i] = new FadeTransition(Duration.millis(500));
			mButtonFadeDown[i] = new FadeTransition(Duration.millis(500));

			mButtonFadeUp[i].setFromValue(0.3);
			mButtonFadeUp[i].setToValue(1.0);
			mButtonFadeDown[i].setFromValue(1.0);
			mButtonFadeDown[i].setToValue(0.3);
		}

		mWeekFade = new FadeTransition[2];
		mWeekFadeBack = new FadeTransition[2];
		for(int i = 0; i < 2; i++){
			mWeekFade[i] = new FadeTransition(Duration.millis(1000));
			mWeekFadeBack[i] = new FadeTransition(Duration.millis(1000));

			mWeekFade[i].setFromValue(0.0);
			mWeekFade[i].setToValue(0.7);

			mWeekFadeBack[i].setFromValue(0.7);
			mWeekFadeBack[i].setToValue(0.0);

			final int lambdai = i;
			mWeekFade[i].setOnFinished(action -> {
				mWeekFadeBack[lambdai].playFromStart();

			});
		}

		// Weekday animation fades away 3 seconds earlier.
		mWeekFadeBack[0].setDelay(Duration.seconds(2));
		mWeekFadeBack[1].setDelay(Duration.seconds(5));
	}

	/**
	 * Called by FXML -after- the .fxml is loaded
	 */
	@FXML
	private void initialize(){
		for(int i = 0, n = 24; i < n; i++){
			Label lbl = new Label(i + "h");
			lbl.minHeightProperty().bind(mLabelsBox.heightProperty().divide(24.0));
			mLabelsBox.getChildren().add(lbl);
		}

		for(AppointmentDayPortController adpc: mDayPorts){
			AnchorPane widget = adpc.getWidget();
			widget.prefWidthProperty().bind(mInnerBox.widthProperty().divide(7.0));
			widget.maxWidthProperty().bind(widget.prefWidthProperty());
			mInnerBox.getChildren().add(widget);
		}
		mInnerPane.setMinWidth(800.0);
		mInnerBox.prefWidthProperty().bind(mInnerPane.widthProperty().subtract(mLabelsBox.widthProperty().add(5))); //Add a spacing of 5
		mInnerBox.maxWidthProperty().bind(mInnerBox.prefWidthProperty());

		mInnerBox.setSpacing(5.0);

		/*
		 * Setup the WeekdaysBox, which is the box that holds the labels containing each weekday name.
		 * Each label is positioned right over the corresponding DayPort.
		 */
		mInnerPane.widthProperty().addListener((obs, oldval, newval) -> {
			AnchorPane.setRightAnchor(mWeekdaysBox, mInnerScrollPane.getWidth() - newval.doubleValue());
		});
		mWeekdaysBox.setOpacity(0.0);
		mWeekdaysBox.spacingProperty().bind(mWeekdaysBox.widthProperty().divide(14.0));
		mWeekdaysBox.setPadding(new Insets(0, 30, 0, 30));
		mLabelsBox.widthProperty().addListener((obs, oldval, newval) -> AnchorPane.setLeftAnchor(mWeekdaysBox, newval.doubleValue()));
		String[] weekdays = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		Background lblFill = new Background(new BackgroundFill(Paint.valueOf("grey"), null, null));
		for(int i = 0; i < 7; i++){
			Label lbl = new Label(weekdays[i]);
			HBox.setHgrow(lbl, Priority.ALWAYS);
			lbl.setMaxWidth(Double.MAX_VALUE);
			lbl.setPrefHeight(30);
			lbl.setTextFill(Paint.valueOf("white"));
			lbl.setAlignment(Pos.CENTER);
			lbl.setBackground(lblFill);
			mWeekdaysBox.getChildren().add(lbl);
		}
		mWeekFade[0].setNode(mWeekdaysBox);
		mWeekFadeBack[0].setNode(mWeekdaysBox);

		mWeekText.setOpacity(0.0);
		mWeekFade[1].setNode(mWeekText);
		mWeekFadeBack[1].setNode(mWeekText);

		mAddButton.setOpacity(0.3);
		mLeftButton.setOpacity(0.3);
		mRightButton.setOpacity(0.3);

		mButtonFadeUp[0].setNode(mAddButton);
		mButtonFadeDown[0].setNode(mAddButton);
		mButtonFadeUp[1].setNode(mLeftButton);
		mButtonFadeDown[1].setNode(mLeftButton);
		mButtonFadeUp[2].setNode(mRightButton);
		mButtonFadeDown[2].setNode(mRightButton);

		mAddButton.setOnMouseEntered(action -> mButtonFadeUp[0].playFromStart());
		mAddButton.setOnMouseExited(action -> mButtonFadeDown[0].playFromStart());
		mLeftButton.setOnMouseEntered(action -> mButtonFadeUp[1].playFromStart());
		mLeftButton.setOnMouseExited(action -> mButtonFadeDown[1].playFromStart());
		mRightButton.setOnMouseEntered(action -> mButtonFadeUp[2].playFromStart());
		mRightButton.setOnMouseExited(action -> mButtonFadeDown[2].playFromStart());

		adjustScroll(Calendar.getInstance());

		DayIntervalsCanvas DIC = new DayIntervalsCanvas(mInnerPane);
		mInnerPane.getChildren().add(DIC);
		DIC.toBack();

		mWeekFadeBack[1].setOnFinished(action -> mMainPane.getChildren().remove(mWeekTextBox));
		mWeekFadeBack[0].setOnFinished(action -> mMainPane.getChildren().remove(mWeekdaysBox));

		//Initializing mTimeline properties
		mTimeline = new Rectangle();
		mTimeline.widthProperty().bind(mInnerPane.widthProperty());
		mTimeline.setHeight(3.0);
		mTimeline.setFill(Color.RED);
		mTimeline.setOpacity(0.5);
		AnchorPane.setTopAnchor(mTimeline, 0.0);
		AnchorPane.setLeftAnchor(mTimeline, 0.0);
		AnchorPane.setRightAnchor(mTimeline, 0.0);
		mInnerPane.getChildren().add(mTimeline);
		
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				Calendar cal = Calendar.getInstance();
				AnchorPane.setTopAnchor(mTimeline, 1.4*((cal.get(Calendar.HOUR_OF_DAY)*60)+cal.get(Calendar.MINUTE)));
			}
		}.start();
		
		runWeekAnimations();
	}

	/**
	 * Adjusts the ScrollPane to show the time represented by the given calendar.
	 * @param calendar The calendar whose time should be visible to the user in the scroll pane.
	 */
	private void adjustScroll(Calendar calendar){
		double ratio = DateUtil.minuteCount(calendar) / (double) DateUtil.MINUTES_IN_DAY;

		if(ratio > 0.6) ratio += (1 - ratio)/2;
		else if(ratio < 0.4) ratio /= 2;

		mInnerScrollPane.setVvalue(ratio);
	}

	/**
	 * Receives the structures needed for working.
	 */
	public void initializeStructures(MainApplication app, CalendarDataModel model){
		if(mModel != null){
			System.err.println(this.getClass().getName() + ": Can only initialize model once.");
			System.exit(1);
		}

		for(AppointmentDayPortController adpc: mDayPorts){
			adpc.initializeStructures(app, model);
		}
		
		mModel = model;
		mModel.getAppointments().forEach((uuid, appt) -> {
			this.addAppointmentView(appt, false);
			this.prepareAppointment(appt);
		});

		mModel.getAppointments().addListener((MapChangeListener.Change<? extends UUID, ? extends Appointment> change) -> {
			if(change.wasRemoved()){
				removeAppointmentView(change.getKey(), true);
			}

			if(change.wasAdded()){
				this.addAppointmentView(change.getValueAdded(), true);
				this.prepareAppointment(change.getValueAdded());
			}
		});

		mMainApp = app;
		mAddButton.setOnAction(action -> {
			mMainApp.displayAppointmentDialog();
		});

		mRightButton.setOnMouseClicked(action -> changeWeek(1, true));
		mLeftButton.setOnMouseClicked(action -> changeWeek(-1, true));
		
		//If pane changes width, relocate appointments
		mMainPane.widthProperty().addListener(change -> changeWeek(0, false));
	}

	/**
	 * Adds an appointment to the UI. The appointment can be any appointment,
	 * with any initDate or endDate. This method only those that belong to the
	 * current week being displayed.
	 * @param appointment Appointment to be added.
	 * @param doAnimation whether appointment relocation should be animated or not
	 */
	private void addAppointmentView(Appointment appointment, boolean doAnimation){
		Calendar init, end;
		Recurrence rec = appointment.getRecurrence();

		init = (Calendar) appointment.getInitDate().clone();
		end = (Calendar) appointment.getEndDate().clone();

		// Gets the interval being displayed.
		Calendar begOfWeek = (Calendar) mAssignedWeek.clone();
		Calendar endOfWeek = (Calendar) mAssignedWeek.clone();
		endOfWeek.add(Calendar.DAY_OF_WEEK, 6);

		// Assumes Appointment does not belong to the current week.
		boolean shouldDisplay = false;

		/*
		 * Decide if should display or not.
		 */
		if(rec == Recurrence.DAILY || rec == Recurrence.WEEKLY || rec == Recurrence.NONE) {
			shouldDisplay = true;

		} else if(rec == Recurrence.MONTHLY) {
			DateUtil.translateInterval(Calendar.MONTH, begOfWeek, init, end);
			shouldDisplay = DateUtil.hasDayIntersection(begOfWeek, endOfWeek, init, end);

		} else if(rec == Recurrence.YEARLY) {
			DateUtil.translateInterval(Calendar.YEAR, begOfWeek, init, endOfWeek);
			shouldDisplay = DateUtil.hasDayIntersection(begOfWeek, endOfWeek, init, end);
		}

		if(!shouldDisplay) return; // Nothing else to do.

		/*
		 * If should display, add the appointment to all due ports.
		 */
		if(rec == Recurrence.DAILY) {
			 // Translate the interval to all 7 days of the week, plus last sunday, then add them.
			Calendar subjectDay = (Calendar) begOfWeek.clone();
			subjectDay.add(Calendar.DATE, -1);
			for(int i = 0; i < 8; i++){
				DateUtil.translateInterval(Calendar.DATE, subjectDay, init, end);

				addIntervalToPorts(appointment, init, end, doAnimation);

				subjectDay.add(Calendar.DATE, 1);
			}

		} else if(rec == Recurrence.WEEKLY) {
			DateUtil.translateInterval(Calendar.DAY_OF_WEEK, begOfWeek, init, end);
			addIntervalToPorts(appointment, init, end, doAnimation);

		} else {
			addIntervalToPorts(appointment, init, end, doAnimation);
		}
	}

	/**
	 *
	 * @param appointment
	 * @param init
	 * @param end
	 * @param doAnimation
	 */
	private void addIntervalToPorts(Appointment appointment, Calendar init, Calendar end, boolean doAnimation){
		/*
		 * If should display, add the appointment to all due ports.
		 */
		Calendar subjectDay = (Calendar) mAssignedWeek.clone();
		for(int i = 0; i < 7; i++){
			/* FOR EACH DAY IN CURRENT WEEK */
			
			/* CALCULATE THE OFFSETS OF THE APPOINTMENT IN THE DAY BEING ANALYZED */
			if(DateUtil.hasDayIntersection(subjectDay, init, end)){
				int offset1, offset2;
				boolean containsInit, containsEnd;

				containsInit = DateUtil.isDayOffset(subjectDay, init, 0);
				containsEnd = DateUtil.isDayOffset(subjectDay, end, 0);

				/* CASE 1: subjectDay is entirely contained in [init, end] */
				if( !containsInit && !containsEnd ){
					offset1 = 0;
					offset2 = DateUtil.MINUTES_IN_DAY;
				}
				/* CASE 2: subjectDay contains 'init', but not 'end'. */
				else if( containsInit && !containsEnd ){
					offset1 = DateUtil.minuteCount(init);
					offset2 = DateUtil.MINUTES_IN_DAY;
				}
				/* CASE 3: subjectDay contains 'end', but not 'init'. */
				else if( !containsInit && containsEnd ){
					offset1 = 0;
					offset2 = DateUtil.minuteCount(end);
				}
				/* CASE 4: subjectDay contains the whole interval [init, end] */
				else {
					offset1 = DateUtil.minuteCount(init);
					offset2 = DateUtil.minuteCount(end);
				}

				mDayPorts.get(i).addAppointment(offset1, offset2, appointment.getID(), doAnimation);
			}

			subjectDay.add(Calendar.DATE, 1);
		}
	}

	/**
	 * Removes an appointment from the list of appointments
	 * @param id the ID of the appointment to be removed
	 * @param doAnimation whether appointment relocation should be animated or not
	 */
	private void removeAppointmentView(UUID id, boolean doAnimation){
		for(AppointmentDayPortController adpc: mDayPorts){
			adpc.removeAppointmentView(id, doAnimation);
		}
	}

	private void prepareAppointment(Appointment appt){
		appt.initDateProperty().addListener((obs, oldval, newval) -> {
			removeAppointmentView(appt.getID(), true);
			addAppointmentView(appt, true);
		});
		appt.endDateProperty().addListener((obs, oldval, newval) -> {
			removeAppointmentView(appt.getID(), true);
			addAppointmentView(appt, true);
		});
		appt.recurrenceProperty().addListener((obs, oldval, newval) -> {
			removeAppointmentView(appt.getID(), true);
			addAppointmentView(appt, true);
		});
	}

	private void changeWeek(int offsetWeek, boolean doWeekAnimation){
		clearDayPorts();
		mAssignedWeek.add(Calendar.DATE, 7*offsetWeek);
		mModel.getAppointments().forEach((uuid, appt) -> addAppointmentView(appt, false));
		if(doWeekAnimation)
			runWeekAnimations();
	}

	private void clearDayPorts(){
		for(AppointmentDayPortController adpc: mDayPorts){
			adpc.removeAll();
		}
	}

	private void runWeekAnimations(){
		Calendar endOfWeek = (Calendar) mAssignedWeek.clone();
		endOfWeek.add(Calendar.DATE, 6);

		StringBuilder displayThis = new StringBuilder("");
		displayThis.append(DateUtil.monthString(mAssignedWeek.get(Calendar.MONTH)));
		displayThis.append(" " + mAssignedWeek.get(Calendar.DATE));
		displayThis.append(" - " + DateUtil.monthString(endOfWeek.get(Calendar.MONTH)));
		displayThis.append(" " + endOfWeek.get(Calendar.DATE));

		mWeekText.setText(displayThis.toString());

		if(!mMainPane.getChildren().contains(mWeekTextBox))
			mMainPane.getChildren().add(mWeekTextBox);

		if(!mMainPane.getChildren().contains(mWeekdaysBox))
			mMainPane.getChildren().add(mWeekdaysBox);

		if(mWeekFade[0].getStatus() != Animation.Status.RUNNING){
			mWeekFade[0].playFromStart();
		}

		// The text animation should
		if(mWeekFade[1].getStatus() != Animation.Status.RUNNING){
			mWeekFade[1].playFromStart();
		}
	}
}
