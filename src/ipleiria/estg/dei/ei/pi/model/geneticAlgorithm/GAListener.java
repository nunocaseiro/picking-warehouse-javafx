package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm;

import ipleiria.estg.dei.ei.pi.model.experiments.ExperimentListener;

public interface GAListener<I extends Individual<? extends GAProblem>, P extends GAProblem> extends ExperimentListener {

    void generationEnded(GeneticAlgorithm<I, P> geneticAlgorithm);

    void runEnded(GeneticAlgorithm<I, P> geneticAlgorithm);
}
