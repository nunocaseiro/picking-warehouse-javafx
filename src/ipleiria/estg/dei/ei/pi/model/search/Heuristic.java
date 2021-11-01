package ipleiria.estg.dei.ei.pi.model.search;

public abstract class Heuristic<S extends State> {

    public abstract double compute(S state, S goalState);
}
