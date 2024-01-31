package csx55.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

public class EventFactory {

    private final static EventFactory EventFactoryInstance = new EventFactory();

    private EventFactory() { };

    public static EventFactory getInstance() {
        return EventFactoryInstance;
    }

    public Event getEvent(byte[] bytes) throws IOException {
        ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(bArrayInputStream));
        int messageType = din.readInt();
        switch(messageType) {
            case (Protocol.REGISTER_REQUEST):
                return new RegisterRequest(bytes);
            case (Protocol.REGISTER_RESPONSE):
                return new RegisterResponse(bytes);
            case (Protocol.DEREGISTER_REQUEST):
                return new DeregisterRequest(bytes);
            case (Protocol.DEREGISTER_RESPONSE):
                return new DeregisterResponse(bytes);
            case (Protocol.MESSAGING_NODES_LIST):
                return new MessagingNodesList(bytes);
            case (Protocol.LINK_WEIGHTS):
                return new LinkWeights(bytes);
            case (Protocol.PARTNER_CONNECTION_REQUEST):
                return new PartnerConnectionRequest(bytes);
            case (Protocol.TASK_INITIATE):
                return new TaskInitiate(bytes);
            case (Protocol.MESSAGE):
                return new Message(bytes);
            case (Protocol.TASK_COMPLETE):
                return new TaskComplete(bytes);
            case (Protocol.PULL_TRAFFIC_SUMMARY):
                return new TaskSummaryRequest(bytes);
            case (Protocol.TRAFFIC_SUMMARY):
                return new TaskSummaryResponse(bytes);
            case (Protocol.POKE):
                return new PartnerPoke(bytes);
            default:
                System.out.println("getEvent() found no matching route, messageType: " + messageType);
                return null;
        }

    }
    
}