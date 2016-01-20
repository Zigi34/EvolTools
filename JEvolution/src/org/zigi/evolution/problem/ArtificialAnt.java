package org.zigi.evolution.problem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
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

	private int[][] yard;
	private int crumbs = 0;
	private int yardWidth = 0;
	private int yardHeight = 0;

	private int posX = 0;
	private int posY = 0;

	private int startPosX = 0;
	private int startPosY = 0;

	private int maxMoves = 440;

	private Direction startDirection = Direction.RIGHT;
	private Direction direction = Direction.RIGHT;

	public static final Logger LOG = Logger.getLogger(ArtificialAnt.class);

	private enum Direction {
		LEFT, TOP, RIGHT, DOWN
	}

	/**
	 * Nacte stezku ze souboru. V sobouru je označené jídlo znakem '1' a prázdné
	 * místo libovolný symbol mimo '1'
	 * 
	 * @param file
	 *            soubor se stezkou
	 * @throws FileNotFoundException
	 */
	public void setYard(File file) throws FileNotFoundException {
		if (!file.exists())
			throw new FileNotFoundException();
		StringBuilder sb = new StringBuilder();
		try (InputStreamReader sr = new InputStreamReader(new FileInputStream(file))) {
			char[] buffer = new char[2048];
			int readSize = 0;
			do {
				sb.append(new String(buffer, 0, readSize));
				readSize = sr.read(buffer);
			} while (readSize > 0);
		} catch (Exception e) {
			LOG.error(e);
		}

		String[] rows = sb.toString().split("\n");
		yardWidth = rows[0].length();
		yardHeight = rows.length;
		yard = new int[yardHeight][yardWidth];
		crumbs = 0;

		for (int row = 0; row < rows.length; row++) {
			for (int col = 0; col < rows[row].length(); col++)
				if (rows[row].charAt(col) == '1') {
					yard[row][col] = 1;
					crumbs++;
				} else
					yard[row][col] = 0;
		}
	}

	public ArtificialAnt() {
		super();

		// add functions
		addFenotype(new Prg2());
		// addFenotype(new Prg3());
		addFenotype(new IfFoodAhead());

		// add terminals
		addFenotype(new Move());
		addFenotype(new LeftDirection());
		addFenotype(new RightDirection());

		// max deep size
		setMaxHeight(6);
	}

	public void setYard(int[][] array) {
		this.yard = array;
		this.crumbs = 0;
		for (int i = 0; i < array.length; i++)
			for (int j = 0; j < array[i].length; j++)
				if (array[i][j] == 1)
					crumbs++;
		this.yardHeight = array.length;
		this.yardWidth = array[0].length;
	}

	@Override
	public Solution randomSolution() {
		TreeSolution ant = new TreeSolution(getMaxHeight());
		ant.addGenotype(randomGenotype());
		List<Node> list = ant.leaves();
		while (!list.isEmpty()) {
			Node val = list.get(0);
			int deep = ant.deepOf(val);
			if (deep < getMaxHeight())
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

		// initialize evaluation
		int nodeIndex = 0;
		int moves = 0;
		double foundCrumbs = 0.0;
		posX = startPosX;
		posY = startPosY;
		direction = startDirection;
		int[][] actualYard = new int[yardHeight][yardWidth];
		int[][] path = new int[yardHeight][yardWidth];
		for (int i = 0; i < yardHeight; i++)
			for (int j = 0; j < yardWidth; j++)
				actualYard[i][j] = yard[i][j];

		// seznam uzlu, ke kterym je treba se vracit zpet
		List<Node> moveBack = new LinkedList<Node>();

		// dokud se muze mravenec pohybovat
		while (moves < maxMoves) {
			nodeIndex = 0;
			nodes = tree.deepNodes();
			while (nodeIndex < nodes.size()) {
				GPFenotype operation = nodes.get(nodeIndex).getValue();
				if (operation instanceof Move) {
					foundCrumbs += move(actualYard, path);
					moves++;
					if (!moveBack.isEmpty()) {
						nodeIndex = nodes.indexOf(moveBack.get(moveBack.size() - 1).getChilds().get(1));
						moveBack.remove(moveBack.size() - 1);
					} else
						break;
				} else if (operation instanceof LeftDirection) {
					changeDirection(Direction.LEFT);
					moves++;
					if (!moveBack.isEmpty()) {
						nodeIndex = nodes.indexOf(moveBack.get(moveBack.size() - 1).getChilds().get(1));
						moveBack.remove(moveBack.size() - 1);
					} else
						break;
				} else if (operation instanceof RightDirection) {
					changeDirection(Direction.RIGHT);
					moves++;
					if (!moveBack.isEmpty()) {
						nodeIndex = nodes.indexOf(moveBack.get(moveBack.size() - 1).getChilds().get(1));
						moveBack.remove(moveBack.size() - 1);
					} else
						break;
				} else if (operation instanceof IfFoodAhead) {
					if (!ifFoodAhead(actualYard)) {
						Node node = nodes.get(nodeIndex);
						Node rightNode = node.getChilds().get(1);
						nodeIndex = nodes.indexOf(rightNode);
						continue;
					}
				} else if (operation instanceof Prg2) {
					moveBack.add(nodes.get(nodeIndex));
				}
				nodeIndex++;
			}
		}
		// printPath(array);
		sol.setFitness(foundCrumbs / crumbs);
		return sol.getFitness();
	}

	public int[][] getPath(Solution sol) {
		TreeSolution tree = (TreeSolution) sol;
		List<Node> nodes = null;
		int index = 0;
		int moves = 0;
		posX = 0;
		posY = 0;
		direction = Direction.RIGHT;

		int[][] path = new int[yardHeight][yardWidth];
		int[][] field = new int[yardHeight][yardWidth];
		for (int i = 0; i < yardHeight; i++)
			for (int j = 0; j < yardWidth; j++)
				field[i][j] = yard[i][j];

		double bit = 0.0;
		List<Node> moveBack = new LinkedList<Node>();

		while (moves < maxMoves) {
			index = 0;
			nodes = tree.deepNodes();
			while (index < nodes.size()) {
				GPFenotype fenotype = nodes.get(index).getValue();
				if (fenotype instanceof Move) {
					bit += move(field, path);
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
		return path;
	}

	/**
	 * Loguje celou prošlou cestu.
	 * 
	 * @param path
	 */
	public static void printPath(int[][] path) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (int i = 0; i < path.length; i++) {
			for (int y = 0; y < path[i].length; y++) {
				sb.append(path[i][y]);
			}
			sb.append("\n");
		}
		LOG.info(sb.toString());
		LOG.info("\n");
	}

	/**
	 * Testuje, zda je drobeček před mravencem
	 * 
	 * @param array
	 * @return
	 */
	private boolean ifFoodAhead(int[][] array) {
		if (direction == Direction.LEFT && array[posY][(posX + yardWidth - 1) % yardWidth] == 1)
			return true;
		else if (direction == Direction.DOWN && array[(posY + 1) % yardHeight][posX] == 1)
			return true;
		else if (direction == Direction.RIGHT && array[posY][(posX + 1) % yardWidth] == 1)
			return true;
		else if (direction == Direction.TOP && array[(posY + yardHeight - 1) % yardHeight][posX] == 1)
			return true;
		return false;
	}

	/**
	 * Pohyb mravence vpřed aktuálním směrem
	 * 
	 * @param yard
	 * @param path
	 * @return
	 */
	private int move(int[][] yard, int[][] path) {
		path[posY][posX] = 1;
		if (direction == Direction.RIGHT)
			posX = (posX + 1) % yardWidth;
		else if (direction == Direction.DOWN)
			posY = (posY + 1) % yardHeight;
		else if (direction == Direction.LEFT)
			posX = (posX + yardWidth - 1) % yardWidth;
		else if (direction == Direction.TOP)
			posY = (posY + yardHeight - 1) % yardHeight;

		path[posY][posX] = 1;
		if (yard[posY][posX] == 1) {
			yard[posY][posX] = 0;
			return 1;
		}
		return 0;
	}

	/**
	 * Změna směru mravence (zabere jeden pohyb)
	 * 
	 * @param direction
	 *            nový směr mravence
	 */
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

	public int[][] getYard() {
		return yard;
	}

	public int getYardWidth() {
		return yardWidth;
	}

	public void setYardWidth(int yardWidth) {
		this.yardWidth = yardWidth;
	}

	public int getYardHeight() {
		return yardHeight;
	}

	public void setYardHeight(int yardHeight) {
		this.yardHeight = yardHeight;
	}

	public int getCrumbs() {
		return crumbs;
	}

	@Override
	public void evaluate(Population pop) {
		for (Solution sol : pop.getSolutions())
			evaluate(sol);
	}
}
