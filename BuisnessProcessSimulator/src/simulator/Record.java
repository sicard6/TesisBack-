package simulator;

public class Record {

	private String name; 
	private String type; 
	private int completedInstances; 
	private int minTime; 
	private int maxTime;
	private int avgTime;
	private int totalTime;
	private int minResourceWaitTime;
	private int maxResourceWaitTime;
	private int avgResourceWaitTime;
	private double ResourceWaitStandardDeviation;
	private int totalResourceWaitTime;
	
	public Record(String name, String type, int completedInstances, int minTime, int maxTime, int avgTime,
			int totalTime, int minResourceWaitTime, int maxResourceWaitTime, int avgResourceWaitTime,
			double resourceWaitStandardDeviation, int totalResourceWaitTime) {
		super();
		this.name = name;
		this.type = type;
		this.completedInstances = completedInstances;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.avgTime = avgTime;
		this.totalTime = totalTime;
		this.minResourceWaitTime = minResourceWaitTime;
		this.maxResourceWaitTime = maxResourceWaitTime;
		this.avgResourceWaitTime = avgResourceWaitTime;
		ResourceWaitStandardDeviation = resourceWaitStandardDeviation;
		this.totalResourceWaitTime = totalResourceWaitTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCompletedInstances() {
		return completedInstances;
	}
	public void setCompletedInstances(int completedInstances) {
		this.completedInstances = completedInstances;
	}
	public int getMinTime() {
		return minTime;
	}
	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}
	public int getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}
	public int getAvgTime() {
		return avgTime;
	}
	public void setAvgTime(int avgTime) {
		this.avgTime = avgTime;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public int getMinResourceWaitTime() {
		return minResourceWaitTime;
	}
	public void setMinResourceWaitTime(int minResourceWaitTime) {
		this.minResourceWaitTime = minResourceWaitTime;
	}
	public int getMaxResourceWaitTime() {
		return maxResourceWaitTime;
	}
	public void setMaxResourceWaitTime(int maxResourceWaitTime) {
		this.maxResourceWaitTime = maxResourceWaitTime;
	}
	public int getAvgResourceWaitTime() {
		return avgResourceWaitTime;
	}
	public void setAvgResourceWaitTime(int avgResourceWaitTime) {
		this.avgResourceWaitTime = avgResourceWaitTime;
	}
	public double getResourceWaitStandardDeviation() {
		return ResourceWaitStandardDeviation;
	}
	public void setResourceWaitStandardDeviation(double resourceWaitStandardDeviation) {
		ResourceWaitStandardDeviation = resourceWaitStandardDeviation;
	}
	public int getTotalResourceWaitTime() {
		return totalResourceWaitTime;
	}
	public void setTotalResourceWaitTime(int totalResourceWaitTime) {
		this.totalResourceWaitTime = totalResourceWaitTime;
	}
	
	
}
