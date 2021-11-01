package ipleiria.estg.dei.ei.pi.gui;

import ipleiria.estg.dei.ei.pi.model.experiments.ExperimentEvent;
import ipleiria.estg.dei.ei.pi.model.experiments.ParameterGUI;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAListener;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.utils.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;

public class ExperimentsFrameController implements Initializable, GAListener {

    @FXML
    public TextArea popSizeArea;
    @FXML
    public TextArea generationsArea;
    @FXML
    public Button removeButton;
    @FXML
    public Button addButton;
    @FXML
    public GridPane editingParametersPane;
    @FXML
    public TextArea selectionMethodArea;
    @FXML
    public TextArea tournamentSizeArea;
    @FXML
    public TextArea selectivePressureArea;
    @FXML
    public TextArea recombinationMethodArea;
    @FXML
    public TextArea recombinationProbArea;
    @FXML
    public TextArea mutationMethodArea;
    @FXML
    public TextArea mutationProbArea;
    @FXML
    public TextArea collisionsHandlingArea;
    @FXML
    public TextArea weightLimitationArea;
    @FXML
    public TextArea numberAgentsArea;
    @FXML
    public TextArea numberPicksArea;
    @FXML
    public TextArea nrRunsArea;
    @FXML
    public TextArea timeWeightsArea;
    @FXML
    public TextArea collisionWeightsArea;
    @FXML
    public TextArea bestIndividualExperimentsArea;
    @FXML
    public ListView<String> selectExpInput;
    @FXML
    public TextField intExpInput;
    @FXML
    public TextField decimalExpInput;
    @FXML
    public Button runExperimentsButton;
    @FXML
    public ListView<String> actualParameters;
    @FXML
    public Label labelEditingParameter;
    @FXML
    public TextArea statisticsArea;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public GridPane gridRunStop;
    @FXML
    public Button stopExperiments;

    private Alert alert;
    private int runsProgress;
    private double allRuns;
    private String actualParameterField;
    private ParameterGUI actualParameterGUI;
    private HashMap<String,ParameterGUI> parameters;
    private HashMap<String,Object> availableParameters;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        runsProgress=0;
        addButton.prefHeightProperty().bind(editingParametersPane.heightProperty());
        addButton.prefWidthProperty().bind(editingParametersPane.widthProperty());
        removeButton.prefHeightProperty().bind(editingParametersPane.heightProperty());
        removeButton.prefWidthProperty().bind(editingParametersPane.widthProperty());
        stopExperiments.prefWidthProperty().bind(gridRunStop.widthProperty());
        runExperimentsButton.prefWidthProperty().bind(gridRunStop.widthProperty());
        editingParametersPane.setVisible(false);
        parameters= new HashMap<>();

        availableParameters= new HashMap<>();
        List<String> values = new LinkedList<>();
        for (SelectionMethod value : SelectionMethod.values()) {
            values.add(value.toString());
        }
        availableParameters.put("Selection",values);
        values = new LinkedList<>();
        for (RecombinationMethod value : RecombinationMethod.values()) {
            values.add(value.toString());
        }
        availableParameters.put("Recombination",values);
        values = new LinkedList<>();
        for (MutationMethod value : MutationMethod.values()) {
            values.add(value.toString());
        }
        availableParameters.put("Mutation",values);
        values = new LinkedList<>();
        for (CollisionsHandling value : CollisionsHandling.values()) {
            values.add(value.toString());
        }
        availableParameters.put("CollisionsHandling",values);
        values = new LinkedList<>();
        for (WeightLimitation value : WeightLimitation.values()) {
            values.add(value.toString());
        }
        availableParameters.put("WeightLimitations",values);
        values = new LinkedList<>();
        for (Statistics value : Statistics.values()) {
            values.add(value.toString());
        }
        availableParameters.put("Statistics",values);


