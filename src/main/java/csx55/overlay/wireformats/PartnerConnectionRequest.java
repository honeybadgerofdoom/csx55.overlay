package csx55.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;

public class PartnerConnectionRequest implements Event {

    private int messageType = Protocol.PARTNER_CONNECTION_REQUEST;
    private String ipAddress;
    private int portNumber;
    private int linkWeight;

    public PartnerConnectionRequest(String ipAddress, int portNumber, int linkWeight) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.linkWeight = linkWeight;
    }

    public PartnerConnectionRequest(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));

        din.readInt(); // Get rid of messageType

        int ipAddressLength = din.readInt();
        byte[] ipAddressBytes = new byte[ipAddressLength];
        din.readFully(ipAddressBytes);
        this.ipAddress = new String(ipAddressBytes);

        this.portNumber = din.readInt();

        this.linkWeight = din.readInt();

        bArrayInputStream.close();
        din.close();
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.messageType);

        byte[] ipAddressBytes = this.ipAddress.getBytes();
        int ipAddressLength = ipAddressBytes.length;
        dout.writeInt(ipAddressLength);
        dout.write(ipAddressBytes);

        dout.writeInt(this.portNumber);
        dout.writeInt(this.linkWeight);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }

    public int getType() {
        return messageType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public int getLinkWeight() {
        return linkWeight;
    }

    public String getPartnerString() {
        return ipAddress + ":" + portNumber;
    }

    @Override
    public String toString() {
        return this.getIpAddress() + ":" + this.getPortNumber() + " " + this.getLinkWeight();
    }

}
