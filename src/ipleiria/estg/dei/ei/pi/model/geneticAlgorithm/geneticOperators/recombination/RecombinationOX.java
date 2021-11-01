package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.recombination;


import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.IntVectorIndividual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RecombinationOX<I extends IntVectorIndividual<? extends GAProblem>, P extends GAProblem> extends Recombination<I, P> {

    public RecombinationOX(double probability) {
        super(probability);
    }

    @Override
    public void recombine(I ind1, I ind2, Random random) {

        final int size = ind1.getNumGenes();
        int start = 0;
        int end = 0;
        while (start == end) {

            final int number1 = random.nextInt(size - 1);
            final int number2 = random.nextInt(size);

            start = Math.min(number1, number2);
            end = Math.max(number1, number2);
        }

        final List<Integer> child1 = new ArrayList<>();
        final List<Integer> child2 = new ArrayList<>();

        for (int i = start; i <= end; i++) {
            child1.add(ind1.getGene(i));
        }

        for (int i = start; i <= end; i++) {
            child2.add(ind2.getGene(i));
        }

        int currentBoxIndex;
        int currentBoxInInd1;
        int currentBoxInInd2;
        for (int i = 0; i < size; i++) {

            currentBoxIndex = (end + i) % size;
            currentBoxInInd1 = ind1.getGene(currentBoxIndex);
            currentBoxInInd2 = ind2.getGene(currentBoxIndex);

            if (!child1.contains(currentBoxInInd2)) {
                child1.add(currentBoxInInd2);
            }

            if (!child2.contains(currentBoxInInd1)) {
                child2.add(currentBoxInInd1);
            }
        }

        Collections.rotate(child1, start);
        Collections.rotate(child2, start);

        for (int i = 0; i < size; i++) {
            ind1.setGene(i, child2.get(i));
        }

        for (int i = 0; i < size; i++) {
            ind2.setGene(i, child1.get(i));
        }
    }

    @Override
    public String toString(){
        return "OX";
    }
}
