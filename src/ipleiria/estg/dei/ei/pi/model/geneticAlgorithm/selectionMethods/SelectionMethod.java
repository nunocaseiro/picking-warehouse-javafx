package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.selectionMethods;


import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.Individual;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.Population;

import java.util.Random;

public abstract class SelectionMethod<I extends Individual<? extends GAProblem>, P extends GAProblem>{

    protected int populationSize;
    
    public SelectionMethod(int populationSize){
        this.populationSize = populationSize;
    }

    public abstract Population<I, P> run(Population<I, P> original, Random random);
}