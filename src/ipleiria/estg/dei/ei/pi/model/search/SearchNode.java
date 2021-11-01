package ipleiria.estg.dei.ei.pi.model.search;

public class SearchNode<S extends State> implements Comparable<SearchNode<? extends State>> {

    private SearchNode<S> parent;
    private double f; // g + h
    private double cost; // cost of the path from the start node to this node
    private S state; // graph node

    public SearchNode(S state) {
        this(null, 0, 0, state);
    }

    public SearchNode(SearchNode<S> parent, double f, double cost, S state) {
        this.parent = parent;
        this.f = f;
        this.cost = cost;
        this.state = state;
    }

    public SearchNode<S> getParent() {
        return this.parent;
    }

    public double getF() {
        return this.f;
    }

    public double getCost() {
        return this.cost;
    }

    public S getState() {
        return state;
    }

    @Override
    public int compareTo(SearchNode searchNode) {
        return (this.f < searchNode.f) ? -1 : (f == searchNode.f) ? 0 : 1;
    }
}
