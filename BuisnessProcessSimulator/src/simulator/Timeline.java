package simulator;

import java.awt.font.NumericShaper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;

import distributions.Distribution;
import distributions.DistributionFactory;
import distributions.Normal;
import model.Activity;
import model.ControlParameter;
import model.DiagramPackage;
import model.ElementParameter;
import model.EndEvent;
import model.ExtendedAttribute;
import model.Formatting;
import model.Gateway;
import model.InterTriggerTimer;
import model.IntermediateEvent;
import model.NodeGraphicsInfo;
import model.Participant;
import model.Probability;
import model.ProcessingTime;
import model.StartEvent;
import model.Task;
import model.Transition;
import model.WaitTime;
import model.WorkflowProcess;

public class Timeline {
	private ArrayList<Moment> moments;
	private ArrayList<State> snapshots;
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	private int arrivalRate;
	private int totalArrivals;
	private HashMap convergingGateways;
	private ArrayList<Participant> participants;
	
	
	public Timeline(DiagramPackage diagramPackage)
	{
		snapshots=new ArrayList<State>();
		Calendar date = Calendar.getInstance();
		State state= new State(this,date);
		snapshots.add(state);
		moments= new ArrayList<Moment>();
		totalArrivals=1;
		convergingGateways= new HashMap<String,Integer>();
		participants= new ArrayList<Participant>();
		getTotalArrivals(diagramPackage);
	}
	public void getTotalArrivals(DiagramPackage diagramPackage)
	{
		WorkflowProcess workflowProcesses=diagramPackage.getPools().get(1).getWorkflowProcess();

		//get the start event
		ArrayList<Activity>activities= workflowProcesses.getActivities();
		for(int j=0;j<activities.size();j++)
		{
			Activity activity = activities.get(j);
			if(activity.hasEvent())
			{			
				if(activity.getEvent() instanceof StartEvent)
				{
					StartEvent startEvent = (StartEvent) activity.getEvent();
					String startEventId= activity.getId();
					ElementParameter startEventElementParameter= startEvent.getElementParameter();

					arrivalRate = startEventElementParameter.getControlParameter().getInterTriggerTimer().getNumericParameterValue();
					totalArrivals=startEventElementParameter.getControlParameter().getTriggerCount().getNumericParameterValue();
				}
			}
		}
	}
	public void placeFirstEvent(DiagramPackage diagramPackage)
	{
		
			WorkflowProcess workflowProcesses=diagramPackage.getPools().get(1).getWorkflowProcess();

			//get the start event
			ArrayList<Activity>activities= workflowProcesses.getActivities();
			for(int j=0;j<activities.size();j++)
			{
				Activity activity = activities.get(j);
				if(activity.hasEvent())
				{			
					if(activity.getEvent() instanceof StartEvent)
					{
						StartEvent startEvent = (StartEvent) activity.getEvent();
						String startEventId= activity.getId();
						ElementParameter startEventElementParameter= startEvent.getElementParameter();

						arrivalRate = startEventElementParameter.getControlParameter().getInterTriggerTimer().getNumericParameterValue();
						totalArrivals=startEventElementParameter.getControlParameter().getTriggerCount().getNumericParameterValue();

						//System.out.println("arrrival rate: "+arrivalRate+" entries per minute");
						//System.out.println("total arrivals: "+totalArrivals);

						
						if(moments.isEmpty())
						{
							Calendar date = Calendar.getInstance();
							Moment moment = new Moment(date);
							//Event eventProcessStart = new Event(Event.PROCESS_START, "processStart", diagramPackage,activity);
							Event eventActivityStart = new Event(Event.ACTIVITY_START, activity.getName(), diagramPackage,activity);
							//hour.getEvents().add(eventProcessStart);
							moment.getEvents().add(eventActivityStart);
							moments.add(moment);
							//System.out.println(" first "+moment.getTime().getTime());
						}
						else
						{
							Moment lastMoment =moments.get(moments.size()-1);
							Calendar lastDate =lastMoment.getTime();
							lastDate.add(Calendar.MINUTE, arrivalRate);
							Moment moment = new Moment(lastDate);
							Event eventActivityStart = new Event(Event.ACTIVITY_START, activity.getName(), diagramPackage,activity);
							moment.getEvents().add(eventActivityStart);
							moments.add(moment);
						}
					}
				}
			}
		
	}

