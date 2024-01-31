package csx55.overlay.dijkstra;

public class ConnectedNode {
    String nodeName;
    int linkWeight;

    public ConnectedNode(String nodeName, int linkWeight) {
        this.nodeName = nodeName;
        this.linkWeight = linkWeight;
    }

    public String getNodeName() {
        return this.nodeName;
    }
    
    public int getLinkWeight() {
        return this.linkWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ConnectedNode)) return false;
        ConnectedNode cN = (ConnectedNode) o;
        return cN.getNodeName().equals(this.getNodeName());
    }

    @Override
    public String toString() {
        return this.nodeName + " (" + this.linkWeight + ")";
    }

}