package poo.calendar.model;

import java.util.Calendar;

public class Appointment {
	private String d_title;
	private Calendar d_initDate;
	private Calendar d_endDate;
	
	public Appointment(String title, Calendar initDate, Calendar endDate){
		title = d_title;
		d_initDate = initDate;
		d_endDate = endDate;
		//TODO: Validade dates
	}
}