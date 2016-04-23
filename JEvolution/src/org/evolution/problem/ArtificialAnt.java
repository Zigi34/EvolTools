package org.evolution.problem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.evolution.solution.Solution;
import org.evolution.solution.TreeSolution;
import org.evolution.solution.type.Configuration;
import org.evolution.solution.type.Direction;
import org.evolution.solution.type.GPFenotype;
import org.evolution.solution.type.IfFoodAhead;
import org.evolution.solution.type.LeftDirection;
import org.evolution.solution.type.Move;
import org.evolution.solution.type.Node;
import org.evolution.solution.type.Prg2;
import org.evolution.solution.type.Prg3;
import org.evolution.solution.type.RightDirection;
import org.evolution.solution.type.Rotation;
import org.evolution.util.Population;
import org.evolution.util.Position;

public class ArtificialAnt extends TreeProblem {

	private int[][] yard;
	private int crumbs = 0;
	private int yardWidth = 0;
	private int yardHeight = 0;

	private int maxMoves = 500;

	public static final Logger LOG = Logger.getLogger(ArtificialAnt.class);

	public ArtificialAnt() {
		super();

		// add functions
		addFenotype(new Prg2());
		addFenotype(new Prg3());
		addFenotype(new IfFoodAhead());

		// add terminals
		addFenotype(new Move());
		addFenotype(new LeftDirection());
		addFenotype(new RightDirection());

		// max deep size
		setMaxHeight(6);

		try {
			setYard(new File("resources/artificial_ant"));
		} catch (Exception e) {
			LOG.warn(e);
		}
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

	private int[][] copyYard() {
		int[][] field = new int[yardHeight][yardWidth];
		for (int i = 0; i < yardHeight; i++)
			for (int j = 0; j < yardWidth; j++)
				field[i][j] = yard[i][j];
		return field;
	}

	@Override
	public void evaluate(Population pop) {
		for (Solution sol : pop.getSolutions()) {
			TreeSolution tree = (TreeSolution) sol;
			List<Node> nodes = null;

			int height = tree.height();
			int max = tree.getMaxHeight();
			if (height > max) {
				sol.setFunctionValue(0.0);
				continue;
			}

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

	public List<Position> generatePath(Solution solution) {
		TreeSolution tree = (TreeSolution) solution;
		List<Node> nodes = null;
		List<Position> positions = new LinkedList<Position>();

		int height = tree.height();
		int max = tree.getMaxHeight();
		if (height > max) {
			solution.setFunctionValue(0.0);
			return positions;
		}

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
					positions.add(new Position(conf.getPosX(), conf.getPosY()));
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
					if (!ifFoodAhead(actualYard, conf.getPosX(), conf.getPosY(), yardWidth, yardHeight, direction)) {
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
		return positions;
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
			conf.setPosY((conf.getPosY() + 1) % yardHeight);
		else if (conf.getDirection() == Direction.LEFT)
			conf.setPosX((conf.getPosX() + yardWidth - 1) % yardWidth);
		else if (conf.getDirection() == Direction.TOP)
			conf.setPosY((conf.getPosY() + yardHeight - 1) % yardHeight);

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

	@Override
	public boolean isMinProblem() {
		return false;
	}
}
