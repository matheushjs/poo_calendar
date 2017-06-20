package poo.calendar.model.xmlwrappers;

import java.io.File;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.scene.paint.Color;
import poo.calendar.DateUtil;
import poo.calendar.model.Appointment;
import poo.calendar.model.CalendarDataModel;
import poo.calendar.model.CalendarGroup;
import poo.calendar.model.Recurrence;
import poo.calendar.model.Task;

/**
 * Class for saving and loading the Calendar Model data from a XML file.
 */
@XmlRootElement(name = "calendar")
public class ModelXMLHandler {
	List<AppointmentWrapper> mAppointments;
	List<TaskWrapper> mTasks;
	List<GroupWrapper> mGroups;
	
	public List<AppointmentWrapper> getmAppointments() {
		return mAppointments;
	}

	@XmlElement(name = "appointment")
	public void setmAppointments(List<AppointmentWrapper> mAppointments) {
		this.mAppointments = mAppointments;
	}

	public List<TaskWrapper> getmTasks() {
		return mTasks;
	}

	@XmlElement(name = "task")
	public void setmTasks(List<TaskWrapper> mTasks) {
		this.mTasks = mTasks;
	}

	public List<GroupWrapper> getmGroups() {
		return mGroups;
	}

	@XmlElement(name = "group")
	public void setmGroups(List<GroupWrapper> mGroups) {
		this.mGroups = mGroups;
	}

	/**
	 * Default constructor, just initialize the lists.
	 */
	public ModelXMLHandler(){
		mAppointments = new LinkedList<>();
		mTasks = new LinkedList<>();
		mGroups = new LinkedList<>();
	}
	
	/**
	 * Transforms all the data within the Calendar Data Model to a suitable List format.
	 * Then saves all these lists into a XML file.
	 * 
	 * @param model the Calendar Data Model to save
	 * @param file the File where to save the Model
	 */
	public static void saveModel(CalendarDataModel model, File file){
		ModelXMLHandler handler = new ModelXMLHandler();
		
        for(Appointment appt: model.getAppointments().values())
        	handler.mAppointments.add(new AppointmentWrapper(appt));
        
        for(Task task: model.getTasks().values())
        	handler.mTasks.add(new TaskWrapper(task));
        
        for(CalendarGroup cg: model.getGroups().values())
        	handler.mGroups.add(new GroupWrapper(cg));
		
	   	try {
    		JAXBContext context = JAXBContext.newInstance(ModelXMLHandler.class);
    		Marshaller marshaller = context.createMarshaller();

    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    		marshaller.marshal(handler, file);
    	} catch (JAXBException e) {
    		e.printStackTrace();
    	}
	}
	
