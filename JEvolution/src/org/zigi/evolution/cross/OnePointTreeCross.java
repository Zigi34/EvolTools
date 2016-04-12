package org.zigi.evolution.cross;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.solution.value.Node;

public class OnePointTreeCross extends CrossFunction {

	private static final Random RAND = new Random();

	@Override
	public boolean cross(List<Solution> solutions) {
		TreeSolution sol1 = (TreeSolution) solutions.get(0);
		TreeSolution sol2 = (TreeSolution) solutions.get(1);

		List<Node> nodes1 = new LinkedList<Node>();
		List<Node> nodes2 = new LinkedList<Node>();

		int height1 = sol1.height();
		int height2 = sol2.height();

		if (height1 <= 0 || height2 <= 0)
			return false;
		recursive(sol1.getRoot(), sol2.getRoot(), nodes1, nodes2);

		Node cross1Node = nodes1.get(RAND.nextInt(nodes1.size() - 1) + 1);
		Node cross2Node = nodes2.get(RAND.nextInt(nodes2.size() - 1) + 1);

		return TreeSolution.changeSubTree(sol1, sol1.getNodes().indexOf(cross1Node), sol2,
				sol2.getNodes().indexOf(cross2Node));
	}

	private void recursive(Node node1, Node node2, List<Node> list1, List<Node> list2) {
		if (node1 != null && node2 != null) {
			list1.add(node1);
			list2.add(node2);
			int size1 = node1.getMaxChild();
			int size2 = node2.getMaxChild();
			int size = (size1 < size2) ? size1 : size2;
			for (int i = 0; i < size; i++) {
				Node n1 = node1.getChilds().get(i);
				Node n2 = node2.getChilds().get(i);
				recursive(n1, n2, list1, list2);
			}
		} else {
			return;
		}
	}
}
