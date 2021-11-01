package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.mutation;


import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.Individual;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.Population;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.GeneticOperator;

import java.util.Random;

public abstract class Mutation<I extends Individual<? extends GAProblem>, P extends GAProblem> extends GeneticOperator {
    
    public Mutation(double probability){
        super(probability);
    }

    public void run(Population<I, P> population, Random random) {
        int populationSize = population.getSize();
        for (int i = 0; i < populationSize; i++) {
            if (random.nextDouble() < getProbability()) {
                mutate(population.getIndividual(i), random);
            }
        }
    }

    public abstract void mutate(I individual, Random random);
}
