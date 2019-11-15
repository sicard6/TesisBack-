package distributions;

import org.apache.commons.math3.distribution.NormalDistribution;

public class LogNormal extends Distribution{

	private double mean;
	private double standardDeviation;
	
	public LogNormal(double mean, double standardDeviation) {
		super();
		this.mean = mean;
		this.standardDeviation = standardDeviation;
		sample();
	}
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getStandardDeviation() {
		return standardDeviation;
	}
	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
	public void sample()
	{
		NormalDistribution normalDistribution = new NormalDistribution(mean, standardDeviation);
		setSample(normalDistribution.sample());
	}
}
