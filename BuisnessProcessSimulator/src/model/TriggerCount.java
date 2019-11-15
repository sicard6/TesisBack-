package model;

public class TriggerCount {

	private String resultRequest;
	private int numericParameterValue;
	
	public TriggerCount(String resultRequest, int numericParameterValue) {
		super();
		this.resultRequest = resultRequest;
		this.numericParameterValue = numericParameterValue;
	}

	public String getResultRequest() {
		return resultRequest;
	}

	public void setResultRequest(String resultRequest) {
		this.resultRequest = resultRequest;
	}

	public int getNumericParameterValue() {
		return numericParameterValue;
	}

	public void setNumericParameterValue(int numericParameterValue) {
		this.numericParameterValue = numericParameterValue;
	}
	
}
