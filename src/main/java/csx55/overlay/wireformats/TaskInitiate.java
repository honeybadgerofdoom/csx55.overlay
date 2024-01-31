package csx55.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;

public class TaskInitiate implements Event {

    private final int messageType = Protocol.TASK_INITIATE;
    private int numberOfRounds;

    public TaskInitiate(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public TaskInitiate(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));
        din.readInt(); // take messageType out of stream
        this.numberOfRounds = din.readInt();
        bArrayInputStream.close();
        din.close();
    }

    public int getRounds() {
        return numberOfRounds;
    }

    public int getType() {
        return this.messageType;
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.messageType);
        dout.writeInt(this.numberOfRounds);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }

}