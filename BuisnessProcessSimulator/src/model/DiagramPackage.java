package model;

import java.util.ArrayList;

public class DiagramPackage {
	
	private String xmlnsXsd;
	private String xmlnsXsi;
	private boolean onlyOneProcess;
	private String id;
	private String name;
	private String xmlns;
	private PackageHeader packageHeader;
	private RedefinableHeader redefinableHeader;
	private ArrayList<ExternalPackage> externalPackages;
	private ArrayList<Participant> participants;
	private ArrayList<ExtendedAttribute> extendedAttributes;
	private ArrayList<Pool> pools;
	private ArrayList<WorkflowProcess> workflowProcesses;
	private int totalArrivals;
	
	public DiagramPackage(String xmlnsXsd, String xmlnsXsi, boolean onlyOneProcess, String id, String name,
			String xmlns, PackageHeader packageHeader, RedefinableHeader redefinableHeader,
			ArrayList<ExternalPackage> externalPackages, ArrayList<Participant> participants,
			ArrayList<ExtendedAttribute> extendedAttributes, ArrayList<Pool> pools,
			ArrayList<WorkflowProcess> workflowProcesses) {
		super();
		this.xmlnsXsd = xmlnsXsd;
		this.xmlnsXsi = xmlnsXsi;
		this.onlyOneProcess = onlyOneProcess;
		this.id = id;
		this.name = name;
		this.xmlns = xmlns;
		this.packageHeader = packageHeader;
		this.redefinableHeader = redefinableHeader;
		this.externalPackages = externalPackages;
		this.participants = participants;
		this.extendedAttributes = extendedAttributes;
		this.pools = pools;
		this.workflowProcesses = workflowProcesses;
		this.totalArrivals=1;
	}

	public String getXmlnsXsd() {
		return xmlnsXsd;
	}
	
	public void setXmlnsXsd(String xmlnsXsd) {
		this.xmlnsXsd = xmlnsXsd;
	}
	
	public String getXmlnsXsi() {
		return xmlnsXsi;
	}
	
	public void setXmlnsXsi(String xmlnsXsi) {
		this.xmlnsXsi = xmlnsXsi;
	}
	
	public boolean isOnlyOneProcess() {
		return onlyOneProcess;
	}
	
	public void setOnlyOneProcess(boolean onlyOneProcess) {
		this.onlyOneProcess = onlyOneProcess;
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
	
	public String getXmlns() {
		return xmlns;
	}
	
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public PackageHeader getPackageHeader() {
		return packageHeader;
	}

	public void setPackageHeader(PackageHeader packageHeader) {
		this.packageHeader = packageHeader;
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

	public ArrayList<WorkflowProcess> getWorkflowProcesses() {
		return workflowProcesses;
	}

	public void setWorkflowProcesses(ArrayList<WorkflowProcess> workflowProcesses) {
		this.workflowProcesses = workflowProcesses;
	}

	public int getTotalArrivals() {
		return totalArrivals;
	}

	public void setTotalArrivals(int totalArrivals) {
		this.totalArrivals = totalArrivals;
	}
	
	
}
