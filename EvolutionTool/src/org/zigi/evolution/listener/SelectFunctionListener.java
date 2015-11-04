package org.zigi.evolution.listener;

import org.apache.log4j.Logger;
import org.zigi.evolution.select.SelectFunction;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SelectFunctionListener<T extends Solution<U>, U extends CloneableValue<U>>
		implements ChangeListener<SelectFunction<T, U>> {

	private static Logger log = Logger.getLogger(SelectFunctionListener.class);

	@Override
	public void changed(ObservableValue<? extends SelectFunction<T, U>> observable, SelectFunction<T, U> oldValue,
			SelectFunction<T, U> newValue) {
		log.error("Select function changed");

	}
}
