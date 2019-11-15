package distributions;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;

public class Uniform extends Distribution {

	private int min;
	private int max;
	public Uniform(int min, int max) {
		super();
		this.min = min;
		this.max = max;
		sample();
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public void sample()
	{
		UniformIntegerDistribution uniformIntegerDistribution = new UniformIntegerDistribution(min, max);
		setSample(uniformIntegerDistribution.sample());
	}
}
