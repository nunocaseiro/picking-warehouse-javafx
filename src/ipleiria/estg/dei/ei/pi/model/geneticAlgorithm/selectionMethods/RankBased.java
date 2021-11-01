package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.selectionMethods;


import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.Individual;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.Population;
import ipleiria.estg.dei.ei.pi.utils.FitnessComparator;

import java.util.Random;


public class RankBased<I extends Individual<? extends GAProblem>, P extends GAProblem> extends SelectionMethod<I,P>  {

    double[] accumulated;
    double ps = 2;

    public RankBased(int popSize) {
        super(popSize);
        accumulated = new double[popSize];
    }

    public RankBased(int popSize, double ps ){
        super(popSize);
        accumulated = new double[popSize];
        this.ps= ps;
    }

    @Override
    public Population<I, P> run(Population<I, P>  original, Random random) {
        original.getIndividuals().sort(new FitnessComparator());

        Population<I, P> result = new Population<>(original.getSize());
        double N=populationSize;
        double[] prob = new double[populationSize];
        for (int i = 1; i <= populationSize; i++) {
            prob[i-1] = (1/N) *(2-ps+2*(ps-1)*((i-1)/(N-1)));
        }

        accumulated[0] = prob[0];
        for (int i = 1; i < populationSize; i++) {
            accumulated[i] = accumulated[i - 1] + prob[i];
        }

        double rankSum = accumulated[populationSize - 1];
        for (int i = 0; i < populationSize; i++) {
            accumulated[i] /= rankSum;
        }

        for (int i = 0; i < populationSize; i++) {
            result.addIndividual(roulette(original, random));
        }
        
        return result;
    }

    private I roulette(Population<I, P> population, Random random) {
        double probability = random.nextDouble();

        for (int i = 0; i < populationSize; i++) {
            if (probability <= accumulated[i]) {
                return (I) population.getIndividual(i).clone();
            }
        }

        //For the case where all individuals have fitness 0
        return (I) population.getIndividual(random.nextInt(populationSize)).clone();
    }
    
    @Override
    public String toString(){
        return "Rank("+ps+")";
    }    
}


