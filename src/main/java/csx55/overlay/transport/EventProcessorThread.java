package csx55.overlay.transport;

import csx55.overlay.node.Node;
import csx55.overlay.util.EventAndSocket;
import csx55.overlay.wireformats.Event;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventProcessorThread implements Runnable {

    private Node node;

    public EventProcessorThread(Node node) {
        this.node = node;
    }

    public void run() {
        ConcurrentLinkedQueue<EventAndSocket> eventQueue = this.node.getEventQueue();
        while (true) { // TODO Termination Condition
            EventAndSocket eventAndSocket = eventQueue.poll();
            if (eventAndSocket == null) continue;
            Event event = eventAndSocket.getEventObject();
            Socket socket = eventAndSocket.getSocket();
            this.node.onEvent(event, socket);
        }
    }

}
