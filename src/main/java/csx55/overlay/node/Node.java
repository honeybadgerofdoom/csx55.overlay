package csx55.overlay.node;

import csx55.overlay.transport.TCPServerThread;
import csx55.overlay.util.EventAndSocket;
import csx55.overlay.wireformats.Event;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface Node {

    public int getPortNumber();
    public ServerSocket getServerSocket();
    public void onEvent(Event event, Socket socket);
    public void addEvent(Event event, Socket socket);
    public ConcurrentLinkedQueue<EventAndSocket> getEventQueue();

    default void startTCPServerThread() {
        TCPServerThread server = new TCPServerThread(this);
        Thread thread = new Thread(server);
        thread.start();
    }

}