package org.evolution.problem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.evolution.problem.regression.KeyVariables;
import org.evolution.solution.Solution;
import org.evolution.solution.TreeSolution;
import org.evolution.solution.type.CosFunction;
import org.evolution.solution.type.DivideFunction;
import org.evolution.solution.type.GPFenotype;
import org.evolution.solution.type.MultiplyFunction;
import org.evolution.solution.type.Node;
import org.evolution.solution.type.NumericConstant;
import org.evolution.solution.type.NumericValue;
import org.evolution.solution.type.NumericVariable;
import org.evolution.solution.type.PowerFunction;
import org.evolution.solution.type.RangedPowerFunction;
import org.evolution.solution.type.SinFunction;
import org.evolution.solution.type.SubtractionFunction;
import org.evolution.solution.type.SumFunction;
import org.evolution.util.Population;

public class RegressionProblem extends TreeProblem {
	private static final Logger LOG = Logger.getLogger(RegressionProblem.class);
	private static final String VARIABLE_NAMES = "abcdefghijklmnopqrstuvwxyz";

	private LinkedHashMap<KeyVariables, Double> dataset = new LinkedHashMap<>();
	private Integer dimension;
	private String name;
	private String datasetPath;
	private String datasetSplitter = ";";

	public RegressionProblem() {
		addFenotype(new SumFunction());
		addFenotype(new SubtractionFunction());
		addFenotype(new MultiplyFunction());
		addFenotype(new DivideFunction());
		addFenotype(new RangedPowerFunction(2.0, 6.0, true));
		addFenotype(new NumericConstant(-10.0, 10.0));

	}

	public RegressionProblem(String name) {
		this();
		this.name = name;
	}

	/**
	 * Load training dataset from file
	 * 
	 * @param file
	 *            file with dataset
	 * @param splitter
	 *            splitting character
	 */
	private void loadDataset() {
		dataset.clear();
		dimension = null;
		if (datasetPath == null) {
			LOG.error("File is null");
			return;
		}
		File file = new File(datasetPath);
		if (!new File(datasetPath).exists()) {
			LOG.error("File is not exist");
			return;
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(datasetSplitter);
				if (this.dimension == null)
					this.dimension = values.length - 1;

				KeyVariables key = new KeyVariables(this.dimension);
				for (int i = 0; i < values.length - 1; i++) {
					Double keyValue = Double.parseDouble(values[i]);
					key.addKey(keyValue);
				}
				dataset.put(key, Double.parseDouble(values[values.length - 1]));
			}
			br.close();
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}

