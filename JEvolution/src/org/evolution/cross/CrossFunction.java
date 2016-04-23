package org.evolution.cross;

import org.evolution.util.Population;

public abstract class CrossFunction {
	public abstract void cross(Population solutions, long offset, long size);
}
