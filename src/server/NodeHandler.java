package server;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class NodeHandler implements Runnable {

    private Socket clientSocket;
    private int nodeId;
    private TokenRing tokenRing;
    private Database db;
    private AtomicInteger lamportClock;

    public NodeHandler(Socket socket, int nodeId, TokenRing tokenRing, Database db, AtomicInteger lamportClock) {
        this.clientSocket = socket;
        this.nodeId = nodeId;
        this.tokenRing = tokenRing;
        this.db = db;
        this.lamportClock = lamportClock;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message = in.readLine();
            if (message == null) return;

            System.out.println("[Node " + nodeId + "] Received from client: " + message);

            if (message.startsWith("TOKEN|")) {
                // Token message
                String[] parts = message.split("\\|");
                int fromNode = Integer.parseInt(parts[1]);
                int fromLamport = Integer.parseInt(parts[2]);
                lamportClock.set(Math.max(lamportClock.get(), fromLamport) + 1);
                tokenRing.receiveToken(fromNode);
                out.println("TOKEN_RECEIVED");
            } else if (message.startsWith("QUERY")) {
                // Query all data
                out.println(tokenRing.getData());
            } else if (message.startsWith("INSERT|")) {
                // Insert data request
                tokenRing.processRequest(message.substring(7));
                out.println("Data received and will be processed when token arrives");
            } else if (message.startsWith("DELETE|")) {
                // Delete data request
                String id = message.substring(7);
                db.delData(id);
                out.println("Data deleted");
            } else {
                out.println("Unknown command");
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}