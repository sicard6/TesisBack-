package simulator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Moment {

	private ArrayList<Event> events;
	private Calendar time;
	
	public Moment(Calendar time)
	{
		this.time=time;
		events= new ArrayList<Event>();
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}
	
	
}
