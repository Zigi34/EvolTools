package org.zigi.jcuda;

public class LinearRegression {

	public static int findReducePoint(Node[] source, int size) {
		int actual = 0;
		int child = source[actual].child;

		if (source[actual].type == Node.TERMINAL)
			return actual;

		for (int i = 1; i < size; i++) {
			if (source[i].type == Node.TERMINAL)
				child++;
			else if (source[i].type == Node.NETERMINAL) {
				child = 0;
				actual = i;
			} else
				continue;

			if (child == source[actual].child)
				return actual;
		}
		return -1;
	}

	public static void reduce(Node[] source, int size, int index, int[] args) {
		Node n = source[index];
		int pos = 0;
		for (int i = index + 1; i < size; i++) {
			if (source[i].type == Node.TERMINAL)
				args[pos++] = i;
		}

		if (n.model == Node.PLUS) {
			n.value = source[args[0]].value + source[args[1]].value;
			source[args[0]].type = Node.NONE;
			source[args[1]].type = Node.NONE;
		} else if (n.model == Node.MINUS) {
			n.value = source[args[0]].value - source[args[1]].value;
			source[args[0]].type = Node.NONE;
			source[args[1]].type = Node.NONE;
		} else if (n.model == Node.KRAT) {
			n.value = source[args[0]].value * source[args[1]].value;
			source[args[0]].type = Node.NONE;
			source[args[1]].type = Node.NONE;
		} else if (n.model == Node.DELENO) {
			n.value = source[args[0]].value / source[args[1]].value;
			source[args[0]].type = Node.NONE;
			source[args[1]].type = Node.NONE;
		} else if (n.model == Node.SIN) {
			n.value = Math.sin(source[args[0]].value);
			source[args[0]].type = Node.NONE;
		} else if (n.model == Node.COS) {
			n.value = Math.cos(source[args[0]].value);
			source[args[0]].type = Node.NONE;
		} else if (n.model == Node.POWER) {
			n.value = Math.pow(source[args[0]].value, 2.0);
			source[args[0]].type = Node.NONE;
		}
		n.child = 0;
		n.model = Node.VALUE;
		n.type = Node.TERMINAL;
	}

	public static void main(String[] args) {
		Node[] nodes = new Node[10];
		nodes[0] = new Node(Node.TERMINAL, Node.VALUE, (byte) 0, 12.0);
		System.out.println("1:" + findReducePoint(nodes, 1));

		Node[] nodes1 = new Node[10];
		nodes1[0] = new Node(Node.NETERMINAL, Node.COS, (byte) 1, 0.0);
		nodes1[1] = new Node(Node.NETERMINAL, Node.PLUS, (byte) 2, 0.0);
		nodes1[2] = new Node(Node.TERMINAL, Node.VALUE, (byte) 0, 180.0);
		nodes1[3] = new Node(Node.NETERMINAL, Node.DELENO, (byte) 2, 0.0);
		nodes1[4] = new Node(Node.TERMINAL, Node.VALUE, (byte) 0, 12.0);
		nodes1[5] = new Node(Node.NETERMINAL, Node.POWER, (byte) 1, 0.0);
		nodes1[6] = new Node(Node.TERMINAL, Node.VALUE, (byte) 0, 5.0);
		System.out.println("2:" + findReducePoint(nodes1, 7));

		Node[] nodes2 = new Node[10];
		nodes2[0] = new Node(Node.NETERMINAL, Node.KRAT, (byte) 2, 0.0);
		nodes2[1] = new Node(Node.NETERMINAL, Node.MINUS, (byte) 2, 0.0);
		nodes2[2] = new Node(Node.NETERMINAL, Node.SIN, (byte) 1, 0.0);
		nodes2[3] = new Node(Node.TERMINAL, Node.VALUE, (byte) 0, 2.0);
		nodes2[4] = new Node(Node.TERMINAL, Node.VALUE, (byte) 0, 69.0);
		nodes2[5] = new Node(Node.NETERMINAL, Node.MINUS, (byte) 2, 0.0);
		nodes2[6] = new Node(Node.TERMINAL, Node.VALUE, (byte) 0, 13.0);
		nodes2[7] = new Node(Node.TERMINAL, Node.VALUE, (byte) 0, 5.0);
		System.out.println("3:" + findReducePoint(nodes2, 8));
	}
}
