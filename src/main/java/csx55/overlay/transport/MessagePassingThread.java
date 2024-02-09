package csx55.overlay.transport;

import csx55.overlay.node.MessagingNode;
import csx55.overlay.node.PartnerNodeRef;
import csx55.overlay.util.TrafficStats;
import csx55.overlay.wireformats.Message;

import java.util.*;

public class MessagePassingThread implements Runnable {

    private final MessagingNode node;
    private final int numberOfRounds;

    public MessagePassingThread(MessagingNode node, int numberOfRounds) { 
        this.node = node;
        this.numberOfRounds = numberOfRounds;
    }

    @Override
    public void run() {
        Random rng = this.node.getRng();
        TrafficStats trafficStats = this.node.getTrafficStats();
        for (int j = 0; j < this.numberOfRounds; j++) {
            int messagesPerRound = 5; // TODO Confirm this
            for (int i = 0; i < messagesPerRound; i++) {

                String sink = this.node.getRandomSinkNode();
                List<String> routePlan = this.node.getShortestPathCalculator().getPath(sink);

                boolean flipNumber = rng.nextBoolean();
                int payload = rng.nextInt(2147483647);
                if (flipNumber) payload *= -1;

                Message message = new Message(payload, routePlan);
                String nextTarget = routePlan.get(1);

                PartnerNodeRef partnerNodeRef = this.node.getPartnerNodes().get(nextTarget);
                partnerNodeRef.writeToSocket(message);
                trafficStats.updateSentMessages(payload);
            }
        }
        this.node.reportAllMessagesPassed();
    }
    
}