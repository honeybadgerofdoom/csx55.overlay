package csx55.overlay.wireformats;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;

public class TaskSummaryResponse implements Event {

    private final int messageType = Protocol.TRAFFIC_SUMMARY;
    private final String ipAddress;
    private final int portNumber, messagesSent, messagesReceived, messagesRelayed;
    private final long sentSummation, receivedSummation;


    public TaskSummaryResponse(String ipAddress, int portNumber, int messagesSent, long sentSummation, int messagesReceived, long receivedSummation, int messagesRelayed) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.messagesSent = messagesSent;
        this.sentSummation = sentSummation;
        this.messagesReceived = messagesReceived;
        this.receivedSummation = receivedSummation;
        this.messagesRelayed = messagesRelayed;
    }

    public TaskSummaryResponse(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));

        din.readInt(); // take messageType out of stream

        int idAddressLength = din.readInt();
        byte[] ipAddressBytes = new byte[idAddressLength]; 
        din.readFully(ipAddressBytes);
        this.ipAddress = new String(ipAddressBytes);
        
        this.portNumber = din.readInt();
        this.messagesSent = din.readInt();
        this.sentSummation = din.readLong();
        this.messagesReceived = din.readInt();
        this.receivedSummation = din.readLong();
        this.messagesRelayed = din.readInt();

        bArrayInputStream.close();
        din.close();
    }

    public String formatRow(String id) {
        return String.format("| %-17s | %17d | %17d | %17d | %17d | %17d |", id, messagesSent, messagesReceived, sentSummation, receivedSummation, messagesRelayed);
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream(); 
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.messageType);

        byte[] ipAddressBytes = this.ipAddress.getBytes();
        int elementLength = ipAddressBytes.length;
        dout.writeInt(elementLength);
        dout.write(ipAddressBytes);

        dout.writeInt(this.portNumber);
        dout.writeInt(this.messagesSent);
        dout.writeLong(this.sentSummation);
        dout.writeInt(this.messagesReceived);
        dout.writeLong(this.receivedSummation);
        dout.writeInt(this.messagesRelayed);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }

    public int getType() {
        return this.messageType;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public int getMessagesSent() {
        return this.messagesSent;
    }

    public long getSentSummation() {
        return this.sentSummation;
    }

    public int getMessagesReceived() {
        return this.messagesReceived;
    }

    public long getReceivedSummation() {
        return this.receivedSummation;
    }

    public int getMessagesRelayed() {
        return this.messagesRelayed;
    }

}