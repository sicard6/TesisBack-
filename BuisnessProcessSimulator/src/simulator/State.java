package simulator;

import java.util.Calendar;
import java.util.Date;

public class State {
	private Timeline timeline;
	private Calendar  currentTime;
	private Calendar spentTime;
	
	public State(Timeline timeline,Calendar currentTime) {
		super();
		this.timeline = timeline;
		this.currentTime=currentTime;
	}

	public Timeline getTimeline() {
		return timeline;
	}

	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}

	public Calendar getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Calendar currentTime) {
		this.currentTime = currentTime;
	}

	public Calendar getSpentTime() {
		return spentTime;
	}

	public void setSpentTime(Calendar spentTime) {
		this.spentTime = spentTime;
	}
}
