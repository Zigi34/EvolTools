package org.zigi.evolution.util;

public class TimeMeasure {

	private long start;
	private long stop;

	public static final int MILLIS_DELAY = 0;

	private TimeMeasure(long start) {
		this.start = start;
	}

	public static TimeMeasure start() {
		return new TimeMeasure(System.nanoTime());
	}

	public Double stop(Integer type) {
		this.stop = System.nanoTime();
		if (type.equals(MILLIS_DELAY))
			return (this.stop - this.start) / 1000000.0;
		else
			return (this.stop - this.start) / 1.0;
	}
}
