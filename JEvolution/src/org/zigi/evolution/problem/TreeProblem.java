package org.zigi.evolution.problem;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.solution.value.Node;

public abstract class TreeProblem extends Problem {

	private List<GPFenotype> terminals = new LinkedList<GPFenotype>();
	private List<GPFenotype> functions = new LinkedList<GPFenotype>();
	private static final Random RAND = new Random();
	private Integer maxHeight = 3;

	private static final Logger LOG = Logger.getLogger(TreeProblem.class);

	/**
	 * Prida se hodnota do stromove struktury
	 * 
	 * @param val
	 *            hodnota stromu
	 */
	public void addFenotype(GPFenotype val) {
		if (val.isTerminal() && !terminals.contains(val))
			terminals.add(val);
		else if (!val.isTerminal() && !functions.contains(val))
			functions.add(val);
	}

	public List<GPFenotype> getFenotypes() {
		List<GPFenotype> list = new LinkedList<GPFenotype>();
		list.addAll(terminals);
		list.addAll(functions);
		return list;
	}

	public List<GPFenotype> getTerminalFenotypes() {
		return terminals;
	}

	public List<GPFenotype> getFunctionFenotypes() {
		return functions;
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
		int maxHeight = getMaxHeight();
		ant.addGenotype(randomFunction());
		List<Node> list = ant.uncompleteNodes();
		while (!list.isEmpty()) {
			Node val = list.get(0);
			int deep = ant.deepOf(val);
			if (deep + 1 < maxHeight)
				ant.addGenotype(randomFunction());
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
		if (RAND.nextDouble() > 0.5)
			return terminals.get(RAND.nextInt(terminals.size())).cloneMe();
		else
			return functions.get(RAND.nextInt(functions.size())).cloneMe();
	}

	/**
	 * Náhodný terminál
	 * 
	 * @return
	 */
	public GPFenotype randomTerminal() {
		return terminals.get(RAND.nextInt(terminals.size())).cloneMe();
	}

	/**
	 * Náhodně vybraná funkce z množiny funkcí
	 * 
	 * @return
	 */
	public GPFenotype randomFunction() {
		return functions.get(RAND.nextInt(functions.size())).cloneMe();
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
