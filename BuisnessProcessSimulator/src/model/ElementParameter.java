package model;

public class ElementParameter {

	private ControlParameter controlParameter;
	private TimeParameter timeParameter;
	private ResourceParameter resourceParameter;
	private CostParameter costParameter;
	private String elementRef;
	
	public ElementParameter()
	{
		
	}

	public ControlParameter getControlParameter() {
		return controlParameter;
	}

	public void setControlParameter(ControlParameter controlParameter) {
		this.controlParameter = controlParameter;
	}

	public TimeParameter getTimeParameter() {
		return timeParameter;
	}

	public void setTimeParameter(TimeParameter timeParameter) {
		this.timeParameter = timeParameter;
	}

	public ResourceParameter getResourceParameter() {
		return resourceParameter;
	}

	public void setResourceParameter(ResourceParameter resourceParameter) {
		this.resourceParameter = resourceParameter;
	}

	public CostParameter getCostParameter() {
		return costParameter;
	}

	public void setCostParameter(CostParameter costParameter) {
		this.costParameter = costParameter;
	}

	public String getElementRef() {
		return elementRef;
	}

	public void setElementRef(String elementRef) {
		this.elementRef = elementRef;
	}
	
	
}
