package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.recombination;


import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.IntVectorIndividual;

import java.util.Random;

public class RecombinationPartialMapped<I extends IntVectorIndividual<? extends GAProblem>, P extends GAProblem> extends Recombination<I, P> {

    private int[] child1, child2, segment1, segment2;
    private int cut1;
    private int cut2;

    public RecombinationPartialMapped(double probability) {
        super(probability);
    }

    @Override
    public void recombine(I ind1, I ind2, Random random) {
        this.child1 = new int[ind1.getNumGenes()];
        this.child2 = new int[ind2.getNumGenes()];
        this.cut1 = random.nextInt(ind1.getNumGenes());
        this.cut2 = random.nextInt(ind1.getNumGenes());
        if (this.cut1 > this.cut2) {
            int aux = this.cut1;
            this.cut1 = this.cut2;
            this.cut2 = aux;
        }

        create_Segments(this.cut1, this.cut2, ind1, ind2);
        crossOver(this.child1, ind1);
        crossOver(this.child2, ind2);

        for (int i = 0; i < ind1.getNumGenes(); i++) {
            ind1.setGene(i, this.child1[i]);
            ind2.setGene(i, this.child2[i]);
        }
    }

    private boolean check_forDuplicates(int[] offspring, int indexOfElement) {
        for (int index = 0; index < offspring.length; index++) {
            if ((offspring[index] == offspring[indexOfElement]) &&
                    (indexOfElement != index)) {
                return true;
            }
        }
        return false;
    }

    // If Element is Duplicated, replace it by using its mapping //
    private void sort_Duplicates(int[] offspring, int indexOfElement) {
        for (int index = 0; index < this.segment1.length; index++) {
            if (this.segment1[index] == offspring[indexOfElement]) {
                offspring[indexOfElement] = this.segment2[index];
            } else if (this.segment2[index] == offspring[indexOfElement]) {
                offspring[indexOfElement] = this.segment1[index];
            }
        }
    }

    private void create_Segments(int cutPoint1, int cutPoint2, I ind1, I ind2) {
        int capacity_ofSegments = (cutPoint2 - cutPoint1) + 1;
        this.segment1 = new int[capacity_ofSegments];
        this.segment2 = new int[capacity_ofSegments];
        int segment1and2Index = 0;
        for (int index = 0; index < ind1.getNumGenes(); index++) {
            if ((index >= cutPoint1) && (index <= cutPoint2)) {
                int x = ind1.getGene(index);
                int y = ind2.getGene(index);
                this.segment1[segment1and2Index] = x;
                this.segment2[segment1and2Index] = y;
                segment1and2Index++;
            }
        }
    }

    private void insert_Segments(int[] offspring, int[] segment) {
        int segmentIndex = 0;
        for (int index = 0; index < offspring.length; index++) {
            if ((index >= this.cut1) && (index <= this.cut2)) {
                offspring[index] = segment[segmentIndex];
                segmentIndex++;
            }
        }
    }

    // offspring2 gets segment 1, offspring1 gets segment2 //
    public void crossOver(int[] offspring, I ind) {
        if (offspring == this.child1) {
            int[] segment = this.segment2;
            insert_Segments(offspring, segment);
        } else if (offspring == this.child2) {
            int[] segment = this.segment1;
            insert_Segments(offspring, segment);
        }

        for (int index = 0; index < offspring.length; index++) {
            if ((index < this.cut1) || (index > this.cut2)) {
                offspring[index] = ind.getGene(index);
            }
        }

        for (int index = 0; index < offspring.length; index++) {
            if ((index < cut1) || (index > this.cut2)) {
                while (check_forDuplicates(offspring, index)) {
                    sort_Duplicates(offspring, index);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "PMX";
    }
}