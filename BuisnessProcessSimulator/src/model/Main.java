package model;

import java.util.ArrayList;
import java.util.Scanner;

import readers.BPMNDiagramReader;
import readers.BPMReader;
import readers.BPSimDataReader;
import simulator.Record;
import simulator.ReportBuilder;
import simulator.Simulator;
import simulator.Timeline;

public class Main {

	public static void main(String[] args) {
		//read file routes from console
		Scanner in = new Scanner(System.in);
		//int processesAmount = in.nextInt();
		int processesAmount = 3;
		ArrayList<DiagramPackage> diagramPackages=new ArrayList<DiagramPackage>();
		for(int i=0;i<processesAmount;i++)
		{
			String diagramRoute = in.nextLine();
		    System.out.println("diagramRoute "+diagramRoute);
		    String simDataRoute = in.nextLine();
		    System.out.println("simDataRoute "+simDataRoute);
			//build model
			BPMReader bPMReader = new BPMReader();
			DiagramPackage diagramPackage=bPMReader.constructModel(diagramRoute,simDataRoute);
			System.out.println("MODEL CONSTRUCTION COMPLETE");
			diagramPackages.add(diagramPackage);
		}
		//create report
		ReportBuilder reportBuilder = new ReportBuilder();
		//start simulator
		Simulator simulator = new Simulator(diagramPackages);
		Timeline timeline = simulator.getTimeline();
		//timeline.placeFirstEvent(diagramPackage);
		timeline.runSimulation(diagramPackages);
		timeline.getExcecTime();		
	}

}
