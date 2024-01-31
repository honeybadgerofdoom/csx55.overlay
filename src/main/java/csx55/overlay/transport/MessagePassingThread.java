package csx55.overlay.transport;

import csx55.overlay.node.MessagingNode;
import csx55.overlay.node.PartnerNodeRef;
import csx55.overlay.util.TrafficStats;
import csx55.overlay.wireformats.Message;
import csx55.overlay.wireformats.TaskComplete;
import csx55.overlay.util.Helpers;

import java.io.IOException;
import java.util.*;
import java.net.*;

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

                /**
                 * This works. No received bugs. No relaying
                 */
                // String sink = this.node.getRandomPartnerNode();
                // List<String> routePlan = new ArrayList<>();
                // routePlan.add(this.node.getIpAddress() + ":" + this.node.getPortNumber());
                // routePlan.add(sink);

                /**
                 * This works. Guaranteeing a routePlan of size 2 max -> no relaying
                 */
//                 List<String> routePlan = new ArrayList<>();
//                 while (routePlan.size() != 2) {
//                     String sink = this.node.getRandomSinkNode();
//                     routePlan = this.node.getShortestPathCalculator().getPath(sink);
//                 }

                /**
                 * This doesn't work, unless we slow the threads down and use a small (< 700) # of rounds
                 */
                String sink = this.node.getRandomSinkNode();
                List<String> routePlan = this.node.getShortestPathCalculator().getPath(sink);

//                if (routePlan.size() == 2) System.out.println("DM Sink: " + sink + ", Route: " + Helpers.getListString(routePlan));
//                if (routePlan.size() == 2) trafficStats.incrementDirectMessageSentTracker(); // FIXME Debugging

                String nextTarget = routePlan.get(1);
                PartnerNodeRef partnerNodeRef = this.node.getPartnerNodes().get(nextTarget);

                boolean flipNumber = rng.nextBoolean();
                int payload = rng.nextInt(2147483647);
                if (flipNumber) payload *= -1;

                Message message = new Message(payload, routePlan);
                partnerNodeRef.writeToSocket(message);
                trafficStats.updateSentMessages(payload);
            }
        }
        System.out.println("Finished sending messages.");
        this.node.reportAllMessagesPassed();
    }
    
}