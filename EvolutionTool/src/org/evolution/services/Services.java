package org.evolution.services;

/**
 * Services all managers
 * 
 * @author zigi
 *
 */
public class Services {

	private Services() {
	}

	public static SelectFunctionService selectFunctionService() {
		return SelectFunctionService.getInstance();
	}

	public static MutateFunctionService mutateFunctionService() {
		return MutateFunctionService.getInstance();
	}

	public static ProblemService problemService() {
		return ProblemService.getInstance();
	}

	public static AlgorithmService algorithmService() {
		return AlgorithmService.getInstance();
	}

	public static PopulationService populationService() {
		return PopulationService.getInstance();
	}
}
