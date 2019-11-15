package readers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.Activity;
import model.ActivitySet;
import model.ConnectorGraphicsInfo;
import model.Coordinate;
import model.DiagramPackage;
import model.EndEvent;
import model.Event;
import model.ExtendedAttribute;
import model.ExternalPackage;
import model.Formatting;
import model.Gateway;
import model.IntermediateEvent;
import model.Lane;
import model.Modification;
import model.NodeGraphicsInfo;
import model.PackageHeader;
import model.Participant;
import model.Pool;
import model.RedefinableHeader;
import model.StartEvent;
import model.Subflow;
import model.Subprocess;
import model.Task;
import model.TaskBuisnessRule;
import model.TaskManual;
import model.TaskReceive;
import model.TaskScript;
import model.TaskSend;
import model.TaskService;
import model.TaskUser;
import model.Transition;
import model.TriggerResultMessage;
import model.WorkflowProcess;

public class BPMNDiagramReader {

	public BPMNDiagramReader()
	{
		
	}
	
	public DiagramPackage readDiagram(String route)
	{
		//packageDiagram variables
		String xmlnsXsd="";
		String xmlnsXsi="";
		boolean onlyOneProcess=false;
		String id="";
		String name="";
		String xmlns="";
		
		//packageHeader variables
		String xpdlVersion ="";
		String vendor ="";
		Date created = new Date();
		Date modificationDate =new Date();
		String description ="";
		String documentation ="";
		String creationVersion ="";
		String packageHeaderVersion ="";
		ArrayList<Modification> modifications = new ArrayList<Modification>();
		
		PackageHeader packageHeader = new PackageHeader(xpdlVersion, vendor, created, modificationDate, description, documentation, creationVersion, packageHeaderVersion, modifications);
		
		//redefinableHeader variables
		String author ="";
		String redefinableHeaderVersion= "";
		String countryKey="";
		RedefinableHeader redefinableHeader = new RedefinableHeader(author, redefinableHeaderVersion, countryKey);
		
		ArrayList<ExternalPackage> externalPackages = new ArrayList<ExternalPackage>();
		ArrayList<Participant> participants = new ArrayList<Participant>();
		ArrayList<ExtendedAttribute> extendedAttributes = new ArrayList<ExtendedAttribute>();
		ArrayList <Pool> pools = new ArrayList<Pool>();
		ArrayList<WorkflowProcess> workflowProcesses= new ArrayList<WorkflowProcess>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document doc =builder.parse(route);
			
			NodeList packageDiagramList = doc.getElementsByTagName("Package");
			Node pd = packageDiagramList.item(0);
			if(pd.getNodeType()== Node.ELEMENT_NODE)
			{
				//read Package element-------------------------------------------------------------------
				Element packageDiagram = (Element) pd;
				xmlnsXsd= packageDiagram.getAttribute("xmlns:xsd");
				xmlnsXsi = packageDiagram.getAttribute("xmlns:xsi");
				onlyOneProcess = Boolean.parseBoolean(packageDiagram.getAttribute("OnlyOneProcess"));
				id = packageDiagram.getAttribute("Id");
				name = packageDiagram.getAttribute("Name");
				xmlns = packageDiagram.getAttribute("xmlns");
				//System.out.println("Package attributes read");
				
				NodeList packageList =packageDiagram.getChildNodes();
				
				for(int i=0; i< packageList.getLength();i++)
				{
					Node nPackage =packageList.item(i);
					if(nPackage.getNodeType()== Node.ELEMENT_NODE)
					{
						Element packageElement = (Element) nPackage;
						String packageElementTagName= packageElement.getTagName();
						// read PackageHeader element-------------------------------------------------
						if(packageElementTagName=="PackageHeader")
						{
							NodeList packageHeaderList = packageElement.getChildNodes();
							packageHeader =readPackageHeader(packageHeaderList);
							//System.out.println("packageHeader read");
						}
						//read RedefinabeHeader element
						if(packageElementTagName=="RedefinableHeader")
						{
							NodeList redefinableHeaderList = packageElement.getChildNodes();
							redefinableHeader= readRedefinableReader(redefinableHeaderList);
							//System.out.println("redefinableHeader read");
						}
						//read ExternalPackages element
						if(packageElementTagName=="ExternalPackages")
						{
							NodeList externalPackagesList = packageElement.getChildNodes();
							externalPackages=readExternalPackages(externalPackagesList);
							//System.out.println("externalPackages read");
						}
						//read Participants element
						if(packageElementTagName=="Participants")
						{
							NodeList participantsList = packageElement.getChildNodes();
							participants =readParticipants(participantsList);
							//System.out.println("participants read");
						}
						if(packageElementTagName=="Pools")
						{
							NodeList poolsList = packageElement.getChildNodes();
							pools=readPools(poolsList);
							//System.out.println("pools read");
						}
						if(packageElementTagName=="WorkflowProcesses")
						{
							NodeList workflowProcessesList = packageElement.getChildNodes();
							workflowProcesses=readWorkflowProcesses(workflowProcessesList);
							//System.out.println("workflow processes read");
						}
						if(packageElementTagName=="ExtendedAttributes")
						{
							NodeList extendedAttributesList = packageElement.getChildNodes();
							extendedAttributes=readExtendedAttributes(extendedAttributesList);
							//System.out.println("extended attributes read");
						}
					}					
				}
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DiagramPackage diagramPackage = new DiagramPackage(xmlnsXsd, xmlnsXsi, onlyOneProcess, id, name, xmlns, packageHeader, redefinableHeader, externalPackages, participants, extendedAttributes, pools, workflowProcesses);
		
		//assign workflow processes to each pool
		assignWorkflowProcesses(diagramPackage);
		
		return diagramPackage;
	}
	
	private void assignWorkflowProcesses(DiagramPackage diagramPackage) {
		ArrayList<WorkflowProcess>workflowProcesses =diagramPackage.getWorkflowProcesses();
		//run through the pools
		for(int i=0;i<diagramPackage.getPools().size();i++)
		{
			//diagramPackage.getPools().get(i).setWorkflowProcess(workflowProcess);;
			//run through the processes		
			Pool pool = diagramPackage.getPools().get(i);
			for(int j=0;j<workflowProcesses.size();j++)
			{
				WorkflowProcess workflowProcess= workflowProcesses.get(j);
				if(workflowProcess.getId().equals(pool.getWorkFlowProcessId()))
				{
					diagramPackage.getPools().get(i).setWorkflowProcess(workflowProcess);
				}
			}
		}
	}

	public PackageHeader readPackageHeader(NodeList nodeList)
	{
		//variables to construct the PackageHeader object
		String xpdlVersion ="";
		String vendor ="";
		Date created = new Date();
		Date modificationDate =new Date();
		String description ="";
		String documentation ="";
		String creationVersion ="";
		String version ="";
		ArrayList<Modification> modifications = new ArrayList<Modification>();
		
		for(int j=0;j<nodeList.getLength();j++)
		{
			Node nPackageHeader = nodeList.item(j);
			if(nPackageHeader.getNodeType()== Node.ELEMENT_NODE)
			{
				Element packageHeaderElement = (Element) nPackageHeader;
				String packageHeaderElementTagName =packageHeaderElement.getTagName();
				if(packageHeaderElementTagName=="XPDLVersion")
				{
					xpdlVersion=nPackageHeader.getTextContent();
				}
				if(packageHeaderElementTagName=="Vendor")
				{
					vendor=nPackageHeader.getTextContent();
				}
				if(packageHeaderElementTagName=="Created")
				{
					created=formatDate(nPackageHeader.getTextContent());
				}
				if(packageHeaderElementTagName=="ModificationDate")
				{
					modificationDate= formatDate(nPackageHeader.getTextContent());
				}
				if(packageHeaderElementTagName=="Description")
				{
					description=nPackageHeader.getTextContent();
				}
				if(packageHeaderElementTagName=="Documentation")
				{
					documentation=nPackageHeader.getTextContent();
				}
				if(packageHeaderElementTagName=="CreationVersion")
				{
					creationVersion=nPackageHeader.getTextContent();
				}
				if(packageHeaderElementTagName=="Version")
				{
					version=nPackageHeader.getTextContent();
				}
				// read Modifications element----------------------------------------------------
				if(packageHeaderElementTagName=="Modifications")
				{
					NodeList modificationList = nPackageHeader.getChildNodes();
					modifications= readModifications(modificationList);
				}
			}
		}
		//----------------------Construct PackageHeader object
		PackageHeader packageHeader = new PackageHeader(xpdlVersion, vendor, created, modificationDate, description, documentation, creationVersion, version, modifications);		
		return packageHeader;
	}
	
	public ArrayList<Modification> readModifications(NodeList nodeList)
	{
		ArrayList<Modification> modifications = new ArrayList<Modification>();
		for(int i =0;i< nodeList.getLength(); i++)
		{
			Node nModification = nodeList.item(i);
			if(nModification.getNodeType()== Node.ELEMENT_NODE)
			{
				Element modificationElement= (Element)nModification;
				String dateStr =modificationElement.getAttribute("Date");
				String userName= modificationElement.getAttribute("UserName");
				Date date = formatDate(dateStr);
//----------------------------------------------Construct modification objects
				Modification modification =new Modification(date, userName);
				modifications.add(modification);
			}
		}
		return modifications;
	}
	
	public RedefinableHeader readRedefinableReader(NodeList nodelist)
	{
		//variables to construct the RedefinableHeader object
		String author ="";
		String version= "";
		String countryKey="";
		for(int i=0;i<nodelist.getLength();i++)
		{
			Node nRedefinableHeader = nodelist.item(i);
			if(nRedefinableHeader.getNodeType()== Node.ELEMENT_NODE)
			{
				Element redefinableHeaderElement = (Element) nRedefinableHeader;
				String redefinableHeaderElementTagName =redefinableHeaderElement.getTagName();
				if(redefinableHeaderElementTagName=="Author")
				{
					author =nRedefinableHeader.getTextContent();
				}
				if(redefinableHeaderElementTagName=="Version")
				{
					version=nRedefinableHeader.getTextContent();
				}
				if(redefinableHeaderElementTagName=="Countrykey")
				{
					countryKey=nRedefinableHeader.getTextContent();
				}
			}
		}
		RedefinableHeader redefinableHeader = new RedefinableHeader(author, version, countryKey);
		return redefinableHeader;
	}
	
	public ArrayList<ExternalPackage> readExternalPackages(NodeList nodeList)
	{
		ArrayList<ExternalPackage> externalPackages = new ArrayList<ExternalPackage>();
		return externalPackages;		
	}
	
	public ArrayList<Participant> readParticipants(NodeList nodeList)
	{
		ArrayList<Participant> participants = new ArrayList<Participant>();
		for(int i=0;i<nodeList.getLength();i++)
		{
			String id="";
			String name="";
			String participantType="";
			String description="";
			ArrayList<ExtendedAttribute>extendedAttributes = new ArrayList<ExtendedAttribute>();
			
			Node nParticipant = nodeList.item(i);
			if(nParticipant.getNodeType()== Node.ELEMENT_NODE)
			{
				Element participantElement = (Element) nParticipant;
				id=participantElement.getAttribute("Id");
				name = participantElement.getAttribute("Name");
				//read each participant
				NodeList participantList = participantElement.getChildNodes();
				for(int j=0;j<participantList.getLength();j++)
				{
					Node nParticipantContent = participantList.item(j);
					if(nParticipantContent.getNodeType()== Node.ELEMENT_NODE)
					{
						Element participantContentElement = (Element) nParticipantContent;
						String participantContentTagName =participantContentElement.getTagName();
						if(participantContentTagName=="ParticipantType")
						{
							participantType =nParticipantContent.getTextContent();
						}
						if(participantContentTagName=="Description")
						{
							description=nParticipantContent.getTextContent();
						}
						if(participantContentTagName=="ExtendedAttributes")
						{
							NodeList extendedAttributeList = nParticipantContent.getChildNodes();
							extendedAttributes= readExtendedAttributes(extendedAttributeList);
						}
					}
				}
				Participant participant = new Participant(id, name, participantType, description, extendedAttributes);
				participants.add(participant);
			}
		}
		return participants;
	}
	
	public ArrayList<ExtendedAttribute> readExtendedAttributes(NodeList nodeList)
	{
		ArrayList<ExtendedAttribute> extendedAttributes = new ArrayList<ExtendedAttribute>();
		for(int i =0;i< nodeList.getLength(); i++)
		{
			Node nExtendedAttribute = nodeList.item(i);
			if(nExtendedAttribute.getNodeType()== Node.ELEMENT_NODE)
			{
				Element modificationElement= (Element)nExtendedAttribute;
				String name =modificationElement.getAttribute("Name");
				String value= modificationElement.getAttribute("Value");
//----------------------------------------------Construct modification objects
				ExtendedAttribute extendedAttribute = new ExtendedAttribute(name);
				if(value!=null)
				{
					extendedAttribute.setValue(value);
				}
				extendedAttributes.add(extendedAttribute);
			}
		}		
		return extendedAttributes;
	}
	
	public ArrayList<Pool> readPools(NodeList nodeList)
	{
		ArrayList<Pool> pools = new ArrayList<Pool>();
		for(int i=0;i<nodeList.getLength();i++)
		{
			String id="";
			String name = "";
			boolean boundaryVisible=false;
			String workflowProcessId= "";
			Formatting formatting = new Formatting("", "", 0, false, false, false, false, 0);
			NodeGraphicsInfo nodeGraphicsInfo = new NodeGraphicsInfo("", 0, 0, 0, 0, false, formatting);
			ArrayList<Lane> lanes= new ArrayList<Lane>();
			Node nPools = nodeList.item(i);
			if(nPools.getNodeType()== Node.ELEMENT_NODE)
			{
				Element poolsElement = (Element) nPools;
				id=poolsElement.getAttribute("Id");
				name = poolsElement.getAttribute("Name");
				workflowProcessId = poolsElement.getAttribute("Process");
				boundaryVisible = Boolean.parseBoolean(poolsElement.getAttribute("BoundaryVisible"));	
				
				NodeList poolList = poolsElement.getChildNodes();
				for(int j=0;j<poolList.getLength();j++)
				{
					Node nPool = poolList.item(j);
					if(nPool.getNodeType()== Node.ELEMENT_NODE)
					{
						Element poolElement = (Element) nPool;
						String poolElementTagName =poolElement.getTagName();
						if(poolElementTagName=="Lanes")
						{
							NodeList laneList = poolElement.getChildNodes();
							lanes= readLanes(laneList);
						}
						if(poolElementTagName=="NodeGraphicsInfos")
						{
							NodeList nodeGraphicInfoList = poolElement.getChildNodes();
							nodeGraphicsInfo=readNodeGraphicsInfo(nodeGraphicInfoList);
						}
						
					}
				}
				if(id!=null)
				{
					Pool pool = new Pool(id, name, boundaryVisible, workflowProcessId, nodeGraphicsInfo, lanes);
					pool.setWorkFlowProcessId(workflowProcessId);
					pools.add(pool);
				}					
			}
		}
		return pools;
	}
	
	public ArrayList<Lane> readLanes(NodeList nodeList)
	{
		ArrayList<Lane>lanes = new ArrayList<Lane>();
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			String id="";
			String name = "";
			Formatting formatting = new Formatting("", "", 0, false, false, false, false, 0);
			NodeGraphicsInfo nodeGraphicsInfo = new NodeGraphicsInfo("", 0, 0, 0, 0, false, formatting);
			String documentation="";
			ArrayList<ExtendedAttribute> extendedAttributes = new ArrayList<ExtendedAttribute>();
			Node nlanes = nodeList.item(i);
			if(nlanes.getNodeType()== Node.ELEMENT_NODE)
			{
				Element lanesElement = (Element) nlanes;
				id=lanesElement.getAttribute("Id");
				name = lanesElement.getAttribute("Name");	
				
				NodeList laneList = lanesElement.getChildNodes();
				for(int j=0;j<laneList.getLength();j++)
				{
					Node nLane = laneList.item(j);
					if(nLane.getNodeType()== Node.ELEMENT_NODE)
					{
						Element laneElement = (Element) nLane;
						String laneElementTagName = laneElement.getTagName();
						if(laneElementTagName=="NodeGraphicsInfos")
						{
							NodeList nodeGraphicsInfoList = laneElement.getChildNodes();
							nodeGraphicsInfo= readNodeGraphicsInfo(nodeGraphicsInfoList);
						}
						if(laneElementTagName=="Documentation")
						{
							documentation=nLane.getTextContent();
						}
						if(laneElementTagName=="ExtendedAttributes")
						{
							NodeList extendedAttributeList = nLane.getChildNodes();
							extendedAttributes= readExtendedAttributes(extendedAttributeList);
						}
					}
				}
				Lane lane = new Lane(id, name, documentation, nodeGraphicsInfo, extendedAttributes);
				lanes.add(lane);
			}
		}
		return lanes;
	}
	
