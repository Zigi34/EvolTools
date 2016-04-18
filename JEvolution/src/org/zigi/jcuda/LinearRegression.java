package org.zigi.jcuda;

public class LinearRegression {

	public static int findReducePoint(NodeStruct[] source, int size) {
		int actual = 0;
		int child = source[actual].child;

		if (source[actual].type == NodeStruct.TERMINAL)
			return actual;

		for (int i = 1; i < size; i++) {
			if (source[i].type == NodeStruct.TERMINAL)
				child++;
			else if (source[i].type == NodeStruct.NETERMINAL) {
				child = 0;
				actual = i;
			} else
				continue;

			if (child == source[actual].child)
				return actual;
		}
		return -1;
	}

	public static void reduce(NodeStruct[] source, int size, int index, int[] args) {
		NodeStruct n = source[index];
		int pos = 0;
		for (int i = index + 1; i < size; i++) {
			if (source[i].type == NodeStruct.TERMINAL)
				args[pos++] = i;
		}

		if (n.model == NodeStruct.PLUS) {
			n.value = source[args[0]].value + source[args[1]].value;
			source[args[0]].type = NodeStruct.NONE;
			source[args[1]].type = NodeStruct.NONE;
		} else if (n.model == NodeStruct.MINUS) {
			n.value = source[args[0]].value - source[args[1]].value;
			source[args[0]].type = NodeStruct.NONE;
			source[args[1]].type = NodeStruct.NONE;
		} else if (n.model == NodeStruct.KRAT) {
			n.value = source[args[0]].value * source[args[1]].value;
			source[args[0]].type = NodeStruct.NONE;
			source[args[1]].type = NodeStruct.NONE;
		} else if (n.model == NodeStruct.DELENO) {
			n.value = source[args[0]].value / source[args[1]].value;
			source[args[0]].type = NodeStruct.NONE;
			source[args[1]].type = NodeStruct.NONE;
		} else if (n.model == NodeStruct.SIN) {
			n.value = Math.sin(source[args[0]].value);
			source[args[0]].type = NodeStruct.NONE;
		} else if (n.model == NodeStruct.COS) {
			n.value = Math.cos(source[args[0]].value);
			source[args[0]].type = NodeStruct.NONE;
		} else if (n.model == NodeStruct.POWER) {
			n.value = Math.pow(source[args[0]].value, 2.0);
			source[args[0]].type = NodeStruct.NONE;
		}
		n.child = 0;
		n.model = NodeStruct.VALUE;
		n.type = NodeStruct.TERMINAL;
	}

	public static void main(String[] args) {
		NodeStruct[] nodes = new NodeStruct[10];
		nodes[0] = new NodeStruct(NodeStruct.TERMINAL, NodeStruct.VALUE, (byte) 0, 12.0);
		System.out.println("1:" + findReducePoint(nodes, 1));

		NodeStruct[] nodes1 = new NodeStruct[10];
		nodes1[0] = new NodeStruct(NodeStruct.NETERMINAL, NodeStruct.COS, (byte) 1, 0.0);
		nodes1[1] = new NodeStruct(NodeStruct.NETERMINAL, NodeStruct.PLUS, (byte) 2, 0.0);
		nodes1[2] = new NodeStruct(NodeStruct.TERMINAL, NodeStruct.VALUE, (byte) 0, 180.0);
		nodes1[3] = new NodeStruct(NodeStruct.NETERMINAL, NodeStruct.DELENO, (byte) 2, 0.0);
		nodes1[4] = new NodeStruct(NodeStruct.TERMINAL, NodeStruct.VALUE, (byte) 0, 12.0);
		nodes1[5] = new NodeStruct(NodeStruct.NETERMINAL, NodeStruct.POWER, (byte) 1, 0.0);
		nodes1[6] = new NodeStruct(NodeStruct.TERMINAL, NodeStruct.VALUE, (byte) 0, 5.0);
		System.out.println("2:" + findReducePoint(nodes1, 7));

		NodeStruct[] nodes2 = new NodeStruct[10];
		nodes2[0] = new NodeStruct(NodeStruct.NETERMINAL, NodeStruct.KRAT, (byte) 2, 0.0);
		nodes2[1] = new NodeStruct(NodeStruct.NETERMINAL, NodeStruct.MINUS, (byte) 2, 0.0);
		nodes2[2] = new NodeStruct(NodeStruct.NETERMINAL, NodeStruct.SIN, (byte) 1, 0.0);
		nodes2[3] = new NodeStruct(NodeStruct.TERMINAL, NodeStruct.VALUE, (byte) 0, 2.0);
		nodes2[4] = new NodeStruct(NodeStruct.TERMINAL, NodeStruct.VALUE, (byte) 0, 69.0);
		nodes2[5] = new NodeStruct(NodeStruct.NETERMINAL, NodeStruct.MINUS, (byte) 2, 0.0);
		nodes2[6] = new NodeStruct(NodeStruct.TERMINAL, NodeStruct.VALUE, (byte) 0, 13.0);
		nodes2[7] = new NodeStruct(NodeStruct.TERMINAL, NodeStruct.VALUE, (byte) 0, 5.0);
		System.out.println("3:" + findReducePoint(nodes2, 8));
	}
}
