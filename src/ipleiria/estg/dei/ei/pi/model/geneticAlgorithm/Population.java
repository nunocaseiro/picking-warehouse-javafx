package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class Population<I extends Individual<? extends GAProblem>, P extends GAProblem> {

    private List<I> individuals;
    private I best;

    public Population(int size) {
        this.individuals = new ArrayList<>(size);
    }

    public Population(int size, Factory<I,P> factory, P problem) {
        this.individuals = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            this.individuals.add(factory.newIndividual(problem));
        }
    }

    public I evaluate() {
        this.best = getIndividual(0);

        for (I individual : this.individuals) {
            individual.computeFitness();
            if (individual.compareTo(this.best) > 0) {
                this.best = individual;
            }
        }

        return this.best;
    }

    public double getAverageFitness() {
        double fitnessSum = 0;

        for (I individual : this.individuals) {
            fitnessSum += individual.getFitness();
        }

        return fitnessSum / this.individuals.size();
    }

    public List<I> getIndividuals() {
        return this.individuals;
    }

    public void addIndividual(I individual) {
        this.individuals.add(individual);
    }

    public I getIndividual(int index) {
        return this.individuals.get(index);
    }

    public I getBest() {
        return this.best;
    }

    public int getSize() {
        return this.individuals.size();
    }


}