	public void runSimulation(ArrayList<DiagramPackage> diagramPackages)
	{
		//the previous activity is only checked in case that an activity should be moved due to lack of resources
		ReportBuilder reportBuilder = new ReportBuilder();
		Formatting formatting = new Formatting("", "", 0, false, false, false, false, 0);
		NodeGraphicsInfo nodeGraphicsInfo = new NodeGraphicsInfo("", 0, 0, 0, 0, false, formatting);
		ArrayList<ExtendedAttribute> extendedAttributes= new ArrayList<ExtendedAttribute>();
		Activity previousActivity= new Activity("", "", "", "", extendedAttributes, nodeGraphicsInfo);
		
		
		for(int a=0;a<diagramPackages.size();a++)
		{
			DiagramPackage diagramPackage= diagramPackages.get(a);
			getTotalArrivals(diagramPackage);
			for(int i=0;i<totalArrivals;i++)
			{
				placeFirstEvent(diagramPackage);
				
			}
			int lastMomentIndex=moments.size()-1;
			for(int i=0;i<moments.size();i++)
			{
				Moment hour = moments.get(i);
				ArrayList<Event> events= hour.getEvents();
				Calendar enteringTime = hour.getTime();
				for(int j=0;j<events.size();j++)
				{
					Event event = events.get(j);
					if(!event.isVisited())
					{
						Record record = new Record("", "", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0);
						System.out.println("started activity at: "+enteringTime.getTime());

						
						Activity activity=event.getActivity();
						int processingTime=0;
						int waitTime=0;
						record.setName(activity.getName());
						String type = "";
						boolean hasAvailableResources=true;
						
						//managing the report data
						//if the activity is an event
						if(activity.hasEvent())
						{
							//if it's the start event
							if(activity.getEvent() instanceof StartEvent)
							{	
								type="StartEvent";
								
							}
							//if it's an intermediate event
							if(activity.getEvent() instanceof IntermediateEvent)
							{
								type="IntermediateEvent";
								IntermediateEvent intermediateEvent = (IntermediateEvent) activity.getEvent();
								ElementParameter elementParameter= intermediateEvent.getElementParameter();
								
								//processingTime
								InterTriggerTimer interTriggerTimerObject = elementParameter.getControlParameter().getInterTriggerTimer();
								processingTime=readInterTriggerTimerTime(interTriggerTimerObject,elementParameter);
								//record
								record.setMinTime(processingTime);
								record.setMaxTime(processingTime);					
								record.setTotalTime(record.getTotalTime()+processingTime);
								record.setAvgTime((record.getMaxTime()+record.getMinTime())/2);
								enteringTime.add(Calendar.MINUTE, processingTime);	
							}
							//if it's an end event
							if(activity.getEvent() instanceof EndEvent)
							{
								type="EndEvent";
							}
							
							record.setType(type);	
						}

						//if it's a gateway
						if(activity.hasGateway())
						{
							type="Gateway";
							record.setType(type);	
						}
						//if it's a task
						if(activity.hasTask())
						{
							type="Task";
							record.setType(type);
							if(!activity.isEndActivity())
							{
								//resources
								hasAvailableResources =hasAvailableResources(activity);		
								if(hasAvailableResources)
								{
									acquireResources(activity);
									Task task = activity.getTask();
									ElementParameter elementParameter= task.getElementParameter();
									
									//processingTime
									ProcessingTime processingTimeObject = elementParameter.getTimeParameter().getProcessingTime();
									processingTime=readProcessingTime(processingTimeObject,elementParameter);
									//record
									record.setMinTime(processingTime);
									record.setMaxTime(processingTime);					
									record.setTotalTime(record.getTotalTime()+processingTime);
									record.setAvgTime((record.getMaxTime()+record.getMinTime())/2);
									activity.setProcessingTime(processingTime);
									
									//waitTime
									if(elementParameter.getTimeParameter().getProcessingTime()!=null)
									{
										WaitTime waitTimeObject = elementParameter.getTimeParameter().getWaitTime();
										waitTime=readWaitTime(waitTimeObject,elementParameter);
										//record
										/*
										 * aqui lo tenia para el tiempo de espera
										 * 
										 * record.setMinResourceWaitTime(waitTime);
										record.setMaxResourceWaitTime(waitTime);
										record.setTotalResourceWaitTime(record.getTotalResourceWaitTime()+waitTime);
										record.setAvgResourceWaitTime((record.getMinResourceWaitTime()+record.getMaxResourceWaitTime())/2);
										
										 */
										record.setMinTime(processingTime+waitTime);
										record.setMaxTime(processingTime+waitTime);
										record.setTotalTime(record.getTotalTime()+processingTime+record.getTotalResourceWaitTime()+waitTime);
										record.setAvgTime((record.getMaxTime()+record.getMinTime())/2);
										activity.setProcessingTime(processingTime+waitTime);
									}
									if(activity.isMovedForLackOfResources())
									{
										Double doubleResourceWaitTime=activity.getTimeMovedForLackOfResources();
										int resourceWaitTime= doubleResourceWaitTime.intValue();
										record.setMinResourceWaitTime(resourceWaitTime);
										record.setMaxResourceWaitTime(resourceWaitTime);
										record.setTotalResourceWaitTime(record.getTotalResourceWaitTime()+resourceWaitTime);
										record.setAvgResourceWaitTime((record.getMinResourceWaitTime()+record.getMaxResourceWaitTime())/2);
									}
									activity.setRecord(record);
									enteringTime.add(Calendar.MINUTE, processingTime);	
								}	
							}
							else
							{
								Record activityRecord = activity.getRecord();
								record.setMinTime(activityRecord.getMinTime());
								record.setMaxTime(activityRecord.getMaxTime());					
								record.setTotalTime(activityRecord.getTotalTime());
								record.setAvgTime(activityRecord.getAvgTime());
								record.setMinResourceWaitTime(activityRecord.getMinResourceWaitTime());
								record.setMaxResourceWaitTime(activityRecord.getMaxResourceWaitTime());
								record.setTotalResourceWaitTime(activityRecord.getTotalResourceWaitTime());
								record.setAvgResourceWaitTime(activityRecord.getAvgResourceWaitTime());
							}
						}
						System.out.println(activity.getName()+" visited");
						if(activity.hasTask())
						{
							if(activity.isEndActivity())
							{
								//specific case of error running once
								if(getDiagramPackageTotalArrivals(diagramPackage)==1)
								{
									if(!activity.isVisitedEnd())
									{
										//place next activities on the timeline								
										placeNextActivities(enteringTime,diagramPackage,activity);
										State state= new State(this,enteringTime);
										snapshots.add(state);
										releaseResources(activity);
										System.out.println("logged new snapshot, current time: "+enteringTime.getTime());
										activity.setVisitedEnd(true);
									}
									
								}		
								else
								{
									//place next activities on the timeline								
									placeNextActivities(enteringTime,diagramPackage,activity);
									State state= new State(this,enteringTime);
									snapshots.add(state);
									releaseResources(activity);
									System.out.println("logged new snapshot, current time: "+enteringTime.getTime());
								}		
								//if you want to see only the end activities
								//in the report uncomment this, and comment the same code below
								record.setCompletedInstances(record.getCompletedInstances());
								reportBuilder.saveRecord(record,activity.isMovedForLackOfResources());
							}
							else
							{
								if(hasAvailableResources)
								{
									//place the end of the activity on the timeline
									placeEndActivity(enteringTime,diagramPackage,activity);
								}
								else
								{
									//place this activity at the end of the timeline
									moveActivity(diagramPackage, activity,enteringTime,previousActivity.getProcessingTime());
									System.out.println("was moved due to lack of resources");
								}
								//if you want to see only the activities without end activities
								//in the report uncomment this, and comment the same code above
								//record.setCompletedInstances(record.getCompletedInstances());
								//reportBuilder.saveRecord(record,activity.isMovedForLackOfResources());
							}
							
						}
						else
						{
							//place next activities on the timeline
							placeNextActivities(enteringTime,diagramPackage,activity);
							//Save snapshot
							record.setCompletedInstances(record.getCompletedInstances());
							reportBuilder.saveRecord(record,false);
							State state= new State(this,enteringTime);
							snapshots.add(state);
							//releaseResources(activity);
							System.out.println("logged new snapshot, current time: "+enteringTime.getTime());
						}						
						previousActivity=activity;
						event.setVisited(true);
					}						
				}
			}
		}
		
		reportBuilder.saveHeader("name", "type", "completedInstances", "minTime", "maxTime", "avgTime", "totalTime","minResourceWaitTime", "maxResourceWaitTime", "avgResourceWaitTime", "resourceWaitStandardDeviation", "totalResourceWaitTime");
		reportBuilder.createReport();
	}

