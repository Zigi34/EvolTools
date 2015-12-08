package org.zigi.evolution.solution.value;

import org.zigi.evolution.solution.value.Cloneable;

public abstract class Genotype implements Cloneable<Genotype> {
	
	public abstract void setValue(Object o);
	
	public abstract Object getValue();
}
