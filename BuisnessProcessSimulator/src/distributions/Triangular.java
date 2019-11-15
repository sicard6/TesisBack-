package distributions;

import org.apache.commons.math3.distribution.TriangularDistribution;

public class Triangular extends Distribution{

	private double lower;
	private double limit;
	private double mode;
	public Triangular(double lower, double limit, double mode) {
		super();
		this.lower = lower;
		this.limit = limit;
		this.mode = mode;
		sample();
	}
	public double getLower() {
		return lower;
	}
	public void setLower(double lower) {
		this.lower = lower;
	}
	public double getLimit() {
		return limit;
	}
	public void setLimit(double limit) {
		this.limit = limit;
	}
	public double getMode() {
		return mode;
	}
	public void setMode(double mode) {
		this.mode = mode;
	}
	
	public void sample()
	{
		TriangularDistribution triangularDistribution = new TriangularDistribution(lower,limit,mode);
		setSample(triangularDistribution.sample()); 
	}
	
}
