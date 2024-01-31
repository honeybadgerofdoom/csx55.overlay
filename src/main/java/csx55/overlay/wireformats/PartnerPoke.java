package csx55.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;

public class PartnerPoke implements Event {

    private final int messageType = Protocol.POKE;
    private String message;

    public PartnerPoke(String message) {
        this.message = message;
    }

    public PartnerPoke(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));

        din.readInt(); // take messageType out of stream

        int messageLength = din.readInt();
        byte[] messageBytes = new byte[messageLength];
        din.readFully(messageBytes);
        this.message = new String(messageBytes);

        bArrayInputStream.close();
        din.close();
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.messageType);

        byte[] messageBytes = this.message.getBytes();
        int elementLength = messageBytes.length;
        dout.writeInt(elementLength);
        dout.write(messageBytes);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }

    public int getType() {
        return this.messageType;
    }

    public String getMessage() {
        return this.message;
    }

}
