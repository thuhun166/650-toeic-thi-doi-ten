package server;

import java.io.*;
import java.net.Socket;

public class Connect extends Thread {

    private String destination;
    private int port;
    private String serverName;
    private String message;
    private Socket connection;
    private DataOutputStream out;
    private BufferedReader in;

    public Connect(String destination, int port, String serverName, String message) {
        this.destination = destination;
        this.port = port;
        this.serverName = serverName;
        this.message = message;
    }

    public void connect() {
        try {
            connection = new Socket(destination, port);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new DataOutputStream(connection.getOutputStream());
            System.out.println("[Connect] Connected to " + serverName + " at " + destination + ":" + port);
        } catch (Exception e) {
            System.out.println("[Connect] Error connecting to " + serverName + ": " + e.getMessage());
        }
    }

    public void sendMessage(String msg) {
        try {
            if (out != null) {
                System.out.println("[Connect] Sending to " + serverName + ": " + msg.substring(0, Math.min(50, msg.length())) + "...");
                out.writeBytes(msg);
                out.write(13);
                out.write(10);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println("[Connect] Error sending message: " + e.getMessage());
        }
    }

    public String receiveMessage() {
        try {
            if (in != null) {
                String response = in.readLine();
                System.out.println("[Connect] Received from " + serverName + ": " + response);
                return response;
            }
        } catch (Exception e) {
            System.out.println("[Connect] Error receiving message: " + e.getMessage());
        }
        return null;
    }

    public void shutdown() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("[Connect] Connection to " + serverName + " closed");
            }
        } catch (Exception e) {
            System.out.println("[Connect] Error closing connection: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        connect();
        sendMessage(message);
        shutdown();
    }
}