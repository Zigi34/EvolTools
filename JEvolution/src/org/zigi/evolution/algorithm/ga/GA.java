package org.zigi.evolution.algorithm.ga;

import java.util.List;

import org.zigi.evolution.Population;
import org.zigi.evolution.algorithm.EA;
import org.zigi.evolution.cross.CrossFunction;
import org.zigi.evolution.mutate.MutateFunction;
import org.zigi.evolution.select.SelectFunction;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.array.ArraySolution;

public class GA<T extends CloneableValue<T>> extends EA<ArraySolution<T>, T> {

	protected MutateFunction<ArraySolution<T>, T> mutate;
	protected CrossFunction<ArraySolution<T>, T> cross;
	protected SelectFunction<ArraySolution<T>, T> select;

	public void run() {
		for (int i = 0; i < getGeneration(); i++) {
			getFunction().evaluate(getPopulation());

			Population<ArraySolution<T>, T> selected = select.select(getPopulation());
			cross.cross(selected);
			mutate.mutate(selected);

			getFunction().evaluate(selected);

			setPopulation(
					getPopulation().bestSolutions(getPopulation().concat(selected), getPopulation().getMaxSolutions()));
			System.out.println(selected + "\n");
		}
	}

	public MutateFunction<ArraySolution<T>, T> getMutate() {
		return mutate;
	}

	public void setMutate(MutateFunction<ArraySolution<T>, T> mutate) {
		this.mutate = mutate;
		this.mutate.setSpace(getSpace());
	}

	public CrossFunction<ArraySolution<T>, T> getCross() {
		return cross;
	}

	public void setCross(CrossFunction<ArraySolution<T>, T> cross) {
		this.cross = cross;
	}

	public SelectFunction<ArraySolution<T>, T> getSelect() {
		return select;
	}

	public void setSelect(SelectFunction<ArraySolution<T>, T> select) {
		this.select = select;
	}

	@Override
	public List<ArraySolution<T>> getResults() {
		// TODO Auto-generated method stub
		return null;
	}
}
