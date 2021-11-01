package ipleiria.estg.dei.ei.pi.model.experiments;

import ipleiria.estg.dei.ei.pi.gui.ExperimentsFrameController;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.utils.exceptions.ValueNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Experiment <E extends ExperimentsFactory, P extends GAProblem> {
    private final E factory;
    private final int numRuns;
    private GeneticAlgorithm ga;
    private P problem;
    private final String experimentTextualRepresentation;
    private final String experimentHeader;
    private final String experimentValues;
    private boolean stopped;
    private ExperimentsFrameController experimentsFrameController;

    public Experiment(
            E factory,
            int numRuns,
            P problem,
            String experimentTextualRepresentation,
            String experimentHeader,
            String experimentValues) {
        this.factory = factory;
        this.numRuns = numRuns;
        this.problem = problem;
        this.experimentTextualRepresentation = experimentTextualRepresentation;
        this.experimentHeader = experimentHeader;
        this.experimentValues = experimentValues;
        this.stopped= false;
    }

    public void run() throws ValueNotFoundException {
        Random random;
        for (int run = 0; run < numRuns; run++) {
            random = new Random(run + 1);
            ga = factory.generateGAInstance(random);
            problem= (P) factory.pickingGAProblem(run + 1, random);
            ga.run(problem);
            if(stopped){
                return;
            }
        }

        fireExperimentEnded();
    }

    public void setStopGa(){
        this.ga.setStopped(true);
        this.stopped=true;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public String getExperimentTextualRepresentation() {
        return experimentTextualRepresentation;
    }

    public String getExperimentHeader() {
        return experimentHeader;
    }

    public String getExperimentValues() {
        return experimentValues;
    }

    //listeners
    final private List<ExperimentListener> listeners = new ArrayList<>(10);

    public synchronized void addExperimentListener(ExperimentListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void fireExperimentEnded() {
        for (ExperimentListener listener : listeners) {
            listener.experimentEnded(new ExperimentEvent(this));
        }
    }

    @Override
    public String toString(){
        return experimentTextualRepresentation;
    }
}
