package distributions;

import org.apache.commons.math3.distribution.ExponentialDistribution;

public class NegativeExponential extends Distribution{

	private double mean;

	public NegativeExponential(double mean) {
		super();
		this.mean = mean;
		sample();
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}
	
	public void sample()
	{
		ExponentialDistribution exponentialDistribution= new ExponentialDistribution(mean);
		setSample(exponentialDistribution.sample());
	}
}
