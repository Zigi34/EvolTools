package org.evolution.solution.type;

import java.util.LinkedList;
import java.util.List;

import org.evolution.util.Cloneable;

public class Node implements Cloneable<Node> {

	private List<Node> childs;
	private Node parent;
	private GPFenotype value;
	private long id;

	/**
	 * Konstruktor pro vytvoření nového uzlu
	 * 
	 * @param fenotype
	 * @param parent
	 * @param list
	 */
	private Node(GPFenotype fenotype) {
		this(fenotype, null);
	}

	/**
	 * Konstruktor pro vytvoření nového uzlu
	 * 
	 * @param val
	 *            hodnota uzlu
	 * @param parent
	 *            nadřazený uzel
	 */
	public Node(GPFenotype val, Node parent) {
		this.childs = new LinkedList<Node>();
		this.value = val;
		this.parent = parent;
		this.id = System.nanoTime();
	}

	/**
	 * Testuje, zda je uzel kompletní (má nastavené všechny podřízené uzly)
	 * 
	 * @return true, pokud jsou nastaveny všechny podřízené uzly
	 */
	public boolean isComplete() {
		return getMaxChild() == childs.size();
	}

	/**
	 * Vložení nového uzlu, pokud není překročen maximální počet podřízených
	 * uzlů tohoto uzlu
	 * 
	 * @param val
	 *            podřízený uzel
	 */
	public void addChild(Node val) {
		if (!isComplete()) {
			childs.add(val);
		}
	}

	public void removeChild(Node node) {
		// odstraneni uzlu z potomka na rodice
		for (Node n : node.getChilds())
			n.setParent(null);

		// odstraneni uzlu z rodice na potomka
		childs.remove(node);
	}

	/**
	 * Vrací podrízené uzly
	 * 
	 * @return
	 */
	public List<Node> getChilds() {
		return childs;
	}

	/**
	 * Vrací maximální počet povolených podřízených uzlů
	 * 
	 * @return
	 */
	public int getMaxChild() {
		return value.getMaxChilds();
	}

	/**
	 * Vrací hodnotu uzlu
	 * 
	 * @return
	 */
	public GPFenotype getValue() {
		return value;
	}

	/**
	 * Nastavení hodnoty uzlu
	 * 
	 * @param value
	 */
	public void setValue(GPFenotype value) {
		this.value = value;
	}

	/**
	 * Vrací nadřazený uzel
	 * 
	 * @return
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Nastavení nadřazeného uzlu
	 * 
	 * @param parent
	 *            odkaz nadřazeného uzlu
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		if (value != null)
			return value.toString();
		return "_";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			Node node = (Node) obj;
			return node.getId() == getId();
		}
		return false;
	}

	public Node cloneMe() {
		return new Node(getValue());
	}

	/**
	 * Zaměňuje vazby dvou uzlů mezi sebou
	 * 
	 * @param node1
	 *            první uzel
	 * @param node2
	 *            druhý uzel
	 */
	public static void change(Node node1, Node node2) {
		Node parent1 = node1.getParent();
		if (parent1 != null) {
			// nahradí odkaz z rodiče na druhý uzel
			int nodeIndex = parent1.getChilds().indexOf(node1);
			parent1.getChilds().set(nodeIndex, node2);
		}

		Node parent2 = node2.getParent();
		if (parent2 != null) {
			// nahradi odkaz z rodiče na první uzel
			int nodeIndex = parent2.getChilds().indexOf(node2);
			parent2.getChilds().set(nodeIndex, node1);
		}

		Node temp = null;
		temp = node1.getParent();

		// záměna nadřazených uzlů
		node1.setParent(node2.getParent());
		node2.setParent(temp);

	}

	/**
	 * Seznam všech uzlů včetně aktuálního procházením do hloubky
	 * 
	 * @return
	 */
	public List<Node> deepNodes() {
		List<Node> list = new LinkedList<Node>();
		deepNodes(list, this);
		return list;
	}

	/**
	 * Prochazeni stromu do hloubky. Po pruchodu kazdym uzlem se nejprve
	 * aktualni uzel vlozi do seznamu a potom se rekurzivne prochazeji potomci.
	 * rekurzivni volani
	 * 
	 * @param list
	 *            seznam s uzly
	 * @param actual
	 *            aktualne prochazeny uzel
	 */
	private void deepNodes(List<Node> list, Node actual) {
		if (actual != null && !list.contains(actual)) {
			list.add(actual);
			for (Node val : actual.getChilds())
				deepNodes(list, val);
		}
	}

	/**
	 * Vraci výšku stromu
	 * 
	 * @return
	 */
	public int height() {
		return height(this, 1);
	}

	/**
	 * Rekurzivne prochazi potomky uzlu do sirky a pokazdem pruchodu do potomka
	 * se zvysi výška podstromu o 1
	 * 
	 * @param node
	 *            aktualne navstiveny uzel
	 * @param maxHeight
	 *            aktualně největší výška větve v podstromě
	 */
	private int height(Node node, int maxHeight) {
		int max = maxHeight;
		if (node != null && node.getChilds().size() > 0) {
			for (Node subNode : node.getChilds()) {
				int deep = height(subNode, maxHeight + 1);
				if (deep > max)
					max = deep;
			}
		}
		return max;
	}

	/**
	 * Vypocet hloubky uzlu. Root uzel ma hloubku 0
	 * 
	 * @param node
	 *            uzel
	 * @return
	 */
	public int deepOf() {
		int deep = 1;
		Node actualNode = this;
		while (actualNode.getParent() != null) {
			actualNode = actualNode.getParent();
			deep++;
		}
		return deep;
	}

	public long getId() {
		return id;
	}

	/**
	 * Testing for leaf
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		if (getChilds().isEmpty())
			return true;
		return false;
	}
}
