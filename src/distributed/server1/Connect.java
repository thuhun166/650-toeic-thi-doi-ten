package distributed.server1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connect extends Thread {

    String destination;
    int port;
    String serverName;
    Socket connection;
    DataOutputStream out;
    BufferedReader in;

    public Connect(String destination, int port, String serverName) {
        this.destination = destination;
        this.port = port;
        this.name = serverName;
    }

    public void connect() {
        try {
            connection = new Socket(destination, port);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new DataOutputStream(connection.getOutputStream());
            System.out.println("Connected to " + serverName + " at port " + port + ".");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void requestServer(String message) {
        try {
            System.out.println("Send a message to: " + serverName);
            out.writeBytes(message);
            out.write(13);
            out.write(10);
            out.flush();
        } catch (Exception e) {
        }
    }

    public void shutdown() {
        try {
            connection.close();
        } catch (Exception e) {
        }
    }
}