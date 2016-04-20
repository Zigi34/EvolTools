package org.zigi.evolution.solution.value;

import java.util.Random;

public class RangedPowerFunction extends GPFenotype {
	private static final Random RAND = new Random();

	private Double minIndex = 1.0;
	private Double maxIndex = 2.0;

	private Double index;
	private boolean district = false;

	public RangedPowerFunction() {
		super(1);
	}

	public RangedPowerFunction(Double min, Double max, boolean district) {
		this();
		minIndex = min;
		maxIndex = max;
		this.district = district;

		randomIndex();
	}

	public GPFenotype cloneMe() {
		RangedPowerFunction item = new RangedPowerFunction();
		item.setDistrict(this.district);
		item.setMinIndex(this.minIndex);
		item.setMaxIndex(this.maxIndex);
		item.setIndex(this.index);
		return item;
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	public Double getMinIndex() {
		return minIndex;
	}

	public void setMinIndex(Double minIndex) {
		this.minIndex = minIndex;
	}

	public Double getMaxIndex() {
		return maxIndex;
	}

	public void setMaxIndex(Double maxIndex) {
		this.maxIndex = maxIndex;
	}

	public void randomIndex() {
		Double max = (maxIndex == null) ? Double.MAX_VALUE : maxIndex;
		Double min = (minIndex == null) ? Double.MIN_VALUE : minIndex;
		Double range = max - min;
		if (range == Double.NaN)
			range = Double.MAX_VALUE;
		index = RAND.nextDouble() * (range) + min;

		if (district) {
			index = (double) Math.round(index);
		}
	}

	public Double getIndex() {
		return index;
	}

	public void setIndex(Double index) {
		if (index == null)
			return;
		if (minIndex != null && index < minIndex)
			return;
		if (maxIndex != null && index > maxIndex)
			return;
		this.index = index;
	}

	public boolean isDistrict() {
		return district;
	}

	public void setDistrict(boolean district) {
		this.district = district;
	}

	@Override
	public String getName() {
		if (minIndex == maxIndex)
			String.format("^%s", minIndex);
		return String.format("^<%s;%s>", minIndex, maxIndex);
	}

	@Override
	public String toString() {
		return String.format("RPOW%s", index == null ? "" : getIndex());
	}
}
