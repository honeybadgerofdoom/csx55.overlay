package csx55.overlay.transport;

import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.Socket;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import csx55.overlay.node.Node;
import csx55.overlay.wireformats.Event;
import csx55.overlay.wireformats.EventFactory;

public class TCPReceiverThread implements Runnable {

    private Node node;
    private Socket socket;
    private DataInputStream din;
    private String socketString;
    
    public TCPReceiverThread(Node node, Socket socket) throws IOException { 
        this.node = node;
        this.socket = socket;
        this.socketString = socket.getRemoteSocketAddress().toString();
        this.din = new DataInputStream(socket.getInputStream()); 
    }

    @Override
    public void run() {

        System.out.println("TCPReceiverThread talking to " + this.socketString);
        int dataLength;

        while (this.socket != null) {
            try {
                dataLength = this.din.readInt();
                byte[] data = new byte[dataLength];
                this.din.readFully(data, 0, dataLength);

                EventFactory eventFactory = EventFactory.getInstance();
                Event event = eventFactory.getEvent(data);

                // Comment out to cut out the CLQ
                this.node.addEvent(event, this.socket);

                // Then comment this in
//                 this.node.onEvent(event, this.socket);

            } catch (SocketException se) {
                System.out.println(se.getMessage());
                break;
            }  catch (IOException ioe) {
                System.out.println(ioe.getMessage()) ;
                break; 
            }
        }
        System.out.println("TCPReceiverThread listening on Socket with remote address " + this.socketString + " has closed.");
    }
    
}