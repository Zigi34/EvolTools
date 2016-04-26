package org.evolution.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.evolution.algorithm.EvolutionAlgorithm;
import org.evolution.algorithm.GeneticProgramming;
import org.evolution.model.AlgorithmModel;
import org.evolution.model.ProblemModel;
import org.evolution.problem.ArtificialAnt;
import org.evolution.services.Services;
import org.evolution.solution.Solution;
import org.evolution.util.Position;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ArtificialAntProgress extends AnchorPane {

	private static final Logger LOG = Logger.getLogger(ArtificialAntProgress.class);

	@FXML
	private Canvas canvas;

	@FXML
	private Label solutionTitle;

	@FXML
	private Label generationTitle;

	@FXML
	private Label fitnessTitle;

	private GraphicsContext context;
	private double cellSize = 0.0;
	private double maxGridSize = 0.0;
	private int xCount = 0;
	private int yCount = 0;
	private int[][] yard;

	public ArtificialAntProgress() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/ArtificialAntProgress.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			LOG.error(exception);
		}

		initialize();
	}

	private void initialize() {
		context = canvas.getGraphicsContext2D();

		ProblemModel model = Services.problemService().getSelected();
		if (model != null && model.getProblem() instanceof ArtificialAnt) {
			ArtificialAnt problem = (ArtificialAnt) model.getProblem();
			xCount = problem.getYardWidth();
			yCount = problem.getYardHeight();

			double canvasWidth = canvas.getWidth();
			double canvasHeight = canvas.getHeight();
			double cellWidth = canvasWidth / xCount;
			double cellHeight = canvasHeight / yCount;

			// stejna vyska a sirka
			if (cellWidth > cellHeight)
				cellWidth = cellHeight;
			else if (cellHeight > cellWidth)
				cellHeight = cellWidth;

			this.cellSize = cellWidth;
			this.maxGridSize = cellWidth * xCount;
			this.yard = problem.getYard();

			AlgorithmModel algModel = Services.algorithmService().getSelected();
			if (algModel.getAlgorithm() instanceof GeneticProgramming) {
				GeneticProgramming alg = (GeneticProgramming) algModel.getAlgorithm();
				alg.addChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						Object value = evt.getNewValue();
						if (value != null && value.equals(EvolutionAlgorithm.NEW_BEST_SOLUTION)) {
							// nové nejlepší řešení
							clearGrid();
							paintGrid();
							paintFoot(Color.GRAY);
							Solution best = alg.getBestSolution();
							List<Position> positions = problem.generatePath(best);
							for (Position position : positions) {
								paintCell(Color.BLUE, position.getX(), position.getY());
							}
						}
					}
				});
			}
		}
		clearGrid();
		paintGrid();
		paintFoot(Color.GRAY);
	}

	private void paintGrid() {
		ProblemModel model = Services.problemService().getSelected();
		if (model != null && model.getProblem() instanceof ArtificialAnt) {
			ArtificialAnt problem = (ArtificialAnt) model.getProblem();
			int width = problem.getYardWidth();
			int height = problem.getYardHeight();

			// context.setFill(Color.BLACK);
			context.setStroke(Color.BLACK);
			context.setLineWidth(1);

			double x = 0.0;
			context.strokeLine(x, 0, x, maxGridSize);
			for (int i = 0; i < width; i++) {
				x += cellSize;
				context.strokeLine(x, 0, x, maxGridSize);
			}

			double y = 0.0;
			context.strokeLine(0, y, maxGridSize, y);
			for (int i = 0; i < height; i++) {
				y += cellSize;
				context.strokeLine(0, y, maxGridSize, y);
			}
		}
	}

	private void paintCell(Color color, int x, int y) {
		context.setFill(color);
		context.setStroke(Color.BLACK);
		context.setLineWidth(1);
		context.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
	}

	private void paintFoot(Color color) {
		context.setFill(color);
		context.setStroke(Color.BLACK);
		context.setLineWidth(1);
		if (yard != null) {
			for (int y = 0; y < yard.length; y++) {
				for (int x = 0; x < yard[y].length; x++) {
					if (yard[y][x] == 1)
						context.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
				}
			}
		}
	}

	private void clearGrid() {
		context.setFill(Color.WHITE);
		context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
}