	public ArrayList<Activity> getNextActivities(DiagramPackage diagramPackage, Activity currentActivity)
	{
		ArrayList<Activity> nextActivities= new ArrayList<Activity>();
		WorkflowProcess workflowProcesses=diagramPackage.getPools().get(1).getWorkflowProcess();

		ArrayList<Activity>activities= workflowProcesses.getActivities();
		ArrayList<Transition>transitions= workflowProcesses.getTransitions();

		for(int i=0;i< transitions.size();i++)
		{
			Transition transition = transitions.get(i);
			if(transition.getId()!="")
			{
				if(transition.getFromActivityId()!="")
				{
					if(transition.getFromActivityId().equals(currentActivity.getId()))
					{

						Activity fromActivity=getActivityById(activities, transition.getFromActivityId());
						if(fromActivity.hasGateway())
						{
							Gateway gateway = fromActivity.getGateway();
							if(gateway.getGatewayDirection()!=null && !gateway.getGatewayDirection().equals("Converging"))
							{
								//its a diverging gateway
								if(gateway.getGatewayType()!=null &&gateway.getGatewayType().equals("Parallel"))
								{
									Activity toActivity= getActivityById(activities, transition.getToActivityId());
									nextActivities.add(toActivity);
								}
							}
							else
							{
								//if its a converging gateway
								//see if the convergingGateway has been visited, if it hasn't it can continue
								//if it has, the road isn't taken,a as it would be repeated
								if(!convergingGateways.containsKey(fromActivity.getName()))
								{
									Activity toActivity= getActivityById(activities, transition.getToActivityId());
									nextActivities.add(toActivity);
									convergingGateways.put(fromActivity.getName(),1);
									
								}
								else
								{
									//the gateway has been visited
									int visitedTimes = (int) convergingGateways.get(fromActivity.getName());
									
									if(visitedTimes==0)
									{
										Activity toActivity= getActivityById(activities, transition.getToActivityId());
										nextActivities.add(toActivity);
									}
									//we must reset the gateway's visited times number every totalArrival times 
									if(visitedTimes==totalArrivals)
									{
										convergingGateways.replace(fromActivity.getName(), 0);
									}
									if(visitedTimes<totalArrivals)
									{
										convergingGateways.replace(fromActivity.getName(), visitedTimes+1);
									}
								}
							}
						}
						else
						{
							Activity toActivity= getActivityById(activities, transition.getToActivityId());
							nextActivities.add(toActivity);
						}
					}
				}
			}

		}
		return nextActivities;
	}

	/**
	 * This method returns the next activity that should be selected by the gateway based on which event
	 * has the least amount of interTrigger time.
	 * It is implied that this type of gateway will only have time intermediate events comming out of it 
	 * @param diagramPackage
	 * @param currentActivity
	 * @return
	 */
	public Activity getNextEventBasedGatewayActivity(DiagramPackage diagramPackage, Activity currentActivity)
	{
		Formatting formatting = new Formatting("", "", 0, false, false, false, false, 0);
		NodeGraphicsInfo nodeGraphicsInfo = new NodeGraphicsInfo("", 0, 0, 0, 0, false, formatting);
		ArrayList<ExtendedAttribute> extendedAttributes= new ArrayList<ExtendedAttribute>();
		Activity resActivity= new Activity("", "", "", "", extendedAttributes, nodeGraphicsInfo);
		
		ArrayList<Activity> nextEventBasedActivities= new ArrayList<Activity>();

		WorkflowProcess workflowProcesses=diagramPackage.getPools().get(1).getWorkflowProcess();

		ArrayList<Activity>activities= workflowProcesses.getActivities();
		ArrayList<Transition>transitions= workflowProcesses.getTransitions();
		
		double currentMin=Double.MAX_VALUE;

		for(int i=0;i< transitions.size();i++)
		{
			Transition transition = transitions.get(i);
			if(transition.getId()!="")
			{
				if(transition.getFromActivityId()!="")
				{
					if(transition.getFromActivityId().equals(currentActivity.getId()))
					{
						
							Activity toActivity= getActivityById(activities, transition.getToActivityId());
							nextEventBasedActivities.add(toActivity);
					}
				}
			}
		}
		for(int i=0;i<nextEventBasedActivities.size();i++)
		{
			Activity activity = nextEventBasedActivities.get(i);
			IntermediateEvent intermediateEvent = (IntermediateEvent) activity.getEvent();
			ElementParameter elementParameter= intermediateEvent.getElementParameter();
			
			//processingTime
			InterTriggerTimer interTriggerTimerObject = elementParameter.getControlParameter().getInterTriggerTimer();
			int processingTime=readInterTriggerTimerTime(interTriggerTimerObject,elementParameter);
			if(processingTime<currentMin)
			{
				resActivity= activity;
				currentMin=processingTime;
			}			
		}
		return resActivity;
	}
	/**
	 * This method returns the next activity that sshould be selected by the gateway given each transition's
	 * assigned probability
	 * @param diagramPackage
	 * @param currentActivity
	 * @return
	 */
	public Activity getNextGatewayActivity(DiagramPackage diagramPackage, Activity currentActivity)
	{

		Formatting formatting = new Formatting("", "", 0, false, false, false, false, 0);
		NodeGraphicsInfo nodeGraphicsInfo = new NodeGraphicsInfo("", 0, 0, 0, 0, false, formatting);
		ArrayList<ExtendedAttribute> extendedAttributes= new ArrayList<ExtendedAttribute>();
		Activity resActivity= new Activity("", "", "", "", extendedAttributes, nodeGraphicsInfo);

		ArrayList<Activity> nextProbActivities= new ArrayList<Activity>();

		WorkflowProcess workflowProcesses=diagramPackage.getPools().get(1).getWorkflowProcess();

		ArrayList<Activity>activities= workflowProcesses.getActivities();
		ArrayList<Transition>transitions= workflowProcesses.getTransitions();
		ArrayList<Float> probabilities= new ArrayList<Float>();

		boolean hasProbability=false;

		for(int i=0;i< transitions.size();i++)
		{
			Transition transition = transitions.get(i);
			if(transition.getId()!="")
			{
				if(transition.getFromActivityId()!="")
				{
					if(transition.getFromActivityId().equals(currentActivity.getId()))
					{

						if(transition.getElementParameter()!=null)
						{
							ElementParameter elementParameter = transition.getElementParameter();

							if(elementParameter.getControlParameter().getProbability()!=null)
							{
								Activity toActivity= getActivityById(activities, transition.getToActivityId());
								nextProbActivities.add(toActivity);

								float probability=elementParameter.getControlParameter().getProbability().getFloatingParameterValue();
								probabilities.add(probability);
								System.out.println("probability to add:"+probability);
								hasProbability=true;
							}

						}
						else
						{
							Activity toActivity= getActivityById(activities, transition.getToActivityId());
							resActivity=toActivity;
						}

					}
				}
			}
		}
		if(hasProbability)
		{
			double chosenProb=Math.random();
			double accumulativeProb=0;
			boolean choseProb=false;
			int index= 0;

			for(int i=0;i<probabilities.size()&&choseProb==false;i++)
			{
				double prob= probabilities.get(i);
				if(accumulativeProb<chosenProb && chosenProb<(accumulativeProb+prob))
				{
					choseProb=true;
					index=i;
				}
				accumulativeProb=accumulativeProb+prob;
			}

			resActivity= nextProbActivities.get(index);
			return resActivity;
		}
		else
		{
			System.out.println("salio de un gateway sin probabilidad");
			return resActivity;
		}
	}

