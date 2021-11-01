package ipleiria.estg.dei.ei.pi.model.search;

import java.util.HashSet;
import java.util.PriorityQueue;

public class NodePriorityQueue extends PriorityQueue<SearchNode> implements NodeCollection {

    private HashSet<Integer> contains;

    public NodePriorityQueue() {
        this.contains = new HashSet<>();
    }

    @Override
    public boolean add(SearchNode searchNode) {
        contains.add(searchNode.getState().getIdentifier());
        return super.add(searchNode);
    }

    @Override
    public void clear() {
        contains.clear();
        super.clear();
    }

    @Override
    public SearchNode poll() {
        SearchNode searchNode = super.poll();
        if (searchNode != null) {
            contains.remove(searchNode.getState().getIdentifier());
        }

        return searchNode;
    }

    public boolean containsNode(int identifier) {
        return contains.contains(identifier);
    }
}
