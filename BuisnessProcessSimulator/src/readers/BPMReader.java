package readers;

import java.util.ArrayList;

import javax.xml.stream.events.StartElement;

import model.Activity;
import model.DiagramPackage;
import model.ElementParameter;
import model.EndEvent;
import model.Event;
import model.ExtendedAttribute;
import model.Formatting;
import model.IntermediateEvent;
import model.Lane;
import model.NodeGraphicsInfo;
import model.Participant;
import model.Pool;
import model.Scenario;
import model.StartEvent;
import model.Task;
import model.Transition;
import model.WorkflowProcess;

public class BPMReader {

	private DiagramPackage diagramPackage;
	private Scenario scenario;
	
	public BPMReader()
	{
		
	}
	
	public DiagramPackage constructModel(String diagramRoute, String simDataRoute)
	{
		BPMNDiagramReader diagramReader = new BPMNDiagramReader();
		diagramPackage=diagramReader.readDiagram(diagramRoute);
		
		
		BPSimDataReader bPSimDataReader = new BPSimDataReader();
		scenario=bPSimDataReader.readBPSimData(simDataRoute);
		try
		{
			//Associate buisnesProcessModelElements with their respective elementParameter
			//Run through the model
			//Run through the packageDiagram's extended attributes
			ArrayList<ExtendedAttribute> packageDiagramExtendedAttributes=diagramPackage.getExtendedAttributes();
			editExtendedAttributes(packageDiagramExtendedAttributes);
			diagramPackage.setExtendedAttributes(packageDiagramExtendedAttributes);
			
			//Run through the participants
			ArrayList<Participant> participants = diagramPackage.getParticipants();
			for(int i=0;i<participants.size();i++)
			{
				//Run through the participant's extended attributes
				Participant participant = participants.get(i);
				ArrayList<ExtendedAttribute> participantExtendedAttributes= participant.getExtendedAttributes();
				editExtendedAttributes(participantExtendedAttributes);				
				participants.get(i).setExtendedAttributes(participantExtendedAttributes);
				
			}
			diagramPackage.setParticipants(participants);
			
			//Run through the pools
			ArrayList<Pool>pools=diagramPackage.getPools();
			for(int i=0; i<pools.size();i++)
			{
				Pool pool = pools.get(i);
				
				//Run through the lanes
				ArrayList<Lane> lanes = pool.getLanes();
				
				for(int j=0;j<lanes.size();j++)
				{
					Lane lane = lanes.get(j);
					ArrayList<ExtendedAttribute> laneExtendedAttributes= lane.getExtendedAttributes();
					editExtendedAttributes(laneExtendedAttributes);
					lanes.get(j).setExtendedAttributes(laneExtendedAttributes);
				}
				pool.setLanes(lanes);
				//System.out.println("read pool "+i+" lanes");
				
				//Run through the workflowProcess
				WorkflowProcess workflowProcess=pool.getWorkflowProcess();
				
				//Run through the workflowProcess' extended attributes
				ArrayList<ExtendedAttribute> workflowProcessExtendedAttributes= workflowProcess.getExtendedAttributes();
				editExtendedAttributes(workflowProcessExtendedAttributes);
				workflowProcess.setExtendedAttributes(workflowProcessExtendedAttributes);
				//System.out.println("workflowprocess extended attributes read");
				
				//Run through the worklowProcess' activities
				ArrayList<Activity> activities = workflowProcess.getActivities();
				editActivities(activities);
				workflowProcess.setActivities(activities);
				//System.out.println("workflowprocess activities read");
				
				//Run through the workflowProcess' transitions
				ArrayList<Transition> transitions = workflowProcess.getTransitions();
				editTransitions(transitions, activities);
				workflowProcess.setTransitions(transitions);
				//System.out.println("workflowprocess transitions read");
				
				pool.setWorkflowProcess(workflowProcess);	
				pools.set(i, pool);
			}
			diagramPackage.setPools(pools);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}		
		return diagramPackage;
	}
	
