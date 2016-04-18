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
import org.zigi.evolution.solution.value.Configuration;
import org.zigi.evolution.solution.value.Direction;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.solution.value.IfFoodAhead;
import org.zigi.evolution.solution.value.LeftDirection;
import org.zigi.evolution.solution.value.Move;
import org.zigi.evolution.solution.value.Node;
import org.zigi.evolution.solution.value.Prg2;
import org.zigi.evolution.solution.value.RightDirection;
import org.zigi.evolution.solution.value.Rotation;
import org.zigi.evolution.util.Population;

public class ArtificialAnt extends TreeProblem {

	private int[][] yard;
	private int crumbs = 0;
	private int yardWidth = 0;
	private int yardHeight = 0;

	// private int posX = 0;
	// private int posY = 0;

	// private int startPosX = 0;
	// private int startPosY = 0;

	private int maxMoves = 440;

	// private Direction startDirection = Direction.RIGHT;
	// private Direction direction = Direction.RIGHT;

	public static final Logger LOG = Logger.getLogger(ArtificialAnt.class);

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
		try {
			InputStreamReader sr = new InputStreamReader(new FileInputStream(file));
			char[] buffer = new char[2048];
			int readSize = 0;
			do {
				sb.append(new String(buffer, 0, readSize));
				readSize = sr.read(buffer);
			} while (readSize > 0);
			sr.close();
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
		setMaxMoves(420);

		try {
			setYard(new File("resources/artificial_ant"));
		} catch (Exception e) {
			LOG.warn(e);
		}
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
		List<Node> list = ant.uncompleteNodes();
		while (!list.isEmpty()) {
			Node val = list.get(0);
			int deep = ant.deepOf(val);
			if (deep < getMaxHeight())
				ant.addGenotype(randomGenotype());
			else
				ant.addGenotype(randomTerminal());
			list = ant.uncompleteNodes();
		}
		return ant;
	}

	private int[][] copyYard() {
		int[][] field = new int[yardHeight][yardWidth];
		for (int i = 0; i < yardHeight; i++)
			for (int j = 0; j < yardWidth; j++)
				field[i][j] = yard[i][j];
		return field;
	}

	@Override
	public void evaluate(Population pop) {
		Double minFunctionValue = isMinProblem() ? Double.MIN_VALUE : Double.MAX_VALUE;
		Double maxFunctionValue = isMinProblem() ? Double.MAX_VALUE : Double.MIN_VALUE;
		Double sumFunctionValue = 0.0;
		for (Solution sol : pop.getSolutions()) {
			TreeSolution tree = (TreeSolution) sol;
			List<Node> nodes = null;

			// initialize evaluation
			int nodeIndex = 0;

			Configuration conf = new Configuration();

			Direction direction = Direction.RIGHT;

			int[][] actualYard = copyYard();

			// seznam uzlu, ke kterym je treba se vracit zpet
			List<Node> moveBack = new LinkedList<Node>();

			// dokud se muze mravenec pohybovat
			while (conf.getMoves() < maxMoves) {
				nodeIndex = 0;
				nodes = tree.deepNodes();
				while (nodeIndex < nodes.size()) {
					GPFenotype operation = nodes.get(nodeIndex).getValue();
					if (operation instanceof Move) {
						conf = move(actualYard, conf, yardWidth, yardHeight);
						if (!moveBack.isEmpty()) {
							nodeIndex = nodes.indexOf(moveBack.get(moveBack.size() - 1).getChilds().get(1));
							moveBack.remove(moveBack.size() - 1);
						} else
							break;
					} else if (operation instanceof LeftDirection) {
						conf = changeDirection(conf, Rotation.LEFT);
						if (!moveBack.isEmpty()) {
							nodeIndex = nodes.indexOf(moveBack.get(moveBack.size() - 1).getChilds().get(1));
							moveBack.remove(moveBack.size() - 1);
						} else
							break;
					} else if (operation instanceof RightDirection) {
						conf = changeDirection(conf, Rotation.RIGHT);
						if (!moveBack.isEmpty()) {
							nodeIndex = nodes.indexOf(moveBack.get(moveBack.size() - 1).getChilds().get(1));
							moveBack.remove(moveBack.size() - 1);
						} else
							break;
					} else if (operation instanceof IfFoodAhead) {
						if (!ifFoodAhead(actualYard, conf.getPosX(), conf.getPosY(), yardWidth, yardHeight,
								direction)) {
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
			Double functionValue = (double) conf.getFoundCrumbs();
			sumFunctionValue += functionValue;
			if (isMinProblem()) {
				if (functionValue < maxFunctionValue) {
					maxFunctionValue = functionValue;
				}
				if (functionValue > minFunctionValue) {
					minFunctionValue = functionValue;
				}
			} else {
				if (functionValue > maxFunctionValue) {
					maxFunctionValue = functionValue;
				}
				if (functionValue < minFunctionValue) {
					minFunctionValue = functionValue;
				}
			}

			sol.setFunctionValue(functionValue);

		}
	}

	/**
	 * Testuje, zda je drobeček před mravencem
	 * 
	 * @param array
	 * @return
	 */
	private static boolean ifFoodAhead(int[][] array, Integer posX, Integer posY, Integer yardWidth, Integer yardHeight,
			Direction direction) {
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
	private static Configuration move(int[][] yard, Configuration conf, Integer yardWidth, Integer yardHeight) {
		conf.moveIncrement();
		if (conf.getDirection() == Direction.RIGHT)
			conf.setPosX((conf.getPosX() + 1) % yardWidth);
		else if (conf.getDirection() == Direction.DOWN)
			conf.setPosY((conf.getPosX() + 1) % yardHeight);
		else if (conf.getDirection() == Direction.LEFT)
			conf.setPosX((conf.getPosX() + yardWidth - 1) % yardWidth);
		else if (conf.getDirection() == Direction.TOP)
			conf.setPosY((conf.getPosX() + yardHeight - 1) % yardHeight);

		if (yard[conf.getPosY()][conf.getPosX()] == 1) {
			yard[conf.getPosY()][conf.getPosX()] = 0;
			conf.foundCrumbsIncrement();
			return conf;
		}
		return conf;
	}

	/**
	 * Změna směru mravence (zabere jeden pohyb)
	 * 
	 * @param direction
	 *            nový směr mravence
	 */
	private static Configuration changeDirection(Configuration conf, Rotation newDirection) {
		conf.moveIncrement();
		if (conf.getDirection() == Direction.LEFT && newDirection == Rotation.LEFT)
			conf.setDirection(Direction.DOWN);
		else if (conf.getDirection() == Direction.DOWN && newDirection == Rotation.LEFT)
			conf.setDirection(Direction.RIGHT);
		else if (conf.getDirection() == Direction.RIGHT && newDirection == Rotation.LEFT)
			conf.setDirection(Direction.TOP);
		else if (conf.getDirection() == Direction.TOP && newDirection == Rotation.LEFT)
			conf.setDirection(Direction.LEFT);
		else if (conf.getDirection() == Direction.LEFT && newDirection == Rotation.RIGHT)
			conf.setDirection(Direction.TOP);
		else if (conf.getDirection() == Direction.DOWN && newDirection == Rotation.RIGHT)
			conf.setDirection(Direction.LEFT);
		else if (conf.getDirection() == Direction.RIGHT && newDirection == Rotation.RIGHT)
			conf.setDirection(Direction.DOWN);
		else if (conf.getDirection() == Direction.TOP && newDirection == Rotation.RIGHT)
			conf.setDirection(Direction.RIGHT);
		return conf;
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
}
