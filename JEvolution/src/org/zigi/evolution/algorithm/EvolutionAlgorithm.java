package org.zigi.evolution.algorithm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zigi.evolution.algorithm.terminate.TerminateFunction;
import org.zigi.evolution.problem.Problem;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

/**
 * Object represent evolution algorithm method
 * 
 * @author zigi
 *
 */
public abstract class EvolutionAlgorithm implements Runnable {
	private Population population;
	private boolean terminate;
	private Problem problem;
	private Solution bestSolution;
	private int generation = 2000;
	private int actualGeneration = 0;
	private Thread thread = new Thread(this);

	protected OutputStreamWriter writer;

	public static final String INIT_STATE = "INIT_STATE";
	public static final String ALGORITHM_TERMINATING = "ALGORITHM_TERMINATING";
	public static final String ALGORITHM_TERMINATED = "ALGORITHM_TERMINATED";
	public static final String ALGORITHM_STARTED = "ALGORITHM_STARTED";
	public static final String NEW_BEST_SOLUTION = "NEW_BEST_SOLUTION";

	public static final String STATE_NAME = "STATE";

	protected String state = INIT_STATE;

	private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
	private List<TerminateFunction> terminateFunction = new LinkedList<>();

	private static final Logger LOG = Logger.getLogger(EvolutionAlgorithm.class);

	/**
	 * Spustí algoritmus
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		if (!thread.isAlive()) {
			thread = new Thread(this);
			terminate = false;

			if (problem == null)
				throw new Exception("Problem není nastavený");

			if (population == null)
				fillPopulation();

			thread.start();
			setState(ALGORITHM_STARTED);
		}
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
		setState(ALGORITHM_TERMINATING);

		if (thread != null) {
			try {
				thread.join(1000);
				thread.interrupt();
			} catch (InterruptedException e) {
				LOG.error(e);
			}
		}
		setState(ALGORITHM_TERMINATED);
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
	 * Testuje, zda se má algoritmus ukončit
	 * 
	 * @return
	 */
	public boolean isTerminate() {
		Boolean terminate = this.terminate;
		Solution best = getBestSolution();
		if (best != null && getProblem().getNormalizedFitness(best) == 1.0)
			return true;
		if (!terminateFunction.isEmpty())
			for (TerminateFunction function : terminateFunction) {
				terminate |= function.isTerminate(this);
				if (terminate)
					return true;
			}
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
		if (bestSolution == null) {
			for (Solution sol : population.getSolutions()) {
				if (!sol.getFunctionValue().isNaN()) {
					bestSolution = sol.cloneMe();
					setState(EvolutionAlgorithm.NEW_BEST_SOLUTION);
					break;
				}
			}
		}

		for (Solution solution : population.getSolutions()) {
			Double fitness = solution.getFunctionValue();
			if (!fitness.isNaN() && problem.isMinProblem() && fitness < bestSolution.getFunctionValue()) {
				bestSolution = solution.cloneMe();
				setState(EvolutionAlgorithm.NEW_BEST_SOLUTION);
			} else if (!fitness.isNaN() && !problem.isMinProblem() && fitness > bestSolution.getFunctionValue()) {
				bestSolution = solution.cloneMe();
				setState(EvolutionAlgorithm.NEW_BEST_SOLUTION);
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

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public int getActualGeneration() {
		return actualGeneration;
	}

	public void setActualGeneration(int actualGeneration) {
		this.actualGeneration = actualGeneration;
	}

	public void increseActualGeneration() {
		this.actualGeneration++;
	}

	/**
	 * Add new terminate function
	 * 
	 * @param function
	 *            terminate function
	 */
	public void addTerminateFunction(TerminateFunction function) {
		terminateFunction.add(function);
	}

	/**
	 * Remove all terminate function
	 */
	public void clearTerminateFunctions() {
		terminateFunction.clear();
	}
}
