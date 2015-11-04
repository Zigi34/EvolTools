package org.zigi.evolution.listener;

import org.apache.log4j.Logger;
import org.zigi.evolution.controller.AlgorithmController;
import org.zigi.evolution.model.AlgorithmDTO;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class AlgorithmListener implements ChangeListener<AlgorithmDTO<?, ?>> {

	private static Logger log = Logger.getLogger(AlgorithmListener.class);

	private AlgorithmController controller;

	public AlgorithmListener(AlgorithmController controller) {
		this.controller = controller;
	}

	public AlgorithmController getController() {
		return controller;
	}

	@Override
	public void changed(ObservableValue<? extends AlgorithmDTO<?, ?>> observable, AlgorithmDTO<?, ?> oldValue,
			AlgorithmDTO<?, ?> newValue) {
		newValue.initSettingPane(getController().getSettingPane());
		log.error("sdasd");
	}
}
