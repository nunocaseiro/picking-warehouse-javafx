package ipleiria.estg.dei.ei.pi.gui;

import ipleiria.estg.dei.ei.pi.model.picking.*;
import ipleiria.estg.dei.ei.pi.utils.PickLocation;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.net.URL;
import java.util.*;

import static ipleiria.estg.dei.ei.pi.utils.PickLocation.LEFT;

public class SimulationFrameController implements Initializable, EnvironmentListener {

    public AnchorPane simulationPane;
    public Group group;
    @FXML
    public StackPane simulationStackPane;

    private List<Node> graphDecisionNodes;
    private List<PickingPick> graphPicks;
    private List<PickingAgent> graphAgents;
    private HashMap<Integer,Edge<Node>> graphEdges;
    private Node offload;

    private double NODE_SIZE = 10;
    private double PADDING = 25;
    private double PADDING_BOXES = 35;
    private double PICKS_SIZE = 20;
    private int SPEED = 175;

    private HashMap<String, Rectangle> picks = new HashMap<>();
    private HashMap<Integer, StackPane> nodes = new HashMap<>();
    private HashMap<Integer, StackPane> agents = new HashMap<>();
    private StackPane offLoad;

    private int maxLine;
    private int maxCol;
    private  double maxWidthPane;
    private  double maxHeightPane;
    private int max;
    private PickingIndividual pickingIndividual;
    private boolean firstTime;

    private Timeline timeline;

    private MainFrameController main;

    public void init(MainFrameController mainFrameController){
        main= mainFrameController;
        firstTime=true;
        timeline= new Timeline();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                maxHeightPane=simulationPane.getHeight();
                maxWidthPane=simulationPane.getWidth();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        group = new Group();
    }

    public void createEdge(List<Node> nodes){
        Line l = new Line(this.nodes.get(nodes.get(0).getIdentifier()).getLayoutX()+NODE_SIZE,this.nodes.get(nodes.get(0).getIdentifier()).getLayoutY()+NODE_SIZE
                ,this.nodes.get(nodes.get(1).getIdentifier()).getLayoutX()+NODE_SIZE,this.nodes.get(nodes.get(1).getIdentifier()).getLayoutY()+NODE_SIZE);
        l.setViewOrder(1.0);
        group.getChildren().add(l);
    }

    public void createShelf(Edge e){
        Node node1 = (Node) e.getNodes().get(0);

        for (int i = 1; i < (int) e.getLength(); i++) {
            Rectangle rL = new Rectangle((node1.getColumn() *PADDING)-(PADDING_BOXES),((node1.getLine()*PADDING*0.95)+(i*PADDING*0.95)),PICKS_SIZE,PICKS_SIZE);
            rL.setStroke(Color.BLACK);
            rL.setStrokeType(StrokeType.INSIDE);
            rL.setFill(Color.WHITE);

            Rectangle rR = new Rectangle((node1.getColumn() *PADDING)+(PADDING_BOXES),(node1.getLine()*PADDING*0.95)+(i*PADDING*0.95),PICKS_SIZE,PICKS_SIZE);

            rR.setStroke(Color.BLACK);
            rR.setStrokeType(StrokeType.INSIDE);
            rR.setFill(Color.WHITE);
            rR.setId("1-1-R");

            group.getChildren().add(rL);
            group.getChildren().add(rR);
            picks.put((node1.getLine()+(i)+"-"+node1.getColumn()+"L"),rL);
            picks.put((node1.getLine()+(i)+"-"+node1.getColumn()+"R"),rR);
        }
    }

    public void createLayout(){
        for (int i = 0; i < graphDecisionNodes.size(); i++) {
            createNode(graphDecisionNodes.get(i));
        }
        for (Integer integer : graphEdges.keySet()) {

            createEdge(graphEdges.get(integer).getNodes());

            if(graphEdges.get(integer).getNodes().get(0).getColumn()==graphEdges.get(integer).getNodes().get(1).getColumn()){
                createShelf(graphEdges.get(integer));
            }
        }
        for (Node graphAgent : graphAgents) {
            createNode(graphAgent);
        }
    }

    private void createPicks() {

        for (PickingPick graphPick : graphPicks) {

            String strBuilder = graphPick.getLine()+"-"+graphPick.getColumn();
            if(graphPick.getPickLocation()== LEFT){
                strBuilder=strBuilder+"L";
                picks.get(strBuilder).setFill(Color.GREEN);

            }
            if(graphPick.getPickLocation()== PickLocation.RIGHT){
                strBuilder=strBuilder+"R";
                picks.get(strBuilder).setFill(Color.GREEN);
            }
        }
    }

