package org.zigi.evolution.solution;

import java.util.LinkedList;
import java.util.List;

import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.solution.value.Node;
import org.zigi.evolution.util.Util;

public class TreeSolution extends Solution {
	private Node root;
	private List<Node> nodes = new LinkedList<Node>();
	private Integer maxDepth;

	public TreeSolution(Integer maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * Konstruktor. Maximalni hloubka stromu se nenastavuje, takze nebude
	 * omezena
	 */
	public TreeSolution() {
		this(null);
	}

	public Integer getMaxHeight() {
		return maxDepth;
	}

	/**
	 * Vraci prvni nekompletni uzel ve stromove strukture tak, ze se strom
	 * prochazi od listovych uzlu smerem ke korenu
	 * 
	 * @return
	 */
	private Node nextUncompleteNode() {
		List<Node> leaves = uncompleteNodes();
		if (leaves != null && !leaves.isEmpty())
			return leaves.get(0);
		return null;
	}

	/**
	 * Vlozi noveho potomka prvnimu nekompletnimu uzlu ve strome. Pokud je
	 * korenovy uzel prazdny, nastavi jej. Prvni nekompletni uzel se bere od
	 * listovych uzlu smerem ke korenu a jedna se o uzly, ktere nemaji doplnene
	 * vsechny vetve
	 * 
	 * @param node
	 *            uzel stromu
	 */
	public void addNode(Node node) {
		if (root == null) {
			root = node;
			nodes.add(node);
		} else {
			Node uncompleteNode = nextUncompleteNode();
			if (uncompleteNode != null) {
				// zjištění aktuální hloubky uzlu
				int actualDeep = deepOf(uncompleteNode);

				// není aktuální uzel již v maximální hloubce
				if (maxDepth != null && actualDeep < maxDepth) {

					// pokud se jedna o poslední přidání uzlu, musí tento uzel
					// být terminálem, jinak se vložení nepodaří
					if (actualDeep + 1 == maxDepth && node.getMaxChild() > 0)
						return;

					// vložení uzlu
					uncompleteNode.addChild(node);
					nodes.add(node);
				}
			}
		}
	}

	@Override
	public void addGenotype(GPFenotype genotype) {
		addNode(new Node(genotype, nextUncompleteNode()));
	}

	/**
	 * Test na prazdny strom. Pokud je koren prazdny, je strom take prazdny.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * Prochazeni stromu do hloubky. Po pruchodu kazdym uzlem se nejprve
	 * aktualni uzel vlozi do seznamu a potom se rekurzivne prochazeji potomci
	 * 
	 * @return
	 */
	public List<Node> deepNodes() {
		List<Node> list = new LinkedList<Node>();
		deepNodes(list, root);
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
	 * Odstraneni podstromu od nejakeho uzlu včetne tohoto uzlu a vraceni
	 * rodicoveskeho uzlu
	 * 
	 * @param node
	 * @return
	 */
	public Node removeSubTree(Node node) {
		Node result = null;
		if (nodes.contains(node)) {
			List<Node> subNodes = node.deepNodes();

			// pokud jde o odstraneni celeho korene
			if (node == root) {
				root = null;
			} else {
				// preskocime na nadrazeny uzel a odstranime tohoto potomka
				result = node.getParent();
				result.removeChild(node);
			}
			nodes.removeAll(subNodes);
		}
		return result;
	}

	/**
	 * Prochazeni stromu do hloubky smerem od listovych uzlu po koren. V seznamu
	 * se vraci pouze nekompletni uzly.
	 * 
	 * @return seznam nekompletnich uzlu
	 */
	public List<Node> uncompleteNodes() {
		List<Node> list = new LinkedList<Node>();
		uncompleteNodes(list, root);
		return list;
	}

	/**
	 * Prochazeni stromu do hloubky smerem od listovych uzlu po koren. V seznamu
	 * se vraci pouze nekompletni uzly. Rekurzivni varianta.
	 * 
	 * @param list
	 *            seznam uzlu nekompletnich uzlů
	 * @param actual
	 *            aktualne prochazeny uzel
	 */
	private void uncompleteNodes(List<Node> list, Node actual) {
		if (actual != null) {
			for (Node val : actual.getChilds()) {
				uncompleteNodes(list, val);
			}
			if (!actual.isComplete()) {
				list.add(actual);
			}
		}
	}

	/**
	 * Returning all leaves
	 * 
	 * @return
	 */
	public List<Node> leaveNodes() {
		List<Node> leaves = new LinkedList<>();
		leaveNodes(leaves, root);
		return leaves;
	}

	/**
	 * Recursice function for get all leaves in tree
	 * 
	 * @param list
	 * @param actual
	 */
	private void leaveNodes(List<Node> list, Node actual) {
		if (actual != null) {
			List<Node> childs = actual.getChilds();
			if (childs == null || childs.size() == 0)
				list.add(actual);
			else
				for (Node val : actual.getChilds()) {
					leaveNodes(list, val);
				}
		}
	}

	/**
	 * Hloubka uzlu
	 * 
	 * @param node
	 * @return
	 */
	public int deepOf(Node node) {
		return node.deepOf();
	}

	/**
	 * Vytvari kopii reseni
	 */
	public Solution cloneMe() {
		TreeSolution tree = new TreeSolution(this.maxDepth);
		tree.setFitness(new Double(getFitness()));
		List<Node> nodes = deepNodes();
		for (Node node : nodes) {
			tree.addGenotype(node.getValue().cloneMe());
		}
		return tree;
	}

	@Override
	public GPFenotype getGenotype(Integer index) {
		return nodes.get(index).getValue();
	}

	public List<GPFenotype> getGenotypes() {
		List<GPFenotype> list = new LinkedList<GPFenotype>();
		for (Node node : nodes) {
			list.add(node.getValue());
		}
		return list;
	}

	@Override
	public void setGenotypes(List<GPFenotype> vals) {
		vals.clear();
		for (GPFenotype gen : vals) {
			addGenotype(gen);
		}
	}

	@Override
	public int size() {
		return nodes.size();
	}

	@Override
	public void setGenotype(int index, GPFenotype genotype) {
		// nelze nastavit
	}

	/**
	 * Vraci uzel na danem indexu podle seznamu vsech uzlu
	 * 
	 * @param index
	 *            index do seznamu uzlů
	 * @return
	 */
	public Node getNode(int index) {
		return nodes.get(index);
	}

	/**
	 * Vraci seznam všech uzlů ve stromě
	 * 
	 * @return
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * Nastaveni korenoveho uzlu
	 * 
	 * @param node
	 */
	public void setRoot(Node node) {
		this.root = node;
	}

	/**
	 * Vraci korenovy uzel
	 * 
	 * @return
	 */
	public Node getRoot() {
		return root;
	}

	/**
	 * Zamění dva uzly a jejich podřízené uzly mezi dvěma stromy. Nekontroluje
	 * se maximální povolená hloubka
	 * 
	 * @param sol1
	 *            první uzel
	 * @param index1
	 *            index uzlu v prvním stromě
	 * @param sol2
	 *            druhý uzel
	 * @param index2
	 *            index uzlu ve druhém stromě
	 */
	public static boolean changeSubTree(TreeSolution sol1, int index1, TreeSolution sol2, int index2) {
		Node node1 = sol1.getNode(index1);
		Node node2 = sol2.getNode(index2);
		int finalHeight1 = node1.deepOf() + node2.height();
		int finalHeight2 = node2.height() + node1.height();

		// pokud by byla prekrocena vyska stromu, neprovadime nic
		if (finalHeight1 > sol1.getMaxHeight() || finalHeight2 > sol2.getMaxHeight())
			return false;

		List<Node> nodes1 = node1.deepNodes();
		List<Node> nodes2 = node2.deepNodes();

		if (index1 == 0)
			sol1.setRoot(node2);
		if (index2 == 0)
			sol2.setRoot(node1);

		// záměna provázání mezi uzly
		Node.change(node1, node2);

		// odstraní uzly z prvního a vloží uzly z druhého
		for (Node node : nodes1) {
			sol1.getNodes().remove(node);
		}
		sol2.getNodes().addAll(nodes2);

		// odstrani uzly z druhého stromu a vloží uzly z prvního
		for (Node node : nodes2) {
			sol2.getNodes().remove(node);
		}
		sol1.getNodes().addAll(nodes1);

		return true;
	}

	/**
	 * Vraci výšku stromu
	 * 
	 * @return
	 */
	public int height() {
		return root.height();
	}

	public int height(Node node) {
		return node.height();
	}

	/**
	 * Rekurzivni tisk uzlů
	 * 
	 * @param sb
	 * @param actual
	 */
	private void printTree(StringBuilder sb, Node actual) {
		sb.append(actual);
		if (actual.getMaxChild() > 0)
			sb.append("(");
		List<Node> nodes = actual.getChilds();
		for (int i = 0; i < nodes.size(); i++) {
			printTree(sb, nodes.get(i));
			if (i < nodes.size() - 1)
				sb.append(",");
		}
		if (actual.getMaxChild() > 0)
			sb.append(")");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (getFitness() == null)
			sb.append("NULL");
		else
			sb.append(Util.formatNumber(getFitness()));
		sb.append("]");
		printTree(sb, root);
		return sb.toString();
	}

	@Override
	public String getGenotypeString() {
		StringBuilder sb = new StringBuilder();
		printTree(sb, root);
		return sb.toString();
	}
}
