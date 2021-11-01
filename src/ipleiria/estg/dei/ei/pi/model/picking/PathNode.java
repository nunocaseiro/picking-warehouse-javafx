package ipleiria.estg.dei.ei.pi.model.picking;

import ipleiria.estg.dei.ei.pi.utils.PickLocation;

public class PathNode extends Node {

    private double time;
    private PickLocation pickLocation;

    public PathNode(int identifier, int line, int column, double time, PickLocation pickLocation) {
        super(identifier, 0, line, column);
        this.time = time;
        this.pickLocation = pickLocation;
    }

    public double getTime() {
        return time;
    }

    public PickLocation getPickLocation() {
        return pickLocation;
    }

    public void setPickLocation(PickLocation pickLocation) {
        this.pickLocation = pickLocation;
    }
}
