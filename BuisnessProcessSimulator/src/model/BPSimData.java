package model;

public class BPSimData {

	private String simulationLevel;
	private String xmlnsNs1;
	private Scenario scenario;
	
	public BPSimData(String simulationLevel, String xmlnsNs1, Scenario scenario) {
		super();
		this.simulationLevel = simulationLevel;
		this.xmlnsNs1 = xmlnsNs1;
		this.scenario = scenario;
	}
	public String getSimulationLevel() {
		return simulationLevel;
	}
	public void setSimulationLevel(String simulationLevel) {
		this.simulationLevel = simulationLevel;
	}
	public String getXmlnsNs1() {
		return xmlnsNs1;
	}
	public void setXmlnsNs1(String xmlnsNs1) {
		this.xmlnsNs1 = xmlnsNs1;
	}
	public Scenario getScenario() {
		return scenario;
	}
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}
}
