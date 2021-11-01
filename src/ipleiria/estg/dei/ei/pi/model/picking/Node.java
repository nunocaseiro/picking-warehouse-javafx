package ipleiria.estg.dei.ei.pi.model.picking;

import ipleiria.estg.dei.ei.pi.model.search.State;

public class Node extends State {

    protected int line;
    protected int column;

    public Node(int identifier, double costFromPreviousState, int line, int column) {
        super(identifier, costFromPreviousState);
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return this.identifier + " {" + this.line + "," + this.column + "}";
    }
}
