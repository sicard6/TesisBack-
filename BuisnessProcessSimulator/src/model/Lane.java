package model;

import java.util.ArrayList;

public class Lane {

	private String id;
	private String name;
	private String documentation;
	private NodeGraphicsInfo nodeGraphicsInfo;
	private ArrayList<ExtendedAttribute> extendedAttributes;
	
	public Lane(String id, String name, String documentation, NodeGraphicsInfo nodeGraphicsInfo,ArrayList<ExtendedAttribute> extendedAttributes) {
		super();
		this.id = id;
		this.name = name;
		this.documentation = documentation;
		this.nodeGraphicsInfo = nodeGraphicsInfo;
		this.extendedAttributes= extendedAttributes;
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

	public NodeGraphicsInfo getNodeGraphicsInfo() {
		return nodeGraphicsInfo;
	}

	public void setNodeGraphicsInfo(NodeGraphicsInfo nodeGraphicsInfo) {
		this.nodeGraphicsInfo = nodeGraphicsInfo;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public ArrayList<ExtendedAttribute> getExtendedAttributes() {
		return extendedAttributes;
	}

	public void setExtendedAttributes(ArrayList<ExtendedAttribute> extendedAttributes) {
		this.extendedAttributes = extendedAttributes;
	}
	
}
