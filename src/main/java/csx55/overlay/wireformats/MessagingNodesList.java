package csx55.overlay.wireformats;

import java.io.*;
import java.util.*;

public class MessagingNodesList implements Event {

    private int messageType = Protocol.MESSAGING_NODES_LIST;
    private int numberOfPeerMessagingNodes;
    private List<String> info;

    public MessagingNodesList(List<String> info) {
        this.numberOfPeerMessagingNodes = info.size();
        this.info = info;
    }

    public MessagingNodesList(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));

        din.readInt(); // take messageType out of stream

        this.numberOfPeerMessagingNodes = din.readInt();

        this.info = new ArrayList<>();
        for (int i = 0; i < this.numberOfPeerMessagingNodes; i++) {
            int currentInfoLength = din.readInt();
            byte[] currentInfoBytes = new byte[currentInfoLength];
            din.readFully(currentInfoBytes);
            this.info.add(new String(currentInfoBytes));
        }

        bArrayInputStream.close();
        din.close();
    }

    public int getType() {
        return this.messageType;
    }

    public List<String> getInfo() {
        return this.info;
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.messageType);
        dout.writeInt(this.numberOfPeerMessagingNodes);

        for (String partnerInfo : this.info) {
            byte[] infoBytes = partnerInfo.getBytes();
            int elementLength = infoBytes.length;
            dout.writeInt(elementLength);
            dout.write(infoBytes);
        }

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }
    
}