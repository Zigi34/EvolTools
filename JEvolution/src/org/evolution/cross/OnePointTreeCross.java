package org.evolution.cross;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.evolution.solution.TreeSolution;
import org.evolution.solution.type.Node;
import org.evolution.util.Population;

public class OnePointTreeCross extends CrossFunction {

	private static final Random RAND = new Random();

	@Override
	public void cross(Population solutions, long offset, long size) {
		for (long i = offset; i < size; i += 2) {
			TreeSolution sol1 = (TreeSolution) solutions.get((int) i);
			TreeSolution sol2 = (TreeSolution) solutions.get((int) i + 1);

			List<Node> nodes1 = new LinkedList<Node>();
			List<Node> nodes2 = new LinkedList<Node>();

			int height1 = sol1.height();
			int height2 = sol2.height();

			if (height1 == 1 && height2 == 1)
				continue;
			recursive(sol1.getRoot(), sol2.getRoot(), nodes1, nodes2);

			Node cross1Node = nodes1.get(RAND.nextInt(nodes1.size()));
			Node cross2Node = nodes2.get(RAND.nextInt(nodes2.size()));

			TreeSolution.changeSubTree(sol1, sol1.getNodes().indexOf(cross1Node), sol2,
					sol2.getNodes().indexOf(cross2Node));
		}
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

	@Override
	public String toString() {
		return "One point cross";
	}
}
