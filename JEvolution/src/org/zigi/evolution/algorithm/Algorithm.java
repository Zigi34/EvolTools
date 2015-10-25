package org.zigi.evolution.algorithm;

public interface Algorithm {
	/**
	 * Spuštění algoritmu
	 */
	void start();
	
	/**
	 * Zastavení algoritmu
	 */
	void stop();
	
	/**
	 * Inicializace před spuštěním
	 */
	void init();
}