        parameters.put("nrRunsArea",new ParameterGUI("nrRunsArea",nrRunsArea,intExpInput,"100"));
        parameters.put("popSizeArea",new ParameterGUI("population size",popSizeArea,intExpInput,"100"));
        parameters.put("generationsArea",new ParameterGUI("# of generations",generationsArea,intExpInput,"100"));
        parameters.put("selectionMethodArea",new ParameterGUI("selection method",selectionMethodArea,selectExpInput,"Tournament"));
        parameters.put("tournamentSizeArea",new ParameterGUI("tournament size",tournamentSizeArea,intExpInput,"4"));
        parameters.put("selectivePressureArea",new ParameterGUI("selective pressure",selectivePressureArea,decimalExpInput,"2"));
        parameters.put("recombinationMethodArea",new ParameterGUI("recombination method",recombinationMethodArea,selectExpInput,"PMX"));
        parameters.put("recombinationProbArea",new ParameterGUI("recombination probability",recombinationProbArea,decimalExpInput,"0.7"));
        parameters.put("mutationMethodArea",new ParameterGUI("mutation method",mutationMethodArea,selectExpInput,"Insert"));
        parameters.put("mutationProbArea",new ParameterGUI("mutation probability",mutationProbArea,decimalExpInput,"0.1"));
        parameters.put("collisionsHandlingArea",new ParameterGUI("collisions handling",collisionsHandlingArea,selectExpInput,"Type2"));
        parameters.put("weightLimitationArea",new ParameterGUI("weight limitation",weightLimitationArea,selectExpInput,"Both"));
        parameters.put("timeWeightsArea",new ParameterGUI("time weight",timeWeightsArea,intExpInput,"1"));
        parameters.put("collisionWeightsArea",new ParameterGUI("collisions weight",collisionWeightsArea,intExpInput,"1"));
        parameters.put("numberAgentsArea",new ParameterGUI("number of agents",numberAgentsArea,intExpInput,"3"));
        parameters.put("numberPicksArea",new ParameterGUI("number of picks",numberPicksArea,intExpInput,"45"));
        parameters.put("statisticsArea",new ParameterGUI("statistics",statisticsArea,selectExpInput,"StatisticBestAverage"));