		// pridame proměnné do seznamu terminalu
		for (int i = 0; i < dimension; i++)
			addFenotype(new NumericVariable(String.valueOf(VARIABLE_NAMES.charAt(i))));

	}

	/**
	 * Aplikuje operaci vybraného uzlu s operátory v podobě podřízených uzlů
	 * 
	 * @param functionNode
	 * @return
	 */
	private Double applyFunction(Node functionNode) {
		if (functionNode == null)
			return null;
		GPFenotype function = functionNode.getValue();
		if (function instanceof SumFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			Double var2 = (Double) childs.get(1).getValue().getValue();
			if (var1.isNaN() || var2.isNaN())
				return Double.NaN;
			return var1 + var2;
		} else if (function instanceof SubtractionFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			Double var2 = (Double) childs.get(1).getValue().getValue();
			if (var1.isNaN() || var2.isNaN())
				return Double.NaN;
			return var1 - var2;
		} else if (function instanceof DivideFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			Double var2 = (Double) childs.get(1).getValue().getValue();
			if (var1.isNaN() || var2 == 0.0)
				return Double.NaN;
			return var1 / var2;
		} else if (function instanceof MultiplyFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			Double var2 = (Double) childs.get(1).getValue().getValue();
			if (var1.isNaN() || var2.isNaN())
				return Double.NaN;
			return var1 * var2;
		} else if (function instanceof PowerFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			Double var2 = (Double) childs.get(1).getValue().getValue();
			if (var1.isNaN() || var2.isNaN())
				return Double.NaN;
			return Math.pow(var1, var2);
		} else if (function instanceof SinFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			if (var1.isNaN())
				return Double.NaN;
			return Math.sin(var1);
		} else if (function instanceof CosFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			if (var1.isNaN())
				return Double.NaN;
			return Math.cos(var1);
		} else if (function instanceof NumericValue) {
			List<Node> childs = functionNode.getChilds();
			return (Double) childs.get(0).getValue().getValue();
		} else if (function instanceof NumericVariable) {
			List<Node> childs = functionNode.getChilds();
			return (Double) childs.get(0).getValue().getValue();
		} else if (function instanceof RangedPowerFunction) {
			List<Node> childs = functionNode.getChilds();
			Double base = (Double) childs.get(0).getValue().getValue();
			Double index = ((RangedPowerFunction) function).getIndex();
			if (base.isNaN() || index.isNaN())
				return Double.NaN;
			Double val = (Double) Math.pow(base, index);
			return val;
		}
		return null;
	}

	/**
	 * Set value for variables according name
	 * 
	 * @param tree
	 *            solution tree
	 * @param name
	 *            name of variable
	 * @param value
	 *            numeric value
	 */
	private void setValueOfVariable(TreeSolution tree, String name, Double value) {
		List<Node> nodes = tree.deepNodes();
		for (Node node : nodes) {
			GPFenotype fenotype = node.getValue();
			if (fenotype instanceof NumericVariable) {
				NumericVariable variable = (NumericVariable) fenotype;
				if (variable.getName().equals(name))
					variable.setValue(value);
			}
		}
	}

	public void evaluate(Population pop) {
		Double sumFunctionValue = 0.0;
		for (Solution sol : pop.getSolutions()) {
			if (sol instanceof TreeSolution) {
				TreeSolution originalTree = (TreeSolution) sol;
				Double sumError = 0.0;

				List<NumericConstant> constants = constants(originalTree);
				for (NumericConstant constant : constants)
					if (constant.getValue() == null)
						constant.evaluateRandom();

				for (KeyVariables key : dataset.keySet()) {
					if (originalTree.height() > originalTree.getMaxHeight()) {
						sumError = Double.MAX_VALUE;
						sol.setFunctionValue(sumError);
						sumFunctionValue += sumError;
						break;
					}

					TreeSolution tree = (TreeSolution) originalTree.cloneMe();
					List<NumericVariable> variables = variables(tree);
					for (int i = 0; i < variables.size(); i++)
						setValueOfVariable(tree, variables.get(i).getName(), key.getKey(i));

					List<Node> leaves = null;
					while (!(leaves = tree.leaveNodes()).isEmpty() && tree.getNodes().size() > 1) {
						for (Node item : leaves) {
							Node parent = item.getParent();
							if (parent == null)
								break;

							// testing for leaves
							boolean valid = true;
							for (Node node : parent.getChilds()) {
								if (!node.isLeaf()) {
									valid = false;
									break;
								}
							}

							if (valid) {
								// get numeric value
								Double val = applyFunction(parent);
								tree.removeSubTree(parent);
								tree.addGenotype(new NumericValue(val));
								break;
							}
						}
					}
					Double result = (Double) tree.getRoot().getValue().getValue();
					if (result.isNaN()) {
						sumError = Double.MAX_VALUE;
						break;
					}
					sumError += Math.pow(result - dataset.get(key), 2.0);
				}

				sumFunctionValue += sumError;
				sol.setFunctionValue(sumError);
			}

		}
		pop.setSumFunctionValue(sumFunctionValue);
	}

	public static String printFunction(TreeSolution sol) {
		StringBuilder sb = new StringBuilder();
		recursiveFunction(sol.getRoot(), sb);
		return sb.toString();
	}

	private static void recursiveFunction(Node node, StringBuilder sb) {
		if (node == null)
			return;
		GPFenotype fun = node.getValue();

		sb.append("(");

		if (fun instanceof MultiplyFunction) {
			recursiveFunction(node.getChilds().get(0), sb);
			sb.append("*");
			recursiveFunction(node.getChilds().get(1), sb);
		} else if (fun instanceof SumFunction) {
			recursiveFunction(node.getChilds().get(0), sb);
			sb.append("+");
			recursiveFunction(node.getChilds().get(1), sb);
		} else if (fun instanceof SubtractionFunction) {
			recursiveFunction(node.getChilds().get(0), sb);
			sb.append("-");
			recursiveFunction(node.getChilds().get(1), sb);
		} else if (fun instanceof DivideFunction) {
			recursiveFunction(node.getChilds().get(0), sb);
			sb.append("/");
			recursiveFunction(node.getChilds().get(1), sb);
		} else if (fun instanceof RangedPowerFunction) {
			RangedPowerFunction rf = (RangedPowerFunction) fun;
			sb.append("POWER(");
			recursiveFunction(node.getChilds().get(0), sb);
			sb.append(",");
			sb.append(rf.getIndex());
			sb.append(")");
		} else if (fun instanceof NumericConstant) {
			NumericConstant con = (NumericConstant) fun;
			sb.append(con.getValue());
		} else if (fun instanceof NumericVariable)
			sb.append("A2");
		else if (fun instanceof NumericValue)
			sb.append(((NumericValue) fun).getValue());
		sb.append(")");
	}

	/**
	 * Vraci seznam promennych, ktere se ve strome vyskytuji
	 * 
	 * @param tree
	 * @return
	 */
	public List<NumericVariable> variables(TreeSolution tree) {
		List<NumericVariable> list = new LinkedList<NumericVariable>();
		for (Node node : tree.deepNodes()) {
			if (node.getValue() instanceof NumericVariable && !list.contains(node.getValue()))
				list.add((NumericVariable) node.getValue());
		}
		return list;
	}

	public List<NumericConstant> constants(TreeSolution tree) {
		List<NumericConstant> list = new LinkedList<NumericConstant>();
		for (Node node : tree.deepNodes()) {
			if (node.getValue() instanceof NumericConstant && !list.contains(node.getValue()))
				list.add((NumericConstant) node.getValue());
		}
		return list;
	}

	public String getDatasetPath() {
		return datasetPath;
	}

	public void setDatasetPath(String path) {
		this.datasetPath = path;
	}

	public String getDatasetSplitter() {
		return datasetSplitter;
	}

	public void setDatasetSplitter(String datasetSplitter) {
		this.datasetSplitter = datasetSplitter;
	}

	@Override
	public void initialize() {
		super.initialize();
		loadDataset();
	}

	@Override
	public String toString() {
		return name;
	}
}