	public ArrayList<Moment> getHours() {
		return moments;
	}
	public void setHours(ArrayList<Moment> hours) {
		this.moments = hours;
	}
	public Activity getActivityById(ArrayList<Activity>activities, String id)
	{
		Formatting formatting = new Formatting("", "", 0, false, false, false, false, 0);
		NodeGraphicsInfo nodeGraphicsInfo = new NodeGraphicsInfo("", 0, 0, 0, 0, false, formatting);
		ArrayList<ExtendedAttribute> extendedAttributes= new ArrayList<ExtendedAttribute>();
		Activity activity= new Activity("", "", "", "", extendedAttributes, nodeGraphicsInfo);

		for(int i=0;i<activities.size();i++)
		{
			if(activities.get(i).getId().equals(id))
			{
				activity=activities.get(i);
			}
		}
		return activity;
	}
	public int formatDurationParameter(String durationParameter)
	{
		int res=0;
		String s[]=durationParameter.split("PT");
		String xM=s[1];
		String xMs[]=xM.split("M");
		String resStr=xMs[0];
		res=Integer.parseInt(resStr);
		return res;
	}

	/**
	 * 
	 * @param time
	 * @return returns the hour that contains such time, if it doesn't exist a new hour in the timeline is created
	 */
	public Moment getHourByTime(Calendar time)
	{
		Moment hour = new Moment(time);

		for(int i=0;i<moments.size();i++)
		{
			Moment iHour= moments.get(i);
			if(iHour.getTime()==time)
			{
				hour=iHour;
			}
		}
		return hour;
	}

	public boolean hasHour(Moment hour)
	{
		boolean res=false;
		for(int i=0;i<moments.size();i++)
		{
			if(moments.get(i)==hour)
			{
				res=true;
			}
		}
		return res;
	}

	public void setHour(Moment hour)
	{
		for(int i=0;i<moments.size();i++)
		{
			if(moments.get(i)==hour)
			{
				moments.set(i, hour);
			}
		}
	}
	public boolean containsEvent(Event event)
	{
		boolean res=false;

		for(int i=0;i<moments.size();i++)
		{
			Moment hour = moments.get(i);
			for(int j=0;j<hour.getEvents().size();j++)
			{
				Event hourEvent= hour.getEvents().get(j);
				if(hourEvent==event)
				{
					res=true;
				}
			}
		}
		return res;
	}

	public void getExcecTime()
	{
		State start= snapshots.get(0);
		State end= snapshots.get(snapshots.size()-1);

		Calendar endCalendar = end.getCurrentTime();
		Calendar startCalendar = start.getCurrentTime();

		System.out.println("Started at "+startCalendar.getTime());
		System.out.println("Finished at "+endCalendar.getTime());

	}
	
