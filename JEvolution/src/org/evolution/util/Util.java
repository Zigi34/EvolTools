package org.evolution.util;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;

public class Util {

	private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#.####");
	private static final Logger LOG = Logger.getLogger(Util.class);

	public static String formatNumber(Double value) {
		return NUMBER_FORMAT.format(value);
	}

	public static void logPopulation(Population pop) {
		if (pop != null) {
			for (int i = 0; i < pop.size(); i++) {
				LOG.info(i + ". " + pop.getSolutions().get(i));
			}
			LOG.info("");
		}
	}
}
