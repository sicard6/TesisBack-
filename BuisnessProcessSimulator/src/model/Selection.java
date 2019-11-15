package model;

import java.util.ArrayList;

public class Selection {

	private ArrayList<String> resultRequest;
	private String expressionParameterValue;
	
	public Selection(ArrayList<String> resultRequest, String expressionParameterValue) {
		super();
		this.resultRequest = resultRequest;
		this.expressionParameterValue = expressionParameterValue;
	}

	public ArrayList<String> getResultRequest() {
		return resultRequest;
	}

	public void setResultRequest(ArrayList<String> resultRequest) {
		this.resultRequest = resultRequest;
	}

	public String getExpressionParameterValue() {
		return expressionParameterValue;
	}

	public void setExpressionParameterValue(String expressionParameterValue) {
		this.expressionParameterValue = expressionParameterValue;
	}
	
}
