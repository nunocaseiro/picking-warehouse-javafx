package ipleiria.estg.dei.ei.pi.gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {

    @FXML
    public BorderPane mainsFrame;
    @FXML
    public TabPane tabPane;
    @FXML
    private Button loadLayoutButton;

    @FXML
    private Button loadPicksButton;

    @FXML
    private Button runGaButton;

    @FXML
    private Button stopGAButton;

    @FXML
    private Button simulationButton;

    @FXML
    private Button startPauseButton;

    @FXML
    private Button stepForwardButton;

    @FXML
    private Button stepBackwardButton;

    @FXML
    private AnchorPane simulationFrame;

    @FXML
    private AnchorPane gaFrame;

    @FXML
    private Slider slider;

    @FXML
    private Tab gaTab ;

    @FXML
    private Tab simulationTab;

    @FXML
    private Tab experimentsTab;

    @FXML
    private SimulationFrameController simulationFrameController;

    @FXML
    private GaFrameController gaFrameController;

    @FXML
    private ExperimentsFrameController experimentsFrameController;

    private Alert alert;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        simulationFrameController.init(this);

        ImageView loadWarehouseIcon = new ImageView(new Image(getClass().getResourceAsStream("/loadWarehouseLayoutIcon.png")));
        ImageView loadPicksIcon = new ImageView(new Image(getClass().getResourceAsStream("/loadPicksIcon.png")));

        ImageView gaTabIcon = new ImageView(new Image(getClass().getResourceAsStream("/gaIcon.png")));
        ImageView simulationTabIcon = new ImageView(new Image(getClass().getResourceAsStream("/simulationIcon.png")));
        ImageView experimentsTabIcon = new ImageView(new Image(getClass().getResourceAsStream("/experimentsIcon.png")));

        ImageView runGaIcon = new ImageView(new Image(getClass().getResourceAsStream("/gaRunIcon.png")));
        ImageView stopGaIcon = new ImageView(new Image(getClass().getResourceAsStream("/stopGARunIcon.png")));
        ImageView simulationIcon = new ImageView(new Image(getClass().getResourceAsStream("/simulationRunIcon.png")));

        ImageView pauseIcon = new ImageView(new Image(getClass().getResourceAsStream("/pausePlayIcon.png")));


        loadLayoutButton.setGraphic(loadWarehouseIcon);
        loadPicksButton.setGraphic(loadPicksIcon);

        gaTab.setGraphic(gaTabIcon);
        simulationTab.setGraphic(simulationTabIcon);
        experimentsTab.setGraphic(experimentsTabIcon);

        runGaButton.setGraphic(runGaIcon);
        stopGAButton.setGraphic(stopGaIcon);
        simulationButton.setGraphic(simulationIcon);

        startPauseButton.setGraphic(pauseIcon);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        slider.setPrefWidth((bounds.getWidth()*0.75)/1.40);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainsFrame.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        slider.setPrefWidth(t1.doubleValue()/1.40);
                    }
                });
            }
        });
    }

    public void showAlert(String errors){
        alert= new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.getDialogPane().setPrefWidth(500);
        alert.setContentText(errors);
        alert.showAndWait();
    }

    public void showInformation(String information){
        alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/successIcon.png"))));
        alert.getDialogPane().setPrefWidth(500);
        alert.setContentText(information);
        alert.showAndWait();
    }

    public void manageButtons(boolean loadLayout, boolean loadPicks, boolean runGa, boolean stopGA, boolean playSimulate, boolean pauseStop, boolean slider){
        this.loadLayoutButton.setDisable(loadLayout);
        this.loadPicksButton.setDisable(loadPicks);
        this.runGaButton.setDisable(runGa);
        this.stopGAButton.setDisable(stopGA);
        this.simulationButton.setDisable(playSimulate);
        this.startPauseButton.setDisable(pauseStop);
        this.slider.setDisable(slider);

    }

    public Button getRunGaButton() {
        return runGaButton;
    }


    public void playPause(){
        simulationFrameController.playPause();
    }

    public void playFromSlider(){
        runGaButton.setDisable(true);
        loadLayoutButton.setDisable(true);
        loadPicksButton.setDisable(true);
        simulationFrameController.startFromSlider(slider.getValue());

    }

    public Button getLoadLayoutButton() {
        return loadLayoutButton;
    }

    public Button getLoadPicksButton() {
        return loadPicksButton;
    }

    public Button getStopGAButton() {
        return stopGAButton;
    }

    public Button getSimulationButton() {
        return simulationButton;
    }

    public Button getStartPauseButton() {
        return startPauseButton;
    }

    public Button getStepForwardButton() {
        return stepForwardButton;
    }

    public Button getStepBackwardButton() {
        return stepBackwardButton;
    }

    public Slider getSlider() {
        return slider;
    }

    public GaFrameController getGaFrameController() {
        return gaFrameController;
    }

    public SimulationFrameController getSimulationFrameController() {
        return simulationFrameController;
    }

    public ExperimentsFrameController getExperimentsFrameController() {
        return experimentsFrameController;
    }



}
