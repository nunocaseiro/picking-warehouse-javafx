package ipleiria.estg.dei.ei.pi.model.picking;

import java.util.HashMap;
import java.util.List;

public abstract class Graph<N extends Node> {

    protected HashMap<Integer, List<N>> successors;
    protected HashMap<Integer, N> nodes;
    protected int graphSize;
    protected HashMap<Integer, Edge<N>> edges;

    public Graph() {
        this.successors = new HashMap<>();
    }

    public HashMap<Integer, List<N>> getSuccessors() {
        return this.successors;
    }

    public List<N> getSuccessors(int identifier) {
        return this.successors.get(identifier);
    }

    public HashMap<Integer, N> getNodes() {
        return this.nodes;
    }

    public int getGraphSize() {
        return this.graphSize;
    }

    public HashMap<Integer, Edge<N>> getEdges() {
        return this.edges;
    }
}
