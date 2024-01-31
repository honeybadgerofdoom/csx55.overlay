package csx55.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;

public class LinkInfo {

    private String node1;
    private String node2;
    private int linkWeight;

    public LinkInfo(String node1, String node2, int linkWeight) {
        this.node1 = node1;
        this.node2 = node2;
        this.linkWeight = linkWeight;
    }

    public LinkInfo(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));

        int node1Length = din.readInt();
        byte[] node1Bytes = new byte[node1Length];
        din.readFully(node1Bytes);
        this.node1 = new String(node1Bytes);

        int node2Length = din.readInt();
        byte[] node2Bytes = new byte[node2Length];
        din.readFully(node2Bytes);
        this.node2 = new String(node2Bytes);

        this.linkWeight = din.readInt();

        bArrayInputStream.close();
        din.close();
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        byte[] node1Bytes = this.node1.getBytes();
        int node1Length = node1Bytes.length;
        dout.writeInt(node1Length);
        dout.write(node1Bytes);

        byte[] node2Bytes = this.node2.getBytes();
        int node2Length = node2Bytes.length;
        dout.writeInt(node2Length);
        dout.write(node2Bytes);

        dout.writeInt(this.linkWeight);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }

    public String getNode1() {
        return node1;
    }

    public String getNode2() {
        return node2;
    }

    public int getLinkWeight() {
        return linkWeight;
    }

    @Override
    public String toString() {
        return node1 + " " + node2 + " " + linkWeight;
    }

}
