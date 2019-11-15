package model;

import java.util.ArrayList;

public class TaskReceive extends Task{

	private boolean instantiate;

	
	public TaskReceive(String loopType, boolean instantiate) {
		super();
		this.instantiate = instantiate;
	}

	public boolean isInstantiate() {
		return instantiate;
	}

	public void setInstantiate(boolean instantiate) {
		this.instantiate = instantiate;
	}
	
}
