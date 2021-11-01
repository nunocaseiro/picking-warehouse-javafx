package ipleiria.estg.dei.ei.pi.model.search;

public abstract class State {

    protected int identifier;
    protected double costFromPreviousState;

    public State(int identifier, double costFromPreviousState) {
        this.identifier = identifier;
        this.costFromPreviousState = costFromPreviousState;
    }

    public int getIdentifier() {
        return this.identifier;
    }

    public double getCostFromPreviousState() {
        return this.costFromPreviousState;
    }

    public void setCostFromPreviousState(double costFromPreviousState) {
        this.costFromPreviousState = costFromPreviousState;
    }
}
