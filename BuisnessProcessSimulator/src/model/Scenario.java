package model;

import java.util.ArrayList;

public class Scenario {

	private String id;
	private String name;
	private String description;
	private String author;
	private String version;
	private ScenarioParameter scenarioParameter;
	private ArrayList<ElementParameter> elementParameters;
	
	public Scenario(String id, String name, String description, String author, String version,
			ScenarioParameter scenarioParameter, ArrayList<ElementParameter> elementParameters) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.author = author;
		this.version = version;
		this.scenarioParameter = scenarioParameter;
		this.elementParameters = elementParameters;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public ScenarioParameter getScenarioParameter() {
		return scenarioParameter;
	}
	public void setScenarioParameter(ScenarioParameter scenarioParameter) {
		this.scenarioParameter = scenarioParameter;
	}
	public ArrayList<ElementParameter> getElementParameters() {
		return elementParameters;
	}
	public void setElementParameters(ArrayList<ElementParameter> elementParameters) {
		this.elementParameters = elementParameters;
	}

}
