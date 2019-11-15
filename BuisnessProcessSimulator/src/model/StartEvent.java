package model;

import java.util.ArrayList;

public class StartEvent extends Event{

	private String trigger;
	private ElementParameter elementParameter;
	
	public StartEvent(String trigger) {
		super();
		this.trigger = trigger;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public ElementParameter getElementParameter() {
		return elementParameter;
	}

	public void setElementParameter(ElementParameter elementParameter) {
		this.elementParameter = elementParameter;
	}
	
	
	
}
