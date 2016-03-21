package com.haifisch.server.master;

public class Client {

    private final String clientAddress;
    private final int clientPort;
    private final String clientReqId;

    public Client(String clientAddress, int port, String clientReqId){
        this.clientAddress = clientAddress;
        this.clientPort = port;
        this.clientReqId = clientReqId;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

    public String getClientReqId() {
        return clientReqId;
    }


}
