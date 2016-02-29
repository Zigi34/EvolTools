package org.zigi.evolution.solution.value;

/**
 * Class represent numeric value
 * 
 * @author zigi
 *
 */
public class NumberGenotyp extends GPFenotype {

	private Double value;

	public NumberGenotyp(int childs) {
		super(childs);
	}

	public GPFenotype cloneMe() {
		NumberGenotyp val = new NumberGenotyp(getMaxChilds());
		val.setValue(getValue());
		return val;
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof Double)
			value = (Double) o;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

}
