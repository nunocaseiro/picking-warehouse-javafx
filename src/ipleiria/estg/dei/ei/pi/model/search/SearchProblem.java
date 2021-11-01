package ipleiria.estg.dei.ei.pi.model.search;

import java.util.List;

public abstract class SearchProblem<S extends State> {

    protected S initialState;
    protected S goalState;

    public SearchProblem(S initialState, S goalState) {
        this.initialState = initialState;
        this.goalState = goalState;
    }

    public S getInitialState() {
        return initialState;
    }

    public S getGoalState() {
        return goalState;
    }

    public abstract boolean isGoal(int identifier);

    public abstract List<S> getSuccessors(int identifier);
}
