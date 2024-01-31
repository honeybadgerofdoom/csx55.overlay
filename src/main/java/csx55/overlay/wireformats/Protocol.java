package csx55.overlay.wireformats;

public class Protocol {

    // message types
    public static final int REGISTER_REQUEST = 1;
    public static final int REGISTER_RESPONSE = 2;
    public static final int DEREGISTER_REQUEST = 3;
    public static final int DEREGISTER_RESPONSE = 4;
    public static final int MESSAGING_NODES_LIST = 5;
    public static final int TASK_INITIATE = 6;
    public static final int TASK_COMPLETE = 7;
    public static final int PULL_TRAFFIC_SUMMARY = 8;
    public static final int TRAFFIC_SUMMARY = 9;
    public static final int LINK_WEIGHTS = 10;
    public static final int PARTNER_CONNECTION_REQUEST = 11;
    public static final int MESSAGE = 12;
    public static final int POKE = 13;
    
    // status codes
    public static final byte SUCCESS = 1;
    public static final byte FAILURE = 0;
}