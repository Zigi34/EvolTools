package org.zigi.evolution.problem;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.solution.value.Genotype;
import org.zigi.evolution.solution.value.Node;

public abstract class TreeProblem extends Problem {

	private List<GPFenotype> values = new LinkedList<GPFenotype>();
	private static final Random RAND = new Random();
	private Integer maxDeepSize;

	/**
	 * Prida se hodnota o stromove struktury
	 * 
	 * @param val
	 *            hodnota stromu
	 */
	public void addFenotype(GPFenotype val) {
		if (!values.contains(val))
			values.add(val);
	}

	@Override
	public Solution randomSolution() {
		if (RAND.nextDouble() <= 0.5) {
			return randomGrowTreeSolution();
		} else {
			return randomGrowTreeSolution();
		}
	}

	/**
	 * Náhodně vygenerovaný strom růstovou metodou. Maximální hloubka stromu se
	 * vezme z parametru maxDeep
	 * 
	 * @return
	 */
	public TreeSolution randomGrowTreeSolution() {
		return randomGrowTreeSolution(getMaxDeepSize());
	}

	/**
	 * Náhodně vygenerovaný strom růstovou metodou. Maximální hloubka stromu je
	 * určena vstupní proměnnou
	 * 
	 * @param deepSize
	 *            maximální hloubka
	 * @return
	 */
	public TreeSolution randomGrowTreeSolution(Integer deepSize) {
		TreeSolution ant = new TreeSolution(deepSize);
		ant.addGenotype(randomGenotype());
		List<Node> list = ant.leaves();
		while (!list.isEmpty()) {
			Node val = list.get(0);
			int deep = ant.deepOf(val);
			if (deep < getMaxDeepSize())
				ant.addGenotype(randomGenotype());
			else
				ant.addGenotype(randomTerminal());
			list = ant.leaves();
		}
		return ant;
	}

	/**
	 * Doplni se chybejici cast stromove struktury tak, aby byl kompletni.
	 * Maximalni hloubka stromu se urcuje z aktualniho stromu
	 * 
	 * @param tree
	 *            stromova struktura
	 * @return
	 */
	public TreeSolution randomGrowTreeSolution(TreeSolution tree) {
		if (tree.getRoot() == null)
			tree.addGenotype(randomGenotype());

		List<Node> list = tree.leaves();
		while (!list.isEmpty()) {
			Node val = list.get(0);
			int deep = tree.deepOf(val);
			if (deep < getMaxDeepSize())
				tree.addGenotype(randomGenotype());
			else
				tree.addGenotype(randomTerminal());
			list = tree.leaves();
		}
		return tree;
	}

	@Override
	public Genotype randomGenotype() {
		if (RAND.nextDouble() <= 0.5)
			return randomFunction();
		else
			return randomTerminal();
	}

	/**
	 * Náhodně vybraná hodnota z množiny terminálů a funkcí
	 * 
	 * @return
	 */
	public GPFenotype randomFenotype() {
		return values.get(RAND.nextInt(values.size()));
	}

	/**
	 * Náhodný terminál
	 * 
	 * @return
	 */
	public GPFenotype randomTerminal() {
		List<GPFenotype> list = new LinkedList<GPFenotype>();
		for (GPFenotype fen : values) {
			if (fen.isTerminal())
				list.add(fen);
		}
		return list.get(RAND.nextInt(list.size()));
	}

	/**
	 * Náhodně vybraná funkce z množiny funkcí
	 * 
	 * @return
	 */
	public GPFenotype randomFunction() {
		List<GPFenotype> list = new LinkedList<GPFenotype>();
		for (GPFenotype fen : values) {
			if (fen.isFunction())
				list.add(fen);
		}
		return list.get(RAND.nextInt(list.size()));
	}

	/**
	 * Vrací všechny fenotypy
	 * 
	 * @return
	 */
	public List<GPFenotype> getFenotypes() {
		return values;
	}

	/**
	 * Nastaví všechny fenotypy
	 * 
	 * @param values
	 */
	public void setFenotypes(List<GPFenotype> values) {
		this.values = values;
	}

	/**
	 * Vrací maximální hloubku stromu
	 * 
	 * @return
	 */
	public Integer getMaxDeepSize() {
		return maxDeepSize;
	}

	/**
	 * Nastavení maximální hloubky stromu
	 * 
	 * @param maxDeepSize
	 */
	public void setMaxDeepSize(Integer maxDeepSize) {
		this.maxDeepSize = maxDeepSize;
	}
}
