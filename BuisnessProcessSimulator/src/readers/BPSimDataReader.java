package readers;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.ControlParameter;
import model.CostParameter;
import model.ElementParameter;
import model.ExtendedAttribute;
import model.FixedCost;
import model.InterTriggerTimer;
import model.Probability;
import model.ProcessingTime;
import model.Quantity;
import model.ResourceParameter;
import model.Scenario;
import model.ScenarioParameter;
import model.Selection;
import model.TimeParameter;
import model.TriggerCount;
import model.UnitCost;
import model.WaitTime;

public class BPSimDataReader {

	public BPSimDataReader()
	{
		
	}
	
	public Scenario readBPSimData(String route)
	{
		String simulationLevel ="";
		String xmlnsNs1="";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		String id="";
		String name="";
		String description="";
		String author="";
		String version ="";
		ArrayList<ElementParameter> elementParameters = new ArrayList<ElementParameter>();
		
		ScenarioParameter scenarioParameter = new ScenarioParameter("", "","");
		try 
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc =builder.parse(route);
			
			NodeList packageDiagramList = doc.getElementsByTagName("ns1:BPSimData");
			Node pd = packageDiagramList.item(0);
			
			if(pd.getNodeType()== Node.ELEMENT_NODE)
			{
				Element packageDiagram = (Element) pd;
				simulationLevel= packageDiagram.getAttribute("simulationLevel");
				xmlnsNs1 = packageDiagram.getAttribute("xmlns:ns1");
				//System.out.println("BPSimData attributes read");
				
				NodeList bPSimDataList =packageDiagram.getChildNodes();
				
				for(int i=0; i< bPSimDataList.getLength();i++)
				{
					Node nBPSimData =bPSimDataList.item(i);
					if(nBPSimData.getNodeType()== Node.ELEMENT_NODE)
					{
						Element bPSimDataElement = (Element) nBPSimData;
						//ns1:Scenario
						String bPSimDataElementTagName= bPSimDataElement.getTagName();
						// read PackageHeader element-------------------------------------------------
						if(bPSimDataElementTagName=="ns1:Scenario")
						{							
							//scenario =readScenario(scenarioList);
							
							
							//reading scenario
							id=bPSimDataElement.getAttribute("id");
							name = bPSimDataElement.getAttribute("name");
							description = bPSimDataElement.getAttribute("description");
							author = bPSimDataElement.getAttribute("author");
							version = bPSimDataElement.getAttribute("version");
							NodeList scenarioList=bPSimDataElement.getChildNodes();
							
							for(int j=0;j<scenarioList.getLength();j++)
							{
								Node nScenario =scenarioList.item(j);
								if(nScenario.getNodeType()== Node.ELEMENT_NODE)
								{
									Element scenarioElement = (Element) nScenario;
									String scenarioElementTagName = scenarioElement.getTagName();
									if(scenarioElementTagName=="ns1:ScenarioParameters")
									{
										NodeList scenarioParameterList = scenarioElement.getChildNodes();
										scenarioParameter = readScenarioParameters(scenarioParameterList);
										//System.out.println("scenarioParameters read");
									}
									if(scenarioElementTagName=="ns1:ElementParameters")
									{
										String elementRef="";
										elementRef= scenarioElement.getAttribute("elementRef");
										NodeList elementParametersList = scenarioElement.getChildNodes();
										ElementParameter elementParameter=readElementParameters(elementParametersList);
										elementParameter.setElementRef(elementRef);
										elementParameters.add(elementParameter);	
										//System.out.println("elementParameters read");
									}

								}
							}
							//System.out.println("Scenario read");
						}
					}
				}
			}			
		} 
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scenario scenario = new Scenario(id, name, description, author, version, scenarioParameter, elementParameters);
		return scenario;
		
	}
	
	public ScenarioParameter readScenarioParameters(NodeList nodeList)
	{
		String baseTimeUnit="";
		String baseCurrencyUnit="";
		String durationParameterValue="";
		
		for(int i =0;i< nodeList.getLength(); i++)
		{
			Node nScenarioParameters = nodeList.item(i);
			if(nScenarioParameters.getNodeType()== Node.ELEMENT_NODE)
			{
				Element scenarioParametersElement= (Element)nScenarioParameters;
				baseTimeUnit =scenarioParametersElement.getAttribute("baseTimeUnit");
				baseCurrencyUnit= scenarioParametersElement.getAttribute("baseCurrencyUnit");
				
				NodeList scenarioParameterList = scenarioParametersElement.getChildNodes();
				for(int j=0;j<scenarioParameterList.getLength();j++)
				{
					Node nScenarioParameter= scenarioParameterList.item(j);
					if(nScenarioParameter.getNodeType()== Node.ELEMENT_NODE)
					{
						Element scenarioParameterElement = (Element)nScenarioParameter;
						String scenarioParameterElementTagName=scenarioParameterElement.getTagName();
						if(scenarioParameterElementTagName=="ns1:DurationParameter")
						{
							durationParameterValue= scenarioParameterElement.getAttribute("value");
						}
						//if ==property parameters, haven't found content here yet
					}
				}
			}
		}
		ScenarioParameter scenarioParameter = new ScenarioParameter(baseTimeUnit, baseCurrencyUnit, durationParameterValue);
		return scenarioParameter;
	}
	
	public ElementParameter readElementParameters(NodeList nodeList)
	{
		boolean hasControlParameters=false;
		boolean hasTimeParameters=false;
		boolean hasResourceParameters=false;
		boolean hasCostParameters=false;
		
		ControlParameter controlParameter = new ControlParameter();
		
		ArrayList<String> resultRequests = new ArrayList<String>();
		ProcessingTime processingTime = new ProcessingTime(resultRequests);
		TimeParameter timeParameter= new TimeParameter(processingTime);
		
		ResourceParameter resourceParameter = new ResourceParameter();
		
		FixedCost fixedCost = new FixedCost(0);
		UnitCost unitCost = new UnitCost(0);
		CostParameter costParameter = new CostParameter(fixedCost, unitCost);
		
		
		for(int i =0;i< nodeList.getLength(); i++)
		{
			Node nElementParameters = nodeList.item(i);
			if(nElementParameters.getNodeType()== Node.ELEMENT_NODE)
			{
				Element elementParametersElement= (Element)nElementParameters;
				String elementParametersElementTagName=elementParametersElement.getTagName();
				//controlParameters 
				if(elementParametersElementTagName=="ns1:ControlParameters")
				{
					hasControlParameters=true;
					NodeList controlParametersList = elementParametersElement.getChildNodes();
					controlParameter=readControlParameters(controlParametersList);
				}	
				if(elementParametersElementTagName=="ns1:TimeParameters")
				{
					hasTimeParameters=true;
					NodeList timeParametersList = elementParametersElement.getChildNodes();
					timeParameter=readTimeParameters(timeParametersList);
				}
				if(elementParametersElementTagName=="ns1:ResourceParameters")
				{
					hasResourceParameters=true;
					NodeList resourceParametersList = elementParametersElement.getChildNodes();
					resourceParameter=readResourceParameters(resourceParametersList);
				}
				if(elementParametersElementTagName=="ns1:CostParameters")
				{
					hasCostParameters=true;
					NodeList costParametersList = elementParametersElement.getChildNodes();
					costParameter=readCostParameters(costParametersList);
				}
			}
		}
		ElementParameter elementParameter = new ElementParameter();
		if(hasControlParameters)
		{
			elementParameter.setControlParameter(controlParameter);
		}
		if(hasCostParameters)
		{
			elementParameter.setCostParameter(costParameter);
		}
		if(hasResourceParameters)
		{
			elementParameter.setResourceParameter(resourceParameter);
		}
		if(hasTimeParameters)
		{
			elementParameter.setTimeParameter(timeParameter);
		}
		return elementParameter;		
	}
	
	public ControlParameter readControlParameters(NodeList nodeList)
	{
		boolean hasInterTriggerTime = false;
		ArrayList<String> resultRequests = new ArrayList<String>();
		int poissonDistributionMean=0;
		int numericParameterValue=0;
		
		boolean hasTriggerCount = false;
		String triggerCountresultRequest="";
		int triggerCountNumericParameterValue=0;
		
		boolean hasProbability=false;
		float floatingParameterValue=0;
		
		for(int i =0;i< nodeList.getLength(); i++)
		{
			Node nControlParameters = nodeList.item(i);
			if(nControlParameters.getNodeType()== Node.ELEMENT_NODE)
			{
				Element controlParametersElement= (Element)nControlParameters;
				String controlParametersElementTagName=controlParametersElement.getTagName();
				//controlParameters 
				if(controlParametersElementTagName=="ns1:InterTriggerTimer")
				{
					hasInterTriggerTime=true;
					NodeList interTriggerTimerList = controlParametersElement.getChildNodes();
					for(int j =0;j< interTriggerTimerList.getLength(); j++)
					{
						Node nInterTriggerList = interTriggerTimerList.item(j);
						if(nInterTriggerList.getNodeType()== Node.ELEMENT_NODE)
						{
							Element interTriggerListElement= (Element)nInterTriggerList;
							String interTriggerListElementTagName=interTriggerListElement.getTagName();
							//controlParameters 
							if(interTriggerListElementTagName=="ns1:ResultRequest")
							{
								String resultRequest = interTriggerListElement.getTextContent();
								resultRequests.add(resultRequest);
							}
							if(interTriggerListElementTagName=="ns1:PoissonDistribution")
							{
								poissonDistributionMean = Integer.parseInt(interTriggerListElement.getAttribute("mean"));
							}
							if(interTriggerListElementTagName=="ns1:NumericParameter")
							{
								numericParameterValue = Integer.parseInt(interTriggerListElement.getAttribute("value"));
							}
						}						
					}
					
				}
				if(controlParametersElementTagName=="ns1:TriggerCount")
				{
					hasTriggerCount=true;
					NodeList triggerCountList = controlParametersElement.getChildNodes();
					for(int j =0;j< triggerCountList.getLength(); j++)
					{
						Node nTriggerCountList = triggerCountList.item(j);
						
						if(nTriggerCountList.getNodeType()== Node.ELEMENT_NODE)
						{
							Element triggerCountListElement= (Element)nTriggerCountList;
							String triggerCountListElementTagName=triggerCountListElement.getTagName();
							if(triggerCountListElementTagName=="ns1:ResultRequest")
							{
								triggerCountresultRequest=triggerCountListElement.getTextContent();
							}
							if(triggerCountListElementTagName=="ns1:NumericParameter")
							{
								triggerCountNumericParameterValue= Integer.parseInt(triggerCountListElement.getAttribute("value"));
							}
						}
					}
				}
				if(controlParametersElementTagName=="ns1:Probability")
				{
					hasProbability=true;
					NodeList probabilityList = controlParametersElement.getChildNodes();
					for(int j =0;j< probabilityList.getLength(); j++)
					{
						Node nProbabilityList = probabilityList.item(j);
						
						if(nProbabilityList.getNodeType()== Node.ELEMENT_NODE)
						{
							Element probabilityListElement= (Element)nProbabilityList;
							String probabilityListElementTagName=probabilityListElement.getTagName();
							if(probabilityListElementTagName=="ns1:FloatingParameter")
							{
								floatingParameterValue= Float.parseFloat(probabilityListElement.getAttribute("value"));
							}
						}
					}
				}
			}	
		}
		ControlParameter controlParameter = new ControlParameter();
		if(hasInterTriggerTime)
		{
			InterTriggerTimer interTriggerTimer = new InterTriggerTimer(resultRequests);
			if(poissonDistributionMean!=0)
			{
				interTriggerTimer.setPoissonDistributionMean(poissonDistributionMean);
			}
			else if(numericParameterValue!=0)
			{
				interTriggerTimer.setNumericParameterValue(numericParameterValue);
			}
			controlParameter.setInterTriggerTimer(interTriggerTimer);
		}
		if(hasTriggerCount)
		{
			TriggerCount triggerCount = new TriggerCount(triggerCountresultRequest, triggerCountNumericParameterValue);
			controlParameter.setTriggerCount(triggerCount);
		}
		if(hasProbability)
		{
			Probability probability = new Probability(floatingParameterValue);
			controlParameter.setProbability(probability);
		}
		return controlParameter;
	}
	
	public TimeParameter readTimeParameters(NodeList nodeList)
	{
		//processingTime parameters
		ArrayList<String> resultRequests = new ArrayList<String>();
		boolean hasFloatingParameterValue=false;
		double floatingParameterValue=0;
		
		boolean hasNumericParameterValue=false;
		int numericParameterValue=0;
		
		boolean hasNormalDistribution=false;
		double normalDistributionMean=0;
		double normalDistributionStandardDeviation=0;
		
		boolean hasTruncatedNormalDistribution=false;
		int truncatedNormalDistributionMean=0;
		int truncatedNormalDistributionStandardDeviation=0;
		int truncatedNormalDistributionMin=0;
		int truncatedNormalDistributionMax=0;
		
		boolean hasTriangularDistribution=false;
		double triangularDistributionMode=0;
		double triangularDistributionMin=0;
		double triangularDistributionMax=0;
		
		boolean hasUniformDistribution=false;
		int uniformDistributionMin=0;
		int uniformDistributionMax=0;
		
		boolean hasLognormalDistribution=false;
		double lognormalDistributionMean=0;
		double lognormalDistributionStandardDeviation=0;
		
		boolean hasBetaDistribution=false;
		double betaDistributionShape=0;
		double betaDistributionScale=0;
		
		boolean hasNegativeExponentialDistribution=false;
		int negativeExponentialDistributionMean=0;
		
		boolean hasGammaDistribution=false;
		double gammaDistributionShape=0;
		double gammaDistributionScale=0;
		
		boolean hasErlangDistribution=false;
		int erlangDistributionMean=0;
		int erlangDistributionK=0;
		
		boolean hasWeibullDistribution=false;
		double weibullDistributionShape=0;
		double weibullDistributionScale=0;
		
		boolean hasBinomialDistribution=false;
		double binomialDistributionProbability=0;
		int binomialDistributionTrials=0;
		
		boolean hasPoissonDistribution=false;
		double poissonDistributionMean=0;
		
		boolean hasDurationParameter=false;
		String durationParameter = "";
		
		//waitTime parameters
		ArrayList<String> waitTimeResultRequests = new ArrayList<String>();
		boolean waitTimeHasFloatingParameterValue=false;
		double waitTimeFloatingParameterValue=0;
		
		boolean waitTimeHasNumericParameterValue=false;
		int waitTimeNumericParameterValue=0;
		
		boolean waitTimeHasNormalDistribution=false;
		double waitTimeNormalDistributionMean=0;
		double waitTimeNormalDistributionStandardDeviation=0;
		
		boolean waitTimeHasTruncatedNormalDistribution=false;
		int waitTimeTruncatedNormalDistributionMean=0;
		int waitTimeTruncatedNormalDistributionStandardDeviation=0;
		int waitTimeTruncatedNormalDistributionMin=0;
		int waitTimeTruncatedNormalDistributionMax=0;
		
		boolean waitTimeHasTriangularDistribution=false;
		double waitTimeTriangularDistributionMode=0;
		double waitTimeTriangularDistributionMin=0;
		double waitTimeTriangularDistributionMax=0;
		
		boolean waitTimeHasUniformDistribution=false;
		int waitTimeUniformDistributionMin=0;
		int waitTimeUniformDistributionMax=0;
		
		boolean waitTimeHasLognormalDistribution=false;
		double waitTimeLognormalDistributionMean=0;
		double waitTimeLognormalDistributionStandardDeviation=0;
		
		boolean waitTimeHasBetaDistribution=false;
		double waitTimeBetaDistributionShape=0;
		double waitTimeBetaDistributionScale=0;
		
		boolean waitTimeHasNegativeExponentialDistribution=false;
		int waitTimeNegativeExponentialDistributionMean=0;
		
		boolean waitTimeHasGammaDistribution=false;
		double waitTimeGammaDistributionShape=0;
		double waitTimeGammaDistributionScale=0;
		
		boolean waitTimeHasErlangDistribution=false;
		int waitTimeErlangDistributionMean=0;
		int waitTimeErlangDistributionK=0;
		
		boolean waitTimeHasWeibullDistribution=false;
		double waitTimeWeibullDistributionShape=0;
		double waitTimeWeibullDistributionScale=0;
		
		boolean waitTimeHasBinomialDistribution=false;
		double waitTimeBinomialDistributionProbability=0;
		int waitTimeBinomialDistributionTrials=0;
		
		boolean waitTimeHasPoissonDistribution=false;
		double waitTimePoissonDistributionMean=0;
		
		boolean waitTimeHasDurationParameter=false;
		String waitTimeDurationParameter = "";

		
		
		
		for(int i =0;i< nodeList.getLength(); i++)
		{
			Node nTimeParameters = nodeList.item(i);
			if(nTimeParameters.getNodeType()== Node.ELEMENT_NODE)
			{
				Element timeParametersElement= (Element)nTimeParameters;
				String timeParametersElementTagName=timeParametersElement.getTagName();
				//timeParameters
				//ProcessingTime
				if(timeParametersElementTagName=="ns1:ProcessingTime")
				{
					NodeList processingTimeList = timeParametersElement.getChildNodes();
					for(int j =0;j< processingTimeList.getLength(); j++)
					{
						Node nProcessingTimeList = processingTimeList.item(j);
						if(nProcessingTimeList.getNodeType()== Node.ELEMENT_NODE)
						{
							Element processingTimeListElement= (Element)nProcessingTimeList;
							String processingTimeListElementTagName=processingTimeListElement.getTagName();
							//controlParameters 
							if(processingTimeListElementTagName=="ns1:ResultRequest")
							{
								String resultRequest = processingTimeListElement.getTextContent();
								resultRequests.add(resultRequest);
							}
							if(processingTimeListElementTagName=="ns1:NumericParameter")
							{
								hasNumericParameterValue=true;
								numericParameterValue = Integer.parseInt(processingTimeListElement.getAttribute("value"));
							}
							if(processingTimeListElementTagName=="ns1:NormalDistribution")
							{
								hasNormalDistribution=true;
								normalDistributionMean = Double.parseDouble(processingTimeListElement.getAttribute("mean"));
								normalDistributionStandardDeviation= Double.parseDouble(processingTimeListElement.getAttribute("standardDeviation"));
							}
							if(processingTimeListElementTagName=="ns1:TruncatedNormalDistribution")
							{
								hasTruncatedNormalDistribution=true;
								truncatedNormalDistributionMean= Integer.parseInt(processingTimeListElement.getAttribute("mean"));
								truncatedNormalDistributionStandardDeviation= Integer.parseInt(processingTimeListElement.getAttribute("standardDeviation"));
								truncatedNormalDistributionMin =Integer.parseInt(processingTimeListElement.getAttribute("min"));
								truncatedNormalDistributionMax= Integer.parseInt(processingTimeListElement.getAttribute("max"));
							}
							if(processingTimeListElementTagName=="ns1:TriangularDistribution")
							{
								hasTriangularDistribution=true;
								triangularDistributionMode=Double.parseDouble(processingTimeListElement.getAttribute("mode"));
								triangularDistributionMin= Double.parseDouble(processingTimeListElement.getAttribute("min"));
								triangularDistributionMax=Double.parseDouble(processingTimeListElement.getAttribute("max"));
							}
							if(processingTimeListElementTagName=="ns1:UniformDistribution")
							{
								hasUniformDistribution=true;
								uniformDistributionMin= Integer.parseInt(processingTimeListElement.getAttribute("min"));
								uniformDistributionMax=Integer.parseInt(processingTimeListElement.getAttribute("max"));
							}
							if(processingTimeListElementTagName=="ns1:LogNormalDistribution")
							{
								hasLognormalDistribution=true;
								lognormalDistributionMean=Double.parseDouble(processingTimeListElement.getAttribute("mean"));;
								lognormalDistributionStandardDeviation=Double.parseDouble(processingTimeListElement.getAttribute("standardDeviation"));;
							}
							if(processingTimeListElementTagName=="ns1:DurationParameter")
							{
								hasDurationParameter=true;
								durationParameter= processingTimeListElement.getAttribute("value");
							}
							if(processingTimeListElementTagName=="ns1:FloatingParameter")
							{
								hasFloatingParameterValue=true;
								floatingParameterValue=Double.parseDouble(processingTimeListElement.getAttribute("value"));
							}
							if(processingTimeListElementTagName=="ns1:BetaDistribution")
							{
								hasBetaDistribution=true;
								betaDistributionScale=Double.parseDouble(processingTimeListElement.getAttribute("scale"));;
								betaDistributionShape=Double.parseDouble(processingTimeListElement.getAttribute("shape"));;
							}
							if(processingTimeListElementTagName=="ns1:NegativeExponentialDistribution")
							{
								hasNegativeExponentialDistribution=true;
								negativeExponentialDistributionMean=Integer.parseInt(processingTimeListElement.getAttribute("mean"));;
							}
							if(processingTimeListElementTagName=="ns1:GammaDistribution")
							{
								hasGammaDistribution=true;
								gammaDistributionScale=Double.parseDouble(processingTimeListElement.getAttribute("scale"));;
								gammaDistributionShape=Double.parseDouble(processingTimeListElement.getAttribute("shape"));;
							}
							if(processingTimeListElementTagName=="ns1:ErlangDistribution")
							{
								hasErlangDistribution=true;
								erlangDistributionMean=Integer.parseInt(processingTimeListElement.getAttribute("mean"));;
								erlangDistributionK=Integer.parseInt(processingTimeListElement.getAttribute("k"));;
							}
							if(processingTimeListElementTagName=="ns1:WeibullDistribution")
							{
								hasWeibullDistribution=true;
								weibullDistributionScale=Integer.parseInt(processingTimeListElement.getAttribute("scale"));;
								weibullDistributionShape=Integer.parseInt(processingTimeListElement.getAttribute("shape"));;
							}
							if(processingTimeListElementTagName=="ns1:BinomialDistribution")
							{
								hasBinomialDistribution=true;
								binomialDistributionProbability=Double.parseDouble(processingTimeListElement.getAttribute("probability"));;
								binomialDistributionTrials=Integer.parseInt(processingTimeListElement.getAttribute("trials"));;
							}
							if(processingTimeListElementTagName=="ns1:PoissonDistribution")
							{
								hasPoissonDistribution=true;
								poissonDistributionMean=Double.parseDouble(processingTimeListElement.getAttribute("mean"));;
							}
						}
					}
				}
				//WaitTime
				if(timeParametersElementTagName=="ns1:WaitTime")
				{
					NodeList waitTimeList = timeParametersElement.getChildNodes();
					for(int j =0;j< waitTimeList.getLength(); j++)
					{
						Node nWaitTimeList = waitTimeList.item(j);
						if(nWaitTimeList.getNodeType()== Node.ELEMENT_NODE)
						{
							Element waitTimeListElement= (Element)nWaitTimeList;
							String waitTimeListElementTagName=waitTimeListElement.getTagName();
							//controlParameters 
							if(waitTimeListElementTagName=="ns1:ResultRequest")
							{
								String resultRequest = waitTimeListElement.getTextContent();
								waitTimeResultRequests.add(resultRequest);
							}
							if(waitTimeListElementTagName=="ns1:NumericParameter")
							{
								waitTimeHasNumericParameterValue=true;
								waitTimeNumericParameterValue = Integer.parseInt(waitTimeListElement.getAttribute("value"));
							}
							if(waitTimeListElementTagName=="ns1:NormalDistribution")
							{
								waitTimeHasNormalDistribution=true;
								waitTimeNormalDistributionMean = Double.parseDouble(waitTimeListElement.getAttribute("mean"));
								waitTimeNormalDistributionStandardDeviation= Double.parseDouble(waitTimeListElement.getAttribute("standardDeviation"));
							}
							if(waitTimeListElementTagName=="ns1:TruncatedNormalDistribution")
							{
								waitTimeHasTruncatedNormalDistribution=true;
								waitTimeTruncatedNormalDistributionMean= Integer.parseInt(waitTimeListElement.getAttribute("mean"));
								waitTimeTruncatedNormalDistributionStandardDeviation= Integer.parseInt(waitTimeListElement.getAttribute("standardDeviation"));
								waitTimeTruncatedNormalDistributionMin =Integer.parseInt(waitTimeListElement.getAttribute("min"));
								waitTimeTruncatedNormalDistributionMax= Integer.parseInt(waitTimeListElement.getAttribute("max"));
							}
							if(waitTimeListElementTagName=="ns1:TriangularDistribution")
							{
								waitTimeHasTriangularDistribution=true;
								waitTimeTriangularDistributionMode=Double.parseDouble(waitTimeListElement.getAttribute("mode"));
								waitTimeTriangularDistributionMin= Double.parseDouble(waitTimeListElement.getAttribute("min"));
								waitTimeTriangularDistributionMax=Double.parseDouble(waitTimeListElement.getAttribute("max"));
							}
							if(waitTimeListElementTagName=="ns1:UniformDistribution")
							{
								waitTimeHasUniformDistribution=true;
								waitTimeUniformDistributionMin= Integer.parseInt(waitTimeListElement.getAttribute("min"));
								waitTimeUniformDistributionMax=Integer.parseInt(waitTimeListElement.getAttribute("max"));
							}
							if(waitTimeListElementTagName=="ns1:LogNormalDistribution")
							{
								waitTimeHasLognormalDistribution=true;
								waitTimeLognormalDistributionMean=Double.parseDouble(waitTimeListElement.getAttribute("mean"));;
								waitTimeLognormalDistributionStandardDeviation=Double.parseDouble(waitTimeListElement.getAttribute("standardDeviation"));;
							}
							if(waitTimeListElementTagName=="ns1:DurationParameter")
							{
								waitTimeHasDurationParameter=true;
								waitTimeDurationParameter= waitTimeListElement.getAttribute("value");
							}
							if(waitTimeListElementTagName=="ns1:FloatingParameter")
							{
								waitTimeHasFloatingParameterValue=true;
								waitTimeFloatingParameterValue=Double.parseDouble(waitTimeListElement.getAttribute("value"));
							}
							if(waitTimeListElementTagName=="ns1:BetaDistribution")
							{
								waitTimeHasBetaDistribution=true;
								waitTimeBetaDistributionScale=Double.parseDouble(waitTimeListElement.getAttribute("scale"));;
								waitTimeBetaDistributionShape=Double.parseDouble(waitTimeListElement.getAttribute("shape"));;
							}
							if(waitTimeListElementTagName=="ns1:NegativeExponentialDistribution")
							{
								waitTimeHasNegativeExponentialDistribution=true;
								waitTimeNegativeExponentialDistributionMean=Integer.parseInt(waitTimeListElement.getAttribute("mean"));;
							}
							if(waitTimeListElementTagName=="ns1:GammaDistribution")
							{
								waitTimeHasGammaDistribution=true;
								waitTimeGammaDistributionScale=Double.parseDouble(waitTimeListElement.getAttribute("scale"));;
								waitTimeGammaDistributionShape=Double.parseDouble(waitTimeListElement.getAttribute("shape"));;
							}
							if(waitTimeListElementTagName=="ns1:ErlangDistribution")
							{
								waitTimeHasErlangDistribution=true;
								waitTimeErlangDistributionMean=Integer.parseInt(waitTimeListElement.getAttribute("mean"));;
								waitTimeErlangDistributionK=Integer.parseInt(waitTimeListElement.getAttribute("k"));;
							}
							if(waitTimeListElementTagName=="ns1:WeibullDistribution")
							{
								waitTimeHasWeibullDistribution=true;
								waitTimeWeibullDistributionScale=Integer.parseInt(waitTimeListElement.getAttribute("scale"));;
								waitTimeWeibullDistributionShape=Integer.parseInt(waitTimeListElement.getAttribute("shape"));;
							}
							if(waitTimeListElementTagName=="ns1:BinomialDistribution")
							{
								waitTimeHasBinomialDistribution=true;
								waitTimeBinomialDistributionProbability=Double.parseDouble(waitTimeListElement.getAttribute("probability"));;
								waitTimeBinomialDistributionTrials=Integer.parseInt(waitTimeListElement.getAttribute("trials"));;
							}
							if(waitTimeListElementTagName=="ns1:PoissonDistribution")
							{
								waitTimeHasPoissonDistribution=true;
								waitTimePoissonDistributionMean=Double.parseDouble(waitTimeListElement.getAttribute("mean"));;
							}
						}
					}
				}
			}
		}
		//setting wait time values
		WaitTime waitTime = new WaitTime(resultRequests);
		if(waitTimeHasDurationParameter)
		{
			waitTime.setDurationParameter(waitTimeDurationParameter);
		}
		if(waitTimeHasNormalDistribution)
		{
			waitTime.setNormalDistributionMean(waitTimeNormalDistributionMean);
			waitTime.setNormalDistributionStandardDeviation(waitTimeNormalDistributionStandardDeviation);
		}
		if(waitTimeHasFloatingParameterValue)
		{
			waitTime.setFloatingParameterValue(waitTimeFloatingParameterValue);
		}
		if(waitTimeHasNumericParameterValue)
		{
			waitTime.setNumericParameterValue(waitTimeNumericParameterValue);
		}
		if(waitTimeHasTriangularDistribution)
		{
			waitTime.setTriangularDistributionMax(waitTimeTriangularDistributionMax);
			waitTime.setTriangularDistributionMin(waitTimeTriangularDistributionMin);
			waitTime.setTriangularDistributionMode(waitTimeTriangularDistributionMode);
		}
		if(waitTimeHasTruncatedNormalDistribution)
		{
			waitTime.setTruncatedNormalDistributionMax(waitTimeTruncatedNormalDistributionMax);
			waitTime.setTruncatedNormalDistributionMean(waitTimeTruncatedNormalDistributionMean);
			waitTime.setTruncatedNormalDistributionMin(waitTimeTruncatedNormalDistributionMin);
			waitTime.setTruncatedNormalDistributionStandardDeviation(waitTimeTruncatedNormalDistributionStandardDeviation);
		}
		if(waitTimeHasUniformDistribution)
		{
			waitTime.setUniformDistributionMax(waitTimeUniformDistributionMax);
			waitTime.setUniformDistributionMin(waitTimeUniformDistributionMin);
		}
		if(waitTimeHasLognormalDistribution)
		{
			waitTime.setLognormalDistributionMean(waitTimeLognormalDistributionMean);
			waitTime.setLognormalDistributionStandardDeviation(waitTimeLognormalDistributionStandardDeviation);
		}
		if(waitTimeHasBetaDistribution)
		{
			waitTime.setBetaDistributionScale(waitTimeBetaDistributionScale);
			waitTime.setBetaDistributionShape(waitTimeBetaDistributionShape);
		}
		if(waitTimeHasNegativeExponentialDistribution)
		{
			waitTime.setNegativeExponentialDistributionMean(waitTimeNegativeExponentialDistributionMean);
		}
		if(waitTimeHasGammaDistribution)
		{
			waitTime.setGammaDistributionScale(waitTimeGammaDistributionScale);
			waitTime.setGammaDistributionShape(waitTimeGammaDistributionShape);
		}
		if(waitTimeHasErlangDistribution)
		{
			waitTime.setErlangDistributionK(waitTimeErlangDistributionK);
			waitTime.setErlangDistributionMean(waitTimeErlangDistributionMean);
		}
		if(waitTimeHasWeibullDistribution)
		{
			waitTime.setWeibullDistributionScale(waitTimeWeibullDistributionScale);
			waitTime.setWeibullDistributionShape(waitTimeWeibullDistributionShape);
		}
		if(waitTimeHasBinomialDistribution)
		{
			waitTime.setBinomialDistributionProbability(waitTimeBinomialDistributionProbability);
			waitTime.setBinomialDistributionTrials(waitTimeBinomialDistributionTrials);
		}
		if(waitTimeHasPoissonDistribution)
		{
			waitTime.setPoissonDistributionMean(waitTimePoissonDistributionMean);
		}
		
		//setting processing time values
		ProcessingTime processingTime = new ProcessingTime(resultRequests);
		if(hasDurationParameter)
		{
			processingTime.setDurationParameter(durationParameter);
		}
		if(hasNormalDistribution)
		{
			processingTime.setNormalDistributionMean(normalDistributionMean);
			processingTime.setNormalDistributionStandardDeviation(normalDistributionStandardDeviation);
		}
		if(hasFloatingParameterValue)
		{
			processingTime.setFloatingParameterValue(floatingParameterValue);
		}
		if(hasNumericParameterValue)
		{
			processingTime.setNumericParameterValue(numericParameterValue);
		}
		if(hasTriangularDistribution)
		{
			processingTime.setTriangularDistributionMax(triangularDistributionMax);
			processingTime.setTriangularDistributionMin(triangularDistributionMin);
			processingTime.setTriangularDistributionMode(triangularDistributionMode);
		}
		if(hasTruncatedNormalDistribution)
		{
			processingTime.setTruncatedNormalDistributionMax(truncatedNormalDistributionMax);
			processingTime.setTruncatedNormalDistributionMean(truncatedNormalDistributionMean);
			processingTime.setTruncatedNormalDistributionMin(truncatedNormalDistributionMin);
			processingTime.setTruncatedNormalDistributionStandardDeviation(truncatedNormalDistributionStandardDeviation);
		}
		if(hasUniformDistribution)
		{
			processingTime.setUniformDistributionMax(uniformDistributionMax);
			processingTime.setUniformDistributionMin(uniformDistributionMin);
		}
		if(hasLognormalDistribution)
		{
			processingTime.setLognormalDistributionMean(lognormalDistributionMean);
			processingTime.setLognormalDistributionStandardDeviation(lognormalDistributionStandardDeviation);
		}
		if(hasBetaDistribution)
		{
			processingTime.setBetaDistributionScale(betaDistributionScale);
			processingTime.setBetaDistributionShape(betaDistributionShape);
		}
		if(hasNegativeExponentialDistribution)
		{
			processingTime.setNegativeExponentialDistributionMean(negativeExponentialDistributionMean);
		}
		if(hasGammaDistribution)
		{
			processingTime.setGammaDistributionScale(gammaDistributionScale);
			processingTime.setGammaDistributionShape(gammaDistributionShape);
		}
		if(hasErlangDistribution)
		{
			processingTime.setErlangDistributionK(erlangDistributionK);
			processingTime.setErlangDistributionMean(erlangDistributionMean);
		}
		if(hasWeibullDistribution)
		{
			processingTime.setWeibullDistributionScale(weibullDistributionScale);
			processingTime.setWeibullDistributionShape(weibullDistributionShape);
		}
		if(hasBinomialDistribution)
		{
			processingTime.setBinomialDistributionProbability(binomialDistributionProbability);
			processingTime.setBinomialDistributionTrials(binomialDistributionTrials);
		}
		if(hasPoissonDistribution)
		{
			processingTime.setPoissonDistributionMean(poissonDistributionMean);
		}
		
		TimeParameter timeParameter = new TimeParameter(processingTime);
		timeParameter.setWaitTime(waitTime);
		return timeParameter;
	}
	
	public ResourceParameter readResourceParameters(NodeList nodeList)
	{
		ArrayList<String> resultRequests = new ArrayList<String>();
		boolean hasSelection=false;
		String expresionParameterValue="";
		
		boolean hasQuantity=false;
		int numericParameterValue=0;
		
		for(int i =0;i< nodeList.getLength(); i++)
		{
			Node nResourceParameters = nodeList.item(i);
			if(nResourceParameters.getNodeType()== Node.ELEMENT_NODE)
			{
				Element resourceParametersElement= (Element)nResourceParameters;
				String resourceParametersElementTagName=resourceParametersElement.getTagName();
				//controlParameters 
				if(resourceParametersElementTagName=="ns1:Selection")
				{
					hasSelection=true;
					NodeList selectionList = resourceParametersElement.getChildNodes();
					for(int j =0;j< selectionList.getLength(); j++)
					{
						Node nSelectionList = selectionList.item(j);
						if(nSelectionList.getNodeType()== Node.ELEMENT_NODE)
						{
							Element selectionListElement= (Element)nSelectionList;
							String selectionListElementTagName=selectionListElement.getTagName();
							//controlParameters 
							if(selectionListElementTagName=="ns1:ResultRequest")
							{
								String resultRequest = selectionListElement.getTextContent();
								resultRequests.add(resultRequest);
							}
							if(selectionListElementTagName=="ns1:ExpressionParameter")
							{
								expresionParameterValue =selectionListElement.getAttribute("value");
							}
						}
					}
				}
				if(resourceParametersElementTagName=="ns1:Quantity")
				{
					hasQuantity=true;
					NodeList quantityList = resourceParametersElement.getChildNodes();
					for(int j =0;j< quantityList.getLength(); j++)
					{
						Node nQuantityList = quantityList.item(j);
						if(nQuantityList.getNodeType()== Node.ELEMENT_NODE)
						{
							Element quantityListElement= (Element)nQuantityList;
							String quantityListElementTagName=quantityListElement.getTagName();
							//controlParameters 
							if(quantityListElementTagName=="ns1:NumericParameter")
							{
								numericParameterValue= Integer.parseInt(quantityListElement.getAttribute("value"));
							}
						}
					}
				}
			}
		}
		ResourceParameter resourceParameter = new ResourceParameter();
		if(hasSelection)
		{
			Selection selection = new Selection(resultRequests, expresionParameterValue);
			resourceParameter.setSelection(selection);
		}
		if(hasQuantity)
		{
			Quantity quantity = new Quantity(numericParameterValue);
			resourceParameter.setQuantity(quantity);
		}
		return resourceParameter;
	}
	
	public CostParameter readCostParameters(NodeList nodeList)
	{
		float fixedCostfloatingParameterValue =0;
		float unitCostfloatingParameterValue =0;
		
		for(int i =0;i< nodeList.getLength(); i++)
		{
			Node nCostParameters = nodeList.item(i);
			if(nCostParameters.getNodeType()== Node.ELEMENT_NODE)
			{
				Element costParametersElement= (Element)nCostParameters;
				String costParametersElementTagName=costParametersElement.getTagName();
				//controlParameters 
				if(costParametersElementTagName=="ns1:FixedCost")
				{
					//hasSelection=true;
					NodeList fixedCostList = costParametersElement.getChildNodes();
					for(int j =0;j< fixedCostList.getLength(); j++)
					{
						Node nFixedCostList = fixedCostList.item(j);
						if(nFixedCostList.getNodeType()== Node.ELEMENT_NODE)
						{
							Element fixedCostListElement= (Element)nFixedCostList;
							String fixedCostListElementTagName=fixedCostListElement.getTagName();
							//controlParameters 
							if(fixedCostListElementTagName=="ns1:FloatingParameter")
							{
								fixedCostfloatingParameterValue= Float.parseFloat(fixedCostListElement.getAttribute("value"));
							}
						}
					}
				}
				if(costParametersElementTagName=="ns1:UnitCost")
				{
					//hasSelection=true;
					NodeList unitCostList = costParametersElement.getChildNodes();
					for(int j =0;j< unitCostList.getLength(); j++)
					{
						Node nUnitCostList = unitCostList.item(j);
						if(nUnitCostList.getNodeType()== Node.ELEMENT_NODE)
						{
							Element unitCostListElement= (Element)nUnitCostList;
							String unitCostListElementTagName=unitCostListElement.getTagName();
							//controlParameters 
							if(unitCostListElementTagName=="ns1:FloatingParameter")
							{
								unitCostfloatingParameterValue= Float.parseFloat(unitCostListElement.getAttribute("value"));
							}
						}
					}
				}
			}
		}
		FixedCost fixedCost = new FixedCost(fixedCostfloatingParameterValue);
		UnitCost unitCost =new UnitCost(unitCostfloatingParameterValue);
		CostParameter costParameter = new CostParameter(fixedCost, unitCost);
		return costParameter;
	}
}
