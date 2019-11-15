package distributions;

import org.apache.commons.math3.distribution.BetaDistribution;

public class Beta extends Distribution{
	
	private double alpha;
	private double beta;
	public Beta(double alpha, double beta) {
		super();
		this.alpha = alpha;
		this.beta = beta;
		sample();
	}
	public double getAlpha() {
		return alpha;
	}
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	public double getBeta() {
		return beta;
	}
	public void setBeta(double beta) {
		this.beta = beta;
	}
	
	public void sample()
	{
		BetaDistribution betaDistribution = new BetaDistribution(alpha, beta);
		setSample(betaDistribution.sample());
	}
	
}
