package distributions;

import org.apache.commons.math3.distribution.GammaDistribution;

public class Gamma extends Distribution{

	private double scale;
	private double shape;
	public Gamma(double shape, double scale) {
		super();
		this.scale = scale;
		this.shape = shape;
		sample();
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	public double getShape() {
		return shape;
	}
	public void setShape(double shape) {
		this.shape = shape;
	}
	
	public void sample()
	{
		GammaDistribution gammaDistribution=new GammaDistribution(shape, scale);
		setSample(gammaDistribution.sample());
	}
}
