package org.zigi.evolution.model;

public class InfoSource {
	private String name;
	private String gui;

	public InfoSource(String name, String gui) {
		this.name = name;
		this.gui = gui;
	}

	/**
	 * Vraci nazev souboru gui s priponou fxml
	 * 
	 * @return
	 */
	public String getGui() {
		return gui;
	}

	@Override
	public String toString() {
		return name;
	}
}
