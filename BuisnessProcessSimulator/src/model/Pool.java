package model;

import java.util.ArrayList;

public class Pool {

	private String id;
	private String name;
	private boolean boundaryVisible;
	private WorkflowProcess workflowProcess;
	private String workFlowProcessId;
	private NodeGraphicsInfo nodeGraphicsInfo;
	private ArrayList<Lane> lanes;
	
	public Pool(String id, String name, boolean boundaryVisible,String workFlowProcessId,
			NodeGraphicsInfo nodeGraphicsInfo, ArrayList<Lane> lanes) {
		super();
		this.id = id;
		this.name = name;
		this.boundaryVisible = boundaryVisible;
		this.nodeGraphicsInfo = nodeGraphicsInfo;
		this.lanes = lanes;
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
	
	public boolean isBoundaryVisible() {
		return boundaryVisible;
	}
	
	public void setBoundaryVisible(boolean boundaryVisible) {
		this.boundaryVisible = boundaryVisible;
	}

	public WorkflowProcess getWorkflowProcess() {
		return workflowProcess;
	}

	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}

	public NodeGraphicsInfo getNodeGraphicsInfo() {
		return nodeGraphicsInfo;
	}

	public void setNodeGraphicsInfo(NodeGraphicsInfo nodeGraphicsInfo) {
		this.nodeGraphicsInfo = nodeGraphicsInfo;
	}

	public ArrayList<Lane> getLanes() {
		return lanes;
	}

	public void setLanes(ArrayList<Lane> lanes) {
		this.lanes = lanes;
	}

	public String getWorkFlowProcessId() {
		return workFlowProcessId;
	}

	public void setWorkFlowProcessId(String workFlowProcessId) {
		this.workFlowProcessId = workFlowProcessId;
	}
}
