package model;

public class TimeParameter {
	
	private ProcessingTime processingTime;
	private WaitTime waitTime;
	
	public TimeParameter(ProcessingTime processingTime) {
		super();
		this.processingTime = processingTime;
	}

	public ProcessingTime getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(ProcessingTime processingTime) {
		this.processingTime = processingTime;
	}

	public WaitTime getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(WaitTime waitTime) {
		this.waitTime = waitTime;
	}
	
	
}
