package poo.calendar;

/**
 * Class for handling generation of unique IDs along the program
 */
public class IDGenerator {
	private static int mCounter = 0;
	
	// Prevent instantiation
	private IDGenerator(){}
	
	/**
	 * @return a unique identifier number.
	 */
	public static int getID(){
		int id = mCounter;
		mCounter++;
		return id;
	}
}
