package model;

import java.util.ArrayList;

/**
 * 
 * @author david
 * Class that contains all elements from the model
 */
public class Model {

	private DiagramPackage diagramPackage;
	private PackageHeader packageHeader;
	private ArrayList<Modification> modifications;
	private RedefinableHeader redefinableHeader;
	private ArrayList<ExternalPackage> externalPackages;
	private ArrayList<Participant> participants;
	private ArrayList<ExtendedAttribute> extendedAttributes;
	private ArrayList<Pool> pools;
	private ArrayList<Lane> lanes;
	private ArrayList<WorkflowProcess> workflowProcesses;
	private ArrayList<Transition> transistions;
	private ArrayList<ActivitySet> activitySets;
	private ArrayList<Activity> activities;
	
	public Model()
	{
		
	}

	public DiagramPackage getDiagramPackage() {
		return diagramPackage;
	}

	public void setDiagramPackage(DiagramPackage diagramPackage) {
		this.diagramPackage = diagramPackage;
	}

	public PackageHeader getPackageHeader() {
		return packageHeader;
	}

	public void setPackageHeader(PackageHeader packageHeader) {
		this.packageHeader = packageHeader;
	}

	public ArrayList<Modification> getModifications() {
		return modifications;
	}

	public void setModifications(ArrayList<Modification> modifications) {
		this.modifications = modifications;
	}

	public RedefinableHeader getRedefinableHeader() {
		return redefinableHeader;
	}

	public void setRedefinableHeader(RedefinableHeader redefinableHeader) {
		this.redefinableHeader = redefinableHeader;
	}

	public ArrayList<ExternalPackage> getExternalPackages() {
		return externalPackages;
	}

	public void setExternalPackages(ArrayList<ExternalPackage> externalPackages) {
		this.externalPackages = externalPackages;
	}

	public ArrayList<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(ArrayList<Participant> participants) {
		this.participants = participants;
	}

	public ArrayList<ExtendedAttribute> getExtendedAttributes() {
		return extendedAttributes;
	}

	public void setExtendedAttributes(ArrayList<ExtendedAttribute> extendedAttributes) {
		this.extendedAttributes = extendedAttributes;
	}

	public ArrayList<Pool> getPools() {
		return pools;
	}

	public void setPools(ArrayList<Pool> pools) {
		this.pools = pools;
	}

	public ArrayList<Lane> getLanes() {
		return lanes;
	}

	public void setLanes(ArrayList<Lane> lanes) {
		this.lanes = lanes;
	}

	public ArrayList<WorkflowProcess> getWorkflowProcesses() {
		return workflowProcesses;
	}

	public void setWorkflowProcesses(ArrayList<WorkflowProcess> workflowProcesses) {
		this.workflowProcesses = workflowProcesses;
	}

	public ArrayList<Transition> getTransistions() {
		return transistions;
	}

	public void setTransistions(ArrayList<Transition> transistions) {
		this.transistions = transistions;
	}

	public ArrayList<ActivitySet> getActivitySets() {
		return activitySets;
	}

	public void setActivitySets(ArrayList<ActivitySet> activitySets) {
		this.activitySets = activitySets;
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
	}
}
