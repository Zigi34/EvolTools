package org.zigi.evolution.evaluate;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zigi.evolution.Population;
import org.zigi.evolution.solution.tree.IfFootAhead;
import org.zigi.evolution.solution.tree.Move;
import org.zigi.evolution.solution.tree.NodeValue;
import org.zigi.evolution.solution.tree.PRG2;
import org.zigi.evolution.solution.tree.TreeSolution;
import org.zigi.evolution.solution.tree.TurnLeft;
import org.zigi.evolution.solution.tree.TurnRight;

public class ArtificialAnt extends EvaluateFunction<TreeSolution, NodeValue> {

	private Integer[][] array;
	private Integer x = 0;
	private Integer y = 0;
	private Integer width = 0;
	private Integer height = 0;
	private Integer baitCount = 0;
	private Integer maxOperation = 0;
	private Direction direction = Direction.RIGHT;
	private static final Logger LOG = Logger.getLogger(ArtificialAnt.class);

	public enum Direction {
		RIGHT, DOWN, LEFT, UP;
	};

	private Direction turnLeft(Direction dir) {
		if (dir == Direction.DOWN)
			return Direction.RIGHT;
		else if (dir == Direction.RIGHT)
			return Direction.UP;
		else if (dir == Direction.LEFT)
			return Direction.DOWN;
		else
			return Direction.LEFT;
	}

	private Direction turnRight(Direction dir) {
		if (dir == Direction.DOWN)
			return Direction.LEFT;
		else if (dir == Direction.RIGHT)
			return Direction.DOWN;
		else if (dir == Direction.LEFT)
			return Direction.UP;
		else
			return Direction.RIGHT;
	}

	private boolean move() {
		if (direction == Direction.DOWN)
			x = (x + 1) % height;
		else if (direction == Direction.LEFT)
			y = (y - 1 + width) % width;
		else if (direction == Direction.RIGHT)
			y = (y + 1) % width;
		else
			x = (x - 1 + height) % height;

		if (array[x][y] == 1) {
			array[x][y] = 0;
			return true;
		}
		return false;
	}

	public void setArray(Integer width, Integer height, Integer[] bait) {
		array = new Integer[width][height];
		for (Integer i = 0; i < bait.length; i += 2) {
			array[bait[i]][bait[i + 1]] = 1;
			baitCount++;
		}
	}

	private void printList(List<NodeValue> list) {
		StringBuilder sb = new StringBuilder();
		for (NodeValue val : list) {
			sb.append(val + ", ");
		}
		LOG.info(sb.toString());
	}

	private boolean isFootAHead() {
		if (direction == Direction.DOWN && array[(x + 1) % height][y] == 1)
			return true;
		else if (direction == Direction.LEFT && array[x][(y - 1 + width) % width] == 1)
			return true;
		else if (direction == Direction.RIGHT && array[x][(y + 1) % width] == 1)
			return true;
		else if (direction == Direction.UP && array[(x - 1 + height) % height][y] == 1)
			return true;
		return false;

	}

	@Override
	public Double evaluate(TreeSolution solution) {
		Double baitCount = 0.0;
		Integer moves = 0;

		LOG.info("OPERATIONS");
		while (moves < maxOperation) {
			List<NodeValue> nodes = new LinkedList<NodeValue>();
			nodes.add(solution.getRoot());

			while (!nodes.isEmpty()) {
				NodeValue node = nodes.remove(0);
				printActalState();
				if (node instanceof TurnLeft) {
					direction = turnLeft(direction);
				} else if (node instanceof TurnRight) {
					direction = turnRight(direction);
				} else if (node instanceof Move) {
					if (move())
						baitCount += 1.0;
				} else if (node instanceof PRG2) {
					PRG2 n = (PRG2) node;
					nodes.addAll(n.getChilds());
					continue;
				} else if (node instanceof IfFootAhead) {
					if (isFootAHead()) {
						IfFootAhead n = (IfFootAhead) node;
						nodes.add(n.getChilds().get(0));
					} else {
						IfFootAhead n = (IfFootAhead) node;
						nodes.add(n.getChilds().get(1));
					}
					continue;
				}
				moves++;
			}
		}
		return baitCount / this.baitCount;
	}

	private void printActalState() {
		LOG.info(String.format("[%s][%s], %s", x, y, direction));
	}

	@Override
	public void evaluate(Population<TreeSolution, NodeValue> population) {

	}

	public Integer[][] getArray() {
		return array;
	}

	public void setArray(Integer[][] array) {
		this.array = array;
		this.width = array.length;
		this.height = array[0].length;
		this.baitCount = 0;
		for (int x = 0; x < array.length; x++)
			for (int y = 0; y < array[x].length; y++)
				if (array[x][y] == 1)
					this.baitCount++;
		this.direction = Direction.RIGHT;
		this.x = 0;
		this.y = 0;
	}

	public Integer getMaxOperation() {
		return maxOperation;
	}

	public void setMaxOperation(Integer maxOperation) {
		this.maxOperation = maxOperation;
	}
}
