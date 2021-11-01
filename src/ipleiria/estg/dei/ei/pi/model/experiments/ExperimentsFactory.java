package ipleiria.estg.dei.ei.pi.model.experiments;

import ipleiria.estg.dei.ei.pi.gui.ExperimentsFrameController;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.picking.PickingGAProblem;
import javafx.fxml.FXML;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class ExperimentsFactory {
    protected int numRuns;
    protected HashMap<String, Parameter> parameters;
    protected Parameter[] orderedParametersVector;
    protected List<String> statisticsNames;
    protected List<ExperimentListener> statistics;
    protected int countAllRuns;
    private int i=0;
    @FXML
    protected ExperimentsFrameController experimentsController;

    public ExperimentsFactory(ExperimentsFrameController experimentsFrameController){
        this.experimentsController = experimentsFrameController;
        readParametersValues();
        readStatisticsValues();
    }

    public abstract GeneticAlgorithm generateGAInstance(Random random);

    protected abstract PickingGAProblem pickingGAProblem(int seed, Random random);

    protected abstract Experiment<ExperimentsFactory, GAProblem>  buildExperiment();



    private void readParametersValues() {
        i=0;
        countAllRuns=1;
        parameters = new HashMap<>();
        orderedParametersVector = new Parameter[17];
        String[] values = new String[1];
        values[0]= experimentsController.getNrRunsArea().getText();
        numRuns=Integer.parseInt(values[0]);
        Parameter parameter = new Parameter("Runs",values);
        parameters.put("Runs",parameter);
        orderedParametersVector[i++] = parameter;

        addParameter("Population size",experimentsController.getParameters().get("popSizeArea").getParameters());
        addParameter("Max generations",experimentsController.getParameters().get("generationsArea").getParameters());
        addParameter("Selection",experimentsController.getParameters().get("selectionMethodArea").getParameters());
        addParameter("Tournament size",experimentsController.getParameters().get("tournamentSizeArea").getParameters());
        addParameter("Selective pressure",experimentsController.getParameters().get("selectivePressureArea").getParameters());
        addParameter("Recombination",experimentsController.getParameters().get("recombinationMethodArea").getParameters());
        addParameter("Recombination probability",experimentsController.getParameters().get("recombinationProbArea").getParameters());
        addParameter("Mutation",experimentsController.getParameters().get("mutationMethodArea").getParameters());
        addParameter("Mutation probability",experimentsController.getParameters().get("mutationProbArea").getParameters());
        addParameter("Collisions handling",experimentsController.getParameters().get("collisionsHandlingArea").getParameters());
        addParameter("Weight limitation",experimentsController.getParameters().get("weightLimitationArea").getParameters());
        addParameter("Time weight",experimentsController.getParameters().get("timeWeightsArea").getParameters());
        addParameter("Collisions weight",experimentsController.getParameters().get("collisionWeightsArea").getParameters());
        addParameter("NumAgents",experimentsController.getParameters().get("numberAgentsArea").getParameters());
        addParameter("NumPicks",experimentsController.getParameters().get("numberPicksArea").getParameters());

        countAllRuns=countAllRuns*numRuns;

    }

    public void indicesManaging(int i){
        orderedParametersVector[i].activeValueIndex++;
        if (i != 0 && orderedParametersVector[i].activeValueIndex >= orderedParametersVector[i].getNumberOfValues()) {
            orderedParametersVector[i].activeValueIndex = 0;
            indicesManaging(--i);
        }
    }

    public boolean hasMoreExperiments(){
        return orderedParametersVector[0].activeValueIndex < orderedParametersVector[0].getNumberOfValues();
    }

    public Experiment<ExperimentsFactory, GAProblem> nextExperiment() {
        if (hasMoreExperiments()) {
            Experiment<ExperimentsFactory, GAProblem> experiment = buildExperiment();
            indicesManaging(orderedParametersVector.length - 1);
            return experiment;
        }
        return null;
    }



    public void addParameter(String keyName,List<String> listToAdd){
        String[] values= new String[listToAdd.size()];
        for (int i = 0; i < listToAdd.size(); i++) {
            values[i]=listToAdd.get(i);
        }
        Parameter parameter= new Parameter(keyName,values);
        parameters.put(keyName,parameter);
        countAllRuns*=listToAdd.size();
        orderedParametersVector[i++] = parameter;

    }

    private void readStatisticsValues() {
        addParameter("Statistics",experimentsController.getParameters().get("statisticsArea").getParameters());
    }

    protected String getParameterValue(String parameterName){
        if(parameters.get(parameterName)!=null){
            return parameters.get(parameterName).getActiveValue();
        }
        return null;
    }

    public HashMap<String, Parameter> getParameters() {
        return parameters;
    }

    public int getCountAllRuns() {
        return countAllRuns;
    }
}
