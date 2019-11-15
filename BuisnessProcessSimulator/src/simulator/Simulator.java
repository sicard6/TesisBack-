package simulator;

import java.util.ArrayList;

import model.DiagramPackage;
import model.Participant;

public class Simulator {
	
	private Timeline timeline;
	private ArrayList<DiagramPackage> diagramPackages;
	
	public Simulator(ArrayList<DiagramPackage> diagramPackages)
	{
		timeline = new Timeline(diagramPackages.get(0));
		this.diagramPackages= diagramPackages;
		for(int i=0;i<diagramPackages.size();i++)
		{
			DiagramPackage diagramPackage= diagramPackages.get(i);
			if(diagramPackage.getParticipants().size()>0)
			{
				setParticipantsTotalResources(diagramPackage, diagramPackage.getParticipants());
				timeline.addParticipants(diagramPackage.getParticipants());
			}
		}		
		
	}
	public Timeline getTimeline() {
		return timeline;
	}
	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}
	public ArrayList<DiagramPackage> getDiagramPackages() {
		return diagramPackages;
	}
	public void setDiagramPackages(ArrayList<DiagramPackage> diagramPackages) {
		this.diagramPackages = diagramPackages;
	}
	public void setParticipantsTotalResources(DiagramPackage diagramPackage,ArrayList<Participant>participants)
	{
		ArrayList<Participant> newParticipants= participants;
		for(int i=0; i< newParticipants.size();i++)
		{
			newParticipants.get(i).setTotalResources(newParticipants.get(i).getTotalResourcesFromFile());
			
		}
		diagramPackage.setParticipants(newParticipants);
		
	}
	
	
	

}
