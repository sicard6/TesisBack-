package distributions;

import org.apache.commons.math3.distribution.PoissonDistribution;

public class Poisson extends Distribution{

	private double p;

	public Poisson(double p) {
		super();
		this.p = p;
		sample();
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}
	
	public void sample()
	{
		PoissonDistribution poissonDistribution = new PoissonDistribution(p);
		setSample(poissonDistribution.sample());
	}
	
}
