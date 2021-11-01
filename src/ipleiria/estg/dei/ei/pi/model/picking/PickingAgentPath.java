package ipleiria.estg.dei.ei.pi.model.picking;

import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.NodePathList;
import ipleiria.estg.dei.ei.pi.model.search.SearchNode;
import ipleiria.estg.dei.ei.pi.utils.PickLocation;

import java.util.ArrayList;
import java.util.List;

public class PickingAgentPath {

    private NodePathList path;
    private double value;
    private double timeWithPenalization;

    public PickingAgentPath() {
        this.path = new NodePathList();
        this.value = 0;
    }

    public NodePathList getPath() {
        return path;
    }

    public double getValue() {
        return this.value;
    }

    public void addPath(List<SearchNode<Node>> path, Node node) {
        for (SearchNode<Node> searchNode : path) {
            this.path.add(new PathNode(searchNode.getState().getIdentifier(), searchNode.getState().getLine(), searchNode.getState().getColumn(), this.value + searchNode.getCost(), PickLocation.NONE));
        }

        if (node instanceof PickingPick) {
            if (path.size() >= 1) {
                this.path.get(this.path.size() - 1).setPickLocation(((PickingPick) node).getPickLocation());
            } else {
                this.path.get(this.path.size() - 1).setPickLocation((PickLocation.BOTH));
            }
        }

        if (path.size() >= 1) {
            this.value += path.get(path.size() - 1).getCost();
        }
    }

    public double getTimeWithPenalization() {
        return timeWithPenalization;
    }

    public void addPenalization(double value) {
        this.timeWithPenalization += value;
    }

    public void setTimeWithPenalization(double timeWithPenalization) {
        this.timeWithPenalization = timeWithPenalization;
    }

    public void addAgentInitialPosition(Node node) {
        this.path.add(new PathNode(node.getIdentifier(), node.getLine(), node.getColumn(), 0, PickLocation.NONE));
    }

    public void populateNodePairsMap() {
        this.path.populateNodePairsMap();
    }
}
