package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.recombination;


import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.IntVectorIndividual;

import java.util.Random;

public class RecombinationOX1<I extends IntVectorIndividual<? extends GAProblem>, P extends GAProblem> extends Recombination<I, P> {

    public RecombinationOX1(double probability) {
        super(probability);
    }

    int numGenes;

    @Override
    public void recombine(I ind1, I ind2, Random random) {
        this.numGenes = ind1.getNumGenes();

        int cut1 = random.nextInt(this.numGenes - 1);
        int cut2 = random.nextInt(this.numGenes);

        int start = Math.min(cut1, cut2);
        int end = Math.max(cut1, cut2);

        int[] child1 = new int[this.numGenes];
        int[] child2 = new int[this.numGenes];

        insertElementsInsideCutArea(child1, ind2, start, end);
        insertElementsInsideCutArea(child2, ind1, start, end);

        insertElementsOutsideCutArea(child1, ind1, end);
        insertElementsOutsideCutArea(child2, ind2, end);

        for (int i = 0; i < this.numGenes; i++) {
            ind1.setGene(i, child1[i]);
            ind2.setGene(i, child2[i]);
        }
    }

    private boolean contains(int[] child, int value){
        for (int i = 0; i < this.numGenes; i++) {
            if (child[i] == value){
                return true;
            }
        }
        return false;
    }

    private void insertElementsInsideCutArea(int[] child, I parent, int start, int end){
        for (int i = start; i <= end; i++) {
            child[i] = parent.getGene(i);
        }
    }

    private void insertElementsOutsideCutArea(int[] child, I parent, int end){
        int pos = (end + 1) % this.numGenes;
        for (int i = 0; i < this.numGenes; i++) {
            if (child[i] != 0){
                continue;
            }

            while (contains(child, parent.getGene(pos))){
                pos = (pos + 1) % this.numGenes;
            }

            child[i] = parent.getGene(pos);
        }
    }

    @Override
    public String toString(){
        return "OX1";
    }
}
