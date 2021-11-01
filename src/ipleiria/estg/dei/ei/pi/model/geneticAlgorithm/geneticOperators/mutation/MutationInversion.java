package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.mutation;


import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.IntVectorIndividual;

import java.util.Random;

public class MutationInversion<I extends IntVectorIndividual<? extends GAProblem>, P extends GAProblem> extends Mutation<I, P> {

    public MutationInversion(double probability) {
        super(probability);
    }

    @Override
    public void mutate(I individual, Random random) {

        int l = individual.getNumGenes();

        int r1 = random.nextInt(l);
        int r2 = random.nextInt(l);

        while (r1 >= r2) {
            r1 = random.nextInt(l);
            r2 = random.nextInt(l);
        }

        int mid = r1 + (((r2 + 1) - r1) / 2);
        int endCount = r2;
        for (int i = r1; i < mid; i++) {
            int tmp = individual.getGene(i);
            individual.setGene(i, individual.getGene(endCount));
            individual.setGene(endCount, tmp);
            endCount--;
        }
    }

    @Override
    public String toString() {
        return "Inversion";

    }
}