package csx55.overlay.transport;

import csx55.overlay.node.Node;
import java.net.Socket;
import java.io.IOException;
import java.net.SocketException;

public class TCPServerThread implements Runnable {

    private final Node messagingNode;

    public TCPServerThread(Node messagingNode) {
        this.messagingNode = messagingNode;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                Socket receiverSocket = this.messagingNode.getServerSocket().accept();
                try {
                    TCPReceiverThread receiver = new TCPReceiverThread(messagingNode, receiverSocket);
                    Thread thread = new Thread(receiver);
                    thread.start();
                } catch (IOException e) {
                    System.out.println("ERROR Adding the following Socket " + receiverSocket.toString());
                }
            }
        } catch (SocketException e) {
            System.out.println("ERROR SocketException in the run() method of TCPServerThread...\n" + e.toString());
        } catch (IOException e) {
            System.out.println("ERROR IOException in the run() method of TCPServerThread...\n" + e.toString());
        }
        System.out.println("TCPServerThread ending.");
    }

}