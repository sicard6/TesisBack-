package model;

import java.util.ArrayList;

public class Participant {

	private String id;
	private String name;
	private String participantType;
	private String description;
	private ArrayList<ExtendedAttribute> extendedAttributes;
	private int usedResources;
	private int totalResources;

	public Participant(String id, String name, String participantType, String description,
			ArrayList<ExtendedAttribute> extendedAttributes) {
		super();
		this.id = id;
		this.name = name;
		this.participantType = participantType;
		this.description = description;
		this.extendedAttributes = extendedAttributes;
		this.usedResources=0;
		totalResources=0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParticipantType() {
		return participantType;
	}

	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<ExtendedAttribute> getExtendedAttributes() {
		return extendedAttributes;
	}

	public void setExtendedAttributes(ArrayList<ExtendedAttribute> extendedAttributes) {
		this.extendedAttributes = extendedAttributes;
	}

	public int getUsedResources() {
		return usedResources;
	}

	public void setUsedResources(int resourcesUsed) {
		this.usedResources = resourcesUsed;
	}
	
	public int getTotalResourcesFromFile()
	{
		int tot=0;
		if(this.getExtendedAttributes().get(0).getElementParameter() != null)
			tot=this.getExtendedAttributes().get(0).getElementParameter().getResourceParameter().getQuantity().getNumericParameterValue();
		return tot;
	}
	public String getResourceName()
	{
		String res="";
		res = this.getExtendedAttributes().get(0).getName();
		return res;
	}

	public int getTotalResources() {
		return totalResources;
	}

	public void setTotalResources(int totalResources) {
		this.totalResources = totalResources;
	}
	
}
