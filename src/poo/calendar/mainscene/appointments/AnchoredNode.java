package poo.calendar.mainscene.appointments;

/**
 * Class for storing the desired information about a single AppointmentView
 * being displayed in this Port.
 */
public class AnchoredNode implements Comparable<AnchoredNode> {
	private int topIndex;
	private int bottomIndex;
	private double leftAnchor;
	private double rightAnchor;

	AppointmentViewController AVC;
	
	public AnchoredNode(AppointmentViewController AVC_in){
		this.AVC = AVC_in;
		leftAnchor = 0.0;
		rightAnchor = 0.0;
		topIndex = 0;
		bottomIndex = 1;
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

	public double getLeftAnchor() {
		return leftAnchor;
	}

	public void setLeftAnchor(double leftAnchor) {
		this.leftAnchor = leftAnchor;
	}

	public double getRightAnchor() {
		return rightAnchor;
	}

	public void setRightAnchor(double rightAnchor) {
		this.rightAnchor = rightAnchor;
	}
	
	public void setBottomIndex(int bottomIndex) {
		this.bottomIndex = bottomIndex;
	}
	
	public AppointmentViewController getAVC() {
		return AVC;
	}
}