	/*
	 * @param val1 corresponds to mean, lower, min, alpha, scale, or p values
	 * @param val2 corresponds to standardDeviation,limit, max, beta, shape, or trials values
	 * @param val3 corresponds to mode
	 * 
	 */
	public int readProcessingTime(ProcessingTime processingTimeObject, ElementParameter elementParameter)
	{
		DistributionFactory distributionFactory = new DistributionFactory();
		int processingTime=0;
		double val1=0;
		double val2=0;
		double val3=0;
		String distributionType="";
		
		if(processingTimeObject.getDurationParameter()!=null)
		{
			distributionType="DurationParameter";
			processingTime=formatDurationParameter(processingTimeObject.getDurationParameter());
		}
		else if(processingTimeObject.getNormalDistributionMean()!=0 && processingTimeObject.getNormalDistributionStandardDeviation()!=0)
		{
			distributionType="Normal";
			val1 = processingTimeObject.getNormalDistributionMean();
			val2 = processingTimeObject.getNormalDistributionStandardDeviation();
		}
		else if(processingTimeObject.getFloatingParameterValue()!=0)
		{
			distributionType="floatingParameterValue";
			Double dprocessingTime = processingTimeObject.getFloatingParameterValue();
			processingTime= dprocessingTime.intValue();
		}
		else if(processingTimeObject.getNumericParameterValue()!=0)
		{
			distributionType="numericParameterValue";
			processingTime=processingTimeObject.getNumericParameterValue();
			
		}
		else if(processingTimeObject.getTruncatedNormalDistributionMax()!=0 && processingTimeObject.getTruncatedNormalDistributionMin()!=0 
				&& processingTimeObject.getTruncatedNormalDistributionMean()!=0 && processingTimeObject.getTruncatedNormalDistributionStandardDeviation()!=0)
		{
			//parece que math no tiene truncated distribution
			distributionType="TruncatedNormal";
			val1 = processingTimeObject.getTruncatedNormalDistributionMean();
			val2 = processingTimeObject.getTruncatedNormalDistributionStandardDeviation();
		}
		else if(processingTimeObject.getTriangularDistributionMax()!=0 && processingTimeObject.getTriangularDistributionMin()!=0 && processingTimeObject.getTriangularDistributionMode()!=0)
		{
			distributionType="Triangular";
			val1=processingTimeObject.getTriangularDistributionMin();
			val2=processingTimeObject.getTriangularDistributionMax();
			val3=processingTimeObject.getTriangularDistributionMode();
		}
		else if(processingTimeObject.getUniformDistributionMax()!=0 && processingTimeObject.getUniformDistributionMin()!=0)
		{
			distributionType="Uniform";
			val1= processingTimeObject.getUniformDistributionMin();
			val2= processingTimeObject.getUniformDistributionMax();
		}
		else if(processingTimeObject.getLognormalDistributionMean()!=0 && processingTimeObject.getLognormalDistributionStandardDeviation()!=0)
		{
			/*
			 * la lognormal me pide shape y scale, pero BPSim me da son mean y standard deviation,
			 * resultando en numeros locos
			 * 
			double scale = processingTimeObject.getLognormalDistributionMean();
			double shape = processingTimeObject.getLognormalDistributionStandardDeviation();
			LogNormalDistribution logNormalDistribution = new LogNormalDistribution(scale, shape);
			Double dprocessingTime=logNormalDistribution.sample();
			processingTime= dprocessingTime.intValue();
			*/
			//esta sirviendo con un tiempo similar usando la distribucion normal 
			distributionType="LogNormal";
			val1 = processingTimeObject.getLognormalDistributionMean();
			val2 = processingTimeObject.getLognormalDistributionStandardDeviation();
		}
		else if(processingTimeObject.getBetaDistributionScale()!=0 && processingTimeObject.getBetaDistributionShape()!=0)
		{
			distributionType="Beta";
			val1= processingTimeObject.getBetaDistributionShape();
			val2= processingTimeObject.getBetaDistributionScale();
		}
		else if(processingTimeObject.getNegativeExponentialDistributionMean()!=0)
		{
			//parece que math no tiene negative exponential, usamos exponential
			distributionType="NegativeExponential";
			val1=processingTimeObject.getNegativeExponentialDistributionMean();
		}
		else if(processingTimeObject.getGammaDistributionScale()!=0 && processingTimeObject.getGammaDistributionShape()!=0)
		{
			distributionType="Gamma";
			val1= processingTimeObject.getGammaDistributionShape();
			val2= processingTimeObject.getGammaDistributionScale();
		}
		else if(processingTimeObject.getErlangDistributionK()!=0 && processingTimeObject.getErlangDistributionMean()!=0)
		{
			//parece que math no tiene la erlang
		}
		else if(processingTimeObject.getWeibullDistributionScale()!=0 && processingTimeObject.getWeibullDistributionShape()!=0)
		{
			distributionType="Weibull";
			val1= processingTimeObject.getWeibullDistributionShape();
			val2= processingTimeObject.getWeibullDistributionScale();
		}
		else if(processingTimeObject.getBinomialDistributionProbability()!=0 && processingTimeObject.getBinomialDistributionTrials()!=0)
		{
			distributionType="Binomial";
			val1= processingTimeObject.getBinomialDistributionProbability();
			val2=processingTimeObject.getBinomialDistributionTrials();
		}
		else if(processingTimeObject.getPoissonDistributionMean()!=0)
		{
			distributionType="Poisson";
			val1= processingTimeObject.getPoissonDistributionMean();
		}		
		//create distribution and get sample
		if(distributionType.equals("Normal")||distributionType.equals("TruncatedNormal")
				||distributionType.equals("Triangular")||distributionType.equals("Uniform")
				||distributionType.equals("LogNormal")||distributionType.equals("Beta")
				||distributionType.equals("NegativeExponential")||distributionType.equals("Gamma")
				||distributionType.equals("Weibull")||distributionType.equals("Binomial")
				||distributionType.equals("Poisson"))
		{
			Distribution distribution= distributionFactory.makeDistribution(distributionType, val1, val2, val3);
			Double dProcessingTime = distribution.getSample();
			processingTime= dProcessingTime.intValue();
		}
		//System.out.println(distributionType+" procTime: "+ processingTime);
		return processingTime;
	}
	
