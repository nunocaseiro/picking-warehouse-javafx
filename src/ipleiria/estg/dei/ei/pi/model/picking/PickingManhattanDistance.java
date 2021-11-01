package ipleiria.estg.dei.ei.pi.model.picking;

import ipleiria.estg.dei.ei.pi.model.search.Heuristic;

public class PickingManhattanDistance extends Heuristic<Node> {

    @Override
    public double compute(Node state, Node goalState) {
        return Math.abs(goalState.getLine() - state.getLine()) + Math.abs(goalState.getColumn() - state.getColumn());
    }
}
