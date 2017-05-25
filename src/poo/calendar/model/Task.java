package poo.calendar.model;

import java.util.Calendar;

public class Task {
	private String d_title;
	private Calendar d_date;
	
	public Task(String title, Calendar date) throws IllegalArgumentException {
		d_title = title;
		d_date = date;
		//TODO: validate date
	}
}
