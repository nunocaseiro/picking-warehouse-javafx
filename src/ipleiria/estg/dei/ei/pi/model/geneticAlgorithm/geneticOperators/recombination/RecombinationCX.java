package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.recombination;


import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.IntVectorIndividual;
import ipleiria.estg.dei.ei.pi.utils.exceptions.ValueNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class RecombinationCX<I extends IntVectorIndividual<? extends GAProblem>, P extends GAProblem> extends Recombination<I, P> {

    public RecombinationCX(double probability) { super(probability); }

    @Override
    public void recombine(I ind1, I ind2, Random random) throws ValueNotFoundException {
        List<Integer> cycle1 = new Vector<Integer>();
        List<Integer> cycle2 = new ArrayList<Integer>();

        int gene1P1 = 0;
        int gene1P2 = 0;
        int start= 0;

        //cycle 1
        gene1P1= ind1.getGene(start);
        gene1P2= ind2.getGene(start);

        int gene2P1= ind1.getGene(ind1.getIndexOf(gene1P2));
        cycle1.add(gene2P1);

        while(gene1P1!=gene2P1){
             gene1P2 = ind2.getGene(ind1.getIndexOf(gene2P1));
             gene2P1 = ind1.getGene(ind1.getIndexOf(gene1P2));
             cycle1.add(gene2P1);
        }

        //cycle 2
        start++;
        gene1P1= ind1.getGene(start);
        gene1P2= ind2.getGene(start);
        while(cycle1.contains(gene1P1)){
            start++;
            if(start>=ind1.getNumGenes()){
                break;
            }
            gene1P1= ind1.getGene(start);
            gene1P2= ind2.getGene(start);
        }

        gene2P1= ind1.getGene(ind1.getIndexOf(gene1P2));
        cycle2.add(gene2P1);

        while(gene1P1!=gene2P1){
            gene1P2 = ind2.getGene(ind1.getIndexOf(gene2P1));
            gene2P1 = ind1.getGene(ind1.getIndexOf(gene1P2));
            cycle2.add(gene2P1);
        }

        //Add cycle 1
        List<Integer> genomeOfChild1 = new Vector<>(ind1.getNumGenes());
        for (int i = 0; i < ind1.getNumGenes(); i++) {
            genomeOfChild1.add(i,0);
        }

        cycle(ind1,ind2,genomeOfChild1,cycle1);

        //Add cycle 2
        List<Integer> genomeOfChild2 = new Vector<>(ind1.getNumGenes());
        for (int i = 0; i < ind1.getNumGenes(); i++) {
            genomeOfChild2.add(i,0);
        }

        cycle(ind1,ind2,genomeOfChild2,cycle2);
        replaceParentsByChild(ind1,genomeOfChild1);
        replaceParentsByChild(ind2,genomeOfChild2);

        cycle1.clear();
        cycle2.clear();
        genomeOfChild1.clear();
        genomeOfChild2.clear();
    }

    private void cycle(I ind1,I ind2, List<Integer> genomeOfChild,List<Integer> cycle1 ) throws ValueNotFoundException {
        for (int i = 0; i < cycle1.size(); i++) {
            int valueOfCycle=cycle1.get(i);
            int indexOfValueInParent1 = ind1.getIndexOf(valueOfCycle);
            genomeOfChild.remove(indexOfValueInParent1);
            genomeOfChild.add(indexOfValueInParent1,valueOfCycle);
        }

        for (int i = 0; i < ind2.getGenome().length; i++) {
            if(!genomeOfChild.contains(ind2.getGene(i))){
                genomeOfChild.remove(i);
                genomeOfChild.add(i,ind2.getGene(i));
            }
        }
    }

    private void replaceParentsByChild(I ind, List<Integer> genomeOfChild){
        for (int i = 0; i < ind.getNumGenes(); i++) {
            ind.setGene(i,genomeOfChild.get(i));
        }

    }




}
