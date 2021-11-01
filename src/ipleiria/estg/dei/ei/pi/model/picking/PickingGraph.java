package ipleiria.estg.dei.ei.pi.model.picking;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ipleiria.estg.dei.ei.pi.model.search.SearchNode;
import ipleiria.estg.dei.ei.pi.utils.EdgeDirection;
import ipleiria.estg.dei.ei.pi.utils.PickLocation;
import ipleiria.estg.dei.ei.pi.utils.exceptions.InvalidNodeException;
import ipleiria.estg.dei.ei.pi.utils.exceptions.InvalidWarehouseException;

import java.util.*;

public class PickingGraph extends Graph<Node> {

    private List<Node> decisionNodes;
    private HashMap<Integer, Node> decisionNodesMap;
    private ArrayList<PickingAgent> agents;
    private Node offloadArea;
    private ArrayList<PickingPick> picks;
    private HashMap<String, EdgeDirection> subEdges;
    private HashMap<String, List<SearchNode<Node>>> pairs;
    private HashMap<Integer, Edge<Node>> aisleNodeEdge;
    private List<Edge<Node>> edgesList;
    private int maxLine;
    private int maxColumn;

    public PickingGraph() {
        this.pairs = new HashMap<>();
    }

    public List<Node> getDecisionNodes() {
        return decisionNodes;
    }

    public ArrayList<PickingPick> getPicks() {

        return picks;
    }

    public int getNumberOfPicks() {
        return this.picks.size();
    }

    public int getMaxLine() {
        return maxLine;
    }

    public int getMaxColumn() {
        return maxColumn;
    }

    public ArrayList<PickingAgent> getAgents() {
        return agents;
    }

    public int getNumberOfAgents() {
        return this.agents.size();
    }

    public Node getOffloadArea() {
        return offloadArea;
    }

    public HashMap<Integer, Edge<Node>> getAisleNodeEdge() {
        return aisleNodeEdge;
    }

    public HashMap<String, EdgeDirection> getSubEdges() {
        return subEdges;
    }

    public HashMap<String, List<SearchNode<Node>>> getPairs() {
        return pairs;
    }

    public HashMap<Integer, Node> getDecisionNodesMap() {
        return decisionNodesMap;
    }

    public void createGraphFromFile(JsonObject jsonLayout, JsonObject jsonPicks) throws InvalidNodeException, InvalidWarehouseException {
        createGeneralGraph(jsonLayout);
        importAgents(jsonLayout);
        importPicks(jsonPicks);
    }

    public void createGraphRandomPicksAndAgents(JsonObject jsonLayout, int seed, int numPicks, int numAgents, int numRuns) throws InvalidNodeException, InvalidWarehouseException {
        createGeneralGraph(jsonLayout);
        generateRandomPicksAndAgents(seed, numPicks, numAgents, numRuns);
    }

    public void createGeneralGraph(JsonObject jsonObject) throws InvalidWarehouseException {
        this.successors = new HashMap<>();
        this.nodes = new HashMap<>();
        this.graphSize = 0;
        this.edges = new HashMap<>();
        this.decisionNodes = new ArrayList<>();
        this.agents = new ArrayList<>();
        this.picks = new ArrayList<>();
        this.subEdges = new HashMap<>();
        this.decisionNodesMap = new HashMap<>();
        this.aisleNodeEdge = new HashMap<>();
        this.edgesList = new ArrayList<>();
//        this.subEdgesSize = 0;

        importDecisionNodes(jsonObject);
        importSuccessors(jsonObject);
        importEdges(jsonObject);
        importOffload(jsonObject);
        importDimensions(jsonObject);
    }

    private void importDimensions(JsonObject jsonObject) {
        this.maxLine = jsonObject.get("maxLine").getAsInt();
        this.maxColumn = jsonObject.get("maxColumn").getAsInt();
    }

