package model;

import java.util.ArrayList;

public class ActivitySet {
	
	private String id;
	private String name;
	private ArrayList<Activity> activities;
	private ArrayList<Transition> transitions;

	public ActivitySet(String id, String name, ArrayList<Activity> activities, ArrayList<Transition> transitions) {
		super();
		this.id = id;
		this.name = name;
		this.activities = activities;
		this.transitions = transitions;
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

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
	}

	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(ArrayList<Transition> transitions) {
		this.transitions = transitions;
	}
	
}
