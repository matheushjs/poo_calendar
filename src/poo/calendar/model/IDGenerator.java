package poo.calendar.model;

public class IDGenerator {
	private static long mCounter = 0;
	
	private IDGenerator(){}
	
	public static long getID(){
		long id = mCounter;
		mCounter++;
		return id;
	}
}
