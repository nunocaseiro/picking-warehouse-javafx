package ipleiria.estg.dei.ei.pi.model.picking;

import java.util.HashMap;
import java.util.List;

public interface EnvironmentListener {

    void updateEnvironment();

    void createEnvironment(List<Node> decisionNodes, HashMap<Integer,Edge<Node>> edges, List<PickingAgent> agents, Node offLoad, int maxLine, int maxCol);

    //void createSimulation();

    void createSimulationPicks(List<PickingPick> pickNodes);
}