    public void createNode(Node node){
        Text text = new Text(String.valueOf(node.getIdentifier()));
        Circle circle = new Circle();
        StackPane stackPane = new StackPane();

        if(graphDecisionNodes.contains(node)){
            circle = new Circle(NODE_SIZE, Color.WHITE);
            stackPane.setViewOrder(-1.0);
            nodes.put(node.getIdentifier(),stackPane);
        }

        if(graphAgents.contains(node)){
            text = new Text(String.valueOf(agents.size()+1));
            circle = new Circle(NODE_SIZE, Color.RED);
            stackPane.setViewOrder(-2.0);
            agents.put(agents.size()+1,stackPane);
        }
        circle.setStroke(Color.BLACK);
        stackPane.getChildren().addAll(circle,text);
        stackPane.setLayoutY(node.getLine()*PADDING*0.95);
        stackPane.setLayoutX(node.getColumn()*PADDING);

        this.group.getChildren().add(stackPane);
    }

    public void createOffLoad(Node node){
        Text text = new Text(String.valueOf(node.getIdentifier()));
        Circle circle;
        StackPane stackPane = new StackPane();
        circle = new Circle(NODE_SIZE, Color.BLACK);
        circle.setStroke(Color.BLACK);
        stackPane.getChildren().addAll(circle,text);
        stackPane.setLayoutY(node.getLine()*PADDING*0.95);
        stackPane.setLayoutX(node.getColumn()*PADDING);
        stackPane.setViewOrder(-1.0);
        offLoad=stackPane;
        this.group.getChildren().add(stackPane);
    }

    public void clearAll(){
        simulationStackPane.getChildren().clear();
        group.getChildren().clear();
        agents.clear();
        picks.clear();
        nodes.clear();
    }

    public void start(PickingIndividual individual) {
        pickingIndividual=individual;
        if(firstTime){
        timeline.getKeyFrames().clear();
        for (PickingAgentPath path : individual.getPaths()) {
            if(max<path.getPath().size()){
                max=path.getPath().size();
            }
        }

        for (int i = 0; i < max; i++) {
            for (int i1 = 1; i1 <= individual.getPaths().size(); i1++) {
                if(i<individual.getPaths().get(i1-1).getPath().size()){
                    PathNode node = individual.getPaths().get(i1-1).getPath().get(i);
                    createKeyFrame(timeline, node, i1);
                }
            }
         }
        firstTime=!firstTime;
        }

        ajustBoxesFill(0.0);

        timeline.playFromStart();
    }

