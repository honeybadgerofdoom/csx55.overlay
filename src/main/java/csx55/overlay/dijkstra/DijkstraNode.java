package csx55.overlay.dijkstra;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class DijkstraNode {

    private String name;
    private List<DijkstraNode> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;
    private Map<DijkstraNode, Integer> adjacentNodes = new HashMap<>();

    @Override
    public String toString() {
        String rtn = name + ": Adjacent Nodes: [";
        int index = 0;
        for (DijkstraNode dijkstraNode : adjacentNodes.keySet()) {
            Integer distance = adjacentNodes.get(dijkstraNode);
            rtn += dijkstraNode.getName() + " (" + distance + ")";
            if (index < adjacentNodes.keySet().size() - 1) rtn += ", ";
            index++;
        }
        rtn += "]";
        return rtn;
    }

    public DijkstraNode(String name) {
        this.name = name;
    }

    public void addDestination(DijkstraNode destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public String getName() {
        return name;
    }

    public List<DijkstraNode> getShortestPath() {
        return shortestPath;
    }

    public Integer getDistance() {
        return distance;
    }

    public Map<DijkstraNode, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setShortestPath(List<DijkstraNode> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

}
