package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.mutation;


import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.IntVectorIndividual;

import java.util.Random;

public class MutationInsert<I extends IntVectorIndividual<? extends GAProblem>, P extends GAProblem> extends Mutation<I, P> {

    public MutationInsert(double probability) {
        super(probability);
    }

    @Override
    public void mutate(I individual, Random random) {
        int cut1 = random.nextInt(individual.getNumGenes()); // cut1=1
        int cut2;

        do {
            cut2 = random.nextInt(individual.getNumGenes()); // cut2=4
        } while (cut1 == cut2);

        if (cut1 > cut2) {
            int aux = cut1;
            cut1 = cut2;
            cut2 = aux;
        }

        for(int i = cut2 - 1; i > cut1; i--) { // int i = 3; 3>1; i--
            int aux = individual.getGene(i + 1); // aux=4
            individual.setGene(i + 1, individual.getGene(i));
            individual.setGene(i, aux);
        }
    }


    @Override
    public String toString() {
        return "Insert";
    }
}