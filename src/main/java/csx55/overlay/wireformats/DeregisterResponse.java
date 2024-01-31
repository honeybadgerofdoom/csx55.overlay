package csx55.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

public class DeregisterResponse implements Event {

    private final int messageType = Protocol.DEREGISTER_RESPONSE;
    private byte statusCode;

    public DeregisterResponse(byte statusCode) {
        this.statusCode = statusCode;
    }

    public DeregisterResponse(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));

        din.readInt(); // take messageType out of stream
        this.statusCode = din.readByte();

        bArrayInputStream.close();
        din.close();
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public int getType() {
        return this.messageType;
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.messageType);

        dout.writeByte(this.statusCode);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }

}