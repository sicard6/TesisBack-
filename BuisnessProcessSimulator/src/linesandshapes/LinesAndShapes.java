package linesandshapes;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Activity;
import model.ConnectorGraphicsInfo;
import model.Coordinate;
import model.DiagramPackage;
import model.Lane;
import model.NodeGraphicsInfo;
import model.Pool;
import model.Transition;
import model.WorkflowProcess;
import readers.BPMNDiagramReader;


public class LinesAndShapes extends JPanel {
	
	private String route;
	
  public static void main(String[] args) {
   
	  JFrame f = new JFrame("Twilight Zone");
	   f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "xml Files", "xml");
	   JFileChooser fileChooser  = new JFileChooser("../");
	   fileChooser.setFileFilter(filter);
	   fileChooser.showOpenDialog(null);
	   String r = fileChooser.getSelectedFile().getAbsolutePath();
	   
	   LinesAndShapes lines = new LinesAndShapes();
	   lines.setRoute(r);
	   f.getContentPane().add(lines);
	   f.setSize(290, 325);
	   f.setLocation(550, 25);
	   f.setVisible(true);
  }
  
  public String getRoute() {
	return route;
  }

  public void setRoute(String route) {
	this.route = route;
  }

public void paint(Graphics g) {
	  //Scanner in = new Scanner(System.in);
	  //String diagramRoute = in.nextLine();

	  //String diagramRoute="C:\\Users\\david\\Documents\\universidad\\10o\\proyecto de grado\\modelos\\proceso eventos de tiempo\\DIAG\\Diagram.xml";
	  //custom color
	  String hexColor = new String("0x45e5B");
	  g.setColor(Color.decode(hexColor));
	  //draw a line (starting x,y; ending x,y)
	  //g.drawLine(10, 10, 40, 10);
	  
	  Graphics2D g2 = (Graphics2D) g;
	  //g2.drawRect(10, 20, 150, 40);
	  
	  BPMNDiagramReader diagramReader = new BPMNDiagramReader();
	  //DiagramPackage diagramPackage=diagramReader.readDiagram();
	  DiagramPackage diagramPackage=diagramReader.readDiagram(route);
	  
	  ArrayList<WorkflowProcess> workflowProcesses=diagramPackage.getWorkflowProcesses();
	  for(int i =0;i<workflowProcesses.size();i++)
	  {
		  WorkflowProcess workflowProcess=workflowProcesses.get(i);
		  ArrayList<Activity> activities = workflowProcess.getActivities();
		  if(activities.size()>0)
		  {
			  for(int j=0;j<activities.size();j++)
			  {
				  Activity activity = activities.get(j);
				  NodeGraphicsInfo acNGI = activity.getNodeGraphicsInfo();
				  Coordinate acCoor= acNGI.getCoordinate();
				  try {
					  int acX= acCoor.getxCoordinate();
					  int acY= acCoor.getyCoordinate();
					  //System.out.println("activity "+activity.getId()+"has coordinates");
					  int acHeight= acNGI.getHeight();
					  int acWidth= acNGI.getWidth();
					  
					  g2.drawRect(acX, acY, acWidth, acHeight);
					  
				  }
				  catch(Exception e){
					  System.out.println("activity "+activity.getId()+"has no content");
				  }  
			  }
		  }
		  ArrayList<Transition> transitions = workflowProcess.getTransitions();
		  
		  if(transitions.size()>0)
		  {			 
			  for(int j=0;j<transitions.size();j++)
			  {
				  Transition transition = transitions.get(j);
				  System.out.println("transition with id "+transition.getId());
				  ConnectorGraphicsInfo trCGI = transition.getConnectorGraphicsInfo();
				  ArrayList<Coordinate> trCoors=trCGI.getCoordinates();
				  try {
					  if(trCoors.size()>0)
					  {
						  for(int k=0;k<trCoors.size();k=k+1)
						  {
							  Coordinate coordFrom= trCoors.get(k);
							  Coordinate coordTo = trCoors.get(k+1);
							  
							  int xCoorFrom = coordFrom.getxCoordinate();
							  int yCoorFrom = coordFrom.getyCoordinate();
							  int xCoorTo = coordTo.getxCoordinate();
							  int yCoorTo = coordTo.getyCoordinate();
							  
							  g.drawLine(xCoorFrom, yCoorFrom, xCoorTo, yCoorTo);

						  }
					  }
					  
				  }
				  catch(Exception e)
				  {
					  System.out.println("transition "+transition.getId()+" has no content or coordinates");
				  }
			  }
		  }
	  }
	}
  
}