        UnaryOperator<TextFormatter.Change> decimalFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*") || newText.matches("\\d*\\,?\\d*")) {
                return change;
            }
            return null;
        };

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?") ) {
                return change;
            }
            return null;
        };

        nrRunsArea.setFocusTraversable(false);

        nrRunsArea.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(),100,integerFilter));
        intExpInput.setTextFormatter(new TextFormatter<>(integerFilter));
        decimalExpInput.setTextFormatter(new TextFormatter<>(decimalFilter));

        selectExpInput.setViewOrder(-1);
    }

    public String handleErrors(){
        StringBuilder error= new StringBuilder();
        if(getNrRunsAreaInt()==0)
            error.append("Number of runs ").append(errors(1));

        if (getPopSizeArea()==0 )
            error.append("Population size").append(errors(1));

        if(getGenerationsArea()==0)
            error.append("# of generations").append(errors(1));

        if(getSelectionMethodArea()==0 )
            error.append("Selection method").append(errors(1));

        if(getTournamentSizeArea()==0 )
            error.append("Tournament size").append(errors(1));

        if(getSelectivePressureArea()==0 )
            error.append("Selective pressure").append(errors(1));

        if(getRecombinationMethodArea()==0 )
            error.append("Recombination method").append(errors(1));

        if(getRecombinationProbArea()==0)
            error.append("Recombination probability").append(errors(3));

        if(getMutationMethodArea()==0 )
            error.append("Mutation method").append(errors(1));

        if(getMutationProbArea()==0)
            error.append("Mutation probability").append(errors(3));

        if(getCollisionsHandlingArea()==0)
            error.append("Collisions handling").append(errors(1));

        if(getWeightLimitationArea()==0)
            error.append("Weight limitation").append(errors(1));

        if(getTimeWeightsArea()==0)
            error.append("Time weight").append(errors(1));

        if(getCollisionWeightsArea()==0)
            error.append("Collisions weight").append(errors(1));

        if(getNumberAgentsArea()==0)
            error.append("Number of agents").append(errors(1));

        if(getNumberPicksArea()==0)
            error.append("Number of agents").append(errors(1));

        if(getStatisticsArea()==0)
            error.append("Statistics ").append(errors(1));

        if(error.length()==0){
            error.append("success");
        }

        return error.toString();
    }


    public String errors(int i){
        switch (i){
            case 1:
                return " cannot be empty or 0\n";
            case 2:
                return " must be between 1 and 2 and cannot be empty\n";
            case 3:
                return " must be between 0 and 1 and cannot be empty\n";
            case 4:
                return " must be even and cannot be empty\n";
            case 5:
                return " must be less than population size and not empty\n";

        }
        return null;
    }

    public void showEditParameters(Event event){
        editingParametersPane.setVisible(true);
        actualParameterField=((Control)event.getSource()).getId();
        actualParameterGUI= parameters.get(actualParameterField);
        actualParameters.getItems().clear();
        selectExpInput.getItems().clear();
        labelEditingParameter.setText("Editing "+ actualParameterGUI.getId());

        if(actualParameterGUI.getControl().getId().equals(selectExpInput.getId())) {
            selectExpInput.setViewOrder(-1);
            intExpInput.setViewOrder(0);
            decimalExpInput.setViewOrder(0);

            List<String> l = actualParameterGUI.getParameters();
            List<String> available;
            switch (actualParameterField){
                case "selectionMethodArea":
                    available  = (List<String>) availableParameters.get("Selection");
                    available.removeIf(l::contains);

                    selectExpInput.getItems().addAll((Collection<? extends String>) availableParameters.get("Selection"));
                    break;
                case "recombinationMethodArea":
                    available = (List<String>) availableParameters.get("Recombination");
                    available.removeIf(l::contains);
                    selectExpInput.getItems().addAll((Collection<? extends String>) availableParameters.get("Recombination"));
                    break;
                case "mutationMethodArea":
                    available = (List<String>) availableParameters.get("Mutation");
                    available.removeIf(l::contains);
                    selectExpInput.getItems().addAll((Collection<? extends String>) availableParameters.get("Mutation"));
                    break;
                case "collisionsHandlingArea":
                    available = (List<String>) availableParameters.get("CollisionsHandling");
                    available.removeIf(l::contains);
                    selectExpInput.getItems().addAll((Collection<? extends String>) availableParameters.get("CollisionsHandling"));
                    break;
                case "weightLimitationArea":
                    available = (List<String>) availableParameters.get("WeightLimitations");
                    available.removeIf(l::contains);
                    selectExpInput.getItems().addAll((Collection<? extends String>) availableParameters.get("WeightLimitations"));
                    break;
                case "statisticsArea":
                    available = (List<String>) availableParameters.get("Statistics");
                    available.removeIf(l::contains);
                    selectExpInput.getItems().addAll((Collection<? extends String>) availableParameters.get("Statistics"));
                    break;
            }
            actualParameters.getItems().addAll(actualParameterGUI.getParameters());
        }

        if(actualParameterGUI.getControl().getId().equals(intExpInput.getId())) {
            selectExpInput.setViewOrder(0);
            intExpInput.setViewOrder(-1);
            decimalExpInput.setViewOrder(0);
            TextField t = (TextField) actualParameterGUI.getControl();
            t.setPromptText("Insert " + actualParameterGUI.getId() + " value");
            actualParameters.getItems().addAll(actualParameterGUI.getParameters());
        }
        if(actualParameterGUI.getControl().getId().equals(decimalExpInput.getId())) {
            selectExpInput.setViewOrder(0);
            intExpInput.setViewOrder(0);
            decimalExpInput.setViewOrder(-1);
            TextField t = (TextField) actualParameterGUI.getControl();
            t.setPromptText("Insert " + actualParameterGUI.getId() + " value");
            actualParameters.getItems().addAll(actualParameterGUI.getParameters());
        }
    }

    public void add(){
        if(actualParameterGUI.getControl().getId().equals(selectExpInput.getId())){
            if(!actualParameterGUI.getParameters().contains(selectExpInput.getSelectionModel().getSelectedItem()) && selectExpInput.getSelectionModel().getSelectedItem()!=null){
                actualParameterGUI.getParameters().add(selectExpInput.getSelectionModel().getSelectedItem());
                List<String> available;
                switch (actualParameterField){
                    case "selectionMethodArea":
                        available  = (List<String>) availableParameters.get("Selection");
                        available.remove(selectExpInput.getSelectionModel().getSelectedItem());

                        break;
                    case "recombinationMethodArea":
                        available = (List<String>) availableParameters.get("Recombination");
                        available.remove(selectExpInput.getSelectionModel().getSelectedItem());

                        break;
                    case "mutationMethodArea":
                        available = (List<String>) availableParameters.get("Mutation");
                        available.remove(selectExpInput.getSelectionModel().getSelectedItem());

                        break;
                    case "collisionsHandlingArea":
                        available = (List<String>) availableParameters.get("CollisionsHandling");
                        available.remove(selectExpInput.getSelectionModel().getSelectedItem());

                        break;
                    case "weightLimitationArea":
                        available = (List<String>) availableParameters.get("WeightLimitations");
                        available.remove(selectExpInput.getSelectionModel().getSelectedItem());

                        break;
                    case "statisticsArea":
                        available = (List<String>) availableParameters.get("Statistics");
                        available.remove(selectExpInput.getSelectionModel().getSelectedItem());
                        break;
                }
                selectExpInput.getItems().remove(selectExpInput.getSelectionModel().getSelectedItem());
            }
            updateActualItems();
        }
        if(actualParameterGUI.getControl().getId().equals(intExpInput.getId())) {
            if(!actualParameterGUI.getParameters().contains(intExpInput.getText().trim()) && !intExpInput.getText().trim().isEmpty() && Integer.parseInt(intExpInput.getText().trim())>0 ){
                if ((actualParameterGUI.getId().equals("population size") && Integer.parseInt(intExpInput.getText().trim())%2!=0)){
                    showAlert("Value must be even and cannot be <= 0");
                }else{
                    actualParameterGUI.getParameters().add(intExpInput.getText().trim());
                }
            }else{
                showAlert("Value already exists on actual parameters or cannot be <= 0");
            }
            updateActualItems();
        }
        if(actualParameterGUI.getControl().getId().equals(decimalExpInput.getId())) {
            if(!actualParameterGUI.getParameters().contains(decimalExpInput.getText().trim()) && !decimalExpInput.getText().trim().isEmpty()){
                if(Double.parseDouble(decimalExpInput.getText().trim())==0){
                    showAlert("Value cannot be 0");
                }else if(actualParameterGUI.getId().equals("selective pressure") && Double.parseDouble(decimalExpInput.getText().trim())<1.0 || Double.parseDouble(decimalExpInput.getText().trim())>2.0){
                    showAlert("Value must be between 1.0 and 2.0");
                } else if ((actualParameterGUI.getId().equals("recombination probability") || actualParameterGUI.getId().equals("mutation probability")) && (Double.parseDouble(decimalExpInput.getText().trim())<0.0 || Double.parseDouble(decimalExpInput.getText().trim())>1.0) ){
                    showAlert("Value must be between 0.0 and 1.0");
                }else{

                    actualParameterGUI.getParameters().add(decimalExpInput.getText().trim());
                }
            }
            updateActualItems();
        }
        intExpInput.clear();
        decimalExpInput.clear();
        actualParameterGUI.getTextArea().setText(listsToString(actualParameterGUI.getParameters()));
    }

    public void remove() {
        actualParameterGUI.getParameters().remove(actualParameters.getSelectionModel().getSelectedItem());
        List<String> available;
        switch (actualParameterField) {
            case "selectionMethodArea":
                available = (List<String>) availableParameters.get("Selection");
                if(actualParameters.getSelectionModel().getSelectedItem()!=null){
                    available.add(actualParameters.getSelectionModel().getSelectedItem());
                }
                break;
            case "recombinationMethodArea":
                available = (List<String>) availableParameters.get("Recombination");
                if(actualParameters.getSelectionModel().getSelectedItem()!=null){
                    available.add(actualParameters.getSelectionModel().getSelectedItem());
                }

                break;
            case "mutationMethodArea":
                available = (List<String>) availableParameters.get("Mutation");
                if(actualParameters.getSelectionModel().getSelectedItem()!=null){
                    available.add(actualParameters.getSelectionModel().getSelectedItem());
                }

                break;
            case "collisionsHandlingArea":
                available = (List<String>) availableParameters.get("CollisionsHandling");
                if(actualParameters.getSelectionModel().getSelectedItem()!=null) {
                    available.add(actualParameters.getSelectionModel().getSelectedItem());
                }
                break;
            case "weightLimitationArea":
                available = (List<String>) availableParameters.get("WeightLimitations");
                if(actualParameters.getSelectionModel().getSelectedItem()!=null) {
                    available.add(actualParameters.getSelectionModel().getSelectedItem());
                }
                break;
            case "statisticsArea":
                available = (List<String>) availableParameters.get("Statistics");
                if(actualParameters.getSelectionModel().getSelectedItem()!=null){

                    available.add(actualParameters.getSelectionModel().getSelectedItem());
                }

                break;
        }
        if(actualParameters.getSelectionModel().getSelectedItem()!=null){

            selectExpInput.getItems().add(actualParameters.getSelectionModel().getSelectedItem());
        }
        updateActualItems();
        actualParameterGUI.getTextArea().setText(listsToString(actualParameterGUI.getParameters()));
    }

    public void updateActualItems(){
        actualParameters.getItems().clear();
        if(actualParameterGUI.getParameters().size()!=0){
            actualParameters.getItems().addAll(actualParameterGUI.getParameters());
        }
    }

    public void showAlert(String errors){
        alert= new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.getDialogPane().setPrefWidth(500);
        alert.setContentText(errors);
        alert.showAndWait();
    }


    public HashMap<String, ParameterGUI> getParameters() {
        return parameters;
    }

    private String listsToString(List<String> list){
        StringBuilder sb = new StringBuilder();
        int i=0;
        for (String o : list) {
            sb.append(o);
            if(list.size()>1 && i!=list.size()-1){
                sb.append(", ");
            }
            i++;
        }
        return sb.toString();
    }

    public Button getRunExperimentsButton() {
        return runExperimentsButton;
    }


    public TextArea getNrRunsArea() {
        return nrRunsArea;
    }


    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public int getRunsProgress() {
        return runsProgress;
    }

    public void setRunsProgress(int runsProgress) {
        this.runsProgress = runsProgress;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setAllRuns(double allRuns) {
        this.allRuns = allRuns;
    }

    public int getNrRunsAreaInt() {
        if(!nrRunsArea.getText().isEmpty())
            return Integer.parseInt(nrRunsArea.getText());
        return 0;
    }

    public int getPopSizeArea() {
        if(!popSizeArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getGenerationsArea() {
        if(!generationsArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getSelectionMethodArea() {
        if(!selectionMethodArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getTournamentSizeArea() {
        if(!tournamentSizeArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public double getSelectivePressureArea() {
        if(!selectivePressureArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getRecombinationMethodArea() {
        if(!recombinationMethodArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public double getRecombinationProbArea() {
        if(!recombinationProbArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getMutationMethodArea() {
        if(!mutationMethodArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public double getMutationProbArea() {
        if(!mutationProbArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getCollisionsHandlingArea() {
        if(!collisionsHandlingArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getWeightLimitationArea() {
        if(!weightLimitationArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getNumberAgentsArea() {
        if(!numberAgentsArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getNumberPicksArea() {
        if(!numberPicksArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getTimeWeightsArea() {
        if(!timeWeightsArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getCollisionWeightsArea() {
        if(!collisionWeightsArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public int getStatisticsArea() {
        if(!statisticsArea.getText().isEmpty())
            return 1;
        return 0;
    }

    public Button getStopExperiments() {
        return stopExperiments;
    }



    @Override
    public void generationEnded(GeneticAlgorithm geneticAlgorithm) {

    }

    @Override
    public void runEnded(GeneticAlgorithm geneticAlgorithm) {
        runsProgress++;
        progressBar.setProgress(runsProgress/allRuns);

    }

    @Override
    public void experimentEnded(ExperimentEvent experimentEvent) {

    }
}
