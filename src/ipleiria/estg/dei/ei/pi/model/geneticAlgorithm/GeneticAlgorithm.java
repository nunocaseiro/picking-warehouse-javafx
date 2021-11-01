package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm;

import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.mutation.Mutation;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.recombination.Recombination;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.selectionMethods.SelectionMethod;
import ipleiria.estg.dei.ei.pi.utils.exceptions.ValueNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm<I extends Individual<? extends GAProblem>, P extends GAProblem> {

    public Random random;
    private Population<I, P> population;
    private Factory<I, P> individualFactory;
    private final SelectionMethod<I, P> selection;
    private final Recombination<I, P> recombination;
    private final Mutation<I, P> mutation;
    private final int populationSize;
    private final int maxGenerations;
    private int t;
    private boolean stopped;
    private I bestInRun;
    private final List<GAListener<I, P>> listeners;

    public GeneticAlgorithm(Factory<I, P> individualFactory, SelectionMethod<I, P> selection, Recombination<I, P> recombination, Mutation<I, P> mutation, int populationSize, int maxGenerations, Random random) {
        this.individualFactory = individualFactory;
        this.selection = selection;
        this.recombination = recombination;
        this.mutation = mutation;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.random = random;
        this.listeners = new ArrayList<>(3);
    }

    public I run(P problem) throws ValueNotFoundException {
        this.t = 0;
        this.population = new Population<>(this.populationSize, this.individualFactory, problem);
        this.bestInRun = this.population.evaluate();
        fireGenerationEnded(this);

        while (!stopCondition(t)) {
            Population<I, P> populationAux = this.selection.run(this.population, this.random);
            this.recombination.run(populationAux, this.random);
            this.mutation.run(populationAux, this.random);
            this.population = populationAux;
            I bestInGen = this.population.evaluate();
            computeBestInRun(bestInGen);
            t++;
            fireGenerationEnded(this);
        }

        fireRunEnded(this);

        return this.bestInRun;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    private boolean stopCondition(int t) {
        return this.stopped || t == this.maxGenerations;
    }

    public I getBestInRun() {
        return bestInRun;
    }

    public Population<I, P> getPopulation() {
        return population;
    }

    public int getT() {
        return t;
    }

    private void computeBestInRun (I bestInGen){
        if (bestInGen.compareTo(this.bestInRun) > 0) {
            this.bestInRun = (I) bestInGen.clone();
        }
    }

    public void fireGenerationEnded(GeneticAlgorithm<I, P> geneticAlgorithm) {
        for (GAListener<I, P> listener : listeners) {
            listener.generationEnded(geneticAlgorithm);
        }
//        if (e.isStopped()) {
//            stop();
//        }
    }

    public void fireRunEnded(GeneticAlgorithm<I, P> geneticAlgorithm) {
        for (GAListener<I, P> listener : listeners) {
            listener.runEnded(geneticAlgorithm);
        }
    }

    public synchronized void addGAListener(GAListener<I, P> listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    @Override
    public String toString() {
        return "Population size: " + this.populationSize + "\n" +
                "Max generations: " + this.maxGenerations + "\n" +
                "Selection: " + this.selection + "\n" +
                "Recombination: " + this.recombination + "\n" +
                "Mutation: " + this.mutation + "\n";
    }
}
