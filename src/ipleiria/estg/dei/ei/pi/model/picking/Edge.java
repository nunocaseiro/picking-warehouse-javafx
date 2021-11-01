package ipleiria.estg.dei.ei.pi.model.picking;

import ipleiria.estg.dei.ei.pi.utils.EdgeDirection;

import java.util.ArrayList;
import java.util.List;

public class Edge<N extends Node> {

    private int edgeNumber;
    private List<N> nodes;
    private  N node1;
    private  N node2;
    private double length;
    private EdgeDirection edgeDirection;

    public Edge(int edgeNumber, double distanceOfNodes, EdgeDirection edgeDirection, N node1, N node2) {
        this.edgeNumber = edgeNumber;
        this.length = distanceOfNodes;
        this.edgeDirection = edgeDirection;
        this.node1 = node1;
        this.node2 = node2;
        this.nodes = new ArrayList<>();
    }

    public int getEdgeNumber() {
        return edgeNumber;
    }

    public List<N> getNodes() {
        return nodes;
    }

    public N getNode1() {
        return node1;
    }

    public N getNode2() {
        return node2;
    }

    public double getLength() {
        return length;
    }

    public EdgeDirection getEdgeDirection() {
        return edgeDirection;
    }

    public void addNode(N node) {
        if (node != null) {
            this.nodes.add(node);
        }
    }
}
