package model;

import java.util.ArrayList;

public class IntermediateEvent extends Event{

	private String trigger;
	private boolean Attached;
	private String targetActivityId;
	private Activity targetActivity;
	private TriggerResultMessage triggerResultMessage;
	private ElementParameter elementParameter;
	
	public IntermediateEvent(String trigger) {
		super();
		this.trigger = trigger;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public boolean isAttached() {
		return Attached;
	}

	public void setAttached(boolean attached) {
		Attached = attached;
	}

	public Activity getTargetActivity() {
		return targetActivity;
	}

	public void setTargetActivity(Activity targetActivity) {
		this.targetActivity = targetActivity;
	}

	public TriggerResultMessage getTriggerResultMessage() {
		return triggerResultMessage;
	}

	public void setTriggerResultMessage(TriggerResultMessage triggerResultMessage) {
		this.triggerResultMessage = triggerResultMessage;
	}

	public String getTargetActivityId() {
		return targetActivityId;
	}

	public void setTargetActivityId(String targetActivityId) {
		this.targetActivityId = targetActivityId;
	}	

	public ElementParameter getElementParameter() {
		return elementParameter;
	}

	public void setElementParameter(ElementParameter elementParameter) {
		this.elementParameter = elementParameter;
	}
	
}
