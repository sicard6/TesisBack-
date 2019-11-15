package model;

import java.util.ArrayList;

public class TaskBuisnessRule extends Task{

	private String BusinessRuleTaskImplementation;

	public TaskBuisnessRule(String loopType, String businessRuleTaskImplementation) {
		super();
		BusinessRuleTaskImplementation = businessRuleTaskImplementation;
	}

	public String getBusinessRuleTaskImplementation() {
		return BusinessRuleTaskImplementation;
	}

	public void setBusinessRuleTaskImplementation(String businessRuleTaskImplementation) {
		BusinessRuleTaskImplementation = businessRuleTaskImplementation;
	}
	
}
