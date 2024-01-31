package csx55.overlay.util;

import java.util.concurrent.locks.ReentrantLock;

public class TrafficStats {

    private int sendTracker, receiveTracker, relayTracker = 0;
    private long sendSummation, receiveSummation = 0L;

    private final ReentrantLock receiveLock = new ReentrantLock();
    private final ReentrantLock relayLock = new ReentrantLock();

    public void reset() {
        sendTracker = 0;
        receiveTracker = 0;
        relayTracker = 0;
        sendSummation = 0L;
        receiveSummation = 0L;
    }

    public void updateSentMessages(long payload) {
        this.sendTracker++;
        this.sendSummation += payload;
    }

    public void updateReceivedMessages(long payload) {
        try {
            this.receiveLock.lock();
            this.receiveTracker++;
            this.receiveSummation += payload;
        } catch (Exception ignored) {
        } finally {
            this.receiveLock.unlock();
        }
    }

    public void incrementRelayTracker() {
        try {
            this.relayLock.lock();
            this.relayTracker++;
        } catch (Exception ignored) {
        } finally {
            this.relayLock.unlock();
        }
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

    public long getSendSummation() {
        return sendSummation;
    }

    public long getReceiveSummation() {
        return receiveSummation;
    }

}
