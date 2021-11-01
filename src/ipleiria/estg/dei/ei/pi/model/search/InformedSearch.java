package ipleiria.estg.dei.ei.pi.model.search;

public abstract class InformedSearch<S extends State> extends GraphSearch<NodePriorityQueue, S>{

    protected Heuristic<S> heuristic;

    public InformedSearch(Heuristic<S> heuristic) {
        super(new NodePriorityQueue());
        this.heuristic = heuristic;
    }
}
