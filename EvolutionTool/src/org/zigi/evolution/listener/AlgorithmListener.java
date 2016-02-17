package org.zigi.evolution.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;

public class AlgorithmListener implements PropertyChangeListener {

	private static final Logger LOG = Logger.getLogger(AlgorithmListener.class);

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// LOG.debug("Event: " + evt.getNewValue());
	}
}
