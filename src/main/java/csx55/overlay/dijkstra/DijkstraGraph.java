package csx55.overlay.dijkstra;

import csx55.overlay.wireformats.LinkInfo;

import java.util.*;

public class DijkstraGraph {

    private List<LinkInfo> linkInfoList;
    private String nodeName;
    private Set<DijkstraNode> nodes = new HashSet<>();
    private DijkstraNode sourceNode;
    private Map<String, List<ConnectedNode>> linkWeightMap;
    private Set<String> allSinkNodes;

    public DijkstraNode getSourceNode() {
        return this.sourceNode;
    }

    public DijkstraGraph(List<LinkInfo> linkInfoList, String nodeName, Set<String> allSinkNodes) {
        this.linkInfoList = linkInfoList;
        this.nodeName = nodeName;
        this.allSinkNodes = allSinkNodes;
        buildGraph();
        buildLinkWeightMap();
    }

    private void buildLinkWeightMap() {
        this.linkWeightMap = new HashMap<>();
        for (LinkInfo linkInfo : this.linkInfoList) {
            String node1 = linkInfo.getNode1();
            String node2 = linkInfo.getNode2();
            int linkWeight = linkInfo.getLinkWeight();
            ConnectedNode cNode1 = new ConnectedNode(node1, linkWeight);
            ConnectedNode cNode2 = new ConnectedNode(node2, linkWeight);
            if (this.linkWeightMap.containsKey(node1)) {
                this.linkWeightMap.get(node1).add(cNode2);
            }
            else {
                List<ConnectedNode> cNodeList = new ArrayList<>();
                cNodeList.add(cNode2);
                this.linkWeightMap.put(node1, cNodeList);
            }
            if (this.linkWeightMap.containsKey(node2)) {
                this.linkWeightMap.get(node2).add(cNode1);
            }
            else {
                List<ConnectedNode> cNodeList = new ArrayList<>();
                cNodeList.add(cNode1);
                this.linkWeightMap.put(node2, cNodeList);
            }
        }
    }

    public void printLinkWeightMap() {
        System.out.println("{");
        for (String key : this.linkWeightMap.keySet()) {
            List<ConnectedNode> cNodeList = this.linkWeightMap.get(key);
            String cNodeListString = "[";
            for (ConnectedNode cNode : cNodeList) {
                cNodeListString += cNode + " ";
            }
            System.out.println("\t" + key + ": " + cNodeListString + "]");
        }
        System.out.println("}");
    }

    private void buildGraph() {
        Set<String> nodes = new HashSet<>();
        for (LinkInfo linkInfo : this.linkInfoList) {
            String node1 = linkInfo.getNode1();
            String node2 = linkInfo.getNode2();
            nodes.add(node1);
            nodes.add(node2);
            if (!node1.equals(this.nodeName)) this.allSinkNodes.add(node1);
            if (!node2.equals(this.nodeName)) this.allSinkNodes.add(node2);
        }

        Set<DijkstraNode> dijkstraNodes = new HashSet<>();
        for (String node : nodes) {
            DijkstraNode dijkstraNode = new DijkstraNode(node);
            if (node.equals(this.nodeName)) {
                this.sourceNode = dijkstraNode;
                dijkstraNode.setDistance(0);
            }
            dijkstraNodes.add(dijkstraNode);
        }

        for (LinkInfo linkInfo : this.linkInfoList) {
            String node1 = linkInfo.getNode1();
            String node2 = linkInfo.getNode2();
            int linkWeight = linkInfo.getLinkWeight();

            DijkstraNode dijkstraNode1 = findNodeByName(node1, dijkstraNodes);
            DijkstraNode dijkstraNode2 = findNodeByName(node2, dijkstraNodes);
            if (dijkstraNode1 != null && dijkstraNode2 != null) {
                dijkstraNode1.addDestination(dijkstraNode2, linkWeight);
                dijkstraNode2.addDestination(dijkstraNode1, linkWeight);
            }
        }

        for (DijkstraNode node : dijkstraNodes) this.addNode(node);
    }

    private DijkstraNode findNodeByName(String name, Set<DijkstraNode> nodes) {
        for (DijkstraNode dijkstraNode : nodes) {
            if (dijkstraNode.getName().equals(name)) return dijkstraNode;
        }
        return null;
    }

    @Override
    public String toString() {
        String rtn = "Dijkstra's Graph\n----------------\n{\n";
        for (DijkstraNode node : this.nodes) {
            rtn += "\t" + node.toString() + "\n";
        }
        rtn += "}";
        return rtn;
    }

    public void addNode(DijkstraNode node) {
        this.nodes.add(node);
    }

    public Set<DijkstraNode> getNodes() {
        return this.nodes;
    }

    public Map<String, List<ConnectedNode>> getLinkWeightMap() {
        return this.linkWeightMap;
    }


}
