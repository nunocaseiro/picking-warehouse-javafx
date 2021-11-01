package ipleiria.estg.dei.ei.pi.model.experiments;

import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;

public class ExperimentEvent {

    Experiment source;

    public ExperimentEvent(Experiment source) {
        this.source = source;
    }

    public Experiment getSource(){
        return source;
    }
}
