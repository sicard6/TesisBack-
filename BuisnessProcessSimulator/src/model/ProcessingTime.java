package model;

import java.util.ArrayList;

public class ProcessingTime {

	private ArrayList<String> resultRequests;
	private double floatingParameterValue;
	private int numericParameterValue;
	private String durationParameter;
	private double normalDistributionMean;
	private double normalDistributionStandardDeviation;
	private int truncatedNormalDistributionMean;
	private int truncatedNormalDistributionStandardDeviation;
	private int truncatedNormalDistributionMin;
	private int truncatedNormalDistributionMax;
	private double triangularDistributionMode;
	private double triangularDistributionMin;
	private double triangularDistributionMax;
	private int uniformDistributionMin;
	private int uniformDistributionMax;
	private double lognormalDistributionMean;
	private double lognormalDistributionStandardDeviation;
	private double betaDistributionShape;
	private double betaDistributionScale;
	private int negativeExponentialDistributionMean;
	private double gammaDistributionShape;
	private double gammaDistributionScale;
	private int erlangDistributionMean;
	private int erlangDistributionK;
	private double weibullDistributionShape;
	private double weibullDistributionScale;
	private double binomialDistributionProbability;
	private int binomialDistributionTrials;
	private double poissonDistributionMean;
	
	
	public ProcessingTime(ArrayList<String> resultRequests) {
		super();
		this.resultRequests = resultRequests;
	}
	
	public ArrayList<String> getResultRequests() {
		return resultRequests;
	}

	public void setResultRequests(ArrayList<String> resultRequests) {
		this.resultRequests = resultRequests;
	}



	public int getNumericParameterValue() {
		return numericParameterValue;
	}

	public void setNumericParameterValue(int numericParameterValue) {
		this.numericParameterValue = numericParameterValue;
	}

	public String getDurationParameter() {
		return durationParameter;
	}

	public void setDurationParameter(String durationParameter) {
		this.durationParameter = durationParameter;
	}

	public double getNormalDistributionMean() {
		return normalDistributionMean;
	}

	public void setNormalDistributionMean(double normalDistributionMean) {
		this.normalDistributionMean = normalDistributionMean;
	}

	public double getNormalDistributionStandardDeviation() {
		return normalDistributionStandardDeviation;
	}

	public void setNormalDistributionStandardDeviation(double normalDistributionStandardDeviation) {
		this.normalDistributionStandardDeviation = normalDistributionStandardDeviation;
	}

	public int getTruncatedNormalDistributionMean() {
		return truncatedNormalDistributionMean;
	}

	public void setTruncatedNormalDistributionMean(int truncatedNormalDistributionMean) {
		this.truncatedNormalDistributionMean = truncatedNormalDistributionMean;
	}

	public int getTruncatedNormalDistributionStandardDeviation() {
		return truncatedNormalDistributionStandardDeviation;
	}

	public void setTruncatedNormalDistributionStandardDeviation(int truncatedNormalDistributionStandardDeviation) {
		this.truncatedNormalDistributionStandardDeviation = truncatedNormalDistributionStandardDeviation;
	}

	public int getTruncatedNormalDistributionMin() {
		return truncatedNormalDistributionMin;
	}

	public void setTruncatedNormalDistributionMin(int truncatedNormalDistributionMin) {
		this.truncatedNormalDistributionMin = truncatedNormalDistributionMin;
	}

	public int getTruncatedNormalDistributionMax() {
		return truncatedNormalDistributionMax;
	}

	public void setTruncatedNormalDistributionMax(int truncatedNormalDistributionMax) {
		this.truncatedNormalDistributionMax = truncatedNormalDistributionMax;
	}

	public double getTriangularDistributionMode() {
		return triangularDistributionMode;
	}

	public void setTriangularDistributionMode(double triangularDistributionMode) {
		this.triangularDistributionMode = triangularDistributionMode;
	}

	public double getTriangularDistributionMin() {
		return triangularDistributionMin;
	}

	public void setTriangularDistributionMin(double triangularDistributionMin) {
		this.triangularDistributionMin = triangularDistributionMin;
	}

