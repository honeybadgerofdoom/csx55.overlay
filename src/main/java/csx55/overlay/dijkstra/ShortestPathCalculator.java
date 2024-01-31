package csx55.overlay.dijkstra;

import java.util.*;

/*
* Code Attribution
*  Dijkstra's Algorithm pseudocode from: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
*  Dijkstra's Algorithm implementation informed from: https://www.baeldung.com/java-dijkstra
*
* Applies to calculateShortestPath() method, and its two private helpers
* */

public class ShortestPathCalculator {

    private DijkstraGraph graph;
    private Map<String, List<String>> pathMap;

    public ShortestPathCalculator(DijkstraGraph graph) {
        this.graph = calculateShortestPath(graph);
        buildPathMap();
    }

    public List<String> getPath(String sink) {
        return this.pathMap.get(sink);
    }

    private void buildPathMap() {
        this.pathMap = new HashMap<>();
        for (DijkstraNode node : this.graph.getNodes()) {
            List<String> pathMembers = new ArrayList<>();
            List<DijkstraNode> shortestPath = node.getShortestPath();
            for (DijkstraNode pathNode : shortestPath) {
                pathMembers.add(pathNode.getName());
            }
            this.pathMap.put(node.getName(), pathMembers);
        }
        for (String key : this.pathMap.keySet()) {
            this.pathMap.get(key).add(key);
        }
    }

    public void printPathMap() {
        String rtn = "{\n";
        Map<String, List<ConnectedNode>> linkWeightMap = this.graph.getLinkWeightMap();
        for (String key : this.pathMap.keySet()) {
            List<String> path = this.pathMap.get(key);
            // path.add(key);
            String pathString = "\t";
            for (int i = 0; i < path.size(); i++) {
                String current = path.get(i);
                if (i < path.size() - 1) {
                    String next = path.get(i + 1);
                    List<ConnectedNode> cNodeList = linkWeightMap.get(path.get(i));
                    ConnectedNode cNode = getConnectedNodeFromName(cNodeList, next);
                    if (cNode != null) {
                        int linkWeight = cNode.getLinkWeight();
                        pathString += current + "--" + linkWeight + "--";
                    }
                    else {
                        System.out.println("Failed to find " + next + " in list of connected nodes for " + current);
                    }
                }
                else {
                    pathString += current;
                }
            }
            rtn += pathString + "\n";
        }
        rtn += "}";
        System.out.println(rtn);
    }

    public ConnectedNode getConnectedNodeFromName(List<ConnectedNode> cNodeList, String name) {
        for (ConnectedNode cNode : cNodeList) {
            if (cNode.getNodeName().equals(name)) return cNode;
        }
        return null;
    }

    public DijkstraGraph calculateShortestPath(DijkstraGraph graph) {

        Set<DijkstraNode> visited = new HashSet<>();
        Set<DijkstraNode> notVisited = new HashSet<>();
        notVisited.add(graph.getSourceNode());

        while (notVisited.size() != 0) {
            DijkstraNode currentNode = getLowestDistanceNode(notVisited);
            notVisited.remove(currentNode);
            for (Map.Entry<DijkstraNode, Integer> adjacencyPair: currentNode.getAdjacentNodes().entrySet()) {
                DijkstraNode adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!visited.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    notVisited.add(adjacentNode);
                }
            }
            visited.add(currentNode);
        }
        return graph;
    }

    private DijkstraNode getLowestDistanceNode(Set<DijkstraNode> notVisited) {
        DijkstraNode lowestDistanceNode = null;
        int smallestWeight = Integer.MAX_VALUE;
        for (DijkstraNode node: notVisited) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < smallestWeight) {
                smallestWeight = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void calculateMinimumDistance(DijkstraNode candidate, Integer linkWeight, DijkstraNode sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + linkWeight < candidate.getDistance()) {
            candidate.setDistance(sourceDistance + linkWeight);
            LinkedList<DijkstraNode> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            candidate.setShortestPath(shortestPath);
        }
    }
    
}