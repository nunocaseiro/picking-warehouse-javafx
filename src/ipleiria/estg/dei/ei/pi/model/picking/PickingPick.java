package ipleiria.estg.dei.ei.pi.model.picking;

import ipleiria.estg.dei.ei.pi.utils.PickLocation;

public class PickingPick extends Node {

    private PickLocation pickLocation;
    private double weight;
    private double capacity;

    public PickingPick(int identifier, double costFromPreviousState, int line, int column, PickLocation pickLocation, double weight, double capacity) {
        super(identifier, costFromPreviousState, line, column);
        this.pickLocation = pickLocation;
        this.weight = weight;
        this.capacity = capacity;
    }

    public PickLocation getPickLocation() {
        return pickLocation;
    }

    public double getWeight() {
        return weight;
    }

    public double getCapacity() {
        return capacity;
    }
}
