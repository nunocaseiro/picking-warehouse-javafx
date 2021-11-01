package ipleiria.estg.dei.ei.pi.model.picking;

import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.*;
import ipleiria.estg.dei.ei.pi.model.search.SearchNode;
import ipleiria.estg.dei.ei.pi.utils.CollisionsHandling;
import ipleiria.estg.dei.ei.pi.utils.EdgeDirection;
import ipleiria.estg.dei.ei.pi.utils.exceptions.NoSolutionFoundException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PickingIndividual extends IntVectorIndividual<PickingGAProblem> {

    private List<PickingAgentPath> paths;
    private double time;
    private int numberTimesOffload;
    private int numberOfCollisions;
    private double waitTime;
    private double maxWaitTime;

    private double weightOnTopOfRestrictionPick;
    private double restrictionPickCapacity;
    private double weightOnAgent;

    public PickingIndividual(PickingGAProblem problem) {
        super(problem);

        int genomeSize = problem.getNumberPicks() + (problem.getNumberAgent() - 1);
        this.genome = new int[genomeSize];

        List<Integer> genes = new LinkedList<>(); // n < 0 -> divisions between paths for different agents | 1 ... x index of the picks in the picks list
        for (int i = 0; i < problem.getNumberAgent() - 1; i++) {
            genes.add(-1 - i);
        }

        for (int i = 0; i < problem.getNumberPicks(); i++) {
            genes.add(i + 1);
        }

        int randomIndex;
        for (int i = 0; i < genomeSize; i++) {
            randomIndex = this.problem.getRandom().nextInt(genes.size());
            this.genome[i] = genes.get(randomIndex);
            genes.remove(randomIndex);
        }
    }

    public PickingIndividual(PickingIndividual pickingIndividual) {
        super(pickingIndividual);
        this.paths = pickingIndividual.paths;
        this.time = pickingIndividual.time;
        this.numberTimesOffload = pickingIndividual.numberTimesOffload;
        this.numberOfCollisions = pickingIndividual.numberOfCollisions;
        this.waitTime = pickingIndividual.waitTime;
        this.maxWaitTime = pickingIndividual.maxWaitTime;
    }

    public List<PickingAgentPath> getPaths() {
        return paths;
    }

    public double getTime() {
        return time;
    }

    public int getNumberOfCollisions() {
        return numberOfCollisions;
    }

    public int getNumberTimesOffload() {
        return numberTimesOffload;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public double getMaxWaitTime() {
        return maxWaitTime;
    }

    @Override
    public void computeFitness() {
        this.paths = new ArrayList<>();
        this.numberTimesOffload = 0;

        List<PickingPick> picks = this.problem.getPicks();
        List<PickingAgent> agents = this.problem.getAgents();
        Node offloadArea = this.problem.getOffloadArea();

        PickingAgentPath pickingAgentPath;
        int i = 0;
        for (PickingAgent agent : agents) {
            pickingAgentPath = new PickingAgentPath();
            pickingAgentPath.addAgentInitialPosition(agent);

            if (i >= this.genome.length || this.genome[i] < 0) { // WHEN THE FIRST ELEMENT OF THE GENOME IS NEGATIVE OR THERE ARE 2 CONSECUTIVE NEGATIVE ELEMENTS IN THE GENOME
                computePath(pickingAgentPath, agent, offloadArea);
                this.paths.add(pickingAgentPath);
                i++;
                continue;
            }

            computePath(pickingAgentPath, agent, picks.get(this.genome[i] - 1));

            this.weightOnTopOfRestrictionPick = 0;
            this.weightOnAgent = picks.get(this.genome[i] - 1).getWeight();
            this.restrictionPickCapacity = picks.get(this.genome[i] - 1).getCapacity();

            while (i < (this.genome.length - 1) && this.genome[i + 1] > 0) {
                this.weightOnAgent += picks.get(this.genome[i + 1] - 1).getWeight();
                handleWeightRestrictions(pickingAgentPath, picks.get(this.genome[i] - 1), picks.get(this.genome[i + 1] - 1), offloadArea, agent);
                i++;
            }

            computePath(pickingAgentPath, picks.get(this.genome[i] - 1), offloadArea);
            this.paths.add(pickingAgentPath);
            i = i + 2;
        }

        this.fitness = 0;
        for (PickingAgentPath path : this.paths) {
            if (this.fitness < path.getValue()) {
                this.fitness = path.getValue();
            }
        }
        this.time = this.fitness;


        detectAndPenalizeCollisions();
    }

    private void handleWeightRestrictions(PickingAgentPath agentPath, PickingPick previousPick, PickingPick nextPick, Node offloadArea, PickingAgent agent) {
        switch (this.problem.getWeightLimitation()) {
            case Picks:
                pickCapacity(agentPath, previousPick, nextPick, offloadArea);
                break;
            case Agents:
                agentCapacity(agentPath, previousPick, nextPick, offloadArea, agent);
                break;
            case Both:
                picksAndAgentCapacity(agentPath, previousPick, nextPick, offloadArea, agent);
                break;
            default:
                computePath(agentPath, previousPick, nextPick);
                break;
        }
    }

    private void pickCapacity(PickingAgentPath agentPath, PickingPick previousPick, PickingPick nextPick, Node offloadArea) {
        if ((this.weightOnTopOfRestrictionPick + nextPick.getWeight()) > this.restrictionPickCapacity) {
            this.numberTimesOffload++;
            computePath(agentPath, previousPick, offloadArea);
            computePath(agentPath, offloadArea, nextPick);

            this.weightOnAgent = nextPick.getWeight();
            this.weightOnTopOfRestrictionPick = 0;
            this.restrictionPickCapacity = nextPick.getCapacity();
        } else {
            this.weightOnTopOfRestrictionPick += nextPick.getWeight();

            if ((this.restrictionPickCapacity - this.weightOnTopOfRestrictionPick) > nextPick.getCapacity()) {
                this.restrictionPickCapacity = nextPick.getCapacity();
                this.weightOnTopOfRestrictionPick = 0;
            }

            computePath(agentPath, previousPick, nextPick);
        }
    }

    private void agentCapacity(PickingAgentPath agentPath, PickingPick previousPick, PickingPick nextPick, Node offloadArea, PickingAgent agent) {
        if (this.weightOnAgent > agent.getCapacity()) {
            this.numberTimesOffload++;
            computePath(agentPath, previousPick, offloadArea);
            computePath(agentPath, offloadArea, nextPick);

            this.weightOnAgent = nextPick.getWeight();
            this.weightOnTopOfRestrictionPick = 0;
            this.restrictionPickCapacity = nextPick.getCapacity();
        } else {
            computePath(agentPath, previousPick, nextPick);
        }
    }

    private void picksAndAgentCapacity(PickingAgentPath agentPath, PickingPick previousPick, PickingPick nextPick, Node offloadArea, PickingAgent agent) {
        if ((this.weightOnTopOfRestrictionPick + nextPick.getWeight()) > this.restrictionPickCapacity || this.weightOnAgent > agent.getCapacity()) {
            this.numberTimesOffload++;
            computePath(agentPath, previousPick, offloadArea);
            computePath(agentPath, offloadArea, nextPick);

            this.weightOnAgent = nextPick.getWeight();
            this.weightOnTopOfRestrictionPick = 0;
            this.restrictionPickCapacity = nextPick.getCapacity();
        } else {
            this.weightOnTopOfRestrictionPick += nextPick.getWeight();

            if ((this.restrictionPickCapacity - this.weightOnTopOfRestrictionPick) > nextPick.getCapacity()) {
                this.restrictionPickCapacity = nextPick.getCapacity();
                this.weightOnTopOfRestrictionPick = 0;
            }

            computePath(agentPath, previousPick, nextPick);
        }
    }

    public void computePath(PickingAgentPath agentPath, Node initialNode, Node goalNode) {
        try {
            if (this.problem.getPairs().containsKey(initialNode.getIdentifier() + "-" + goalNode.getIdentifier())) {
                agentPath.addPath(this.problem.getPairs().get(initialNode.getIdentifier() + "-" + goalNode.getIdentifier()), goalNode);
            } else {
                List<SearchNode<Node>> l = this.problem.getSearchMethod().graphSearch(new PickingSearchProblem(initialNode, goalNode, this.problem.getGraph()));
                l.remove(0);

                agentPath.addPath(l, goalNode);

                this.problem.getPairs().put(initialNode.getIdentifier() + "-" + goalNode.getIdentifier(), l);
            }
        } catch (NoSolutionFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void detectAndPenalizeCollisions() {
        this.numberOfCollisions = 0;
        this.waitTime = 0;
        this.maxWaitTime = 0;

        for (PickingAgentPath agentPath : this.paths) {
            agentPath.populateNodePairsMap();
            agentPath.setTimeWithPenalization(agentPath.getValue());
        }

        // NODE COLLISIONS
        PathNode node;
        for (int i = 0; i < this.paths.size() - 1; i++) {
            for (int j = i + 1; j < this.paths.size(); j++) {
                for (int k = 0; k < this.paths.get(i).getPath().size(); k++) {
                    node = this.paths.get(i).getPath().get(k);
                    if (this.paths.get(j).getPath().containsNodeAtTime(node.getIdentifier(), node.getTime())) {
                        this.numberOfCollisions++;
                        handleNodeCollisions(node, k > 0 ? this.paths.get(i).getPath().get(k - 1) : node, this.paths.get(i), this.paths.get(j));
                    }
                }
            }
        }

        // AISLE COLLISIONS
        List<TimePair> pairs;
        for (int i = 0; i < this.paths.size() - 1; i++) {
            NodePathList path = this.paths.get(i).getPath();
            for (int j = i + 1; j < this.paths.size(); j++) {
                NodePathList path1 = this.paths.get(j).getPath();
                for (int k = 0; k < path.size() - 1; k++) {
                    if (isEdgeOneWay(path.get(k).getIdentifier(), path.get(k + 1).getIdentifier())) {
                        pairs = path1.getPair(path.get(k + 1), path.get(k));
                        if (pairs != null) {
                            for (TimePair timePair : pairs) {
                                if (rangesOverlap(path.get(k).getTime(), path.get(k + 1).getTime(), timePair.getNode1Time(), timePair.getNode2Time())) {
                                    this.numberOfCollisions++;
                                    handleAisleCollisions(path.get(k), this.paths.get(i), path.get(k + 1), timePair.getNode1Time(), this.paths.get(j));
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.problem.getCollisionsHandling() == CollisionsHandling.Type1 && this.problem.getTimeWeight() > 0 && this.problem.getCollisionWeight() > 0) {
            this.fitness = (this.fitness * this.problem.getTimeWeight()) + (this.numberOfCollisions * this.problem.getCollisionWeight());
        }
    }

    private void handleNodeCollisions(PathNode node, PathNode previousNode, PickingAgentPath agent1, PickingAgentPath agent2) {

        if (this.problem.getGraph().getDecisionNodesMap().containsKey(node.getIdentifier())) {

            handleCollisionType(1, agent1, 1, agent2);
        } else {
            Edge<Node> e = this.problem.getGraph().getAisleNodeEdge().get(node.getIdentifier());

            int agent1Distance = Math.abs(node.getLine() - e.getNode1().getLine()) + Math.abs(node.getColumn() - e.getNode1().getColumn());
            int agent2Distance = Math.abs(node.getLine() - e.getNode2().getLine()) + Math.abs(node.getColumn() - e.getNode2().getColumn());

            if (agent1Distance < Math.abs(previousNode.getLine() - e.getNode1().getLine()) + Math.abs(previousNode.getColumn() - e.getNode1().getColumn())) {
                int aux = agent1Distance;
                agent1Distance = agent2Distance;
                agent2Distance = aux;
            }

            handleCollisionType(agent1Distance, agent1, agent2Distance, agent2);
        }
    }

    private void handleAisleCollisions(PathNode node1, PickingAgentPath agent1, PathNode node2, double node2Time, PickingAgentPath agent2) {
        int agent1Distance = 0;
        int agent2Distance = 0;
        double timeDifference = Math.abs(node1.getTime() - node2Time);

        if (node1.getTime() < node2Time) {
            agent1Distance += timeDifference;
        } else {
            agent2Distance += timeDifference;
        }

        int distance = (int) ((Math.abs(node1.getLine() - node2.getLine()) + Math.abs(node1.getColumn() - node2.getColumn())) - timeDifference) / 2;
        agent1Distance += distance;
        agent2Distance += distance;

        Edge<Node> e = null;
        if (!this.problem.getDecisionNodesMap().containsKey(node1.getIdentifier()) || !this.problem.getDecisionNodesMap().containsKey(node2.getIdentifier())) {
            if (!this.problem.getDecisionNodesMap().containsKey(node1.getIdentifier())) {
                e = this.problem.getAisleNodeEdge().get(node1.getIdentifier());
            } else {
                e = this.problem.getAisleNodeEdge().get(node2.getIdentifier());
            }

            if ((Math.abs(node1.getLine() - e.getNode1().getLine()) + Math.abs(node1.getColumn() - e.getNode1().getColumn())) < (Math.abs(node2.getLine() - e.getNode1().getLine()) + Math.abs(node2.getColumn() - e.getNode1().getColumn()))) {
                agent1Distance += Math.abs(node1.getLine() - e.getNode1().getLine()) + Math.abs(node1.getColumn() - e.getNode1().getColumn());
                agent2Distance += Math.abs(node2.getLine() - e.getNode2().getLine()) + Math.abs(node2.getColumn() - e.getNode2().getColumn());
            } else {
                agent1Distance += Math.abs(node1.getLine() - e.getNode2().getLine()) + Math.abs(node1.getColumn() - e.getNode2().getColumn());
                agent2Distance += Math.abs(node2.getLine() - e.getNode1().getLine()) + Math.abs(node2.getColumn() - e.getNode1().getColumn());
            }
        }

        handleCollisionType(agent1Distance, agent1, agent2Distance, agent2);
    }

    private void handleCollisionType(int agent1Distance, PickingAgentPath agent1, int agent2Distance, PickingAgentPath agent2) {
        int distance;
        switch (this.problem.getCollisionsHandling()) {
            case Type2:
                distance = Math.min(agent1Distance, agent2Distance) + 1;
                break;
            case Type3:

                if (agent1.getTimeWithPenalization() + agent1Distance < agent2.getTimeWithPenalization() + agent2Distance) {
                    agent1.addPenalization(agent1Distance);
                    distance = agent1Distance;
                } else {
                    agent2.addPenalization(agent2Distance);
                    distance = agent2Distance;
                }
                distance += 1;

                break;
            default:
                distance = 0;
                break;
        }

        this.fitness += distance;
        this.waitTime += distance;
        if (this.maxWaitTime < distance) {
            this.maxWaitTime = distance;
        }
    }

    private boolean isEdgeOneWay(int node1, int node2) {
        if (this.problem.getGraph().getSubEdges().containsKey(node1 + "-" + node2)) {
            return this.problem.getGraph().getSubEdges().get(node1 + "-" + node2) == EdgeDirection.ONEWAY;
        }
        return this.problem.getGraph().getSubEdges().get(node2 + "-" + node1) == EdgeDirection.ONEWAY;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fitness: ");
        sb.append(fitness).append("\n");
        sb.append("Time: ");
        sb.append(this.time).append("\n");
        sb.append("Number of Collisions: ");
        sb.append(this.numberOfCollisions).append("\n");
        sb.append("Collision Wait Time: ");
        sb.append(this.waitTime).append("\n");
        sb.append("Number of Time Agents Went to Offload: ");
        sb.append(this.numberTimesOffload).append("\n");

        for (int i = 0; i < this.paths.size(); i++) {
            sb.append("\n").append("Agent ").append(i + 1).append(" path time : ");
            sb.append(this.paths.get(i).getValue());
        }

        return sb.toString();
    }



    private boolean rangesOverlap(double x1, double x2, double y1, double y2) {
        return x1 < y2 && y1 < x2;
    }

    /** returns 1 if this is better than individual (this.fitness < individual.getFitness()), returns -1 if this is worst than individual (this.fitness > individual.getFitness()), else returns 0 */
    @Override
    public int compareTo(Individual<? extends GAProblem> individual) {
        return Double.compare(individual.getFitness(), this.fitness);
    }

    @Override
    public PickingIndividual clone() {
        return new PickingIndividual(this);
    }

    public static class PickingIndividualFactory implements Factory<PickingIndividual, PickingGAProblem> {
        @Override
        public PickingIndividual newIndividual(PickingGAProblem problem) {
            return new PickingIndividual(problem);
        }
    }
}
