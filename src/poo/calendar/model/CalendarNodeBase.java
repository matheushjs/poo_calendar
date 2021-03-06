package poo.calendar.model;

import java.util.UUID;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A model abstract class that contains the common attributes of all nodes
 * in the calendar.
 */
public abstract class CalendarNodeBase {
	// The Node's title
	private StringProperty mTitle;

	// Node's description
	private StringProperty mDescription;
	
	// Node's Group ID
	private ObjectProperty<UUID> mGroupID;
	
	// The Node's ID
	private UUID mID;
	
	/**
	 * Node's default constructor
	 * @param title the node's title
	 * @param description the node's description
	 * @param groupID the node's group ID
	 * @throws NullPointerException When any argument, but groupID, is null
	 */
	public CalendarNodeBase(String title, String description, UUID groupID) throws NullPointerException {
		mTitle = new SimpleStringProperty();
		mDescription = new SimpleStringProperty();
		mGroupID = new SimpleObjectProperty<UUID>();
		
		this.setTitle(title);
		this.setDescription(description);
		this.setGroupID(groupID);
		
		mID = UUID.randomUUID();
	}

	/**
	 * @return the node's title
	 */
	public final String getTitle(){
		return mTitle.get();
	}
	
	/**
	 * @param str new title for the node
	 * @throws NullPointerException if given title is null
	 */
	public final void setTitle(String str) throws NullPointerException {
		if(str == null)
			throw new NullPointerException("Title for a calendar node cannot be null");
		mTitle.set(str);
	}
	
	/**
	 * @return title property of the node
	 */
	public StringProperty titleProperty(){
		return mTitle;
	}
	
	/**
	 * @return the node's description
	 */
	public final String getDescription(){
		return mDescription.get();
	}
	
	/**
	 * @param desc new description for the node
	 * @throws NullPointerException if argument is null
	 */
	public final void setDescription(String desc) throws NullPointerException {
		if(desc == null)
			throw new NullPointerException("Description of a calendar node cannot be null");
		mDescription.set(desc);
	}
	
	/**
	 * @return the node's description property
	 */
	public StringProperty descriptionProperty(){
		return mDescription;
	}
	
	/**
	 * @return the ID for the group to which this node is related
	 */
	public final UUID getGroupID(){
		return mGroupID.get();
	}
	
	/**
	 * @param id the ID for the new group to which this node should be related to
	 */
	public final void setGroupID(UUID id){
		if(id == null)
			id = CalendarGroup.DEFAULT_ID;
		mGroupID.set(id);
	}
	
	/**
	 * @return ID property of the node
	 */
	public ObjectProperty<UUID> groupIDProperty(){
		return mGroupID;
	}
	
	/**
	 * @return the ID of the node
	 */
	public final UUID getID(){
		return mID;
	}
	
	/**
	 * @param id new ID for the node
	 */
	public final void setID(UUID id){
		mID = id;
	}
}