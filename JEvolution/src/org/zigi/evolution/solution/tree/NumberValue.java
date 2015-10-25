package org.zigi.evolution.solution.tree;

public class NumberValue implements Terminal {
	private Number value;
	
	public NumberValue(Number value) {
		this.value = value;
	}
	
	public Integer getInt() {
		return value.intValue();
	}
	
	public Double getDouble() {
		return value.doubleValue();
	}
}
