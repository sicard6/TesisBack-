package simulator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ReportBuilder {
	
	private static String filepath="C:\\Users\\david\\Documents\\universidad\\10o\\proyecto de grado\\modelos\\proceso de simulacion\\DIAG\\Report.txt";
	private static String tempFile="C:\\Users\\david\\Documents\\universidad\\10o\\proyecto de grado\\modelos\\proceso de simulacion\\DIAG\\Temp.txt";
	private ArrayList<Record> records;
	private static Scanner scanner;
	
	public ReportBuilder()
	{
		records = new ArrayList<Record>();
	}
	
	public void saveRecord(Record record,boolean activityMoved)
	{
		if(!containsRecord(record.getName(),record.getType()))
		{
			records.add(record);
		}
		else
		{
			
			Record containedRecord = getRecord(record.getName(),record.getType());
			int recordIndex = getRecordIndex(record.getName(),record.getType());
			Record editedRecord=containedRecord;
			
			//edit the record on record list
			//processingTimes
			//if(!activityMoved)
			{
				editedRecord.setCompletedInstances(editedRecord.getCompletedInstances()+1);
			}			
			if(record.getMinTime()<editedRecord.getMinTime())
			{
				editedRecord.setMinTime(record.getMinTime());
			}
			if(record.getMaxTime()>editedRecord.getMaxTime())
			{
				editedRecord.setMaxTime(record.getMaxTime());
			}
			editedRecord.setAvgTime(((containedRecord.getMinTime()+containedRecord.getMaxTime())/2+(editedRecord.getMinTime()+editedRecord.getMaxTime())/2)/2);
			editedRecord.setTotalTime(editedRecord.getTotalTime()+record.getTotalTime());
			
			//waitTimes
			if(record.getMinResourceWaitTime()<editedRecord.getMinResourceWaitTime())
			{
				editedRecord.setMinResourceWaitTime(record.getMinResourceWaitTime());
			}
			if(record.getMaxResourceWaitTime()>editedRecord.getMaxResourceWaitTime())
			{
				editedRecord.setMaxResourceWaitTime(record.getMaxResourceWaitTime());
			}
			editedRecord.setAvgResourceWaitTime(((containedRecord.getMinResourceWaitTime()+containedRecord.getMaxResourceWaitTime())/2+(editedRecord.getMinResourceWaitTime()+editedRecord.getMaxResourceWaitTime())/2)/2);
			editedRecord.setTotalResourceWaitTime(editedRecord.getTotalResourceWaitTime()+record.getTotalResourceWaitTime());
			
			records.set(recordIndex, editedRecord);
			
		}
		
	}	
	public void createReport()
	{
		try
		{
			FileWriter fw = new FileWriter(filepath,true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			
			for(int i=0;i<records.size();i++)
			{
				Record record = records.get(i);
				pw.println(record.getName()+","+record.getType()+","+record.getCompletedInstances()+","
						+record.getMinTime()+","+record.getMaxTime()+","+record.getAvgTime()+","+record.getTotalTime()+","
						+record.getMinResourceWaitTime()+","+record.getMaxResourceWaitTime()+","
						+record.getAvgResourceWaitTime()+","
						+record.getResourceWaitStandardDeviation()+","+record.getTotalResourceWaitTime());
			}
			pw.flush();
			pw.close();
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean containsRecord(String name, String type)
	{
		boolean res= false;
		//System.out.println("nombre record registrando: " +name);
		//System.out.println("tipo record registrando: " +type);
		for(int i=0;i<records.size();i++)
		{
			Record actual = records.get(i);
			//System.out.println("nombre record actual: " +actual.getName());
			//System.out.println("tipo record actual: " +actual.getType());
			if(name.equals(actual.getName())
				&& type.equals(actual.getType()))
			{
				res=true;
			}
		}		
		return res;
	}
	public Record getRecord(String name, String type)
	{
		Record record = new Record("", "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		for(int i=0;i<records.size();i++)
		{
			Record actual = records.get(i);
			if(name.equals(actual.getName())
				&& type.equals(actual.getType()))
			{
				record = actual;
			}
		}	
		return record;
	}
	public int getRecordIndex(String name, String type)
	{
		int res=0;
		for(int i=0;i<records.size();i++)
		{
			Record actual = records.get(i);
			if(name.equals(actual.getName())
				&& type.equals(actual.getType()))
			{
				res=i;
			}
		}	
		return res;
	}

	public void saveHeader(String string, String string2, String string3, String string4, String string5,
			String string6, String string7, String string8, String string9, String string10, String string11,
			String string12) {
		try
		{
			FileWriter fw = new FileWriter(filepath,true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);

				pw.println(string+","+string2+","+string3+","+string4+","+string5+","+string6+","+string7+","+
						   string8+","+string9+","+string10+","+string11+","+string12);
				pw.flush();
				pw.close();
			
		}
		catch(Exception E)
		{
			
		}
		
	}

}
