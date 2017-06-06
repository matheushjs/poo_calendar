package poo.calendar;

/**
 * Class for handling generation of unique IDs along the program
 */
public class IDGenerator {
	private static long mCounter = 0;
	
	// Prevent instantiation
	private IDGenerator(){}
	
	/**
	 * @return a unique identifier number.
	 */
	public static long getID(){
		long id = mCounter;
		mCounter++;
		return id;
	}
}
