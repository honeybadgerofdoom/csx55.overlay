package csx55.overlay.util;

import csx55.overlay.wireformats.LinkInfo;
import csx55.overlay.wireformats.LinkWeights;
import csx55.overlay.wireformats.MessagingNodesList;

import java.util.*;

public class OverlayCreator {

    private final List<String> nodes;
    private final int numberOfLinks;

    private final int size;
    private int[][] matrix;
    private Map<String, List<ConnectedNode>> overlay;

    public OverlayCreator(List<String> nodes, int numberOfLinks) {
        this.nodes = nodes;
        this.numberOfLinks = numberOfLinks;
        this.size = nodes.size();
    }

    public void printMatrix() {
        System.out.println(getMatrixString());
    }

    public Map<String, List<ConnectedNode>> getOverlay() {
        return this.overlay;
    }

    public void createOverlay() {
        setupMatrix();
        connectMatrixNeighbors();
        fillMatrix();
        matrixToOverlay();
    }

    public Map<String, MessagingNodesList> overlayToMessagingNodesListMap() {
        Map<String, MessagingNodesList> messagingNodeMap = new HashMap<>();
        for (String key : overlay.keySet()) {
            List<ConnectedNode> connectedNodeList = overlay.get(key);
            List<String> partnerNodes = new ArrayList<>();
            for (ConnectedNode connectedNode : connectedNodeList) {
                partnerNodes.add(connectedNode.getName());
            }
            MessagingNodesList messagingNodesList = new MessagingNodesList(partnerNodes);
            messagingNodeMap.put(key, messagingNodesList);
        }
        return messagingNodeMap;
    }

    public LinkWeights buildLinkWeightsMessage() {
        List<LinkInfo> allLinkInfo = new ArrayList<>();
        for (String key : this.overlay.keySet()) {
            List<ConnectedNode> connectedNodeList = this.overlay.get(key);
            for (ConnectedNode connectedNode : connectedNodeList) {
                String partnerString = connectedNode.getName();
                int linkWeight = connectedNode.getWeight();
                LinkInfo linkInfo = new LinkInfo(key, partnerString, linkWeight);
                allLinkInfo.add(linkInfo);
            }
        }
        return new LinkWeights(allLinkInfo);
    }

    public void printOverlay() {
        String overlayString = "{\n";
        for (String key : this.overlay.keySet()) {
            List<ConnectedNode> connectedNodesList = this.overlay.get(key);
            overlayString += "\t" + key + ": " + getConnectedNodeListString(connectedNodesList) + "\n";
        }
        overlayString += "}";
        System.out.println(overlayString);
    }

    private void matrixToOverlay() {
        this.overlay = new HashMap<>();
        for (int i = 0; i < this.size; i++) {
            String node = this.nodes.get(i);
            List<ConnectedNode> connectedNodes = new ArrayList<>();
            for (int j = i; j < this.size; j++) {
                if (i == j) continue;
                int weight = this.matrix[i][j];
                if (weight == 0) continue;
                String partner = this.nodes.get(j);
                ConnectedNode connectedNode = new ConnectedNode(partner, weight);
                connectedNodes.add(connectedNode);
            }
            this.overlay.put(node, connectedNodes);
        }
    }

    private String getMatrixString() {
        String matrixString = "AdjacencyMatrix\n-------------------\n";
        for (int i = 0; i < this.size; i++) {
            String row = "[";
            for (int j = 0; j < this.size; j++) {
                String suffix = "";
                if (j < this.size - 1) {
                    suffix = " ";
                }
                row += this.matrix[i][j] + suffix;
            }
            row += "]";
            if (i < this.size - 1) {
                row += "\n";
            }
            matrixString += row;
        }
        return matrixString;
    }

    private void setupMatrix() {
        this.matrix = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.matrix[i][j] = 0;
            }
        }
    }

    private void connectMatrixNeighbors() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (j <= i) continue; // Ignore bottom-diagonal of matrix...we already set those values
                int newWeight = getRandomWeight();
                if (i == 0 && j == this.size - 1) {
                    this.matrix[0][this.size - 1] = newWeight;
                    this.matrix[this.size - 1][0] = newWeight;
                }
                else if (j == i + 1) {
                    this.matrix[i][j] = newWeight;
                    this.matrix[j][i] = newWeight;
                }
            }
        }
    }

    private void fillMatrix() {
//        int p = this.numberOfLinks;
        int p = this.size - 2;
        while (p > 1 && matrixIsNotFull()) {
            for (int i = 0; i < this.size; i++) {
                if (rowIsFull(i)) continue;
                int j = i + p;
                if (j > this.size - 1) {
                    j = j - this.size;
                }
                int newWeight = getRandomWeight();
                this.matrix[i][j] = newWeight;
                this.matrix[j][i] = newWeight;
            }
            if (matrixIsImbalanced()) {
                rewind(p);
            }
            p--;
        }
    }

    private boolean matrixIsImbalanced() {
        int[] firstRow = this.matrix[0];
        int firstRowCount = 0;
        for (int i : firstRow) {
            if (i > 0) firstRowCount++;
        }
        for (int i = 1; i < this.size; i++) {
            int[] row = this.matrix[i];
            int rowCount = 0;
            for (int j : row) {
                if (j > 0) rowCount++;
            }
            if (rowCount != firstRowCount) return true;
        }
        return false;
    }

    private void rewind(int p) {
        for (int i = 0; i < this.size; i++) {
            int j = i + p;
            if (j > this.size - 1) {
                j = j - this.size;
            }
            this.matrix[i][j] = 0;
            this.matrix[j][i] = 0;
        }
    }

    private boolean matrixIsNotFull() {
        int doneRows = 0;
        for (int i = 0; i < this.size; i++) {
            if (rowIsFull(i)) doneRows++;
        }
        return doneRows != this.size;
    }

    private boolean rowIsFull(int index) {
        int count = 0;
        int[] row = this.matrix[index];
        for (int i : row) {
            if (i > 0) count++;
        }
        return count == this.numberOfLinks;
    }

    public static int getRandomWeight() {
        Random random = new Random(); // TODO Use a seed here coming from ctor?
        return random.nextInt((10 - 1) + 1) + 1;
    }

    public static String getConnectedNodeListString(List<ConnectedNode> list) {
        String listString = "[";
        for (int i = 0; i < list.size(); i++) {
            String suffix = ", ";
            if (i == list.size() - 1) {
                suffix = "";
            }
            listString += list.get(i).toString() + suffix;
        }
        listString += "]";
        return listString;
    }

    private class ConnectedNode {

        private final String name;
        private final int weight;

        public ConnectedNode(String name, int weight) {
            this.name = name;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return name + " (" + weight + ")";
        }

    }
    
}