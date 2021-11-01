package ipleiria.estg.dei.ei.pi.model.picking;

import ipleiria.estg.dei.ei.pi.model.search.SearchProblem;

import java.util.List;

public class PickingSearchProblem extends SearchProblem<Node> {

    private PickingGraph graph;

    public PickingSearchProblem(Node initialState, Node goalState, PickingGraph graph) {
        super(initialState, goalState);
        this.graph = graph;
    }

    @Override
    public boolean isGoal(int identifier) {
        return this.goalState.getIdentifier() == identifier;
    }

    @Override
    public List<Node> getSuccessors(int identifier) {
        return this.graph.getSuccessors(identifier);
    }
}
