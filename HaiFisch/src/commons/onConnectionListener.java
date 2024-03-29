package commons;

/**
 * Connection interface.
 */
public interface onConnectionListener {

    /**
     * It's a callback
     *
     * @param payload the data transmitted
     */
    void onConnect(NetworkPayload payload);

}
