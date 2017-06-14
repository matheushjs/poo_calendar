package poo.calendar.mainscene.appointments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import poo.calendar.ControlledWidget;
import poo.calendar.DateUtil;
import poo.calendar.controller.MainApplication;
import poo.calendar.model.CalendarDataModel;

/**
 * Class for controlling the appointment day port, where all appointments for a given day are displayed.
 * Each DayPort is an AnchorPane of fixed width.
 * For internal calculations, every Appointment is rounded to fit in 15-minute intervals.
 */
public class AppointmentDayPortController extends ControlledWidget<AnchorPane> {
	// Height of the widget. The widget will have this size as a strong limit,
	// meaning it won't expand nor shrink.
	private static int PORT_HEIGHT = 2016; //Divisible by 96
	
	// Main Node
	private AnchorPane mMainPane;
	
	// List containing 96 other lists, where each represents a 15-minute interval in a day.
	// Each inner list contains all AnchoredNodes that displayed in that interval.
	private List<List<AnchoredNode>> mIntervalInfo;
	
	// Array of controllers of AppointmentViews summoned by this DayPort.
	private Map<UUID, AnchoredNode> mAnchoredNodes;
	
	// Link to the main application
	private MainApplication mMainApp;
	
	// Calendar storing the date this DayPort represents
	private Calendar mAssignedDay;
	
	// Model data
	private CalendarDataModel mModel;
	
	/**
	 * Default constructor
	 * @param day the day this DayPort represents
	 */
	public AppointmentDayPortController(Calendar day) {
		mAssignedDay = (Calendar) day.clone();
		DateUtil.resetToDate(mAssignedDay);
		
		mIntervalInfo = new ArrayList<List<AnchoredNode>>(96);
		for(int i = 0; i < 96; i++)
			mIntervalInfo.add(new LinkedList<AnchoredNode>());
		
		mAnchoredNodes = new HashMap<>();
	}
	
	/**
	 * Sets the controlled widget to a valid state, independent of any model data.
	 * Automatically called upon construction.
	 */
	protected void initializeWidget(){
		mMainPane = new AnchorPane();
		mMainPane.setPrefHeight(PORT_HEIGHT);
		mMainPane.setMinHeight(PORT_HEIGHT);
		mMainPane.setMaxHeight(PORT_HEIGHT);
	}
	
	/**
	 * returns the controlled widget.
	 */
	public AnchorPane getWidget(){
		return mMainPane;
	}
	
	/**
	 * Receives the structures needed for working.
	 * @param app
	 * @param model
	 */
	public void initializeStructures(MainApplication app, CalendarDataModel model){
		mModel = model;
		mMainApp = app;
	}
	
	/**
	 * Adds an appointment to the DayPort.
	 * Each day is composed of 24*60 minutes. This method receives the number of minutes of where in the day
	 * the given appointment should begin and end.
	 * 
	 * initMinutes and endMinutes arguments must be used to control the exact spanning location of the
	 * appointment view.
	 * 
	 * @param initMinutes Number of minutes of the initial time of the appointment
	 * @param endMinutes Number of minutes of the ending time of the appointment
	 * @param id ID of the appointment being added
	 */
	public void addAppointment(int initMinutes, int endMinutes, UUID id){
		if(mModel == null){
			System.err.println("Must initialize model for AppointmentDayPortController");
			System.exit(1);
		}
		
		AppointmentViewController AVC = new AppointmentViewController();
		
		String range = DateUtil.hourString(initMinutes) + " - " + DateUtil.hourString(endMinutes);
		AVC.initializeStructures(mMainApp, mModel, id);
		AVC.setHourRange(range);
		
		/* Round initMinutes to the next lower 15-minute interval. */
		/* Round endMinutes to the next higher 15-minute interval. */
		initMinutes = initMinutes - (initMinutes % 15);
		endMinutes = endMinutes + (endMinutes % 15 == 0 ? 0 : 15 - (endMinutes % 15));
		
		/* Transform minutes into the correspondent 15-minute interval index (there are 96 intervals) 
		 * The 97th interval is a sentinel, since widgets don't actually span through endInterval*/
		int initInterval = initMinutes/15;
		int endInterval = endMinutes/15;
		
		/* Create the AnchoredNode that wraps the widget being added */
		/* Widget spans from initInterval inclusive, to endInterval exclusive */
		AnchoredNode AN = new AnchoredNode(AVC);
		AN.setTopIndex(initInterval);
		AN.setBottomIndex(endInterval);
		
		/* Add the new widget to whatever intervals it spans through */
		for(int i = initInterval; i < endInterval; i++){
			mIntervalInfo.get(i).add(AN);
		}
		
		/* Add the new AnchoredNode to the map */
		mAnchoredNodes.put(AVC.getID(), AN);
		
		/* Calculate the side anchors */
		decideWidgetVAnchors(AN);
		decideWidgetHAnchors(AN);
		mMainPane.getChildren().add(AN.getAVC().getWidget());
	}

