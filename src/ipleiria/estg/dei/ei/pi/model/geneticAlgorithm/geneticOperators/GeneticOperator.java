package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.geneticOperators;


public abstract class GeneticOperator {

    protected double probability;

    public GeneticOperator(double probability){
        this.probability = probability;
    }
    
    public double getProbability(){
        return this.probability;
    }
}