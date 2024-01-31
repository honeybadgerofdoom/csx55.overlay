package csx55.overlay.wireformats;

import java.io.*;
import java.util.*;

public class Message implements Event {

    private final int messageType = Protocol.MESSAGE;
    private int payload;
    private int routePlanLength;
    private List<String> routePlan;

    public Message(int payload, List<String> routePlan) {
        this.payload = payload;
        this.routePlanLength = routePlan.size();
        this.routePlan = routePlan;
    }

    public Message(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));

        din.readInt(); // take messageType out of stream

        this.payload = din.readInt();
        this.routePlanLength = din.readInt();

        this.routePlan = new ArrayList<>();
        for (int i = 0; i < this.routePlanLength; i++) {
            int currentNodeLength = din.readInt();
            byte[] currentNodeBytes = new byte[currentNodeLength];
            din.readFully(currentNodeBytes);
            this.routePlan.add(new String(currentNodeBytes));
        }

        bArrayInputStream.close();
        din.close();
    }

    public List<String> getRoutePlan() {
        return this.routePlan;
    }

    public int getPayload() {
        return this.payload;
    }

    public int getType() {
        return this.messageType;
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.messageType);
        dout.writeInt(this.payload);
        dout.writeInt(this.routePlanLength);

        for (String node : this.routePlan) {
            byte[] nodeBytes = node.getBytes();
            int elementLength = nodeBytes.length;
            dout.writeInt(elementLength);
            dout.write(nodeBytes);
        }

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }
    
}