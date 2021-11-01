package ipleiria.estg.dei.ei.pi.model.picking;

import com.google.gson.JsonObject;
import ipleiria.estg.dei.ei.pi.model.experiments.Experiment;
import ipleiria.estg.dei.ei.pi.model.experiments.ExperimentsFactory;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.Individual;
import ipleiria.estg.dei.ei.pi.utils.exceptions.InvalidNodeException;
import ipleiria.estg.dei.ei.pi.utils.exceptions.InvalidWarehouseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Environment<I extends Individual<? extends GAProblem>> {

    private PickingGraph graph;
    private I bestInRun;
    private HashMap<String, List<Node>> pairsMap;
    private Boolean pauseGA;
    private JsonObject jsonLayout;
    private ArrayList<EnvironmentListener> environmentListeners;
    private GeneticAlgorithm<PickingIndividual, PickingGAProblem> geneticAlgorithm;
    private Experiment<ExperimentsFactory, GAProblem> experiment;

    public Environment() {
        this.graph = new PickingGraph();
        this.environmentListeners = new ArrayList<>();
        this.jsonLayout = null;
    }

    public void setGeneticAlgorithm(GeneticAlgorithm<PickingIndividual, PickingGAProblem> geneticAlgorithm) {
        this.geneticAlgorithm = geneticAlgorithm;
    }



    public PickingGraph getGraph() {
        return graph;
    }

    public JsonObject getJsonLayout() {
        return jsonLayout;
    }

    public I getBestInRun() {
        return bestInRun;
    }

    public void setBestInRun(I bestInRun) {
        this.bestInRun = bestInRun;
    }


    public void loadWarehouseFile(JsonObject jsonLayout) throws InvalidWarehouseException {
        this.jsonLayout = jsonLayout;
        this.graph.createGeneralGraph(jsonLayout);
        fireCreateEnvironment();
    }

    public Experiment<ExperimentsFactory, GAProblem> getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment<ExperimentsFactory, GAProblem> experiment) {
        this.experiment = experiment;
    }

    public void loadGraph(JsonObject jsonPicks) throws InvalidNodeException, InvalidWarehouseException {
        this.graph.createGraphFromFile(this.jsonLayout, jsonPicks);
        fireCreateEnvironment();
        fireCreateSimulationPicks();
    }

    public synchronized void addEnvironmentListener(EnvironmentListener l) {
        if (!environmentListeners.contains(l)) {
            environmentListeners.add(l);
        }
    }

    public void fireUpdateEnvironment() {
        for (EnvironmentListener listener : environmentListeners) {
            listener.updateEnvironment();
        }
    }

    public void fireCreateEnvironment() {
        for (EnvironmentListener listener : environmentListeners) {
            listener.createEnvironment(graph.getDecisionNodes(),graph.getEdges(),graph.getAgents(),graph.getOffloadArea(),graph.getMaxLine(),graph.getMaxColumn());
        }
    }

    /*public void fireCreateSimulation() {
        for (EnvironmentListener listener : listeners) {
            listener.createSimulation();
        }
    }*/

    public void fireCreateSimulationPicks() {
        for (EnvironmentListener listener : environmentListeners) {
            listener.createSimulationPicks(graph.getPicks());
        }
    }


    public void stopGA() {
        this.geneticAlgorithm.setStopped(true);
    }
}
