package org.zigi.evolution.algorithm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zigi.evolution.problem.Problem;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

public abstract class EvolutionAlgorithm implements Runnable {

	private Population population;
	private boolean terminate;
	private Problem problem;
	private Solution bestSolution;

	protected OutputStreamWriter writer;

	public static final String INIT_STATE = "INIT_STATE";

	public static final String STATE_NAME = "STATE";

	protected String state = INIT_STATE;

	private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

	private static final Logger LOG = Logger.getLogger(EvolutionAlgorithm.class);

	/**
	 * Spustí algoritmus
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		terminate = false;

		if (problem == null)
			throw new Exception("Problem není nastavený");

		if (population == null)
			fillPopulation();

		run();
	}

	/**
	 * Naplnění populace náhodnými řešeními pomocí objektu proglému
	 */
	private void fillPopulation() {
		population = new Population();
		for (int i = 0; i < population.getMaxSolutions(); i++)
			population.add(problem.randomSolution());
	}

	/**
	 * Zastavení algoritmus
	 */
	public void stop() {
		terminate = true;
	}

	/**
	 * Získání populace jedinců
	 * 
	 * @return
	 */
	public Population getPopulation() {
		return population;
	}

	/**
	 * Nastavení populace jedinců
	 * 
	 * @param pop
	 */
	public void setPopulation(Population pop) {
		this.population = pop;
	}

	/**
	 * Nastavení velikosti maximální velikosti populace. Aktuální populace se
	 * smaže
	 * 
	 * @param size
	 *            maximální kapacita
	 */
	public void setPopulationSize(int size) {
		population = new Population(size);
	}

	/**
	 * Testuje, zda se máalgoritmus ukončit
	 * 
	 * @return
	 */
	public boolean isTerminate() {
		return terminate;
	}

	/**
	 * Nastavení běhu/zastavení algoritmu
	 * 
	 * @param terminate
	 */
	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}

	/**
	 * Znovu spuštění algoritmu od začátku
	 * 
	 * @throws Exception
	 */
	public void restart() throws Exception {
		terminate = false;
		start();
	}

	/**
	 * Získání instance problému
	 * 
	 * @return
	 */
	public Problem getProblem() {
		return problem;
	}

	/**
	 * Nastavení instance problému
	 * 
	 * @param problem
	 */
	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	/**
	 * Získání instance nejlepšího řešení
	 * 
	 * @return
	 */
	public Solution getBestSolution() {
		return bestSolution;
	}

	/**
	 * Prochazi celou populaci a hleda reseni, ktera jsou lepsi nez aktualne
	 * nejlepsi reseni. Pokud takove reseni nalezne, nahradi posledni nejlepsi
	 * reseni
	 * 
	 * @param population
	 */
	public void checkBestSolution(Population population) {
		if (bestSolution == null)
			bestSolution = population.getSolutions().get(0).cloneMe();

		for (Solution solution : population.getSolutions()) {
			if (solution.getFitness() > bestSolution.getFitness()) {
				bestSolution = solution.cloneMe();
				LOG.info(String.format("New best solution: %s", bestSolution));
			}
		}
	}

	/**
	 * Stream pro zapisovani informaci o prubehu algoritmu
	 * 
	 * @return
	 */
	public OutputStreamWriter getWriter() {
		return writer;
	}

	/**
	 * Nastavuje (povoluje) stream pro zapis informaci o algoritmu
	 * 
	 * @param writer
	 */
	public void setWriter(OutputStreamWriter writer) {
		this.writer = writer;
	}

	/**
	 * Nastavuje (povoluje) stream pro zapis informaci o algoritmu
	 * 
	 * @param file
	 *            soubor pro zápis
	 * @throws FileNotFoundException
	 */
	public void setWriter(File file) throws FileNotFoundException {
		this.writer = new OutputStreamWriter(new FileOutputStream(file));
	}

	/**
	 * Identifikuje zmenu pro posluchace
	 * 
	 * @param object
	 *            objekt vyvolavajici udalost
	 * @param property
	 *            parametr, ktery se zmenil
	 * @param oldValue
	 *            stara hodnota
	 * @param newValue
	 *            nova hodnota
	 */
	private void notifyListeners(Object object, String property, Object oldValue, Object newValue) {
		for (PropertyChangeListener name : listeners) {
			name.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
		}
	}

	/**
	 * Prida posluchace udalosti
	 * 
	 * @param listener
	 */
	public void addChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}

	public List<PropertyChangeListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<PropertyChangeListener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * Vraci stav algoritmu
	 * 
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * Změni stav algoritmu
	 * 
	 * @param state
	 */
	protected void setState(String state) {
		notifyListeners(this, "STATE", this.state, state);
		this.state = state;
	}
}
