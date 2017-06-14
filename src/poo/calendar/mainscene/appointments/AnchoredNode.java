package poo.calendar.mainscene.appointments;

/**
 * Class for storing the desired information about a single AppointmentView
 * being displayed in this Port.
 */
public class AnchoredNode implements Comparable<AnchoredNode> {
	private int topIndex;
	private int bottomIndex;
	AppointmentViewController AVC;
	
	public AnchoredNode(AppointmentViewController AVC_in){
		this.AVC = AVC_in;
	}
	
	/**
	 * Node with lower topIndex comes first.
	 * If equal, Node with higher bottomIndex comes first.
	 * Else, comparison by UUID.
	 */
	public int compareTo(AnchoredNode other){
		if(this.AVC.getID() == other.AVC.getID()) return 0;
		
		if(this.topIndex < other.topIndex)
			return -1;
		else if(this.topIndex > other.topIndex)
			return 1;
		else {
			if(this.bottomIndex > other.bottomIndex)
				return -1;
			else if(this.bottomIndex < other.bottomIndex)
				return 1;
			else return this.AVC.getID().compareTo(other.AVC.getID());
		}
	}

	public int getTopIndex() {
		return topIndex;
	}

	public void setTopIndex(int topIndex) {
		this.topIndex = topIndex;
	}

	public int getBottomIndex() {
		return bottomIndex;
	}

	public void setBottomIndex(int bottomIndex) {
		this.bottomIndex = bottomIndex;
	}

	public AppointmentViewController getAVC() {
		return AVC;
	}
}