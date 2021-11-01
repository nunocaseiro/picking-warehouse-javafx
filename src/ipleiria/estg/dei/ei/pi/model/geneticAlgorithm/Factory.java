package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm;

public interface Factory<T extends Individual<? extends GAProblem>, P extends GAProblem> {

    T newIndividual(P problem);
}
