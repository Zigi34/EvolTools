package org.zigi.evolution.problem;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.solution.value.IfFoodAhead;
import org.zigi.evolution.solution.value.LeftDirection;
import org.zigi.evolution.solution.value.Move;
import org.zigi.evolution.solution.value.Node;
import org.zigi.evolution.solution.value.Prg2;
import org.zigi.evolution.solution.value.RightDirection;
import org.zigi.evolution.util.Population;

public class ArtificialAnt extends TreeProblem {

	private int[][] array;
	private int bitCount = 0;

	private int posX = 0;
	private int posY = 0;
	private int width = 0;
	private int height = 0;

	private int maxMoves = 400;

	private Direction direction = Direction.RIGHT;
	private static final Random RAND = new Random();

	private enum Direction {
		LEFT, TOP, RIGHT, DOWN
	}

	public ArtificialAnt() {
		super();

		// add functions
		addFenotype(new Prg2());
		addFenotype(new IfFoodAhead());

		// add terminals
		addFenotype(new Move());
		addFenotype(new LeftDirection());
		addFenotype(new RightDirection());

		// max deep size
		setMaxDeepSize(9);
	}

	public void setArray(int width, int height, int[] food) {
		this.bitCount = food.length;
		this.width = width;
		this.height = height;
		this.array = new int[height][width];
		for (int i = 0; i < food.length; i++)
			array[food[i] / width][food[i] % width] = 1;
	}

	public int[][] getArray() {
		return array;
	}

	public void setArray(int[][] array) {
		this.array = array;
		this.bitCount = 0;
		for (int i = 0; i < array.length; i++)
			for (int j = 0; j < array[i].length; j++)
				if (array[i][j] == 1)
					bitCount++;
		this.height = array.length;
		this.width = array[0].length;
	}

	public int getBitCount() {
		return bitCount;
	}

	public void setBitCount(int bitCount) {
		this.bitCount = bitCount;
	}

	@Override
	public Solution randomSolution() {
		TreeSolution ant = new TreeSolution(getMaxDeepSize());
		ant.addGenotype(randomGenotype());
		List<Node> list = ant.leaves();
		while (!list.isEmpty()) {
			Node val = list.get(0);
			int deep = ant.deepOf(val);
			if (deep < getMaxDeepSize())
				ant.addGenotype(randomGenotype());
			else
				ant.addGenotype(randomTerminal());
			list = ant.leaves();
		}
		return ant;
	}

	@Override
	public Double evaluate(Solution sol) {
		TreeSolution tree = (TreeSolution) sol;
		List<Node> nodes = null;
		int index = 0;
		int moves = 0;
		posX = 0;
		posY = 0;
		direction = Direction.RIGHT;
		int[][] field = new int[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				field[i][j] = array[i][j];

		double bit = 0.0;
		List<Node> moveBack = new LinkedList<Node>();

		while (moves < maxMoves) {
			index = 0;
			nodes = tree.deepNodes();
			while (index < nodes.size()) {
				GPFenotype fenotype = nodes.get(index).getValue();
				if (fenotype instanceof Move) {
					bit += move(field);
					moves++;
					if (!moveBack.isEmpty()) {
						index = nodes.indexOf(moveBack.get(moveBack.size() - 1).getChilds().get(1));
						moveBack.remove(moveBack.size() - 1);
					} else
						break;
				} else if (fenotype instanceof LeftDirection) {
					changeDirection(Direction.LEFT);
					moves++;
					if (!moveBack.isEmpty()) {
						index = nodes.indexOf(moveBack.get(moveBack.size() - 1).getChilds().get(1));
						moveBack.remove(moveBack.size() - 1);
					} else
						break;
				} else if (fenotype instanceof RightDirection) {
					changeDirection(Direction.RIGHT);
					moves++;
					if (!moveBack.isEmpty()) {
						index = nodes.indexOf(moveBack.get(moveBack.size() - 1).getChilds().get(1));
						moveBack.remove(moveBack.size() - 1);
					} else
						break;
				} else if (fenotype instanceof IfFoodAhead) {
					if (!ifFoodAhead(field)) {
						Node node = nodes.get(index);
						Node rightNode = node.getChilds().get(1);
						index = nodes.indexOf(rightNode);
						continue;
					}
				} else if (fenotype instanceof Prg2) {
					moveBack.add(nodes.get(index));
				}
				index++;
			}
		}
		sol.setFitness(bit / bitCount);
		return sol.getFitness();
	}

	private boolean ifFoodAhead(int[][] array) {
		if (direction == Direction.LEFT && array[posY][(posX + width - 1) % width] == 1)
			return true;
		else if (direction == Direction.DOWN && array[(posY + 1) % height][posX] == 1)
			return true;
		else if (direction == Direction.RIGHT && array[posY][(posX + 1) % width] == 1)
			return true;
		else if (direction == Direction.TOP && array[(posY + height - 1) % height][posX] == 1)
			return true;
		return false;
	}

	private int move(int[][] array) {
		if (direction == Direction.RIGHT)
			posX = (posX + 1) % width;
		else if (direction == Direction.DOWN)
			posX = (posY + 1) % height;
		else if (direction == Direction.LEFT)
			posX = (posX + width - 1) % width;
		else if (direction == Direction.TOP)
			posY = (posY + height - 1) % height;

		if (array[posY][posX] == 1) {
			array[posY][posX] = 0;
			return 1;
		}
		return 0;
	}

	private void changeDirection(Direction direction) {
		if (this.direction == Direction.LEFT && direction == Direction.LEFT)
			this.direction = Direction.DOWN;
		else if (this.direction == Direction.DOWN && direction == Direction.LEFT)
			this.direction = Direction.RIGHT;
		else if (this.direction == Direction.RIGHT && direction == Direction.LEFT)
			this.direction = Direction.TOP;
		else if (this.direction == Direction.TOP && direction == Direction.LEFT)
			this.direction = Direction.LEFT;
		else if (this.direction == Direction.LEFT && direction == Direction.RIGHT)
			this.direction = Direction.TOP;
		else if (this.direction == Direction.DOWN && direction == Direction.RIGHT)
			this.direction = Direction.LEFT;
		else if (this.direction == Direction.RIGHT && direction == Direction.RIGHT)
			this.direction = Direction.DOWN;
		else if (this.direction == Direction.TOP && direction == Direction.RIGHT)
			this.direction = Direction.RIGHT;
	}

	public int getMaxMoves() {
		return maxMoves;
	}

	public void setMaxMoves(int maxMoves) {
		this.maxMoves = maxMoves;
	}

	@Override
	public void evaluate(Population pop) {
		for (Solution sol : pop.getSolutions())
			evaluate(sol);
	}
}
