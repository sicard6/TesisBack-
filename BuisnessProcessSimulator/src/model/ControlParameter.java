package model;

public class ControlParameter {
	private InterTriggerTimer interTriggerTimer;
	private TriggerCount triggerCount;
	private Probability probability;
	
	public ControlParameter()
	{
		
	}

	public InterTriggerTimer getInterTriggerTimer() {
		return interTriggerTimer;
	}

	public void setInterTriggerTimer(InterTriggerTimer interTriggerTimer) {
		this.interTriggerTimer = interTriggerTimer;
	}

	public TriggerCount getTriggerCount() {
		return triggerCount;
	}

	public void setTriggerCount(TriggerCount triggerCount) {
		this.triggerCount = triggerCount;
	}

	public Probability getProbability() {
		return probability;
	}

	public void setProbability(Probability probability) {
		this.probability = probability;
	}	
	
}
