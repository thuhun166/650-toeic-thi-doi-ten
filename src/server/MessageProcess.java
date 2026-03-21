package server;

import java.io.*;
import java.net.Socket;

public class MessageProcess {

    private String startNode;
    private String jeton;
    private String lamportClock;
    private String serverName;
    private String messageType;
    private String action;
    private String content;

    public MessageProcess(String recMess) {
        int i;
        String temp = recMess;

        try {
            // Format: @$1|100000|10|Server1|INSERT|id|content$@
            i = temp.indexOf("@$");
            temp = temp.substring(i + 2);

            i = temp.indexOf("|");
            startNode = temp.substring(0, i);
            temp = temp.substring(i + 1);

            i = temp.indexOf("|");
            jeton = temp.substring(0, i);
            temp = temp.substring(i + 1);

            i = temp.indexOf("|");
            lamportClock = temp.substring(0, i);
            temp = temp.substring(i + 1);

            i = temp.indexOf("|");
            serverName = temp.substring(0, i);
            temp = temp.substring(i + 1);

            i = temp.indexOf("|");
            messageType = temp.substring(0, i);
            temp = temp.substring(i + 1);

            i = temp.indexOf("|");
            action = temp.substring(0, i);
            temp = temp.substring(i + 1);

            i = temp.indexOf("$@");
            content = temp.substring(0, i);

        } catch (Exception ex) {
            System.out.println("Error parsing message: " + ex.getMessage());
        }
    }

    public String getStartNode() {
        return startNode;
    }

    public String getJeton() {
        return jeton;
    }

    public String getLamportClock() {
        return lamportClock;
    }

    public String getServerName() {
        return serverName;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getAction() {
        return action;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "@$" + startNode + "|" + jeton + "|" + lamportClock + "|" + serverName + "|" + messageType + "|" + action + "|" + content + "$@";
    }
}