	public NodeGraphicsInfo readNodeGraphicsInfo(NodeList nodeList)
	{	
		String toolId="";
		int height=0;
		int width =0;
		int borderColor=0;
		int fillColor=0;
		boolean borderVisible=false;
		int xCoordinate =0;
		int yCoordinate=0;
		Coordinate coordinate= new Coordinate(0, 0);
		Formatting formatting = new Formatting("", "", 0, false, false, false, false, 0);
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node nNGIs = nodeList.item(i);
			if(nNGIs.getNodeType()== Node.ELEMENT_NODE)
			{
				Element nGIsElement = (Element) nNGIs;
				toolId=nGIsElement.getAttribute("ToolId");
				height = Integer.parseInt(nGIsElement.getAttribute("Height"));
				width = Integer.parseInt(nGIsElement.getAttribute("Width"));
				borderColor = Integer.parseInt(nGIsElement.getAttribute("BorderColor"));
				fillColor = Integer.parseInt(nGIsElement.getAttribute("FillColor"));
				borderVisible = Boolean.parseBoolean(nGIsElement.getAttribute("BorderVisible"));
				NodeList nGIList = nGIsElement.getChildNodes();
				for(int j=0;j<nGIList.getLength();j++)
				{
					Node nNGI = nGIList.item(j);
					if(nNGI.getNodeType()== Node.ELEMENT_NODE)
					{
						Element nNGIElement = (Element) nNGI;
						String nGITagName = nNGIElement.getTagName();
						if(nGITagName=="Coordinates")
						{
							xCoordinate= Integer.parseInt(nNGIElement.getAttribute("XCoordinate"));
							yCoordinate= Integer.parseInt(nNGIElement.getAttribute("YCoordinate"));
							coordinate = new Coordinate(xCoordinate, yCoordinate);
							
						}
						if(nGITagName=="Formatting")
						{
							NodeList formattingList = nNGIElement.getChildNodes();
							formatting = readFormatting(formattingList);	
						}
					}
				}
			}
		}
		NodeGraphicsInfo nodeGraphicsInfo = new NodeGraphicsInfo(toolId, height, width, borderColor, fillColor, borderVisible, formatting, coordinate);
		return nodeGraphicsInfo;
	}
	
	public Formatting readFormatting(NodeList nodeList)
	{
		String alignment= "";
		String fontName= "";
		int sizeFont=0;
		boolean bold=false;
		boolean italic=false;
		boolean strikeout=false;
		boolean underline=false;
		int colorFont=0;
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node nFormatting = nodeList.item(i);
			if(nFormatting.getNodeType()== Node.ELEMENT_NODE)
			{
				Element formattingElement = (Element) nFormatting;
				String formattingElementTagName =formattingElement.getTagName();
				if(formattingElementTagName=="Alignment")
				{
					alignment=nFormatting.getTextContent();
				}
				if(formattingElementTagName=="FontName")
				{
					fontName=nFormatting.getTextContent();
				}
				if(formattingElementTagName=="SizeFont")
				{
					sizeFont= Integer.parseInt(formattingElement.getTextContent());
				}
				if(formattingElementTagName=="Bold")
				{
					bold = Boolean.parseBoolean(formattingElement.getTextContent());
				}
				if(formattingElementTagName=="Italic")
				{
					italic = Boolean.parseBoolean(formattingElement.getTextContent());
				}
				if(formattingElementTagName=="Strikeout")
				{
					strikeout = Boolean.parseBoolean(formattingElement.getTextContent());
				}
				if(formattingElementTagName=="Underline")
				{
					underline = Boolean.parseBoolean(formattingElement.getTextContent());
				}
				if(formattingElementTagName=="ColorFont")
				{
					colorFont= Integer.parseInt(formattingElement.getTextContent());
				}
			}
		}
		Formatting formatting = new Formatting(alignment, fontName, sizeFont, bold, italic, strikeout, underline, colorFont);
		return formatting;
	}
	
	public ArrayList<WorkflowProcess> readWorkflowProcesses(NodeList nodeList)
	{
		ArrayList<WorkflowProcess> workflowProcesses = new ArrayList<WorkflowProcess>();
		for(int i=0;i<nodeList.getLength();i++)
		{
			String id="";
			String name = "";
			Date created = new Date();
			String description ="";
			RedefinableHeader redefinableHeader = new RedefinableHeader("", "", "");
			ArrayList<ExtendedAttribute>extendedAttributes = new ArrayList<ExtendedAttribute>();
			ArrayList<Transition> transitions = new ArrayList<Transition>();
			ArrayList<ActivitySet>activitySets = new ArrayList<ActivitySet>();
			ArrayList<Activity> activities = new ArrayList<Activity>();
			Node nWorkflowProcesses = nodeList.item(i);
			if(nWorkflowProcesses.getNodeType()== Node.ELEMENT_NODE)
			{
				
				Element workflowProcessesElement = (Element) nWorkflowProcesses;
				id=workflowProcessesElement.getAttribute("Id");
				name = workflowProcessesElement.getAttribute("Name");
				
				NodeList workflowProcessList = workflowProcessesElement.getChildNodes();
				for(int j=0;j<workflowProcessList.getLength();j++)
				{
					Node nWorkFlowProcess = workflowProcessList.item(j);
					if(nWorkFlowProcess.getNodeType()== Node.ELEMENT_NODE)
					{
						Element workflowProcessElement = (Element) nWorkFlowProcess;
						String workflowProcessElementTagName =workflowProcessElement.getTagName();
						if(workflowProcessElementTagName=="ProcessHeader")
						{
							NodeList processHeaderList = workflowProcessElement.getChildNodes();
							ArrayList<String> processHeaderInfo= readProcessHeader(processHeaderList);
							String createdStr= processHeaderInfo.get(0);
							created = formatDate(createdStr);
							description=processHeaderInfo.get(1);
						}
						if(workflowProcessElementTagName=="RedefinableHeader")
						{
							NodeList redefinableHeaderList = workflowProcessElement.getChildNodes();
							redefinableHeader=readRedefinableHeader(redefinableHeaderList);
						}
						if(workflowProcessElementTagName=="ActivitySets")
						{
							NodeList activitySetsList = workflowProcessElement.getChildNodes();
							activitySets=readActivitySets(activitySetsList,name);
							
						}
						if(workflowProcessElementTagName=="DataInputOutputs")
						{
							NodeList dIOList = workflowProcessElement.getChildNodes();
							
						}
						if(workflowProcessElementTagName=="Activities")
						{
							NodeList activityList = workflowProcessElement.getChildNodes();
							activities=readActivities(activityList,name);
							
						}
						if(workflowProcessElementTagName=="Transitions")
						{
							NodeList transitionList = workflowProcessElement.getChildNodes();
							transitions=readTransitions(transitionList);
							
						}
						if(workflowProcessElementTagName=="ExtendedAttributes")
						{
							NodeList extendedAttributeList = workflowProcessElement.getChildNodes();
							extendedAttributes= readExtendedAttributes(extendedAttributeList);
							
						}
					}
				}
			}
			WorkflowProcess workflowProcess = new WorkflowProcess(id, name, created, description, redefinableHeader, extendedAttributes, transitions, activitySets, activities);
			workflowProcesses.add(workflowProcess);
		}
		return workflowProcesses;
	}
	/**
	 * Method that returns an String ArrayList, where the 0 index contains the created date as string,
	 * and the 1 index contains the description. Both are part of the processHeader of a workflowProcess
	 * @param nodeList
	 * @return
	 */
	public ArrayList<String> readProcessHeader(NodeList nodeList)
	{
		ArrayList<String> processHeader = new ArrayList<String>();
		String created ="";
		String description="";		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node nProcessHeader = nodeList.item(i);
			if(nProcessHeader.getNodeType()== Node.ELEMENT_NODE)
			{
				Element processHeaderElement = (Element) nProcessHeader;
				String processHeaderElementTagName =processHeaderElement.getTagName();
				if(processHeaderElementTagName=="Created")
				{
					created=nProcessHeader.getTextContent();
				}
				if(processHeaderElementTagName=="Description")
				{
					description=nProcessHeader.getTextContent();
				}
			}
		}
		processHeader.add(created);
		processHeader.add(description);
		return processHeader;
	}
	
	public RedefinableHeader readRedefinableHeader(NodeList nodeList)
	{
		String author ="";
		String version="";		
		String countryKey="";
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node nRedefinableHeader = nodeList.item(i);
			if(nRedefinableHeader.getNodeType()== Node.ELEMENT_NODE)
			{
				Element redefinableHeaderElement = (Element) nRedefinableHeader;
				String redefinableHeaderElementTagName =redefinableHeaderElement.getTagName();
				if(redefinableHeaderElementTagName=="Author")
				{
					author=nRedefinableHeader.getTextContent();
				}
				if(redefinableHeaderElementTagName=="Version")
				{
					version=nRedefinableHeader.getTextContent();
				}
				if(redefinableHeaderElementTagName=="CountryKey")
				{
					countryKey=nRedefinableHeader.getTextContent();
				}
			}
		}
		RedefinableHeader redefinableHeader = new RedefinableHeader(author, version, countryKey);
		return redefinableHeader;
	}
		
	public ArrayList<ActivitySet> readActivitySets(NodeList nodeList, String processName)
	{
		ArrayList<ActivitySet> activitySets = new ArrayList<ActivitySet>();
		for(int i=0;i<nodeList.getLength();i++)
		{
			String id="";
			String name = "";
			ArrayList<Activity> activities= new ArrayList<Activity>();
			ArrayList<Transition> transitions = new ArrayList<Transition>();
			Node nActivitySets = nodeList.item(i);
			if(nActivitySets.getNodeType()== Node.ELEMENT_NODE)
			{
				Element activitySetsElement = (Element) nActivitySets;
				id=activitySetsElement.getAttribute("Id");
				name = activitySetsElement.getAttribute("Name");
				name= processName+"-"+name;
				NodeList activitySetList = activitySetsElement.getChildNodes();
				for(int j=0;j<activitySetList.getLength();j++)
				{
					Node nActiviySet = activitySetList.item(j);
					if(nActiviySet.getNodeType()== Node.ELEMENT_NODE)
					{
						Element activitySetElement = (Element) nActiviySet;
						String activitySetElementTagName =activitySetElement.getTagName();
						if(activitySetElementTagName=="Asociations")
						{
							//no asociatons found yet
						}
						if(activitySetElementTagName=="Artifacts")
						{
							//no artifacts found yet
						}
						if(activitySetElementTagName=="Activities")
						{
							NodeList activityList = activitySetElement.getChildNodes();
							readActivities(activityList,processName);
							activities=readActivities(activityList,processName);
						}
						if(activitySetElementTagName=="Transitions")
						{
							NodeList transitionList = activitySetElement.getChildNodes();
							transitions =readTransitions(transitionList);
						}
					}
				}
			}
			ActivitySet activitySet = new ActivitySet(id, name, activities,transitions);
			activitySets.add(activitySet);
		}
		return activitySets;
	}
	
	public ArrayList<Activity> readActivities(NodeList nodeList, String processName)
	{
		
		ArrayList<Activity> activities = new ArrayList<Activity>();
		for(int i=0;i<nodeList.getLength();i++)
		{
			String id="";
			String name = "";
			String description="";
			String documentation="";
			Formatting formatting = new Formatting("", "", 0, false, false, false, false, 0);
			NodeGraphicsInfo nodeGraphicsInfo = new NodeGraphicsInfo("", 0, 0, 0, 0, false, formatting);
			ArrayList<ExtendedAttribute> extendedAttributes= new ArrayList<ExtendedAttribute>();
			
			//if the activity is an event
			boolean isEvent=false;
			Event event = new Event();
			
			//if the activity is a gateway
			boolean isGateway=false;
			Gateway gateway = new Gateway("");
			
			//if the activity is a task
			boolean isTask=false;
			Task task = new Task();
			String loopType="";
			boolean isSubProcess=false;
			boolean isATransaction=false;
			String activitySetId="";
			
			boolean isTaskUser=false;
			String implementation="";
			boolean isTaskService=false;
			boolean isTaskScript=false;
			String script="";
			boolean isTaskSend=false;
			boolean isTaskManual=false;
			boolean isTaskBuisnessRule=false;
			String buisnessRuleTaskImplementation="";
			boolean isSubflow=false;
			boolean isTaskReceive=false;
			boolean instantiate=false;
			
			Node nActivities = nodeList.item(i);
			if(nActivities.getNodeType()== Node.ELEMENT_NODE)
			{
				Element activitiesElement = (Element) nActivities;
				id=activitiesElement.getAttribute("Id");
				name = activitiesElement.getAttribute("Name");
				name= processName+"-"+name;
				isATransaction=Boolean.parseBoolean(activitiesElement.getAttribute("IsATransaction"));
				NodeList activityList = activitiesElement.getChildNodes();

				//System.out.println("activity "+id);
				for(int j=0;j<activityList.getLength();j++)
				{
					Node nActivity = activityList.item(j);
					if(nActivity.getNodeType()== Node.ELEMENT_NODE)
					{
						Element activityElement = (Element) nActivity;
						String activityElementTagName =activityElement.getTagName();
						if(activityElementTagName=="Description")
						{
							//i havent' found an activity with description
						}
						if(activityElementTagName=="Event")
						{
							
							NodeList eventList = activityElement.getChildNodes();
							event= readEvent(eventList);
							isEvent=true;

						}
						if(activityElementTagName=="Loop")
						{
							loopType=activityElement.getAttribute("LoopType");
						}
						if(activityElementTagName=="BlockActivity")
						{
							activitySetId= activityElement.getAttribute("ActivitySetId");
						}
						if(activityElementTagName=="Implementation")
						{
							NodeList implementationList = activityElement.getChildNodes();
							task = readImplementation(implementationList);
							isTask=true;
						}
						if(activityElementTagName=="Route")
						{
							String markerVisibleStr= activityElement.getAttribute("MarkerVisible");
							boolean markerVisible = Boolean.parseBoolean(markerVisibleStr);
							String gatewayDirection= activityElement.getAttribute("GatewayDirection");
							String gatewayType= activityElement.getAttribute("GatewayType");
							String exclusiveType= activityElement.getAttribute("ExclusiveType");
							gateway = new Gateway(gatewayDirection);
							if(markerVisible==true)
							{
								gateway.setMarkerVisible(true);
							}
							if(gatewayType!="")
							{
								gateway.setGatewayType(gatewayType);
							}
							if(exclusiveType!="")
							{
								gateway.setExclusiveType(exclusiveType);
							}
							isGateway=true;
						}
						if(activityElementTagName=="Documentation")
						{
							//i havent found an activity with documentation
						}
						if(activityElementTagName=="NodeGraphicsInfos")
						{
							NodeList nodeGraphicsInfosList = activityElement.getChildNodes();
							nodeGraphicsInfo= readNodeGraphicsInfo(nodeGraphicsInfosList);
						}
						if(activityElementTagName=="ExtendedAttributes")
						{
							NodeList extendedAttributesList = activityElement.getChildNodes();
							extendedAttributes= readExtendedAttributes(extendedAttributesList);
						}
					}
				}
			}
			Activity activity = new Activity(id, name, description, documentation, extendedAttributes, nodeGraphicsInfo);
			
			if(activitySetId!="")
			{
				task = new Subprocess(isATransaction, activitySetId);
				activity.setTask(task);
			}
			if(isEvent)
			{
				activity.setEvent(event);
			}
			if(loopType!="")
			{
				activity.setLoopType(loopType);
			}
			if(isTask)
			{
				activity.setTask(task);
			}
			if(isGateway)
			{
				activity.setGateway(gateway);
			}
			activities.add(activity);
		}
		return activities;
	}
	
	public Event readEvent(NodeList nodeList)
	{
		String trigger="";
		String result="";
		String targetActivityId="";
		boolean isAttached=false;
		TriggerResultMessage triggerResultMessage= new TriggerResultMessage("", "");
		boolean isStart =false;
		boolean isIntermediate=false;
		boolean isEnd=false;
		boolean hasTriggerResultMessage=false;
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node nEvent = nodeList.item(i);
			if(nEvent.getNodeType()== Node.ELEMENT_NODE)
			{
				Element eventElement = (Element) nEvent;
				String eventElementTagName =eventElement.getTagName();
				if(eventElementTagName=="StartEvent")
				{
					trigger=eventElement.getAttribute("Trigger");	
					isStart=true;
				}
				if(eventElementTagName=="IntermediateEvent")
				{
					trigger=eventElement.getAttribute("Trigger");
					targetActivityId=eventElement.getAttribute("Target");
					String isAttachedStr=eventElement.getAttribute("IsAttached");
					isAttached= Boolean.parseBoolean(isAttachedStr);
					
					NodeList triggerList= eventElement.getChildNodes();
					triggerResultMessage=readTriggerResultMessage(triggerList);
					//if the trigger's content is empty, the event has no triggerResultMessage attached
					if(triggerResultMessage.getCatchThrow()!="" && triggerResultMessage.getId()!="")
					{
						hasTriggerResultMessage=true;
					}
					isIntermediate=true;
					
				}
				if(eventElementTagName=="EndEvent")
				{
					result=eventElement.getAttribute("Result");
					isEnd=true;
				}
			}
		}
		if(isStart==true)
		{
			StartEvent startEvent= new StartEvent(trigger);
			return startEvent;
		}
		else if(isIntermediate==true)
		{
			IntermediateEvent intermediateEvent = new IntermediateEvent(trigger);
			if(isAttached)
			{
				intermediateEvent.setAttached(true);
			}
			if(targetActivityId!="")
			{
				intermediateEvent.setTargetActivityId(targetActivityId);
			}
			if(hasTriggerResultMessage==true)
			{
				intermediateEvent.setTriggerResultMessage(triggerResultMessage);
			}
			return intermediateEvent;
		}
		//is end
		else
		{
			EndEvent endEvent = new EndEvent(result);
			return endEvent;
		}
	}
	
	public Task readImplementation(NodeList nodeList)
	{
		boolean isOnlyTask=true;
		String loopType="";
		boolean isSubProcess=false;
		boolean isATransaction=false;
		String activitySetId="";
		
		boolean isTaskUser=false;
		String implementation="";
		boolean isTaskService=false;
		boolean isTaskScript=false;
		String script="";
		boolean isTaskSend=false;
		boolean isTaskManual=false;
		boolean isTaskBuisnessRule=false;
		String buisnessRuleTaskImplementation="";
		boolean isTaskReceive=false;
		boolean instantiate=false;
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node nImplementations = nodeList.item(i);
			if(nImplementations.getNodeType()== Node.ELEMENT_NODE)
			{
				Element implementationsElement = (Element) nImplementations;
				//Task
				//TODO for some reason it gets into Task, yet its children such as TaskUser aren't
				if(implementationsElement.getTagName()=="Task")
				{
					NodeList implementationsList = implementationsElement.getChildNodes();
					for(int j=0;j<implementationsList.getLength();j++)
					{
						Node nImplementation = implementationsList.item(j);
						if(nImplementation.getNodeType()== Node.ELEMENT_NODE)
						{
							Element implementationElement = (Element) nImplementation;
							String implementationElementTagName =implementationElement.getTagName();						
							if(implementationElementTagName=="TaskUser")
							{
								implementation=implementationElement.getAttribute("Implementation");
								isOnlyTask=false;
								isTaskUser=true;
							}
							if(implementationElementTagName=="TaskService")
							{
								//I havent' found attached services
								isOnlyTask=false;
								isTaskService=true;
								
							}
							if(implementationElementTagName=="TaskScript")
							{
								//I havent' found attached scripts
								NodeList taskScriptList = implementationElement.getChildNodes();
								for(int k=0;k<taskScriptList.getLength();k++)
								{
									Node nTaskScript =taskScriptList.item(k);
									if(nTaskScript.getNodeType()==Node.ELEMENT_NODE)
									{
										Element taskScriptElement = (Element) nTaskScript;
										String taskScriptElementTagName =taskScriptElement.getTagName();
										if(taskScriptElementTagName=="Script")
										{
											script = nTaskScript.getTextContent();
											isOnlyTask=false;
											isTaskScript=true;
										}
									}
								}
							}
							if(implementationElementTagName=="TaskSend")
							{
								//I havent' found attached sends
								isOnlyTask=false;
								isTaskSend=true;
							}
							if(implementationElementTagName=="TaskManual")
							{
								//I havent' found attached manuals
								isOnlyTask=false;
								isTaskSend=true;
							}
							if(implementationElementTagName=="TaskBuisnessRule")
							{
								buisnessRuleTaskImplementation=implementationElement.getAttribute("BuisnessRuleTaskImplementation");
								isOnlyTask=false;
								isTaskBuisnessRule=true;
							}
							if(implementationElementTagName=="TaskReceive")
							{
								instantiate=Boolean.parseBoolean(implementationElement.getAttribute("Instantiate"));
								isOnlyTask=false;
								isTaskReceive=true;
							}
						}
					}
					//construct the task
					if(isTaskUser)
					{
						TaskUser taskUser = new TaskUser(implementation);
						return taskUser;
					}
					else if(isTaskService)
					{
						TaskService taskService = new TaskService();
						return taskService;
					}
					else if(isTaskScript)
					{
						TaskScript taskScript = new TaskScript(loopType, script);
						return taskScript;
					}
					else if(isTaskSend)
					{
						TaskSend taskSend = new TaskSend();
						return taskSend;
					}
					else if(isTaskManual)
					{
						TaskManual taskManual = new TaskManual();
						return taskManual;
					}
					else if(isTaskBuisnessRule)
					{
						TaskBuisnessRule taskBuisnessRule = new TaskBuisnessRule(loopType, buisnessRuleTaskImplementation);
						return taskBuisnessRule;
					}
					else if(isTaskReceive)
					{
						TaskReceive taskReceive = new TaskReceive(loopType, instantiate);
						return taskReceive;
					}	
				}
				if(implementationsElement.getTagName()=="SubFlow")
				{
					Subflow subflow = new Subflow();
					return subflow;
				}
			}
		}
		Task task = new Task();
		return task;
	}
	
	public TriggerResultMessage readTriggerResultMessage(NodeList nodeList)
	{
		String catchThrow="";
		String messageId="";
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node nTrigger = nodeList.item(i);
			if(nTrigger.getNodeType()== Node.ELEMENT_NODE)
			{
				Element triggerElement = (Element) nTrigger;
				String triggerElementTagName =triggerElement.getTagName();
				if(triggerElementTagName=="TriggerTimer")
				{
					//nothing found yet, thats why we only read TriggerREsultMessages
				}
				if(triggerElementTagName=="TriggerResultMessage")
				{
					catchThrow= triggerElement.getAttribute("CatchThrow");
					NodeList triggerResultsList=triggerElement.getChildNodes();
					for(int j=0;j<triggerResultsList.getLength();j++)
					{
						Node ntriggerResults= triggerResultsList.item(j);
						if(ntriggerResults.getNodeType()== Node.ELEMENT_NODE)
						{
							Element triggerResult= (Element) ntriggerResults;
							messageId=triggerResult.getAttribute("Id");
						}
					}
				}
			}
		}
		TriggerResultMessage triggerResultMessage = new TriggerResultMessage(catchThrow, messageId);
		return triggerResultMessage;
	}
	
	public ArrayList<Transition> readTransitions(NodeList nodeList)
	{
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		for(int i=0;i<nodeList.getLength();i++)
		{
			String id="";
			String fromActivityId = "";
			String toActivityId="";
			String conditionType="";
			String description="";
			ArrayList<ExtendedAttribute> extendedAttributes= new ArrayList<ExtendedAttribute>();
			
			Formatting formatting = new Formatting("", "", 0, false, false, false, false, 0);
			ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
			ConnectorGraphicsInfo connectorGraphicsInfo = new ConnectorGraphicsInfo("",0, "","",0,0, formatting, coordinates);
			Node nTransitions = nodeList.item(i);
			if(nTransitions.getNodeType()== Node.ELEMENT_NODE)
			{
				Element transitionsElement = (Element) nTransitions;
				id=transitionsElement.getAttribute("Id");
				fromActivityId = transitionsElement.getAttribute("From");
				toActivityId = transitionsElement.getAttribute("To");
				NodeList transitionList = transitionsElement.getChildNodes();
				//System.out.println("transition "+id);
				for(int j=0;j<transitionList.getLength();j++)
				{
					Node nTransition = transitionList.item(j);
					if(nTransition.getNodeType()== Node.ELEMENT_NODE)
					{
						Element transitionElement = (Element) nTransition;
						String transitionElementTagName =transitionElement.getTagName();

						if(transitionElementTagName=="Condition")
						{
							conditionType = transitionElement.getAttribute("Type");
						}
						if(transitionElementTagName=="Description")
						{
							//i havent' found a transition with description
						}
						if(transitionElementTagName=="ConnectorGraphicsInfos")
						{
							NodeList connectorGraphicsInfosList = transitionElement.getChildNodes();
							connectorGraphicsInfo= readConnectorGraphicsInfo(connectorGraphicsInfosList);
						}
						if(transitionElementTagName=="ExtendedAttributes")
						{
							NodeList extendedAttributesList = transitionElement.getChildNodes();
							extendedAttributes= readExtendedAttributes(extendedAttributesList);
						}
					}
				}
			}
			Transition transition = new Transition(id, conditionType, description, connectorGraphicsInfo, fromActivityId, toActivityId, extendedAttributes);
			transitions.add(transition);
		}
		return transitions;
	}
	
	public ConnectorGraphicsInfo readConnectorGraphicsInfo(NodeList nodeList)
	{
		String toolId="";
		int borderColor=0;
		String textX="";
		String textY="";
		int textWidth =0;
		int textHeight =0;
		
		int xCoordinate =0;
		int yCoordinate=0;
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
		Formatting formatting = new Formatting("", "", 0, false, false, false, false, 0);
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node nCGIs = nodeList.item(i);
			if(nCGIs.getNodeType()== Node.ELEMENT_NODE)
			{
				Element nCIsElement = (Element) nCGIs;
				toolId=nCIsElement.getAttribute("ToolId");
				borderColor = Integer.parseInt(nCIsElement.getAttribute("BorderColor"));
				textX=nCIsElement.getAttribute("TextX");
				textY=nCIsElement.getAttribute("TextY");
				textWidth = Integer.parseInt(nCIsElement.getAttribute("TextWidth"));
				textHeight = Integer.parseInt(nCIsElement.getAttribute("TextHeight"));
				NodeList nCIList = nCIsElement.getChildNodes();
				for(int j=0;j<nCIList.getLength();j++)
				{
					Node nNCI = nCIList.item(j);
					if(nNCI.getNodeType()== Node.ELEMENT_NODE)
					{
						Element nNCIElement = (Element) nNCI;
						String nCITagName = nNCIElement.getTagName();
						if(nCITagName=="Formatting")
						{
							NodeList formattingList = nNCIElement.getChildNodes();
							formatting = readFormatting(formattingList);	
						}
						if(nCITagName=="Coordinates")
						{
							
							xCoordinate= Math.round(Float.parseFloat(nNCIElement.getAttribute("XCoordinate")));
							yCoordinate= Math.round(Float.parseFloat(nNCIElement.getAttribute("YCoordinate")));
							Coordinate coordinate = new Coordinate(xCoordinate, yCoordinate);
							coordinates.add(coordinate);
							
						}
					}
				}
			}
		}
		ConnectorGraphicsInfo connectorGraphicsInfo = new ConnectorGraphicsInfo(toolId, borderColor, textX, textY, textWidth, textHeight, formatting, coordinates);
		return connectorGraphicsInfo;
	}
	
	
	public Date formatDate(String pDate)
	{
		Date rta = new Date();
		String edit = pDate.replace("T"," ");
		edit = edit.replace("."," ");
		String[] editList=edit.split(" ");
		edit = editList[0]+" "+editList[1];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			rta = sdf.parse(edit);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rta;
	}
}
