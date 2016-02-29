package org.zigi.evolution.problem;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.solution.value.Node;

public abstract class TreeProblem extends Problem {

	private List<GPFenotype> values = new LinkedList<GPFenotype>();
	private static final Random RAND = new Random();
	private Integer maxHeight = 3;

	/**
	 * Prida se hodnota do stromove struktury
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
			return randomGrowTreeSolution(getMaxHeight());
		} else {
			return randomFullTreeSolution(getMaxHeight());
		}
	}

	/**
	 * Náhodně vygenerovaný strom růstovou metodou. Maximální hloubka stromu se
	 * vezme z parametru maxDeep
	 * 
	 * @return
	 */
	public TreeSolution randomGrowTreeSolution() {
		return randomGrowTreeSolution(getMaxHeight());
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
		List<Node> list = ant.uncompleteNodes();
		while (!list.isEmpty()) {
			Node val = list.get(0);
			int deep = ant.deepOf(val);
			if (deep < getMaxHeight())
				ant.addGenotype(randomGenotype());
			else
				ant.addGenotype(randomTerminal());
			list = ant.uncompleteNodes();
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

		List<Node> list = tree.uncompleteNodes();
		while (!list.isEmpty()) {
			Node val = list.get(0);
			int deep = tree.deepOf(val);
			if (deep < getMaxHeight())
				tree.addGenotype(randomGenotype());
			else
				tree.addGenotype(randomTerminal());
			list = tree.uncompleteNodes();
		}
		return tree;
	}

	/**
	 * Náhodně generovaný strom podle úplné metody
	 * 
	 * @param tree
	 * @return
	 */
	public TreeSolution randomFullTreeSolution(TreeSolution tree) {
		if (tree.getRoot() == null)
			tree.addGenotype(randomFunction());

		List<Node> list = tree.uncompleteNodes();
		while (!list.isEmpty()) {
			Node val = list.get(0);
			int deep = tree.deepOf(val);
			if (deep < getMaxHeight())
				tree.addGenotype(randomFunction());
			else
				tree.addGenotype(randomTerminal());
			list = tree.uncompleteNodes();
		}
		return tree;
	}

	/**
	 * Vygeneruje náhodně strom úplnou metodou bez omezení na výšce stromu
	 * 
	 * @return
	 */
	public TreeSolution randomFullTreeSolution() {
		return randomFullTreeSolution(getMaxHeight());
	}

	/**
	 * Vegeneruje náhodně strom úplnou metodou s omezením na výšku stromu podle
	 * parametru
	 * 
	 * @param deepSize
	 * @return
	 */
	public TreeSolution randomFullTreeSolution(Integer deepSize) {
		TreeSolution ant = new TreeSolution(deepSize);
		ant.addGenotype(randomGenotype());
		List<Node> list = ant.uncompleteNodes();
		while (!list.isEmpty()) {
			Node val = list.get(0);
			int deep = ant.deepOf(val);
			if (deep < getMaxHeight())
				ant.addGenotype(randomGenotype());
			else
				ant.addGenotype(randomTerminal());
			list = ant.uncompleteNodes();
		}
		return ant;
	}

	@Override
	public GPFenotype randomGenotype() {
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
			if (!fen.isTerminal())
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
	 * Vrací maximální povolenou výšku stromu
	 * 
	 * @return
	 */
	public Integer getMaxHeight() {
		return maxHeight;
	}

	/**
	 * Nastavení maximální výsky stromu
	 * 
	 * @param maxHeight
	 */
	public void setMaxHeight(Integer maxHeight) {
		this.maxHeight = maxHeight;
	}
}