	/*
	 * @param val1 corresponds to mean, lower, min, alpha, scale, or p values
	 * @param val2 corresponds to standardDeviation,limit, max, beta, shape, or trials values
	 * @param val3 corresponds to mode
	 * 
	 */
	public int readWaitTime(WaitTime waitTimeObject, ElementParameter elementParameter)
	{
		DistributionFactory distributionFactory = new DistributionFactory();
		int waitTime=0;
		double val1=0;
		double val2=0;
		double val3=0;
		String distributionType="";
		
		if(waitTimeObject.getDurationParameter()!=null)
		{
			distributionType="DurationParameter";
			waitTime=formatDurationParameter(waitTimeObject.getDurationParameter());
		}
		else if(waitTimeObject.getNormalDistributionMean()!=0 && waitTimeObject.getNormalDistributionStandardDeviation()!=0)
		{
			distributionType="Normal";
			val1 = waitTimeObject.getNormalDistributionMean();
			val2 = waitTimeObject.getNormalDistributionStandardDeviation();
		}
		else if(waitTimeObject.getFloatingParameterValue()!=0)
		{
			distributionType="floatingParameterValue";
			Double dprocessingTime = waitTimeObject.getFloatingParameterValue();
			waitTime= dprocessingTime.intValue();
		}
		else if(waitTimeObject.getNumericParameterValue()!=0)
		{
			distributionType="numericParameterValue";
			waitTime=waitTimeObject.getNumericParameterValue();
			
		}
		else if(waitTimeObject.getTruncatedNormalDistributionMax()!=0 && waitTimeObject.getTruncatedNormalDistributionMin()!=0 
				&& waitTimeObject.getTruncatedNormalDistributionMean()!=0 && waitTimeObject.getTruncatedNormalDistributionStandardDeviation()!=0)
		{
			//parece que math no tiene truncated distribution
			distributionType="TruncatedNormal";
			val1 = waitTimeObject.getTruncatedNormalDistributionMean();
			val2 = waitTimeObject.getTruncatedNormalDistributionStandardDeviation();
		}
		else if(waitTimeObject.getTriangularDistributionMax()!=0 && waitTimeObject.getTriangularDistributionMin()!=0 && waitTimeObject.getTriangularDistributionMode()!=0)
		{
			distributionType="Triangular";
			val1=waitTimeObject.getTriangularDistributionMin();
			val2=waitTimeObject.getTriangularDistributionMax();
			val3=waitTimeObject.getTriangularDistributionMode();
		}
		else if(waitTimeObject.getUniformDistributionMax()!=0 && waitTimeObject.getUniformDistributionMin()!=0)
		{
			distributionType="Uniform";
			val1= waitTimeObject.getUniformDistributionMin();
			val2= waitTimeObject.getUniformDistributionMax();
		}
		else if(waitTimeObject.getLognormalDistributionMean()!=0 && waitTimeObject.getLognormalDistributionStandardDeviation()!=0)
		{
			/*
			 * la lognormal me pide shape y scale, pero BPSim me da son mean y standard deviation,
			 * resultando en numeros locos
			 * 
			double scale = processingTimeObject.getLognormalDistributionMean();
			double shape = processingTimeObject.getLognormalDistributionStandardDeviation();
			LogNormalDistribution logNormalDistribution = new LogNormalDistribution(scale, shape);
			Double dprocessingTime=logNormalDistribution.sample();
			processingTime= dprocessingTime.intValue();
			*/
			//esta sirviendo con un tiempo similar usando la distribucion normal 
			distributionType="LogNormal";
			val1 = waitTimeObject.getLognormalDistributionMean();
			val2 = waitTimeObject.getLognormalDistributionStandardDeviation();
		}
		else if(waitTimeObject.getBetaDistributionScale()!=0 && waitTimeObject.getBetaDistributionShape()!=0)
		{
			distributionType="Beta";
			val1= waitTimeObject.getBetaDistributionShape();
			val2= waitTimeObject.getBetaDistributionScale();
		}
		else if(waitTimeObject.getNegativeExponentialDistributionMean()!=0)
		{
			//parece que math no tiene negative exponential, usamos exponential
			distributionType="NegativeExponential";
			val1=waitTimeObject.getNegativeExponentialDistributionMean();
		}
		else if(waitTimeObject.getGammaDistributionScale()!=0 && waitTimeObject.getGammaDistributionShape()!=0)
		{
			distributionType="Gamma";
			val1= waitTimeObject.getGammaDistributionShape();
			val2= waitTimeObject.getGammaDistributionScale();
		}
		else if(waitTimeObject.getErlangDistributionK()!=0 && waitTimeObject.getErlangDistributionMean()!=0)
		{
			//parece que math no tiene la erlang
		}
		else if(waitTimeObject.getWeibullDistributionScale()!=0 && waitTimeObject.getWeibullDistributionShape()!=0)
		{
			distributionType="Weibull";
			val1= waitTimeObject.getWeibullDistributionShape();
			val2= waitTimeObject.getWeibullDistributionScale();
		}
		else if(waitTimeObject.getBinomialDistributionProbability()!=0 && waitTimeObject.getBinomialDistributionTrials()!=0)
		{
			distributionType="Binomial";
			val1= waitTimeObject.getBinomialDistributionProbability();
			val2=waitTimeObject.getBinomialDistributionTrials();
		}
		else if(waitTimeObject.getPoissonDistributionMean()!=0)
		{
			distributionType="Poisson";
			val1= waitTimeObject.getPoissonDistributionMean();
		}		
		//create distribution and get sample
		if(distributionType.equals("Normal")||distributionType.equals("TruncatedNormal")
				||distributionType.equals("Triangular")||distributionType.equals("Uniform")
				||distributionType.equals("LogNormal")||distributionType.equals("Beta")
				||distributionType.equals("NegativeExponential")||distributionType.equals("Gamma")
				||distributionType.equals("Weibull")||distributionType.equals("Binomial")
				||distributionType.equals("Poisson"))
		{
			Distribution distribution= distributionFactory.makeDistribution(distributionType, val1, val2, val3);
			Double dProcessingTime = distribution.getSample();
			waitTime= dProcessingTime.intValue();
		}
		//System.out.println(distributionType+" waitTime: "+ waitTime);
		return waitTime;		
	}
	
