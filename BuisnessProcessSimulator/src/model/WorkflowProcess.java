package model;

import java.util.ArrayList;
import java.util.Date;

public class WorkflowProcess {

	private String id;
	private String name;
	private Date created;
	private String description;
	private RedefinableHeader redefinableHeader;
	private ArrayList<ExtendedAttribute> extendedAttributes;
	private ArrayList<Transition> transitions;
	private ArrayList<ActivitySet> activitySets;
	private ArrayList<Activity> activities;

	public WorkflowProcess(String id, String name, Date created, String description,
			RedefinableHeader redefinableHeader, ArrayList<ExtendedAttribute> extendedAttributes,
			ArrayList<Transition> transitions, ArrayList<ActivitySet> activitySets, ArrayList<Activity> activities) {
		super();
		this.id = id;
		this.name = name;
		this.created = created;
		this.description = description;
		this.redefinableHeader = redefinableHeader;
		this.extendedAttributes = extendedAttributes;
		this.transitions = transitions;
		this.activitySets = activitySets;
		this.activities = activities;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public RedefinableHeader getRedefinableHeader() {
		return redefinableHeader;
	}

	public void setRedefinableHeader(RedefinableHeader redefinableHeader) {
		this.redefinableHeader = redefinableHeader;
	}

	public ArrayList<ExtendedAttribute> getExtendedAttributes() {
		return extendedAttributes;
	}

	public void setExtendedAttributes(ArrayList<ExtendedAttribute> extendedAttributes) {
		this.extendedAttributes = extendedAttributes;
	}

	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(ArrayList<Transition> transitions) {
		this.transitions = transitions;
	}

	public ArrayList<ActivitySet> getActivitySets() {
		return activitySets;
	}

	public void setActivitySets(ArrayList<ActivitySet> activitySets) {
		this.activitySets = activitySets;
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
	}
	
}