    private void importDecisionNodes(JsonObject jsonObject) {
        JsonArray jsonNodes = jsonObject.getAsJsonArray("nodes");

        JsonObject jsonNode;
        Node decisionNode;
        for (JsonElement elementNode : jsonNodes) {
            jsonNode = elementNode.getAsJsonObject();
            decisionNode = new Node(jsonNode.get("nodeNumber").getAsInt(), 0, jsonNode.get("line").getAsInt(), jsonNode.get("column").getAsInt());

            this.nodes.put(jsonNode.get("nodeNumber").getAsInt(), decisionNode);
            this.decisionNodes.add(decisionNode);
            this.decisionNodesMap.put(decisionNode.getIdentifier(), decisionNode);
            this.graphSize++;
        }
    }

    private void importSuccessors(JsonObject jsonObject) throws InvalidWarehouseException {
        JsonArray jsonNodes = jsonObject.getAsJsonArray("nodes");

        JsonObject jsonNode;
        JsonArray jsonSuccessors;
        JsonObject jsonSuccessor;
        Node node;
        for (JsonElement elementNode : jsonNodes) {
            jsonNode = elementNode.getAsJsonObject();

            List<Node> successors = new ArrayList<>();
            this.successors.put(jsonNode.get("nodeNumber").getAsInt(), successors);

            jsonSuccessors = jsonNode.getAsJsonArray("successors");
            for (JsonElement elementSuccessor : jsonSuccessors) {
                jsonSuccessor = elementSuccessor.getAsJsonObject();

                node = this.nodes.get(jsonSuccessor.get("nodeNumber").getAsInt());
                checkNode(node);
                successors.add(new Node(node.getIdentifier(), jsonSuccessor.get("distance").getAsDouble(), node.getLine(), node.getColumn()));
            }
        }
    }

    private void importEdges(JsonObject jsonObject) throws InvalidWarehouseException {
        JsonArray jsonEdges = jsonObject.getAsJsonArray("edges");

        JsonObject jsonEdge;
        Edge<Node> edge;
        Node node1;
        Node node2;
        for (JsonElement elementEdge : jsonEdges) {
            jsonEdge = elementEdge.getAsJsonObject();

            node1 = this.nodes.get(jsonEdge.get("node1Number").getAsInt());
            checkNode(node1);
            node2 = this.nodes.get(jsonEdge.get("node2Number").getAsInt());
            checkNode(node2);

//            node1.addEdge(jsonEdge.get("edgeNumber").getAsInt());
//            node2.addEdge(jsonEdge.get("edgeNumber").getAsInt());

            edge = new Edge<>(jsonEdge.get("edgeNumber").getAsInt(), jsonEdge.get("distance").getAsDouble(), jsonEdge.get("direction").getAsInt() == 1 ? EdgeDirection.ONEWAY : EdgeDirection.TWOWAY, node1, node2);
            edge.addNode(node1);
            edge.addNode(node2);

            this.edgesList.add(edge);
            this.edges.put(jsonEdge.get("edgeNumber").getAsInt(), edge);
            this.subEdges.put(jsonEdge.get("node1Number") +  "-" + jsonEdge.get("node2Number"), jsonEdge.get("direction").getAsInt() == 1 ? EdgeDirection.ONEWAY : EdgeDirection.TWOWAY);
//            this.subEdgesSize++;
        }
    }

    private void importAgents(JsonObject jsonObject) throws InvalidNodeException, InvalidWarehouseException {
        JsonArray jsonAgents = jsonObject.getAsJsonArray("agents");

        JsonObject jsonAgent;
        for (JsonElement elementAgent : jsonAgents) {
            jsonAgent = elementAgent.getAsJsonObject();

            addAgent(jsonAgent.get("edgeNumber").getAsInt(), jsonAgent.get("line").getAsInt(), jsonAgent.get("column").getAsInt(), jsonAgent.get("capacity").getAsDouble());
        }
    }

    private void importOffload(JsonObject jsonObject) throws InvalidWarehouseException {
        Node node = this.nodes.get(jsonObject.get("offloadArea").getAsInt());
        checkNode(node);
        this.offloadArea = new Node(node.getIdentifier(), 0, node.getLine(), node.getColumn());
    }

