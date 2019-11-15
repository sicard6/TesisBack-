package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import simulator.Record;

public class Activity {

	private String id;
	private String name;
	private String description;
	private String documentation;
	private String loopType;
	private ArrayList<ExtendedAttribute> extendedAttributes;
	private NodeGraphicsInfo nodeGraphicsInfo;
	private Event event;
	private Gateway gateway;
	private Task task;
	private boolean isEndActivity;
	private boolean movedForLackOfResources;
	private double timeMovedForLackOfResources;
	private int processingTime;
	private boolean visitedEnd;
	private Record record;


	public Activity(String id, String name, String description, String documentation,
			ArrayList<ExtendedAttribute> extendedAttributes, NodeGraphicsInfo nodeGraphicsInfo) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.documentation = documentation;
		this.extendedAttributes = extendedAttributes;
		this.nodeGraphicsInfo = nodeGraphicsInfo;
		this.isEndActivity=false;
		this.movedForLackOfResources=false;
		this.timeMovedForLackOfResources=0;
		this.visitedEnd=false;
		this.record= new Record("", "",0,0,0,0,0,0,0,0,0,0);
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

	public NodeGraphicsInfo getNodeGraphicsInfo() {
		return nodeGraphicsInfo;
	}

	public void setNodeGraphicsInfo(NodeGraphicsInfo nodeGraphicsInfo) {
		this.nodeGraphicsInfo = nodeGraphicsInfo;
	}

	public String getLoopType() {
		return loopType;
	}

	public void setLoopType(String loopType) {
		this.loopType = loopType;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	public boolean hasEvent()
	{
		boolean res=false;
		if(this.getEvent()!=null)
		{
			res=true;
		}
		return res;
	}

	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}
	
	public boolean hasGateway()
	{
		boolean res=false;
		if(this.getGateway()!=null)
		{
			res=true;
		}
		return res;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	public boolean hasTask()
	{
		boolean res=false;
		if(this.getTask()!=null)
		{
			res=true;
		}
		return res;
	}
	/**
	 * Key= Resource name, Value= amount of resources needed to excecute the task
	 * @return
	 */
	public HashMap getRequiredResources()
	{
		HashMap requiredResources = new HashMap<String, Integer>();
		if(this.hasTask())
		{
			if(getTask().getElementParameter() != null && getTask().getElementParameter().getResourceParameter()!=null)
			{
				String expression=getTask().getElementParameter().getResourceParameter().getSelection().getExpressionParameterValue();
				requiredResources=parseGetResources(expression);
				
			}	
		}
		return requiredResources;
	}
	public HashMap<String,Integer> parseGetResources(String expression)
	{
		
		String[]parsed1=expression.split("[ ,()]+");
		ArrayList<String>parsed2 = new ArrayList<String>();
		HashMap parsed3= new HashMap<String, Integer>();
		for(int i = 0;i<parsed1.length;i++)
		{
			if(parsed1[i].contains("getResource") | parsed1[i].contains("|"))
			{
				
			}
			else
			{
				parsed2.add(parsed1[i]);
			}
		}
		for(int i=0;i< parsed2.size();i=i+2)
		{
			String nameToAdd = parsed2.get(i).substring(1,parsed2.get(i).length()-1);
			int valueToAdd = Integer.parseInt( parsed2.get(i+1));
			parsed3.put(nameToAdd,valueToAdd);
		}
		return parsed3;
	}

	public boolean isEndActivity() {
		return isEndActivity;
	}

	public void setEndActivity(boolean isEndActivity) {
		this.isEndActivity = isEndActivity;
	}

	public boolean isMovedForLackOfResources() {
		return movedForLackOfResources;
	}

	public void setMovedForLackOfResources(boolean movedForLackOfResources) {
		this.movedForLackOfResources = movedForLackOfResources;
	}

	public double getTimeMovedForLackOfResources() {
		return timeMovedForLackOfResources;
	}

	public void setTimeMovedForLackOfResources(double timeMovedForLackOfResources) {
		this.timeMovedForLackOfResources = timeMovedForLackOfResources;
	}

	public int getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}

	public boolean isVisitedEnd() {
		return visitedEnd;
	}

	public void setVisitedEnd(boolean visitedEnd) {
		this.visitedEnd = visitedEnd;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	
	
	
	
	
	
}
