package ipleiria.estg.dei.ei.pi.model.search;

import java.util.List;

public class AStarSearch<S extends State> extends InformedSearch<S> {

    public AStarSearch(Heuristic<S> heuristic) {
        super(heuristic);
    }

    @Override
    public void addSuccessorsToFrontier(List<S> successors, SearchNode<S> parent, S goalState) {
        double g;

        for (S s : successors) {
            g = parent.getCost() + s.getCostFromPreviousState();

            if (!this.frontier.containsNode(s.getIdentifier()) && !this.explored.contains(s.getIdentifier())) {
                this.frontier.add(new SearchNode<>(parent, g + this.heuristic.compute(s, goalState), g,  s));
            }
        }
    }
}