    private void importPicks(JsonObject jsonObject) throws InvalidNodeException, InvalidWarehouseException {
        this.picks = new ArrayList<>();
        this.pairs = new HashMap<>();

        JsonArray jsonPicks = jsonObject.getAsJsonArray("picks");

        JsonObject jsonPick;
        for (JsonElement elementNode : jsonPicks) {
            jsonPick = elementNode.getAsJsonObject();

            addPick(jsonPick.get("edgeNumber").getAsInt(), jsonPick.get("line").getAsInt(), jsonPick.get("column").getAsInt(), jsonPick.get("location").getAsInt(), jsonPick.get("weight").getAsDouble(), jsonPick.get("capacity").getAsDouble());
        }
    }

    private void generateRandomPicksAndAgents(int seed, int numPicks, int numAgents, int numRuns) throws InvalidNodeException, InvalidWarehouseException {
        HashMap<String, Integer> positionsPicks = new HashMap<>();
        HashMap<String, Integer> positionsAgents = new HashMap<>();

        // GET POSSIBLE POSITIONS
        Node n1;
        Node n2;
        int line;
        int column;
        for (Edge<Node> e : this.edgesList) {
            n1 = e.getNode1();
            n2 = e.getNode2();
            line = n1.getLine();
            column = n1.getColumn();

            if (n1.getColumn() == n2.getColumn()) {
                for (int i = Math.min(n1.getLine(), n2.getLine()) + 1; i < Math.max(n1.getLine(), n2.getLine()); i++) {
                    positionsPicks.put(i + "-" + column, e.getEdgeNumber());
                    positionsAgents.put(i + "-" + column, e.getEdgeNumber());
                }
            } else {
                for (int i = Math.min(n1.getColumn(), n2.getColumn()) + 1; i < Math.max(n1.getColumn(), n2.getColumn()); i++) {
                    positionsAgents.put(line + "-" + i, e.getEdgeNumber());
                }
            }

            if (!positionsAgents.containsKey(line + "-" + column)) {
                positionsAgents.put(line + "-" + column, e.getEdgeNumber());
            }

            if (!positionsAgents.containsKey(n2.getLine() + "-" + n2.getColumn())) {
                positionsAgents.put(n2.getLine() + "-" + n2.getColumn(), e.getEdgeNumber());
            }
        }

        // GENERATE RANDOM PICKS AND AGENTS
        HashSet<String> cells = new HashSet<>();
        Random randomPicks = new Random(seed);
        Random randomAgents = new Random(numRuns + seed);
        int picks = 0;
        int offset;
        int weight;
        int capacity;
        while (picks < numPicks) {
            line = randomPicks.nextInt(this.maxLine + 1);
            column = randomPicks.nextInt(this.maxColumn + 1);
            offset = randomPicks.nextInt(2) == 0 ? -1 : 1;
            weight = randomPicks.nextInt(25) + 1;
            capacity = randomPicks.nextInt(18) + 3;

            if (cells.contains(line + "-" + column + "-" + offset)) {
                continue;
            }

            if (!positionsPicks.containsKey(line + "-" + column)) {
                continue;
            }

            cells.add(line + "-" + column + "-" + offset);
            picks++;
            addPick(positionsPicks.get(line + "-" + column), line, column, offset, weight, weight * capacity);
        }

        int agents = 0;
        while (agents < numAgents) {
            line = randomAgents.nextInt(this.maxLine + 1);
            column = randomAgents.nextInt(this.maxColumn + 1);

            if (!positionsAgents.containsKey(line + "-" + column)) {
                continue;
            }

            agents++;
            addAgent(positionsAgents.get(line + "-" + column), line, column, 75);
        }

        this.pairs = new HashMap<>();
    }

