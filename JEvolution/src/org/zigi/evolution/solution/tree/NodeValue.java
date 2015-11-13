package org.zigi.evolution.solution.tree;

import java.util.List;

import org.apache.log4j.Logger;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Valuable;

public abstract class NodeValue extends CloneableValue<NodeValue> implements Valuable<Object> {
	protected String name;
	private NodeValue parent;
	private static final Logger LOG = Logger.getLogger(NodeValue.class);

	public abstract Boolean isTerminal();

	public abstract Boolean isNonTerminal();

	/**
	 * Vrai hodnotu instance vypoctenou z hodnot potomku nebo vykona vsechny
	 * operace v potomcich a potom vraci rizeni tomuto uzlu
	 */
	public abstract Object getValue();

	public String getName() {
		return name;
	}

	/**
	 * Pripoji noveho potomka k tomuto uzlu za predpokladu, ze nebyl prekrocen
	 * limitni pocet potomku a za predpokladu, ze potomci jsou odpovidajiciho
	 * validniho typu
	 * 
	 * @param node
	 */
	public void addChild(NodeValue node) {
		if (getChilds().size() < getChildCount()) {
			Integer index = getChilds().size();
			if (getChildTypes().get(index).isAssignableFrom(node.getResultType())) {
				node.setParent(this);
				getChilds().add(node);
			} else {
				LOG.error("Neni pripustny typ!");
			}
		}
	}

	/**
	 * Vraci instance vsech potomku
	 * 
	 * @return
	 */
	public abstract List<NodeValue> getChilds();

	/**
	 * Vraci rodicovky uzel
	 * 
	 * @return
	 */
	public NodeValue getParent() {
		return parent;
	}

	/**
	 * Nastavi rodicovsky uzel
	 * 
	 * @param parent
	 */
	public void setParent(NodeValue parent) {
		this.parent = parent;
	}

	/**
	 * Vraci pocet moznych potomku k tomuto uzlu
	 * 
	 * @return
	 */
	public abstract Integer getChildCount();

	/**
	 * Vraci typy vysledku
	 * 
	 * @return
	 */
	public abstract Class<?> getResultType();

	/**
	 * Vraci typy potomku
	 * 
	 * @return
	 */
	public abstract List<Class<?>> getChildTypes();

	public Boolean isAnyEmptyChild() {
		return getChilds().size() < getChildCount();
	}

	public Integer getDeep(TreeSolution tree) {
		Integer deep = 1;
		NodeValue node = this;
		while (node.getParent() != null) {
			deep++;
			node = node.getParent();
		}
		return deep;
	}
}
