package csx55.overlay.transport;

import java.io.DataInputStream;
import java.net.Socket;
import java.io.IOException;

import csx55.overlay.node.Node;
import csx55.overlay.wireformats.Event;
import csx55.overlay.wireformats.EventFactory;

public class TCPReceiverThread implements Runnable {

    private final Node node;
    private final Socket socket;
    private final DataInputStream din;
    private final String socketString;
    
    public TCPReceiverThread(Node node, Socket socket) throws IOException { 
        this.node = node;
        this.socket = socket;
        this.socketString = socket.getRemoteSocketAddress().toString();
        this.din = new DataInputStream(socket.getInputStream()); 
    }

    @Override
    public void run() {

        int dataLength;

        while (this.socket != null) {
            try {
                dataLength = this.din.readInt();
                byte[] data = new byte[dataLength];
                this.din.readFully(data, 0, dataLength);

                EventFactory eventFactory = EventFactory.getInstance();
                Event event = eventFactory.getEvent(data);
                this.node.addEvent(event, this.socket);
            } catch (IOException se) {
                System.out.println(se.getMessage());
                break;
            }
        }
        System.out.println("TCPReceiverThread listening on Socket with remote address " + this.socketString + " has closed.");
    }
    
}