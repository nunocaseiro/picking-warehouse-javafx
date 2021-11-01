package ipleiria.estg.dei.ei.pi.model.picking;

public class PickingAgent extends Node {

    private double capacity;

    public PickingAgent(int identifier, double costFromPreviousState, int line, int column, double capacity) {
        super(identifier, costFromPreviousState, line, column);
        this.capacity = capacity;
    }

    public double getCapacity() {
        return capacity;
    }
}
