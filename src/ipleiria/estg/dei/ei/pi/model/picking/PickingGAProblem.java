package ipleiria.estg.dei.ei.pi.model.picking;

import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.GAProblem;
import ipleiria.estg.dei.ei.pi.model.search.AStarSearch;
import ipleiria.estg.dei.ei.pi.model.search.SearchNode;
import ipleiria.estg.dei.ei.pi.utils.CollisionsHandling;
import ipleiria.estg.dei.ei.pi.utils.WeightLimitation;

import java.util.*;

public class PickingGAProblem extends GAProblem {

    private final int numberPicks;
    private final int numberAgent;
    private final PickingGraph graph;
    private final AStarSearch<Node> searchMethod;
    private final ArrayList<PickingPick> picks;
    private final ArrayList<PickingAgent> agents;
    private final Node offloadArea;
    private HashMap<String, List<SearchNode<Node>>> pairs;
    private final HashMap<Integer, Edge<Node>> aisleNodeEdge;
    private HashMap<Integer, Node> decisionNodesMap;
    private HashMap<Integer, Edge<Node>> edges;
    private final WeightLimitation weightLimitation;
    private final CollisionsHandling collisionsHandling;
    private int timeWeight;
    private int collisionWeight;
    private Random random;


    public PickingGAProblem(PickingGraph graph, AStarSearch<Node> searchMethod, WeightLimitation weightLimitation, CollisionsHandling collisionsHandling, int timeWeight, int collisionWeight, Random random) {
        this.graph = graph;
        this.searchMethod = searchMethod;
        this.weightLimitation = weightLimitation;
        this.collisionsHandling = collisionsHandling;
        this.numberPicks = this.graph.getNumberOfPicks();
        this.numberAgent = this.graph.getNumberOfAgents();
        this.picks = this.graph.getPicks();
        this.agents = this.graph.getAgents();
        this.offloadArea = this.graph.getOffloadArea();
        this.pairs = this.graph.getPairs();
        this.aisleNodeEdge = this.graph.getAisleNodeEdge();
        this.decisionNodesMap = this.graph.getDecisionNodesMap();
        this.edges = this.graph.getEdges();
        this.timeWeight = timeWeight;
        this.collisionWeight = collisionWeight;
        this.random = random;
    }

    public int getTimeWeight() { return timeWeight; }

    public int getCollisionWeight() { return collisionWeight; }

    public int getNumberPicks() {
        return numberPicks;
    }

    public int getNumberAgent() {
        return numberAgent;
    }

    public PickingGraph getGraph() {
        return graph;
    }

    public AStarSearch<Node> getSearchMethod() {
        return searchMethod;
    }

    public ArrayList<PickingPick> getPicks() {
        return picks;
    }

    public ArrayList<PickingAgent> getAgents() {
        return agents;
    }

    public Node getOffloadArea() {
        return offloadArea;
    }

    public HashMap<String, List<SearchNode<Node>>> getPairs() {
        return pairs;
    }

    public WeightLimitation getWeightLimitation() {
        return weightLimitation;
    }

    public CollisionsHandling getCollisionsHandling() {
        return collisionsHandling;
    }

    public HashMap<Integer, Edge<Node>> getAisleNodeEdge() {
        return aisleNodeEdge;
    }

    public HashMap<Integer, Node> getDecisionNodesMap() {
        return decisionNodesMap;
    }

    public HashMap<Integer, Edge<Node>> getEdges() {
        return edges;
    }

    public Random getRandom() {
        return random;
    }
}