	public ElementParameter getMatchingElementParameter(String buisnesProcessModelElementId)
	{
		ElementParameter elementParameter= new ElementParameter();
		ArrayList<ElementParameter> elementParameters= scenario.getElementParameters();
		for(int i=0;i<elementParameters.size();i++)
		{
			ElementParameter actualElementParameter = elementParameters.get(i);
			if(formatElementRefStr(actualElementParameter.getElementRef()).contentEquals(buisnesProcessModelElementId))
			{
				elementParameter=actualElementParameter;
			}
		}
		return elementParameter;
	}
	
	public boolean hasMatchingElementParameter(String buisnesProcessModelElementId)
	{
		//System.out.println("hasMatching element parameter?");
		boolean res=false;
		ArrayList<ElementParameter> elementParameters= scenario.getElementParameters();
		//System.out.println("elementParameters size: "+elementParameters.size());
		for(int i=0;i<elementParameters.size();i++)
		{
			ElementParameter elementParameter = elementParameters.get(i);
			if(formatElementRefStr(elementParameter.getElementRef()).equals(buisnesProcessModelElementId))
			{
				res=true;
			}
		}		
		return res;
	}
	
	public void editExtendedAttributes(ArrayList<ExtendedAttribute> extendedAttributes)
	{
		for(int i=0;i<extendedAttributes.size();i++)
		{
			ExtendedAttribute extendedAttribute= extendedAttributes.get(i);
			String extendedAttributeName= extendedAttribute.getName();
			if(hasMatchingElementParameter(extendedAttributeName))
			{
				extendedAttributes.get(i).setElementParameter(getMatchingElementParameter(extendedAttributeName));
			}
		}
	}
	public void editTransitions(ArrayList<Transition> transitions, ArrayList<Activity>activities)
	{
		for(int i=0;i<transitions.size();i++)
		{
			Transition transition = transitions.get(i);
			String transitionId= transition.getId();
			
			if(hasMatchingElementParameter(transitionId))
			{
				transitions.get(i).setElementParameter(getMatchingElementParameter(transitionId));
				//System.out.println("set transition");
			}
			
			for(int j=0;j<activities.size();j++)
			{
				Activity activity= activities.get(j);
				if(transition.getFromActivityId().equals(activity.getId()))
				{
					transitions.get(i).setToActivity(activity);
				}
				if(transition.getToActivityId().equals(activity.getId()))
				{
					transitions.get(i).setToActivity(activity);
				}
			}
			
			
		}
	}
	
	public void editActivities(ArrayList<Activity>activities)
	{
		for(int i =0;i<activities.size();i++)
		{
			Activity activity = activities.get(i);
			//Run through the activity's Extended attributes
			ArrayList<ExtendedAttribute> activityExtendedAttributes = activity.getExtendedAttributes();
			editExtendedAttributes(activityExtendedAttributes);			
			
			activities.get(i).setExtendedAttributes(activityExtendedAttributes);
			
			//if the activity has an associated task
			if(activity.hasTask()) 
			{
				String activityId = activity.getId();
				if(hasMatchingElementParameter(activityId));
				{
					activities.get(i).getTask().setElementParameter(getMatchingElementParameter(activityId));
					//System.out.println("set task");
				}
			}
			
			//if the activity has an associated event
			if(activity.hasEvent())
			{
				//System.out.println("has event");				
				if(activity.getEvent() instanceof StartEvent)
				{
					StartEvent startEvent = (StartEvent) activity.getEvent();
					String startEventId= activity.getId();
					if(hasMatchingElementParameter(startEventId))
					{
						startEvent.setElementParameter(getMatchingElementParameter(startEventId));
						activities.get(i).setEvent(startEvent);
					}
				}
				if(activity.getEvent() instanceof IntermediateEvent)
				{
					IntermediateEvent intermediateEvent = (IntermediateEvent) activity.getEvent();
					String intermediateEventId= activity.getId();
					if(hasMatchingElementParameter(intermediateEventId))
					{
						intermediateEvent.setElementParameter(getMatchingElementParameter(intermediateEventId));
						activities.get(i).setEvent(intermediateEvent);
					}
				}
			}
		}
	}
	
	public String formatElementRefStr(String elementRefStr)
	{
		String edit = elementRefStr;
		if(edit.contains("Id_"))
		{
			String[] editList=edit.split("_");
			edit= editList[1];
		}		
		return edit;
	}
}
