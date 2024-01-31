package csx55.overlay.util;

import java.util.concurrent.locks.ReentrantLock;

public class TrafficStats {

    private int sendTracker, receiveTracker, relayTracker, directMessageReceiveTracker, directMessageSentTracker, messageTracker = 0;
    private long sendSummation, receiveSummation = 0L;

    private final ReentrantLock receiveLock = new ReentrantLock();
    private final ReentrantLock relayLock = new ReentrantLock();

    public void reset() {
        sendTracker = 0;
        receiveTracker = 0;
        relayTracker = 0;
        directMessageReceiveTracker = 0;
        directMessageSentTracker = 0;
        messageTracker = 0;
        sendSummation = 0L;
        receiveSummation = 0L;
    }

    public void updateSentMessages(long payload) {
        this.sendTracker++;
        this.sendSummation += payload;
    }

    public synchronized void updateReceivedMessages(long payload) {
//        try {
//            this.receiveLock.lock();
//        } catch (Exception ignored) {
            this.receiveTracker++;
            this.receiveSummation += payload;
//        } finally {
//            this.receiveLock.unlock();
//        }
    }

    public synchronized void incrementRelayTracker() {
//        try {
//            this.relayLock.lock();
//        } catch (Exception ignored) {
            this.relayTracker++;
//        } finally {
//            this.relayLock.unlock();
//        }
    }

    public synchronized void incrementDirectMessageReceiveTracker() {
        this.directMessageReceiveTracker++;
    }

    public synchronized void incrementDirectMessageSentTracker() {
        this.directMessageSentTracker++;
    }

    public synchronized void incrementMessageTracker() {
        this.messageTracker++;
    }

    public int getSendTracker() {
        return sendTracker;
    }

    public int getReceiveTracker() {
        return receiveTracker;
    }

    public int getRelayTracker() {
        return relayTracker;
    }

    public int getDirectMessageReceiveTracker() {
        return directMessageReceiveTracker;
    }

    public int getDirectMessageSentTracker() {
        return directMessageSentTracker;
    }

    public int getMessageTracker() {
        return messageTracker;
    }

    public long getSendSummation() {
        return sendSummation;
    }

    public long getReceiveSummation() {
        return receiveSummation;
    }

}