	/**
	 * Sets the top/bottom anchors of the widget.
	 */
	private void decideWidgetVAnchors(AnchoredNode AN){
		Node widget = AN.getAVC().getWidget();
		
		double topAnchor = PORT_HEIGHT * AN.getTopIndex() / (double) 96 ;
		double bottomAnchor = PORT_HEIGHT * (1 - (AN.getBottomIndex() / (double) 96));
		
		AnchorPane.setTopAnchor(widget, topAnchor);
		AnchorPane.setBottomAnchor(widget, bottomAnchor);
	}
	
	/**
	 * Sets the left/right anchors of the widget.
	 * @param AN
	 */
	private void decideWidgetHAnchors(AnchoredNode AN){
		/* Gets all nodes that will be affected by the addition of AN */
		Set<AnchoredNode> affectedNodes = new HashSet<>();
		for(int i = AN.getTopIndex(), n = AN.getBottomIndex(); i < n; i++)
			affectedNodes.addAll(mIntervalInfo.get(i));
		
		relocateAnchoredNodes(affectedNodes);
	}
	
	/*
	 * Main procedure for deciding nodes' left/right anchors.
	 * All nodes in the given set are considered to be all nodes in a sub-interval of this DayPort.
	 * In other words, if there is a node outside of the set that shares an interval with any node that
	 * is inside the set, there will be overlaying of nodes.
	 */
	private void relocateAnchoredNodes(Set<AnchoredNode> affectedNodes){
		/* Calculates the bounds of the interval to analyze */
		int topIndex = Integer.MAX_VALUE;
		int bottomIndex = Integer.MIN_VALUE;
		for(AnchoredNode node: affectedNodes){
			topIndex = Integer.min(topIndex, node.getTopIndex());
			bottomIndex = Integer.max(bottomIndex, node.getBottomIndex());
		}
		
		/* Calculate max number of nodes in the same interval */
		int max = 0;
		for(int i = topIndex; i < bottomIndex; i++){
			max = Integer.max(max, mIntervalInfo.get(i).size());
		}
		
		/* Queue for removing AnchoredNodes in a ordered manner */
		Queue<AnchoredNode> q = new PriorityQueue<>(affectedNodes);
		
		/* We have a range of N intervals, and a max number of M nodes for each interval */
		/* So we create a NxM matrix of booleans representing where nodes have been allocated */
		boolean[][] mat = new boolean[bottomIndex - topIndex][max];
		
		/* Allocate each node in the port */
		while(!q.isEmpty()){
			AnchoredNode node = q.poll();
			
			/* Get bounds of node being added */
			int top = node.getTopIndex();
			int bottom = node.getBottomIndex();
			
			boolean good;
			for(int i = 0; i < max; i++){
				/* For each column, see if the node fits in that column */
				good = true;
				
				for(int j = top; j < bottom; j++){
					if(mat[j-top][i] == true){
						good = false;
						break;
					}
				}
				
				if(good == true){
					/* If could fit, invalidate the spots used */
					for(int j = top; j < bottom; j++)
						mat[j-top][i] = true;
					
					/* i holds the column to place the anchored node,
					 * and that's what we use to calculate the side anchors */
					double gap = mMainPane.getWidth() / (double) max;
					double leftAnchor = i * gap;
					double rightAnchor = (max - i - 1) * gap;
					AnchorPane.setLeftAnchor(node.getAVC().getWidget(), leftAnchor);
					AnchorPane.setRightAnchor(node.getAVC().getWidget(), rightAnchor);
					
					break;
				}
			}
		}
	}
	
	/**
	 * Removes an appointment view from this DayPort.
	 * Relocated remaining widgets if needed.
	 * @param id
	 */
	public void removeAppointmentView(UUID id){
		/* Get removed node */
		AnchoredNode removed = mAnchoredNodes.remove(id);
		if(removed == null) return;
		
		mMainPane.getChildren().remove(removed.getAVC().getWidget());
		
		/* Get affected nodes while removing the node from the list of intervals */
		Set<AnchoredNode> affected = new HashSet<>();
		for(int i = 0; i < mIntervalInfo.size(); i++){
			if(mIntervalInfo.get(i).remove(removed)){
				affected.addAll(mIntervalInfo.get(i));
			}
		}
		
		relocateAnchoredNodes(affected);
	}
	
	/**
	 * @return DAY_OF_MONTH this DayPort represents
	 */
	public int getAssignedDay(){
		return mAssignedDay.get(Calendar.DATE);
	}
	
	/**
	 * Removes all appointments in the Port.
	 */
	public void removeAll(){
		mMainPane.getChildren().clear();
		mAnchoredNodes.clear();
		for(List<AnchoredNode> i: mIntervalInfo)
			i.clear();
	}
}
