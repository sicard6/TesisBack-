package model;

import java.util.ArrayList;

public class Transition {

	private String id;
	private String conditionType;
	private String description;
	private ConnectorGraphicsInfo connectorGraphicsInfo;
	private String fromActivityId;
	private String toActivityId;
	private ArrayList<ExtendedAttribute> extendedAttributes;
	private Activity fromActivity;
	private Activity toActivity;
	private ElementParameter elementParameter;

	public Transition(String id, String conditionType, String description, ConnectorGraphicsInfo connectorGraphicsInfo,
			String fromActivityId, String toActivityId, ArrayList<ExtendedAttribute> extendedAttributes) {
		super();
		this.id = id;
		this.conditionType = conditionType;
		this.description = description;
		this.connectorGraphicsInfo = connectorGraphicsInfo;
		this.fromActivityId = fromActivityId;
		this.toActivityId = toActivityId;
		this.extendedAttributes = extendedAttributes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ConnectorGraphicsInfo getConnectorGraphicsInfo() {
		return connectorGraphicsInfo;
	}

	public void setConnectorGraphicsInfo(ConnectorGraphicsInfo connectorGraphicsInfo) {
		this.connectorGraphicsInfo = connectorGraphicsInfo;
	}

	public Activity getFromActivity() {
		return fromActivity;
	}

	public void setFromActivity(Activity fromActivity) {
		this.fromActivity = fromActivity;
	}

	public Activity getToActivity() {
		return toActivity;
	}

	public void setToActivity(Activity toActivity) {
		this.toActivity = toActivity;
	}

	public ElementParameter getElementParameter() {
		return elementParameter;
	}

	public void setElementParameter(ElementParameter elementParameter) {
		this.elementParameter = elementParameter;
	}

	public String getFromActivityId() {
		return fromActivityId;
	}

	public void setFromActivityId(String fromActivityId) {
		this.fromActivityId = fromActivityId;
	}

	public String getToActivityId() {
		return toActivityId;
	}

	public void setToActivityId(String toActivityId) {
		this.toActivityId = toActivityId;
	}

	public ArrayList<ExtendedAttribute> getExtendedAttributes() {
		return extendedAttributes;
	}

	public void setExtendedAttributes(ArrayList<ExtendedAttribute> extendedAttributes) {
		this.extendedAttributes = extendedAttributes;
	}
	
}
