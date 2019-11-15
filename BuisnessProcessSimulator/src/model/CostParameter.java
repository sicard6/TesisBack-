package model;

public class CostParameter {

	private FixedCost fixedCost;
	private UnitCost unitCost;
	
	public CostParameter(FixedCost fixedCost, UnitCost unitCost) {
		super();
		this.fixedCost = fixedCost;
		this.unitCost = unitCost;
	}

	public FixedCost getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(FixedCost fixedCost) {
		this.fixedCost = fixedCost;
	}

	public UnitCost getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(UnitCost unitCost) {
		this.unitCost = unitCost;
	}
	
}
