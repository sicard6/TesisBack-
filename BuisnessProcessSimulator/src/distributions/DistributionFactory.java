package distributions;

import org.apache.commons.math3.distribution.NormalDistribution;

public class DistributionFactory {

	/**
	 * 
	 * @param distributionType
	 * @param val1 corresponds to mean, lower, min, alpha, scale, or p values
	 * @param val2 corresponds to standardDeviation,limit, max, beta, shape, or trials values
	 * @param val3 corresponds to mode
	 * @return
	 */
	public Distribution makeDistribution (String distributionType, double val1, double val2, double val3)
	{
		Distribution distribution = null;
		if(distributionType.equals("Beta"))
		{
			distribution= new Beta(val1,val2);
		}
		else if(distributionType.equals("Binomial"))
		{
			Double v2 = val2;
			distribution= new Binomial(val1,v2.intValue());
		}
		else if(distributionType.equals("Gamma"))
		{
			distribution= new Gamma(val1,val2);
		}
		else if(distributionType.equals("LogNormal"))
		{
			distribution= new LogNormal(val1,val2);
		}
		else if(distributionType.equals("NegativeExponential"))
		{
			distribution= new NegativeExponential(val1);
		}
		else if(distributionType.equals("Normal"))
		{
			distribution= new Normal(val1,val2);
		}
		else if(distributionType.equals("Poisson"))
		{
			distribution= new Poisson(val1);
		}
		else if(distributionType.equals("Triangular"))
		{
			distribution= new Triangular(val1,val2,val3);
		}
		else if(distributionType.equals("TruncatedNormal"))
		{
			distribution= new TruncatedNormal(val1,val2);
		}
		else if(distributionType.equals("Uniform"))
		{
			Double v1= val1;
			Double v2= val2;
			distribution= new Uniform(v1.intValue(),v2.intValue());
		}
		else if(distributionType.equals("Weibull"))
		{
			distribution= new Weibull(val1,val2);
		}		
		return distribution;
	}
	
}