	/*
	 * @param val1 corresponds to mean, lower, min, alpha, scale, or p values
	 * @param val2 corresponds to standardDeviation,limit, max, beta, shape, or trials values
	 * @param val3 corresponds to mode
	 * 
	 */
	public int readInterTriggerTimerTime(InterTriggerTimer interTriggerTimerObject, ElementParameter elementParameter)
	{
		DistributionFactory distributionFactory = new DistributionFactory();
		int processingTime=0;
		double val1=0;
		double val2=0;
		double val3=0;
		String distributionType="";
		
		if(interTriggerTimerObject.getDurationParameter()!=null)
		{
			distributionType="DurationParameter";
			processingTime=formatDurationParameter(interTriggerTimerObject.getDurationParameter());
		}
		else if(interTriggerTimerObject.getNormalDistributionMean()!=0 && interTriggerTimerObject.getNormalDistributionStandardDeviation()!=0)
		{
			distributionType="Normal";
			val1 = interTriggerTimerObject.getNormalDistributionMean();
			val2 = interTriggerTimerObject.getNormalDistributionStandardDeviation();
		}
		else if(interTriggerTimerObject.getFloatingParameterValue()!=0)
		{
			distributionType="floatingParameterValue";
			Double dprocessingTime = interTriggerTimerObject.getFloatingParameterValue();
			processingTime= dprocessingTime.intValue();
		}
		else if(interTriggerTimerObject.getNumericParameterValue()!=0)
		{
			distributionType="numericParameterValue";
			processingTime=interTriggerTimerObject.getNumericParameterValue();
			
		}
		else if(interTriggerTimerObject.getTruncatedNormalDistributionMax()!=0 && interTriggerTimerObject.getTruncatedNormalDistributionMin()!=0 
				&& interTriggerTimerObject.getTruncatedNormalDistributionMean()!=0 && interTriggerTimerObject.getTruncatedNormalDistributionStandardDeviation()!=0)
		{
			//parece que math no tiene truncated distribution
			distributionType="TruncatedNormal";
			val1 = interTriggerTimerObject.getTruncatedNormalDistributionMean();
			val2 = interTriggerTimerObject.getTruncatedNormalDistributionStandardDeviation();
		}
		else if(interTriggerTimerObject.getTriangularDistributionMax()!=0 && interTriggerTimerObject.getTriangularDistributionMin()!=0 && interTriggerTimerObject.getTriangularDistributionMode()!=0)
		{
			distributionType="Triangular";
			val1=interTriggerTimerObject.getTriangularDistributionMin();
			val2=interTriggerTimerObject.getTriangularDistributionMax();
			val3=interTriggerTimerObject.getTriangularDistributionMode();
		}
		else if(interTriggerTimerObject.getUniformDistributionMax()!=0 && interTriggerTimerObject.getUniformDistributionMin()!=0)
		{
			distributionType="Uniform";
			val1= interTriggerTimerObject.getUniformDistributionMin();
			val2= interTriggerTimerObject.getUniformDistributionMax();
		}
		else if(interTriggerTimerObject.getLognormalDistributionMean()!=0 && interTriggerTimerObject.getLognormalDistributionStandardDeviation()!=0)
		{
			/*
			 * la lognormal me pide shape y scale, pero BPSim me da son mean y standard deviation,
			 * resultando en numeros locos
			 * 
			double scale = processingTimeObject.getLognormalDistributionMean();
			double shape = processingTimeObject.getLognormalDistributionStandardDeviation();
			LogNormalDistribution logNormalDistribution = new LogNormalDistribution(scale, shape);
			Double dprocessingTime=logNormalDistribution.sample();
			processingTime= dprocessingTime.intValue();
			*/
			//esta sirviendo con un tiempo similar usando la distribucion normal 
			distributionType="LogNormal";
			val1 = interTriggerTimerObject.getLognormalDistributionMean();
			val2 = interTriggerTimerObject.getLognormalDistributionStandardDeviation();
		}
		else if(interTriggerTimerObject.getBetaDistributionScale()!=0 && interTriggerTimerObject.getBetaDistributionShape()!=0)
		{
			distributionType="Beta";
			val1= interTriggerTimerObject.getBetaDistributionShape();
			val2= interTriggerTimerObject.getBetaDistributionScale();
		}
		else if(interTriggerTimerObject.getNegativeExponentialDistributionMean()!=0)
		{
			//parece que math no tiene negative exponential, usamos exponential
			distributionType="NegativeExponential";
			val1=interTriggerTimerObject.getNegativeExponentialDistributionMean();
		}
		else if(interTriggerTimerObject.getGammaDistributionScale()!=0 && interTriggerTimerObject.getGammaDistributionShape()!=0)
		{
			distributionType="Gamma";
			val1= interTriggerTimerObject.getGammaDistributionShape();
			val2= interTriggerTimerObject.getGammaDistributionScale();
		}
		else if(interTriggerTimerObject.getErlangDistributionK()!=0 && interTriggerTimerObject.getErlangDistributionMean()!=0)
		{
			//parece que math no tiene la erlang
		}
		else if(interTriggerTimerObject.getWeibullDistributionScale()!=0 && interTriggerTimerObject.getWeibullDistributionShape()!=0)
		{
			distributionType="Weibull";
			val1= interTriggerTimerObject.getWeibullDistributionShape();
			val2= interTriggerTimerObject.getWeibullDistributionScale();
		}
		else if(interTriggerTimerObject.getBinomialDistributionProbability()!=0 && interTriggerTimerObject.getBinomialDistributionTrials()!=0)
		{
			distributionType="Binomial";
			val1= interTriggerTimerObject.getBinomialDistributionProbability();
			val2=interTriggerTimerObject.getBinomialDistributionTrials();
		}
		else if(interTriggerTimerObject.getPoissonDistributionMean()!=0)
		{
			distributionType="Poisson";
			val1= interTriggerTimerObject.getPoissonDistributionMean();
		}		
		//create distribution and get sample
		if(distributionType.equals("Normal")||distributionType.equals("TruncatedNormal")
				||distributionType.equals("Triangular")||distributionType.equals("Uniform")
				||distributionType.equals("LogNormal")||distributionType.equals("Beta")
				||distributionType.equals("NegativeExponential")||distributionType.equals("Gamma")
				||distributionType.equals("Weibull")||distributionType.equals("Binomial")
				||distributionType.equals("Poisson"))
		{
			Distribution distribution= distributionFactory.makeDistribution(distributionType, val1, val2, val3);
			Double dProcessingTime = distribution.getSample();
			processingTime= dProcessingTime.intValue();
		}
		//System.out.println(distributionType+" procTime: "+ processingTime);
		return processingTime;
	}
	
	public boolean hasAvailableResources(Activity activity)
	{
		boolean hasAvailableResources=true;
		HashMap<String,Integer>requiredResources= activity.getRequiredResources();
		Iterator it = requiredResources.entrySet().iterator();
		while (it.hasNext()) {
		    HashMap.Entry pair = (HashMap.Entry)it.next();
		    String reqResName=(String)pair.getKey();
		    int reqResValue=(int) pair.getValue();
		    
		    for(int k=0;k<participants.size();k++)
		    {
		    	Participant participant = participants.get(k);	
		    	String resourceName=participant.getResourceName();
		    	int resourceAmount=participant.getTotalResources();
		    	int usedResources = participant.getUsedResources();
		    	int availableResources= resourceAmount-usedResources;
		    	if(reqResName.equals(resourceName))
		    	{
		    		//there are not enough resources
		    		if(availableResources<reqResValue)
		    		{
		    			hasAvailableResources=false;
		    		}
		    	}
		    }
		    
		    it.remove(); // avoids a ConcurrentModificationException
		}
		return hasAvailableResources;
	}
	public void acquireResources(Activity activity)
	{
		HashMap<String,Integer>requiredResources= activity.getRequiredResources();
		Iterator it = requiredResources.entrySet().iterator();
		while (it.hasNext()) {
		    HashMap.Entry pair = (HashMap.Entry)it.next();
		    String reqResName=(String)pair.getKey();
		    int reqResValue=(int) pair.getValue();
		    
		    for(int k=0;k<participants.size();k++)
		    {
		    	Participant participant = participants.get(k);	
		    	String resourceName=participant.getResourceName();
		    	if(reqResName.equals(resourceName))
		    	{
		    		participant.setUsedResources(participant.getUsedResources()+reqResValue);
		    	}
		    }
		    it.remove(); // avoids a ConcurrentModificationException
		}
	}
	public void releaseResources(Activity activity)
	{
		HashMap<String,Integer>requiredResources= activity.getRequiredResources();
		Iterator it = requiredResources.entrySet().iterator();
		while (it.hasNext()) {
		    HashMap.Entry pair = (HashMap.Entry)it.next();
		    String reqResName=(String)pair.getKey();
		    int reqResValue=(int) pair.getValue();
		    
		    for(int k=0;k<participants.size();k++)
		    {
		    	Participant participant = participants.get(k);	
		    	String resourceName=participant.getResourceName();
		    	if(reqResName.equals(resourceName))
		    	{
		    		participant.setUsedResources(participant.getUsedResources()-reqResValue);
		    	}
		    }
		    it.remove(); // avoids a ConcurrentModificationException
		}
	}
	
