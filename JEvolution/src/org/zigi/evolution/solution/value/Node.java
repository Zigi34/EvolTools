package org.zigi.evolution.solution.value;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Node implements Cloneable<Node> {

	private List<Node> childs = new LinkedList<Node>();
	private Node parent;
	private GPFenotype value;
	private String id;

	/**
	 * Konstruktor pro vytvoření nového uzlu
	 * 
	 * @param fenotype
	 * @param parent
	 * @param uniqId
	 * @param list
	 */
	private Node(GPFenotype fenotype, Node parent, String uniqId, List<Node> list) {
		this.id = uniqId;
		this.parent = parent;
		this.childs.addAll(list);
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
		this.value = val;
		this.parent = parent;
		this.id = UUID.randomUUID().toString();
	}

	/**
	 * Testuje, zda je uzel kompletní (má nastavené všechny podřízené uzly)
	 * 
	 * @return true, pokud jsou nastaveny všechny podřízené uzly
	 */
	public boolean isComplete() {
		return childs.size() == value.getMaxChilds();
	}

	/**
	 * Vložení nového uzlu, pokud není překročen maximální počet podřízených
	 * uzlů tohoto uzlu
	 * 
	 * @param val
	 *            podřízený uzel
	 */
	public void addChild(Node val) {
		if (childs.size() < getMaxChild())
			childs.add(val);
	}

	public void removeChild(Node node) {
		if (childs.contains(node)) {
			// odstraneni uzlu z potomka na rodice
			node.setParent(null);
			// odstraneni uzlu z rodice na potomka
			childs.remove(node);
		}
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
	 * Nastavení podřízených uzlů
	 * 
	 * @param childs
	 */
	public void setChilds(List<Node> childs) {
		this.childs = childs;
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
			return getId() == node.getId();
		}
		return false;
	}

	/**
	 * Vrací jedinečný identifikátor uzlu
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	public Node cloneMe() {
		return new Node(getValue(), getParent(), UUID.randomUUID().toString(), getChilds());
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
			parent1.getChilds().set(parent1.getChilds().indexOf(node1), node2);
		}

		Node parent2 = node2.getParent();
		if (parent2 != null) {
			// nahradi odkaz z rodiče na první uzel
			parent2.getChilds().set(parent2.getChilds().indexOf(node2), node1);
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
}
