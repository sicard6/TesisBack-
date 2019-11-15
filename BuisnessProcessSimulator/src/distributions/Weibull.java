package distributions;

import org.apache.commons.math3.distribution.WeibullDistribution;

public class Weibull extends Distribution {

	private double alpha;
	private double beta;
	public Weibull(double alpha, double beta) {
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
		WeibullDistribution weibullDistribution = new WeibullDistribution(alpha, beta);
		setSample(weibullDistribution.sample());
	}
}
