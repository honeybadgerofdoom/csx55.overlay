package csx55.overlay.node;

import csx55.overlay.transport.TCPSender;
import csx55.overlay.wireformats.Event;

import java.io.IOException;
import java.net.Socket;

public class PartnerNodeRef {

    private Socket socket;
    private int linkWeight;
    private TCPSender tcpSender;

    public PartnerNodeRef(Socket socket, int linkWeight) {
        this.socket = socket;
        this.linkWeight = linkWeight;
        initializeTCPSender(socket);
    }

    private void initializeTCPSender(Socket socket) {
        try {
            this.tcpSender = new TCPSender(socket);
        } catch (IOException e) {
            System.out.println("Failed to initialize TCPSender " + e);
        }
    }

    public synchronized void writeToSocket(Event event) {
        try {
            byte[] bytes = event.getBytes();
            this.tcpSender.sendData(bytes);
        } catch (IOException e) {
            System.out.println("Failed to write to socket " + this.socket);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public int getLinkWeight() {
        return linkWeight;
    }

    public void setLinkWeight(int linkWeight) {
        this.linkWeight = linkWeight;
    }

    @Override
    public String toString() {
        return socket + " " + linkWeight;
    }

}