    private void addAgent(int edgeNumber, int line, int column, double capacity) throws InvalidNodeException, InvalidWarehouseException {
        checkEdges(edgeNumber);
        Node node = addNode(edgeNumber, line, column);

        this.agents.add(new PickingAgent(node.getIdentifier(), 0, node.getLine(), node.getColumn(), capacity));
        this.aisleNodeEdge.put(node.getIdentifier(), this.edges.get(edgeNumber));
    }

    private void addPick(int edgeNumber, int line, int column, int location, double weight, double capacity) throws InvalidNodeException, InvalidWarehouseException {
        checkEdges(edgeNumber);
        Node node = addNode(edgeNumber, line, column);

        this.picks.add(new PickingPick(node.getIdentifier(), 0, node.getLine(), node.getColumn(), location == -1 ? PickLocation.LEFT: PickLocation.RIGHT, weight, capacity));
        this.aisleNodeEdge.put(node.getIdentifier(), this.edges.get(edgeNumber));
    }

    private Node addNode(int edgeNumber, int line, int column) throws InvalidNodeException {
        Node node1 = null; // top or left node
        int node1DistanceToNewNode = Integer.MIN_VALUE;
        Node node2 = null; // bottom or right
        int node2DistanceToNewNode = Integer.MAX_VALUE;

        int distance;
        for (Node node : this.edges.get(edgeNumber).getNodes()) {
            distance = (node.getLine() - line) + (node.getColumn() - column);

            if (distance == 0) {
                return node;
            } else if ((distance < 0) && (distance > node1DistanceToNewNode)) {
                node1 = node;
            } else if ((distance > 0) && (distance < node2DistanceToNewNode)) {
                node2 = node;
            }
        }

        if (node1 == null || node2 == null) {
            throw new InvalidNodeException("Could not add node in edge: " + edgeNumber + " line: " + line + " column: " + column);
        }

        // CREATE NEW NODE
        this.graphSize++;
        Node newNode = new Node(this.graphSize, 0, line, column);
        this.nodes.put(this.graphSize, newNode);
        this.edges.get(edgeNumber).addNode(newNode);

        // ADD NEW NODE TO SUCCESSORS
        List<Node> successors = new ArrayList<>();
        this.successors.put(this.graphSize, successors);
        successors.add(new Node(node1.getIdentifier(), Math.abs(node1.getLine() - line) + Math.abs(node1.getColumn() - column), node1.getLine(), node1.getColumn()));
        successors.add(new Node(node2.getIdentifier(), Math.abs(node2.getLine() - line) + Math.abs(node2.getColumn() - column), node2.getLine(), node2.getColumn()));

        // ALTER NODE1 AND NODE2 SUCCESSORS
        removeNodeFromEachOtherSuccessors(node1, node2);
        this.successors.get(node1.getIdentifier()).add(new Node(this.graphSize, Math.abs(node1.getLine() - line) + Math.abs(node1.getColumn() - column), line, column));
        this.successors.get(node2.getIdentifier()).add(new Node(this.graphSize, Math.abs(node2.getLine() - line) + Math.abs(node2.getColumn() - column), line, column));

        // CREATE NEW SUB EDGES
        this.subEdges.put(node1.getIdentifier() + "-" + this.graphSize, this.edges.get(edgeNumber).getEdgeDirection());
        this.subEdges.put(node2.getIdentifier() + "-" + this.graphSize, this.edges.get(edgeNumber).getEdgeDirection());

        return newNode;
    }

    private void removeNodeFromEachOtherSuccessors(Node n1, Node n2) {
        this.successors.get(n1.getIdentifier()).removeIf(node -> node.getIdentifier() == n2.getIdentifier());
        this.successors.get(n2.getIdentifier()).removeIf(node -> node.getIdentifier() == n1.getIdentifier());
    }

    private void checkNode(Node node) throws InvalidWarehouseException {
        if (node == null) {
            throw new InvalidWarehouseException("Invalid Warefouse File");
        }
    }

    private void checkEdges(int number) throws InvalidWarehouseException {
        if (!this.edges.containsKey(number)) {
            throw new InvalidWarehouseException("Trying to insert node into invalid position");
        }
    }
}
