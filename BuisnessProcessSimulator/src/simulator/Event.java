package simulator;

import model.Activity;
import model.DiagramPackage;

public class Event {
	private String eventType;
	private String name;
	private DiagramPackage diagramPackage;
	private Activity activity;
	private boolean visited;
	public static final String PROCESS_START="processStart";
	public static final String PROCESS_END="processEnd";
	public static final String ACTIVITY_START="activiyStart";
	public static final String ACTIVITY_END="activityEnd";
	
	public Event(String eventType, String name, DiagramPackage diagramPackage, Activity activity) {
		super();
		this.eventType = eventType;
		this.name = name;
		this.diagramPackage = diagramPackage;
		this.activity=activity;
		this.visited=false;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DiagramPackage getDiagramPackage() {
		return diagramPackage;
	}

	public void setDiagramPackage(DiagramPackage diagramPackage) {
		this.diagramPackage = diagramPackage;
	}
	
	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void concatEvent()
	{
		
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	
}
