package distributions;

import org.apache.commons.math3.distribution.BinomialDistribution;

public class Binomial extends Distribution {

	private double p;
	private int trials;
	public Binomial(double p, int trials) {
		super();
		this.p = p;
		this.trials = trials;
		sample();
	}
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	public int getTrials() {
		return trials;
	}
	public void setTrials(int trials) {
		this.trials = trials;
	}
	
	public void sample()
	{
		BinomialDistribution binomialDistribution = new BinomialDistribution(trials, p);
		setSample(binomialDistribution.sample());
	}
}