    private void createKeyFrame(Timeline t, PathNode node, int i1){
        KeyFrame k;
        KeyFrame k2;
        KeyFrame k3;
        KeyFrame keyFrame;
        switch (node.getPickLocation()){
            case NONE:
                if(nodes.containsKey(node.getIdentifier())){
                    k = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),new KeyValue(agents.get(i1).layoutXProperty(),nodes.get(node.getIdentifier()).getLayoutX()),new KeyValue(agents.get(i1).layoutYProperty(),nodes.get(node.getIdentifier()).getLayoutY()));
                    keyFrame = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),e->setBar());
                }else{
                    if(picks.containsKey(node.getLine()+"-"+node.getColumn()+"L")){
                        k = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),new KeyValue(agents.get(i1).layoutXProperty(),picks.get(node.getLine()+"-"+node.getColumn()+"L").getX()+ PADDING_BOXES),new KeyValue(agents.get(i1).layoutYProperty(),picks.get(node.getLine()+"-"+node.getColumn()+"L").getY()));
                        keyFrame = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),e->setBar());
                    }else{
                        k = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),new KeyValue(agents.get(i1).layoutXProperty(),picks.get(node.getLine()+"-"+node.getColumn()+"R").getX()- PADDING_BOXES),new KeyValue(agents.get(i1).layoutYProperty(),picks.get(node.getLine()+"-"+node.getColumn()+"R").getY()));
                        keyFrame = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),e->setBar());
                    }
                }
                timeline.getKeyFrames().add(k);
                timeline.getKeyFrames().add(keyFrame);
                break;
            case LEFT:
                k = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),new KeyValue(agents.get(i1).layoutXProperty(),picks.get(node.getLine()+"-"+node.getColumn()+"L").getX()+ PADDING_BOXES),new KeyValue(agents.get(i1).layoutYProperty(),picks.get(node.getLine()+"-"+node.getColumn()+"L").getY()));
                k2 = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),e->setPickEmpty(picks.get(node.getLine()+"-"+node.getColumn()+"L")));
                keyFrame = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),e->setBar());
                timeline.getKeyFrames().add(k);
                timeline.getKeyFrames().add(k2);
                timeline.getKeyFrames().add(keyFrame);
                break;
            case RIGHT:
                k = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),new KeyValue(agents.get(i1).layoutXProperty(),picks.get(node.getLine()+"-"+node.getColumn()+"R").getX()- PADDING_BOXES),new KeyValue(agents.get(i1).layoutYProperty(),picks.get(node.getLine()+"-"+node.getColumn()+"R").getY()));
                k2 = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),e->setPickEmpty(picks.get(node.getLine()+"-"+node.getColumn()+"R")));
                keyFrame = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),e->setBar());
                timeline.getKeyFrames().add(k);
                timeline.getKeyFrames().add(k2);
                timeline.getKeyFrames().add(keyFrame);
                break;
            case BOTH:
                k = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),new KeyValue(agents.get(i1).layoutXProperty(),picks.get(node.getLine()+"-"+node.getColumn()+"R").getX()- PADDING_BOXES),new KeyValue(agents.get(i1).layoutYProperty(),picks.get(node.getLine()+"-"+node.getColumn()+"R").getY()));
                k2 = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),e->setPickEmpty(picks.get(node.getLine()+"-"+node.getColumn()+"L")));
                k3 = new KeyFrame(Duration.millis((node.getTime()+1)*SPEED),e->setPickEmpty(picks.get(node.getLine()+"-"+node.getColumn()+"R")));
                keyFrame = new KeyFrame(Duration.millis((node.getTime()+1)*250),e->setBar());
                timeline.getKeyFrames().add(k);
                timeline.getKeyFrames().add(k2);
                timeline.getKeyFrames().add(k3);
                timeline.getKeyFrames().add(keyFrame);

        }
    }

    public void playPause(){
        if(timeline.getStatus()== Animation.Status.RUNNING){
            timeline.pause();
        }else if(timeline.getStatus()== Animation.Status.PAUSED){
            timeline.play();
        }
    }


    public void setBar(){
        main.getSlider().setValue(timeline.getCurrentTime().toMillis());
    }

    private void setPickEmpty(Rectangle pick){
        pick.setFill(Color.WHITE);
    }

    private void ajustBoxesFill(Double time){
        for (int i = 0; i < max; i++) {
            for (int i1 = 1; i1 <= pickingIndividual.getPaths().size(); i1++) {
                if(i<pickingIndividual.getPaths().get(i1-1).getPath().size()){
                    PathNode node = pickingIndividual.getPaths().get(i1-1).getPath().get(i);
                    if(node.getPickLocation()!=PickLocation.NONE){
                        Rectangle rectangle;
                        switch (node.getPickLocation()){
                            case LEFT:
                                rectangle = picks.get(node.getLine()+"-"+node.getColumn()+"L");
                                if((node.getTime()+1)*SPEED<time){
                                    rectangle.setFill(Color.WHITE);
                                }else{
                                    rectangle.setFill(Color.GREEN);
                                }
                                break;
                            case RIGHT:
                                rectangle = picks.get(node.getLine()+"-"+node.getColumn()+"R");
                                if((node.getTime()+1)*SPEED<time){
                                    rectangle.setFill(Color.WHITE);
                                }else{
                                    rectangle.setFill(Color.GREEN);
                                }
                                break;
                            case BOTH:
                                rectangle = picks.get(node.getLine()+"-"+node.getColumn()+"L");
                                Rectangle rectangle1 = picks.get(node.getLine()+"-"+node.getColumn()+"R");
                                if((node.getTime()+1)*SPEED<time){
                                    rectangle.setFill(Color.WHITE);
                                    rectangle1.setFill(Color.WHITE);
                                }else{
                                    rectangle.setFill(Color.GREEN);
                                    rectangle1.setFill(Color.GREEN);
                                }
                                break;
                        }
                    }
                }

            }}
    }


    public void startFromSlider(Double time){
        ajustBoxesFill(time);
        timeline.playFrom(Duration.millis(time));
    }

    public Timeline getTimeline() {
        return timeline;
    }

    @Override
    public void updateEnvironment() {

    }

    @Override
    public void createEnvironment(List<Node> decisionNodes, HashMap<Integer,Edge<Node>> edges, List<PickingAgent> agents, Node offLoad, int mLine, int mCol) {
        simulationStackPane.getChildren().clear();
        group.getChildren().clear();
        nodes.clear();
        picks.clear();
        this.agents.clear();
        this.offLoad=null;
        this.graphDecisionNodes= decisionNodes;
        this.graphEdges=edges;
        this.graphAgents=agents;
        this.maxLine=mLine;
        this.maxCol=mCol;
        adjustSizes();
        createLayout();
        this.offload=offLoad;
        createOffLoad(offLoad);
        this.simulationStackPane.getChildren().add(group);
    }

    private void adjustSizes() {
        this.PADDING = maxHeightPane/maxLine;
        this.NODE_SIZE= PADDING-15;
        this.PICKS_SIZE= NODE_SIZE*2;
        this.PADDING_BOXES = (PICKS_SIZE+(NODE_SIZE/2));
    }


    @Override
    public void createSimulationPicks(List<PickingPick> pickNodes) {
        this.graphPicks= pickNodes;
        createPicks();
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }
}
