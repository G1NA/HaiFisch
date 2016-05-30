package commons;

/**
 * Network payload type enumeration.
 */

public enum NetworkPayloadType {
    //0 for checkIn(s) request, 1 checkIn result, 2 new checkIn, 3 Connection Acknowledge, 4 status check, 5 reducer_node signal
    CHECK_IN_REQUEST, CHECK_IN_RESULTS, CHECK_IN, CONNECTION_ACK, STATUS_CHECK, STATUS_REPLY, START_REDUCE
}