	public double getTriangularDistributionMax() {
		return triangularDistributionMax;
	}

	public void setTriangularDistributionMax(double triangularDistributionMax) {
		this.triangularDistributionMax = triangularDistributionMax;
	}

	public int getUniformDistributionMin() {
		return uniformDistributionMin;
	}

	public void setUniformDistributionMin(int uniformDistributionMin) {
		this.uniformDistributionMin = uniformDistributionMin;
	}

	public int getUniformDistributionMax() {
		return uniformDistributionMax;
	}

	public void setUniformDistributionMax(int uniformDistributionMax) {
		this.uniformDistributionMax = uniformDistributionMax;
	}

	public double getFloatingParameterValue() {
		return floatingParameterValue;
	}

	public void setFloatingParameterValue(double floatingParameterValue) {
		this.floatingParameterValue = floatingParameterValue;
	}

	public double getLognormalDistributionMean() {
		return lognormalDistributionMean;
	}

	public void setLognormalDistributionMean(double lognormalDistributionMean) {
		this.lognormalDistributionMean = lognormalDistributionMean;
	}

	public double getLognormalDistributionStandardDeviation() {
		return lognormalDistributionStandardDeviation;
	}

	public void setLognormalDistributionStandardDeviation(double lognormalDistributionStandardDeviation) {
		this.lognormalDistributionStandardDeviation = lognormalDistributionStandardDeviation;
	}

	public double getBetaDistributionShape() {
		return betaDistributionShape;
	}

	public void setBetaDistributionShape(double betaDistributionShape) {
		this.betaDistributionShape = betaDistributionShape;
	}

	public double getBetaDistributionScale() {
		return betaDistributionScale;
	}

	public void setBetaDistributionScale(double betaDistributionScale) {
		this.betaDistributionScale = betaDistributionScale;
	}

	public int getNegativeExponentialDistributionMean() {
		return negativeExponentialDistributionMean;
	}

	public void setNegativeExponentialDistributionMean(int negativeExponentialDistributionMean) {
		this.negativeExponentialDistributionMean = negativeExponentialDistributionMean;
	}

	public double getGammaDistributionShape() {
		return gammaDistributionShape;
	}

	public void setGammaDistributionShape(double gammaDistributionShape) {
		this.gammaDistributionShape = gammaDistributionShape;
	}

	public double getGammaDistributionScale() {
		return gammaDistributionScale;
	}

	public void setGammaDistributionScale(double gammaDistributionScale) {
		this.gammaDistributionScale = gammaDistributionScale;
	}

	public int getErlangDistributionMean() {
		return erlangDistributionMean;
	}

	public void setErlangDistributionMean(int erlangDistributionMean) {
		this.erlangDistributionMean = erlangDistributionMean;
	}

	public int getErlangDistributionK() {
		return erlangDistributionK;
	}

	public void setErlangDistributionK(int erlangDistributionK) {
		this.erlangDistributionK = erlangDistributionK;
	}

	public double getWeibullDistributionShape() {
		return weibullDistributionShape;
	}

	public void setWeibullDistributionShape(double weibullDistributionShape) {
		this.weibullDistributionShape = weibullDistributionShape;
	}

	public double getWeibullDistributionScale() {
		return weibullDistributionScale;
	}

	public void setWeibullDistributionScale(double weibullDistributionScale) {
		this.weibullDistributionScale = weibullDistributionScale;
	}

	public double getBinomialDistributionProbability() {
		return binomialDistributionProbability;
	}

	public void setBinomialDistributionProbability(double binomialDistributionProbability) {
		this.binomialDistributionProbability = binomialDistributionProbability;
	}

	public int getBinomialDistributionTrials() {
		return binomialDistributionTrials;
	}

	public void setBinomialDistributionTrials(int binomialDistributionTrials) {
		this.binomialDistributionTrials = binomialDistributionTrials;
	}

	public double getPoissonDistributionMean() {
		return poissonDistributionMean;
	}

	public void setPoissonDistributionMean(double poissonDistributionMean) {
		this.poissonDistributionMean = poissonDistributionMean;
	}
	
	
		
}
