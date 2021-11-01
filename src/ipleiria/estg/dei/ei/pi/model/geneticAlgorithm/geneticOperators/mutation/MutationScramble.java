package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators.mutation;


import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.IntVectorIndividual;
import ipleiria.estg.dei.ei.pi.model.picking.PickingGAProblem;
import ipleiria.estg.dei.ei.pi.utils.exceptions.ValueNotFoundException;

import java.util.LinkedList;
import java.util.Random;

public class MutationScramble<I extends IntVectorIndividual<? extends GAProblem>, P extends GAProblem> extends Mutation<I, P> {

    public MutationScramble(double probability) {
        super(probability);
    }

    @Override
    public void mutate(I individual, Random random) {
        PickingGAProblem gaProblem = (PickingGAProblem) individual.getProblem();
        int min = (gaProblem.getNumberAgent()-1)*-1;
        int cut1 = random.nextInt(individual.getNumGenes());

        int cut2;
        do {

            cut2 = random.nextInt(individual.getNumGenes());
        } while (cut1 == cut2);

        if (cut1 > cut2) {
            int aux = cut1;
            cut1 = cut2;
            cut2 = aux;
        }

        LinkedList<Integer> list = new LinkedList<>();
        for (int j = 0; j <= cut2-cut1; j++) {
            list.add(individual.getGene(cut1+j));
        }

        int num1=-1;
        int num2=-1;

        for(int i = 0; i < list.size(); i++){
            do{
                num1= random.nextInt((individual.getNumGenes()-min)+1)+min;
            }while(!list.contains(num1) || num1==0);

            do{
                num2= random.nextInt((individual.getNumGenes()-min)+1)+min;
            }while(!list.contains(num2) || num2==0);


            int auxNum2 = 0;
            try {
                auxNum2 = individual.getIndexOf(num2);
                individual.setGene(individual.getIndexOf(num1), num2);
            } catch (ValueNotFoundException e) {
                e.printStackTrace();
            }
            individual.setGene(auxNum2, num1);
        }
    }

    @Override
    public String toString() {
        return "Scramble";
    }
}