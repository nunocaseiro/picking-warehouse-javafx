package ipleiria.estg.dei.ei.pi.model.geneticAlgorithm;

import ipleiria.estg.dei.ei.pi.model.picking.PathNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodePathList extends ArrayList<PathNode> {

    private HashMap<Double, Integer> contains;
    private HashMap<String, List<TimePair>> nodePairs;

    public NodePathList() {
        this.contains = new HashMap<>();
    }

    @Override
    public boolean add(PathNode node) {
        this.contains.put(node.getTime(), node.getIdentifier());
        return super.add(node);
    }

    public boolean containsNodeAtTime(int nodeNumber, double time) {
        if (this.contains.get(time) == null) {
            return false;
        }

        return nodeNumber == this.contains.get(time);
    }

    public List<TimePair> getPair(PathNode n1, PathNode n2) {
        return this.nodePairs.get(n1.getIdentifier() + "-" + n2.getIdentifier());
    }

    public void populateNodePairsMap() {
        this.nodePairs = new HashMap<>();

        PathNode n1;
        PathNode n2;
        List<TimePair> times;
        for (int i = 0; i < this.size() - 1; i++) {
            n1 = this.get(i);
            n2 = this.get(i + 1);

            times = this.nodePairs.get(n1.getIdentifier() + "-" + n2.getIdentifier());
            if (times != null) {
                times.add(new TimePair(n1.getTime(), n2.getTime(), i));
            } else {
                times = new ArrayList<>();
                times.add(new TimePair(n1.getTime(), n2.getTime(), i));

                this.nodePairs.put(n1.getIdentifier() + "-" + n2.getIdentifier(), times);
            }
        }
    }
}
