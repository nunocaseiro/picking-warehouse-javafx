package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm;

public abstract class Individual<P extends GAProblem> implements Comparable<Individual<? extends GAProblem>> {

    protected double fitness;
    protected P problem;

    public Individual(P problem) {
        this.problem = problem;
    }

    public Individual(Individual<P> original) {
        this.problem = original.problem;
        this.fitness = original.fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public abstract int getNumGenes();

    public abstract void computeFitness();

    public P getProblem() {
        return problem;
    }

    //    public abstract void swapGenes(Individual<P> other, int g);

    @Override
    public abstract Individual<P> clone();
}
