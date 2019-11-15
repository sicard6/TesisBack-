package model;

public class ExtendedAttribute {

	private String name;
	private String value;
	private ElementParameter elementParameter; 

	public ExtendedAttribute(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ElementParameter getElementParameter() {
		return elementParameter;
	}

	public void setElementParameter(ElementParameter elementParameter) {
		this.elementParameter = elementParameter;
	}
	
}
