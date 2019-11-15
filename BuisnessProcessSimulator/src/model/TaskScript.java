package model;

import java.util.ArrayList;

public class TaskScript extends Task{

	private String Script;

	
	public TaskScript(String loopType, String script) {
		super();
		Script = script;
	}

	public String getScript() {
		return Script;
	}

	public void setScript(String script) {
		Script = script;
	}
	
}
