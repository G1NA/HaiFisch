package commons;

import java.io.Serializable;

/**
 * Acknowledgement of a connection with a server.
 */

public class ConnectionAcknowledge implements Serializable {

    private static final long serialVersionUID = 7342155718917655917L;

    public final ConnectionAcknowledgeType TYPE;
    public final String serverName;
    public final int port;
    public int status;

    /**
     * @param type       1 for mapper 2 for reducer type 3 inform for reducer existence
     * @param serverName The name of the server concerning the acknowledge
     * @param port       The port of the server concerning the acknowledge
     */
    public ConnectionAcknowledge(ConnectionAcknowledgeType type, String serverName, int port) {
        this.TYPE = type;
        this.serverName = serverName;
        this.port = port;
    }

}
