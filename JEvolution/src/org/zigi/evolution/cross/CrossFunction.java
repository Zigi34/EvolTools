package org.zigi.evolution.cross;

import org.zigi.evolution.util.Population;

public abstract class CrossFunction {
	public abstract void cross(Population solutions, long offset, long size);
}
