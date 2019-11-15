package model;

import java.util.ArrayList;

public class Subprocess extends Task {

	private ActivitySet activitySet;
	private boolean isATransaction;
	private String activitySetId;

	public Subprocess( boolean isATransaction, String activitySetId) {
		super();
		this.isATransaction = isATransaction;
		this.activitySetId = activitySetId;
	}

	public ActivitySet getActivitySet() {
		return activitySet;
	}

	public void setActivitySet(ActivitySet activitySet) {
		this.activitySet = activitySet;
	}

	public boolean isATransaction() {
		return isATransaction;
	}

	public void setATransaction(boolean isATransaction) {
		this.isATransaction = isATransaction;
	}

	public String getActivitySetId() {
		return activitySetId;
	}

	public void setActivitySetId(String activitySetId) {
		this.activitySetId = activitySetId;
	}

}
