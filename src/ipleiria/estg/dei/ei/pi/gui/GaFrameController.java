package ipleiria.estg.dei.ei.pi.gui;

import ipleiria.estg.dei.ei.pi.model.experiments.ExperimentEvent;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAListener;
import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.pi.model.picking.PickingGAProblem;
import ipleiria.estg.dei.ei.pi.model.picking.PickingIndividual;
import ipleiria.estg.dei.ei.pi.utils.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class GaFrameController implements Initializable, GAListener<PickingIndividual, PickingGAProblem> {

    @FXML
    public GridPane parametersGrid;
    @FXML
    public TextField seedGaField;
    @FXML
    public TextField popSizeField;
    @FXML
    public TextField generationsField;
    @FXML
    public TextField tournamentSizeField;
    @FXML
    public TextField selectivePressureField;
    @FXML
    public TextField recombinationProbField;
    @FXML
    public TextField mutationProbField;
    @FXML
    public TextField agentsCapacityField;

    @FXML
    public ChoiceBox<SelectionMethod> selectionMethodFieldSelection;
    @FXML
    public ChoiceBox<RecombinationMethod> recombinationMethodField;
    @FXML
    public ChoiceBox<MutationMethod> mutationMethodField;
    @FXML
    public ChoiceBox<CollisionsHandling> collisionsHandlingFieldCollisions;
    @FXML
    public ChoiceBox<WeightLimitation> weightLimitationField;
    @FXML
    public LineChart<Number,Number> gaChart;
    @FXML
    public TextField timeWeightField;
    @FXML
    public TextField collisionWeightField;
    @FXML
    public TextArea bestInRunArea;

    private XYChart.Series<Number,Number> seriesBestIndividual;
    private XYChart.Series<Number,Number> seriesAverageFitness;

    private UnaryOperator<TextFormatter.Change> integerFilter;
    private UnaryOperator<TextFormatter.Change> decimalFilter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectionMethodFieldSelection.getItems().addAll(SelectionMethod.values());
        selectionMethodFieldSelection.setValue(selectionMethodFieldSelection.getItems().get(0));

        recombinationMethodField.getItems().addAll(RecombinationMethod.values());
        recombinationMethodField.setValue(recombinationMethodField.getItems().get(0));

        mutationMethodField.getItems().addAll(MutationMethod.values());
        mutationMethodField.setValue(mutationMethodField.getItems().get(0));

        weightLimitationField.getItems().addAll(WeightLimitation.values());
        weightLimitationField.setValue(weightLimitationField.getItems().get(0));

        collisionsHandlingFieldCollisions.getItems().addAll(CollisionsHandling.values());
        collisionsHandlingFieldCollisions.setValue(collisionsHandlingFieldCollisions.getItems().get(0));

        seriesBestIndividual = new XYChart.Series<>();
        seriesAverageFitness = new XYChart.Series<>();
        seriesAverageFitness.setName("Average");
        seriesBestIndividual.setName("Best");
        gaChart.getData().add(seriesBestIndividual);
        gaChart.getData().add(seriesAverageFitness);
        

        selectionMethodFieldSelection.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(t1.intValue()==0){
                    tournamentSizeField.setDisable(false);
                    selectivePressureField.setDisable(true);
                }else{
                    tournamentSizeField.setDisable(true);
                    selectivePressureField.setDisable(false);
                }
            }
        });

        collisionsHandlingFieldCollisions.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(t1.intValue()!=0){
                    timeWeightField.setDisable(true);
                    collisionWeightField.setDisable(true);
                }else{
                    timeWeightField.setDisable(false);
                    collisionWeightField.setDisable(false);
                }
            }
        });


        decimalFilter = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                String newText = change.getControlNewText();
                if ((newText.matches("\\d*\\.?\\d*") || newText.matches("\\d*\\,?\\d*")) ) {
                    return change;
                }
                return null;
            }
        };


        integerFilter = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                String newText = change.getControlNewText();
                if (newText.matches("-?([1-9][0-9]*)?") ) {
                    return change;
                }
                return null;
            }
        };

        seedGaField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(),1,integerFilter));
        popSizeField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(),100,integerFilter));
        generationsField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(),100,integerFilter));
        tournamentSizeField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(),4,integerFilter));
        selectivePressureField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(),2.0,decimalFilter));
        recombinationProbField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(),0.8,decimalFilter));
        mutationProbField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(),0.1,decimalFilter));
        timeWeightField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(),1,integerFilter));
        collisionWeightField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(),1,integerFilter));
    }

    public int getTimeWeightField() {
        if (!timeWeightField.getText().isEmpty()){
            return Integer.parseInt(timeWeightField.getText());
        }
        return 0;
    }

    public int getCollisionWeightField() {
        if(!collisionWeightField.getText().isEmpty()) {
            return Integer.parseInt(collisionWeightField.getText());
        }
        return 0;
    }

    public CollisionsHandling getCollisionsHandlingValue(){
            return CollisionsHandling.valueOf(collisionsHandlingFieldCollisions.getSelectionModel().getSelectedItem().toString());
    }

    public WeightLimitation getWeightLimitationValue(){
        return WeightLimitation.valueOf(weightLimitationField.getSelectionModel().getSelectedItem().toString());
    }

    public int getSeedGaField() {
        if(!seedGaField.getText().isEmpty()){
            return Integer.parseInt(seedGaField.getText());
        }
        return 0;
    }

    public int getPopSizeField() {
        if(!popSizeField.getText().isEmpty())
            return Integer.parseInt(popSizeField.getText());
        return 0;
    }

    public int getGenerationsField() {
        if(!generationsField.getText().isEmpty())
            return Integer.parseInt(generationsField.getText());
        return 0;
    }

    public int getTournamentSizeField() {
        if(!tournamentSizeField.getText().isEmpty())
            return Integer.parseInt(tournamentSizeField.getText());
        return 0;
    }

    public double getSelectivePressureField() {
        if(!selectivePressureField.getText().isEmpty())
            return Double.parseDouble(selectivePressureField.getText());
        return 0;
    }

    public double getRecombinationProbField() {
        if(!recombinationProbField.getText().isEmpty())
            return Double.parseDouble(recombinationProbField.getText());
        return 0;
    }

    public double getMutationProbField() {
        if(!mutationProbField.getText().isEmpty())
            return Double.parseDouble(mutationProbField.getText());
        return 0;
    }

    public int getAgentsCapacityField() {
        return Integer.parseInt(agentsCapacityField.getText());
    }

    public SelectionMethod getSelectionMethodFieldSelection() {
        return selectionMethodFieldSelection.getValue();
    }

    public RecombinationMethod getRecombinationMethodField() {
        return recombinationMethodField.getValue();
    }

    public MutationMethod getMutationMethodField() {
        return mutationMethodField.getValue();
    }

    public ChoiceBox<CollisionsHandling> getCollisionsHandlingFieldCollisions() {
        return collisionsHandlingFieldCollisions;
    }

    public ChoiceBox<WeightLimitation> getWeightLimitationField() {
        return weightLimitationField;
    }

    public XYChart.Series<Number, Number> getSeriesBestIndividual() {
        return seriesBestIndividual;
    }

    public XYChart.Series<Number, Number> getSeriesAverageFitness() {
        return seriesAverageFitness;
    }

    public TextArea getBestInRunArea() {
        return bestInRunArea;
    }

    public String handleErrors(){
        StringBuilder error= new StringBuilder();
        if(getSeedGaField()==0 || getSeedGaField()<0 )
            error.append("Seed value").append(errors(1));

        if (getPopSizeField()==0 || getPopSizeField()%2!=0 || getPopSizeField()<0)
            error.append("Population size").append(errors(4));

        if(getGenerationsField()==0 || getGenerationsField()<0)
            error.append("# of generations").append(errors(1));

        if(getTournamentSizeField()==0 || getTournamentSizeField()>getPopSizeField() || getTournamentSizeField()<0)
            error.append("Tournament size").append(errors(5));

        if(getSelectivePressureField()==0 || getSelectivePressureField()<1.0 || getSelectivePressureField()>2.0)
            error.append("Selective pressure").append(errors(2));

        if(getRecombinationProbField()==0 || getRecombinationProbField()<0 || getRecombinationProbField()>1)
            error.append("Recombination probability").append(errors(3));

        if(getMutationProbField()==0 || getMutationProbField()<0 || getMutationProbField()>1)
            error.append("Selective probability").append(errors(3));

        if(getTimeWeightField()==0 || getTimeWeightField()<0)
            error.append("Time weight").append(errors(1));

        if(getCollisionWeightField()==0 || getCollisionWeightField()<0)
            error.append("Collisions weight").append(errors(1));

        if(error.length()==0){
            error.append("success");
        }

        return error.toString();
    }

    public String errors(int i){
        switch (i){
            case 1:
                return " cannot be empty or <= 0 \n";
            case 2:
                return " must be between 1 and 2 and cannot be empty\n";
            case 3:
                return " must be between 0 and 1 and cannot be empty\n";
            case 4:
                return " must be even and cannot be empty or <= 0\n";
            case 5:
                return " must be less than population size and not empty or <= 0\n";

        }
        return null;
    }

    @Override
    public void generationEnded(GeneticAlgorithm<PickingIndividual, PickingGAProblem> geneticAlgorithm) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                seriesBestIndividual.getData().add(new XYChart.Data<>(geneticAlgorithm.getT(),geneticAlgorithm.getBestInRun().getFitness()));
                seriesAverageFitness.getData().add(new XYChart.Data<>(geneticAlgorithm.getT(),geneticAlgorithm.getPopulation().getAverageFitness()));

            }
        });
    }

    @Override
    public void runEnded(GeneticAlgorithm<PickingIndividual, PickingGAProblem> geneticAlgorithm) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            bestInRunArea.setText(geneticAlgorithm.getBestInRun().toString());

            }
        });
    }

    @Override
    public void experimentEnded(ExperimentEvent experimentEvent) {

    }
}