	public void moveActivity(DiagramPackage diagramPackage, Activity activity,Calendar enteringTime, int processingTime)
	{
		activity.setMovedForLackOfResources(true);
		Moment lastMoment =moments.get(moments.size()-1);
		Calendar lastDate =lastMoment.getTime();
		lastDate.add(Calendar.MINUTE, processingTime);
		Moment moment = new Moment(lastDate);
		activity.setTimeMovedForLackOfResources(activity.getTimeMovedForLackOfResources()+processingTime);
		Event eventActivityStart = new Event(Event.ACTIVITY_START, activity.getName(), diagramPackage,activity);
		moment.getEvents().add(eventActivityStart);
		moments.add(moment);
	}
	public void placeEndActivity(Calendar enteringTime,DiagramPackage diagramPackage,Activity activity)
	{
				Activity endActivity= activity;
				endActivity.setName(endActivity.getName()+"End");
				endActivity.setEndActivity(true);

				Event eventActivityStart = new Event(Event.ACTIVITY_START, endActivity.getName(), diagramPackage,endActivity);

				Moment startActivityHour= getHourByTime(enteringTime);
				startActivityHour.getEvents().add(eventActivityStart);
				if(hasHour(startActivityHour))
				{
					setHour(startActivityHour);
				}
				else
				{
					moments.add(startActivityHour);
				}
		
	}
	
	public void placeNextActivities(Calendar enteringTime,DiagramPackage diagramPackage,Activity activity)
	{
		if(activity.hasGateway())
		{

			Gateway gateway=activity.getGateway();
			
			//event based gateway
			if(gateway.getExclusiveType()!=null)
			{
				Activity nextActivity = getNextEventBasedGatewayActivity(diagramPackage, activity);

				Event eventActivityStart = new Event(Event.ACTIVITY_START, nextActivity.getName(), diagramPackage,nextActivity);

				Moment startActivityHour= getHourByTime(enteringTime);
				startActivityHour.getEvents().add(eventActivityStart);
				if(hasHour(startActivityHour))
				{
					setHour(startActivityHour);
				}
				else
				{
					moments.add(startActivityHour);
				}
			}
			//regular gateway
			else if(gateway.getGatewayType()==null)
			{
				Activity nextActivity = getNextGatewayActivity(diagramPackage, activity);

				Event eventActivityStart = new Event(Event.ACTIVITY_START, nextActivity.getName(), diagramPackage,nextActivity);

				Moment startActivityHour= getHourByTime(enteringTime);
				startActivityHour.getEvents().add(eventActivityStart);
				if(hasHour(startActivityHour))
				{
					setHour(startActivityHour);
				}
				else
				{
					moments.add(startActivityHour);
				}
			}
			//non parallel gateway
			if(gateway.getGatewayType()!=null && !gateway.getGatewayType().equals("Parallel"))
			{
				Activity nextActivity = getNextGatewayActivity(diagramPackage, activity);

				Event eventActivityStart = new Event(Event.ACTIVITY_START, nextActivity.getName(), diagramPackage,nextActivity);

				Moment startActivityHour= getHourByTime(enteringTime);
				startActivityHour.getEvents().add(eventActivityStart);
				if(hasHour(startActivityHour))
				{
					setHour(startActivityHour);
				}
				else
				{
					moments.add(startActivityHour);
				}
			}
			else if(gateway.getGatewayType()!=null && gateway.getGatewayType().equals("Parallel"))
			{
				//it's a parallel gateway
				ArrayList<Activity> nextActivities= getNextActivities(diagramPackage, activity);

				for(int k=0;k<nextActivities.size();k++)
				{
					Activity nextActivity= nextActivities.get(k);

					Event eventActivityStart = new Event(Event.ACTIVITY_START, nextActivity.getName(), diagramPackage,nextActivity);

					Moment startActivityHour= getHourByTime(enteringTime);
					startActivityHour.getEvents().add(eventActivityStart);
					if(hasHour(startActivityHour))
					{
						setHour(startActivityHour);
					}
					else
					{
						moments.add(startActivityHour);
					}
				}
				
			}
			

		}
		else
		{
			ArrayList<Activity> nextActivities= getNextActivities(diagramPackage, activity);

			for(int k=0;k<nextActivities.size();k++)
			{
				Activity nextActivity= nextActivities.get(k);

				Event eventActivityStart = new Event(Event.ACTIVITY_START, nextActivity.getName(), diagramPackage,nextActivity);

				Moment startActivityHour= getHourByTime(enteringTime);
				startActivityHour.getEvents().add(eventActivityStart);
				if(hasHour(startActivityHour))
				{
					setHour(startActivityHour);
				}
				else
				{
					moments.add(startActivityHour);
				}
			}
		}
	}
	
	public ArrayList<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(ArrayList<Participant> resources) {
		this.participants = resources;
	}
	public void addParticipants(ArrayList<Participant> resources) {
		for(int i=0;i<resources.size();i++)
		{
			Participant participant = resources.get(i);
			if(!containsParticipant(participant))
			{
				participants.add(participant);
			}
			else
			{
				if(participant.getTotalResources()>getParticipant(participant).getTotalResources())
				{
					setParticipantTotalResources(participant,participant.getTotalResources());
				}
			}
		}
	}
	public boolean containsParticipant(Participant participant) {
		boolean containsParticipant=false;
		for(int i=0;i<participants.size();i++)
		{
			if(participant.getName().equals(participants.get(i).getName())) {
				containsParticipant=true;
			}
		}
		return containsParticipant;
	}
	public Participant getParticipant(Participant participant) {
		ArrayList<ExtendedAttribute>extendedAttributes=new ArrayList<ExtendedAttribute>();
		Participant res = new Participant("", "", "", "",extendedAttributes );
		
		for(int i=0;i<participants.size();i++)
		{
			if(participant.getName().equals(participants.get(i).getName())) {
				res=participant;
			}
		}
		return res;
	}
	public void setParticipantTotalResources(Participant participant,int totalResources) {
		
		for(int i=0;i<participants.size();i++)
		{
			if(participant.getName().equals(participants.get(i).getName())) {
				participants.get(i).setTotalResources(totalResources);
			}
		}
	}
	public int getDiagramPackageTotalArrivals(DiagramPackage diagramPackage)
	{
		int res=0;
		WorkflowProcess workflowProcesses=diagramPackage.getPools().get(1).getWorkflowProcess();

		//get the start event
		ArrayList<Activity>activities= workflowProcesses.getActivities();
		for(int j=0;j<activities.size();j++)
		{
			Activity activity = activities.get(j);
			if(activity.hasEvent())
			{			
				if(activity.getEvent() instanceof StartEvent)
				{
					StartEvent startEvent = (StartEvent) activity.getEvent();
					String startEventId= activity.getId();
					ElementParameter startEventElementParameter= startEvent.getElementParameter();

					res=startEventElementParameter.getControlParameter().getTriggerCount().getNumericParameterValue();
				}
			}
		}
		return res;
	}

}