	/**
	 * Loads the Calendar Data Model with data from the XML file.
	 * First reads the XML file, getting the lists of Wrapped data.
	 * Then convert each Wrapped date in the corresponding data, adding them to the Calendar Model.
	 * 
	 * @param model the Calendar Data Model object to which we should save the lists read from the XML
	 * @param file the file from which to read the data.
	 */
	public static void loadModel(CalendarDataModel model, File file){
	    try {
	        JAXBContext context = JAXBContext.newInstance(ModelXMLHandler.class);
	        Unmarshaller um = context.createUnmarshaller();

	        ModelXMLHandler handler = (ModelXMLHandler) um.unmarshal(file);
	        
	        for(AppointmentWrapper appt: handler.getmAppointments())
	        	model.addAppointment(appt.getAppointment());
	        
	        for(TaskWrapper task: handler.getmTasks())
	        	model.addTask(task.getTask());
	        
	        for(GroupWrapper cg: handler.getmGroups())
	        	model.addGroup(cg.getCalendarGroup());
	    } catch (JAXBException e){
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * Wrapper class for saving Appointments in the XML file.
	 */
	static class AppointmentWrapper {
		private String mTitle;
		private String mDescription;
		private UUID mGroupID;
		private UUID mID;
		private Calendar mInitDate;
		private Calendar mEndDate;
		private Recurrence mRecurrence;
		
		public AppointmentWrapper(){}
		
		public AppointmentWrapper(Appointment appt){
			mTitle = appt.getTitle();
			mDescription = appt.getDescription();
			mGroupID = appt.getGroupID();
			mID = appt.getID();
			mInitDate = appt.getInitDate();
			mEndDate = appt.getEndDate();
			mRecurrence = appt.getRecurrence();
		}
		
		public Appointment getAppointment(){
			Appointment appt = new Appointment(mTitle, mDescription, mInitDate, mEndDate, mGroupID);
			appt.setRecurrence(mRecurrence);
			appt.setID(mID);
			return appt;
		}
		
		public String getmTitle() {
			return mTitle;
		}
		
		@XmlElement
		public void setmTitle(String mTitle) {
			this.mTitle = mTitle;
		}
		
		public String getmDescription() {
			return mDescription;
		}
		
		@XmlElement
		public void setmDescription(String mDescription) {
			this.mDescription = mDescription;
		}
		
		public String getmGroupID() {
			return mGroupID.toString();
		}
		
		@XmlElement
		public void setmGroupID(String mGroupID) {
			this.mGroupID = UUID.fromString(mGroupID);
		}
		
		public String getmID() {
			return mID.toString();
		}
		
		@XmlAttribute
		public void setmID(String mID) {
			this.mID = UUID.fromString(mID);
		}
		
		public String getmInitDate() {
			return DateUtil.dateString(mInitDate) + "/-/" + DateUtil.hourString(mInitDate);
		}
		
		@XmlElement
		public void setmInitDate(String mInitDate) {
			String[] s = mInitDate.split("/-/");
			this.mInitDate = DateUtil.parseFields(s[0], s[1]);
		}
		
		public String getmEndDate() {
			return DateUtil.dateString(mEndDate) + "/-/" + DateUtil.hourString(mEndDate);
		}
		
		@XmlElement
		public void setmEndDate(String mEndDate) {
			String[] s = mEndDate.split("/-/");
			this.mEndDate = DateUtil.parseFields(s[0], s[1]);
		}
		
		public String getmRecurrence() {
			return mRecurrence.toString();
		}
		
		@XmlElement
		public void setmRecurrence(String mRecurrence) {
			this.mRecurrence = Recurrence.valueOf(mRecurrence);
		}
	}
	
	/**
	 * Wrapper class for saving Tasks in the XML file.
	 */
	static class TaskWrapper {
		private String mTitle;
		private String mDescription;
		private UUID mGroupID;
		private UUID mID;
		private Calendar mDeadlineDate;
		
		public TaskWrapper(){}
		
		public TaskWrapper(Task task){
			mTitle = task.getTitle();
			mDescription = task.getDescription();
			mGroupID = task.getGroupID();
			mID = task.getID();
			mDeadlineDate = task.getDeadlineDate();
		}
		
		public Task getTask(){
			Task task = new Task(mTitle, mDescription, mDeadlineDate, mGroupID);
			task.setID(mID);
			return task;
		}
		
		public String getmTitle() {
			return mTitle;
		}
		
		@XmlElement
		public void setmTitle(String mTitle) {
			this.mTitle = mTitle;
		}
		
		public String getmDescription() {
			return mDescription;
		}
		
		@XmlElement
		public void setmDescription(String mDescription) {
			this.mDescription = mDescription;
		}
		
		public String getmGroupID() {
			return mGroupID.toString();
		}
		
		@XmlElement
		public void setmGroupID(String mGroupID) {
			this.mGroupID = UUID.fromString(mGroupID);
		}
		
		public String getmID() {
			return mID.toString();
		}
		
		@XmlAttribute
		public void setmID(String mID) {
			this.mID = UUID.fromString(mID);
		}
		
		public String getmDeadlineDate() {
			if(mDeadlineDate == null) return "";
			else return DateUtil.dateString(mDeadlineDate) + "/-/" + DateUtil.hourString(mDeadlineDate);
		}
		
		@XmlElement
		public void setmDeadlineDate(String mDeadlineDate) {
			if(mDeadlineDate.compareTo("") == 0){
				this.mDeadlineDate = null;
				return;
			}
			
			String[] s = mDeadlineDate.split("/-/");
			this.mDeadlineDate = DateUtil.parseFields(s[0], s[1]);
		}
		
		
	}
	
	/**
	 * Wrapper class for saving Calendar Groups in the XML file.
	 */
	static class GroupWrapper {
		private String mName;
		private Color mColor;
		private UUID mID;
		
		public GroupWrapper(){}
		
		public GroupWrapper(CalendarGroup group){
			mName = group.getName();
			mColor = group.getColor();
			mID = group.getID();
		}
		
		public CalendarGroup getCalendarGroup(){
			CalendarGroup cg = new CalendarGroup(mName, mColor);
			cg.setID(mID);
			return cg;
		}
		
		public String getmName() {
			return mName;
		}
		
		@XmlElement
		public void setmName(String mName) {
			this.mName = mName;
		}
		
		public String getmColor() {
			return mColor.toString();
		}
		
		@XmlElement
		public void setmColor(String mColor) {
			this.mColor = Color.valueOf(mColor);
		}
		
		public String getmID() {
			return mID.toString();
		}
		
		@XmlAttribute
		public void setmID(String mID) {
			this.mID = UUID.fromString(mID);
		}
	}
}