package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm;

public class TimePair {

    private double node1Time;
    private double node2Time;
    private int index;

    public TimePair(double node1Time, double node2Time, int index) {
        this.node1Time = node1Time;
        this.node2Time = node2Time;
        this.index = index;
    }

    public double getNode1Time() {
        return node1Time;
    }

    public double getNode2Time() {
        return node2Time;
    }

    public int getIndex() {
        return index;
    }
}
