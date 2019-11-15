package model;

import java.util.ArrayList;

public class TaskUser extends Task {

	private String implementation;
	
	public TaskUser(String implementation) {
		super();
		this.implementation = implementation;
	}

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	public String getImplemntation() {
		return implementation;
	}

	public void setImplemntation(String implemntation) {
		this.implementation = implemntation;
	}
	
}
