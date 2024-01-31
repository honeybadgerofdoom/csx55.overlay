package csx55.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

public class RegisterResponse implements Event {

    private final int messageType = Protocol.REGISTER_RESPONSE;
    private byte statusCode;
    private String additionalInfo;

    public RegisterResponse(byte statusCode, String additionalInfo) {
        this.statusCode = statusCode;
        this.additionalInfo = additionalInfo;
    }

    public RegisterResponse(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));

        din.readInt(); // take messageType out of stream
        this.statusCode = din.readByte();

        int infoLength = din.readInt();
        byte[] infoBytes = new byte[infoLength]; 
        din.readFully(infoBytes);
        this.additionalInfo = new String(infoBytes);

        bArrayInputStream.close();
        din.close();
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
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

        byte[] additionalInfoBytes = this.additionalInfo.getBytes();
        int elementLength = additionalInfoBytes.length;
        dout.writeInt(elementLength);
        dout.write(additionalInfoBytes);


        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }
    
}